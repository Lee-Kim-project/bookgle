package bookgle.bookgle.search.service;

import bookgle.bookgle.exception.ExceptionStatus;
import bookgle.bookgle.exception.ServiceException;
import bookgle.bookgle.search.config.NaruProperties;
import bookgle.bookgle.search.config.NaverMapsProperties;
import bookgle.bookgle.search.domain.*;
import bookgle.bookgle.search.projection.LibraryCodeLatLng;
import bookgle.bookgle.search.repository.LibraryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {

    private final NaruProperties naruProperties;
    private final NaverMapsProperties naverMapsProperties;
    private final WebClient webClient;
    private final LibraryRepository libraryRepository;
    private final ObjectMapper objectMapper;
    private static final Set<String> searchOptions = Set.of("title", "author", "publisher");

    public SearchService(NaruProperties naruProperties, NaverMapsProperties naverMapsProperties, LibraryRepository libraryRepository, ObjectMapper objectMapper) {
        this.naruProperties = naruProperties;
        this.naverMapsProperties = naverMapsProperties;
        this.libraryRepository = libraryRepository;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.create();
    }

    // 유저가 입력한 책 제목과 동일한 제목을 가진 책들을 찾습니다.
    public List<Book> searchBooks(String searchOption, String keyword) throws JsonProcessingException {
        // searchOption은 title, author, publisher의 3가지만 가능
        if (!searchOptions.contains(searchOption)) {
            throw new ServiceException(ExceptionStatus.ILLEGAL_SEARCH_OPTION);
        }

        Optional<JsonNode> response = Optional.ofNullable(webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(NaruUrl.SCHEME.get())
                        .host(NaruUrl.HOST.get())
                        .path(NaruUrl.BOOKS_INFO.get())
                        .queryParam("authKey", naruProperties.getApiKey())
                        .queryParam(searchOption, "\"" + keyword + "\"")
                        .queryParam("pageSize", 100)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block());

        // json 파싱
        List<Book> books = new ArrayList<>();
        if (response.isPresent()) {
            JsonNode docs = response.get().get("response").get("docs");
//            JsonNode docs = response.orElseThrow(() -> new ServiceException(NOT_FOUND_BOOK)).get("response").get("docs");
            ObjectMapper objectMapper = new ObjectMapper();
            for (JsonNode doc : docs) {
                books.add(objectMapper.readValue(doc.get("doc").toString(), Book.class));
            }
        }

        return books;
    }

    // 유저가 검색한 책을 소장하고 있는 도서관들을 유저가 선택한 지역에 기반해서 찾습니다.
    public List<Library> searchLibraries(String isbn, String[] regions) throws IOException {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(NaruUrl.SCHEME.get())
                        .host(NaruUrl.HOST.get())
                        .path(NaruUrl.ALL_LIBRARY_HAS_THE_BOOK.get())
                        .queryParam("authKey", naruProperties.getApiKey())
                        .queryParam("isbn", isbn)
                        .queryParam("region", CityCode.SEOUL.getCode())
                        .queryParam("pageSize", 100)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // json 파싱
        List<Library> libraries = new ArrayList<>();
        for (JsonNode lib : response.get("response").get("libs")) {
            libraries.add(objectMapper.readValue(lib.get("lib").toString(), Library.class));
        }

        libraries = searchLibrariesByRegion(libraries, regions);

        updateLntLngOfLibrariesFromDb(libraries);

        return libraries;
    }

    public void updateLntLngOfLibrariesFromDb(List<Library> libraries) {
        List<LibraryCodeLatLng> codeLatLngs = libraryRepository.findByCodeIn(libraries.stream().map(Library::getCode).toList());
        libraries.forEach(library -> {
            codeLatLngs.forEach(codeLatLng -> {
                if (library.getCode().equals(codeLatLng.getCode())) {
                    library.setLatitude(codeLatLng.getLatitude());
                    library.setLongitude(codeLatLng.getLongitude());
                }
            });
        });
    }

    // 유저가 선택한 지역(구)에 있는 도서관들을 찾습니다.
    public List<Library> searchLibrariesByRegion(List<Library> libraries, String[] regions) {
        if (Arrays.stream(regions).noneMatch(region -> region.equals("서울"))) {
            List<Library> librariesByRegion = new ArrayList<>();
            for (Library library : libraries) {
                // 모든 주소가 "시 구 ~" 형태이므로 1번째 인덱스에 있는 값이 구에 대한 정보
                String district = library.getAddress().split(" ")[1];
                if (Arrays.stream(regions).anyMatch(region -> region.equals(district)))
                    librariesByRegion.add(library);
            }
            libraries = librariesByRegion;
        }

        return libraries;
    }

    public boolean isBookAvailable(String libCode, String isbn) {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(NaruUrl.SCHEME.get())
                        .host(NaruUrl.HOST.get())
                        .path(NaruUrl.IS_BOOK_AVAILABLE.get())
                        .queryParam("authKey", naruProperties.getApiKey())
                        .queryParam("isbn13", isbn)
                        .queryParam("libCode", libCode)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String loanAvailable = response.get("response").get("result").get("loanAvailable").textValue();
        if (loanAvailable.equals("Y"))
            return true;
        return false;
    }

    public void updateLntLngOfLibraryFromApi(Library library) {
        List<String> latlng = searchLatLngByAddress(library.getAddress());
        if (!latlng.isEmpty()) {
            library.setLatitude(latlng.get(0)); // latitude
            library.setLongitude(latlng.get(1)); // longitude
        }
    }

    // 특정 도시에 있는 모든 도서관들을 찾습니다.
    public List<Library> searchAllLibrariesByCity(int cityCode) throws JsonProcessingException {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(NaruUrl.SCHEME.get())
                        .host(NaruUrl.HOST.get())
                        .path(NaruUrl.ALL_LIBRARIES.get())
                        .queryParam("authKey", naruProperties.getApiKey())
                        .queryParam("region", cityCode)
                        .queryParam("pageSize", 400) // 서울지역의 총 도서관 수 344개
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        List<Library> libraries = new ArrayList<>();
        for (JsonNode lib : response.get("response").get("libs")) {
            libraries.add(objectMapper.readValue(lib.get("lib").toString(), Library.class));
        }

        return libraries;
    }

    // 주소를 통해 [위도, 경도] 좌표를 찾습니다.
    public List<String> searchLatLngByAddress(String address) {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(NaverMapsUrl.SCHEME.get())
                        .host(NaverMapsUrl.HOST.get())
                        .path(NaverMapsUrl.GEOCODING.get())
                        .queryParam("query", address)
                        .build())
                .header("x-ncp-apigw-api-key-id", naverMapsProperties.getClientId())
                .header("x-ncp-apigw-api-key", naverMapsProperties.getClientSecret())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // 도서관 정보나루 api에서 받아온 도서관 주소 중 유효하지 않은 주소가 있다.
        JsonNode addresses = response.get("addresses").get(0);
        if (addresses == null) {
            return new ArrayList<>();
        }

        String lat = addresses.get("y").asText();
        String lng = addresses.get("x").asText();

        return Arrays.asList(lat, lng);
    }

    public void saveLibraries(List<Library> libraries) {
        libraryRepository.saveAll(libraries);
    }
}
