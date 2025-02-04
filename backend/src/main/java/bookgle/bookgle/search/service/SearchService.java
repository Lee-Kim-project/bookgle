package bookgle.bookgle.search.service;

import bookgle.bookgle.search.domain.ApiConfig;
import bookgle.bookgle.search.domain.Book;
import bookgle.bookgle.search.domain.SearchUrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
}
