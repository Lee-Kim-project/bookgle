package bookgle.bookgle.search.controller;

import bookgle.bookgle.dto.BookAvailabilityDto;
import bookgle.bookgle.dto.SearchBooksDto;
import bookgle.bookgle.dto.SearchLibrariesDto;
import bookgle.bookgle.search.config.NaverMapsProperties;
import bookgle.bookgle.search.domain.Book;
import bookgle.bookgle.search.domain.Library;
import bookgle.bookgle.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final NaverMapsProperties naverMapsProperties;

    // 유저가 선택한 검색 옵션과 키워드로 찾은 책들의 목록을 보여줍니다.
    @GetMapping("/search/books")
    public SearchBooksDto searchBooks(String searchOption, String keyword) throws IOException {
        List<Book> books;
        books = searchService.searchBooks(searchOption, keyword);

        SearchBooksDto searchBooksDto = new SearchBooksDto(books, books.size());

        return searchBooksDto;
    }

    // 책 목록 중에서 유저가 선택한 책을 소장하고 있는 도서관들을 찾아서 보여줍니다.
    @GetMapping("/search/libraries")
    public SearchLibrariesDto searchLibraries(String isbn, String[] regions) throws IOException {
        // 유저가 선택한 책의 isbn을 통해서 해당 책을 소장하고 있는 도서관들을 찾는다.
        List<Library> libraries = searchService.searchLibraries(isbn, regions);

        SearchLibrariesDto searchLibrariesDto = new SearchLibrariesDto(libraries, naverMapsProperties.getClientId(), isbn);

        return searchLibrariesDto;
    }

    // 유저가 선택한 도서관에서 유저가 빌리고자 하는 책의 대출가능여부를 보여줍니다.
    @GetMapping("/search/isAvailable")
    public BookAvailabilityDto checkAvailable(String libCode, String isbn) {
        boolean result = searchService.isBookAvailable(libCode, isbn);

        BookAvailabilityDto bookAvailabilityDto = new BookAvailabilityDto();
        if (result)
            bookAvailabilityDto.setMessage("대출 가능");
        else
            bookAvailabilityDto.setMessage("대출 불가");

        return bookAvailabilityDto;
    }
}
