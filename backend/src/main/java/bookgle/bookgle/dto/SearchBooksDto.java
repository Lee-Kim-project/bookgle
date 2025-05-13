package bookgle.bookgle.dto;

import bookgle.bookgle.search.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchBooksDto {
    private List<Book> books;
    private int booksSize;
}
