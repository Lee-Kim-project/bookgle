package bookgle.bookgle.search.service;

import bookgle.bookgle.search.config.NaruProperties;
import bookgle.bookgle.search.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    private final NaruProperties naruProperties;
    private final WebClient webClient;

    public SearchService(NaruProperties apiConfig) {
        this.naruProperties = apiConfig;
        // baseurl 지정해서 생성
        this.webClient = WebClient.builder()
                .baseUrl(SearchUrl.BASE_URL.getUrl())
                .build();
    }

    // 유저가 입력한 책 제목과 동일한 제목을 가진 책들을 찾습니다.
    public List<Book> searchBooks(String title) throws JsonProcessingException {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SearchUrl.BOOKS_INFO.getUrl())
                        .queryParam("authKey", naruProperties.getApiKey())
                        .queryParam("title", title)
                        .queryParam("pageSize", 100)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // json 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        List<Book> books = new ArrayList<>();
        for (JsonNode doc : response.get("response").get("docs")) {
            books.add(objectMapper.readValue(doc.get("doc").toString(), Book.class));
        }

        return books;
    }

    // 유저가 검색한 책을 소장하고 있는 도서관들을 유저가 선택한 지역에 기반해서 찾습니다.
    public List<Library> searchLibraries(String isbn, String[] regions) throws IOException {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SearchUrl.ALL_LIBRARY_HAS_THE_BOOK.getUrl())
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
        ObjectMapper objectMapper = new ObjectMapper();
        List<Library> libraries = new ArrayList<>();
        for (JsonNode lib : response.get("response").get("libs")) {
            libraries.add(objectMapper.readValue(lib.get("lib").toString(), Library.class));
        }

        libraries = searchLibrariesByRegion(libraries, regions);

        return libraries;
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
                        .path(SearchUrl.IS_BOOK_AVAILABLE.getUrl())
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
}
