package bookgle.bookgle.search.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private final String bookname;
    private final String authors;
    private final String publisher;
    private final int publicationYear;
    private final String isbn;

    @JsonCreator // json에서 Book class로 역직렬화할때 이 생성자를 사용하도록 함
    public Book(@JsonProperty("bookname") String bookname, @JsonProperty("authors") String authors,
                @JsonProperty("publisher") String publisher, @JsonProperty("publication_year") int publicationYear,
                @JsonProperty("isbn13") String isbn) {
        this.bookname = bookname;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }
}
