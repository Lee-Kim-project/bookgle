package bookgle.bookgle.search.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // json파일의 속성들 중에서 Book 클래스에 정의된 속성들만 가져온다.
public class Book {
    private final String title;
    private final String authors;
    private final String publisher;
    private final int publicationYear;
    private final String isbn;

    @JsonCreator // json에서 Book class로 역직렬화할때 이 생성자를 사용하도록 함
    public Book(@JsonProperty("bookname") String bookname, @JsonProperty("authors") String authors,
                @JsonProperty("publisher") String publisher, @JsonProperty("publication_year") int publicationYear,
                @JsonProperty("isbn13") String isbn) {
        this.title = bookname;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }
}