package bookgle.bookgle.search.service;

import bookgle.bookgle.search.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final ApiConfig apiConfig;
    private final WebClient webClient;

    public SearchService(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
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
                        .queryParam("authKey", apiConfig.getApiKey())
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



    // 유저가 검색한 책을 소장하고 있는 도서관들을 찾습니다.
    public void searchLibrary(String isbn) throws IOException {
        JsonNode response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SearchUrl.ALL_LIBRARY_HAS_THE_BOOK.getUrl())
                        .queryParam("authKey", apiConfig.getApiKey())
                        .queryParam("isbn", isbn)
                        .queryParam("region", RegionCode.SEOUL.getCode())
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

//        // 테스트 출력용
//        for (Library lib : libraries)
//            System.out.println(lib.getName());
    }
}
