package bookgle.bookgle.search.controller;

import bookgle.bookgle.exception.ServiceException;
import bookgle.bookgle.search.config.NaverMapsProperties;
import bookgle.bookgle.search.domain.Book;
import bookgle.bookgle.search.domain.Library;
import bookgle.bookgle.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final NaverMapsProperties naverMapsProperties;

    @GetMapping("/")
    public String welcome() {
        return "main.html";
    }

    // 유저가 검색한 책 제목과 동일한 책들의 목록을 찾아서 보여줍니다.
    @GetMapping("/search/books")
    public String searchBooks(Model model, String title, String[] region) throws IOException {
        // 유저가 입력한 책 제목으로 isbn을 검색한다.
        List<Book> books;
        try {
            books = searchService.searchBooks(title);
        } catch (ServiceException e) {
            return "error.html";
        }

        // 유저가 입력한 책 제목과 동일한 제목의 책 목록을 보여준다.
        model.addAttribute("books", books);
        model.addAttribute("booksCount", books.size());

        // 유저가 지역을 선택하지 않았을 경우 서울이 default 값
        if (region == null)
            model.addAttribute("regions", "서울");
        else
            model.addAttribute("regions", region);

        return "bookList.html";
    }

    // 책 목록 중에서 유저가 선택한 책을 소장하고 있는 도서관들을 찾아서 보여줍니다.
    @GetMapping("/search/libraries")
    public String searchLibraries(Model model, String isbn, String[] regions) throws IOException {
        // 유저가 선택한 책의 isbn을 통해서 해당 책을 소장하고 있는 도서관들을 찾는다.
        List<Library> libraries = searchService.searchLibraries(isbn, regions);

        // map에 도서관들을 마커로 표시해서 보여준다.
        model.addAttribute("libraries", libraries);
        model.addAttribute("clientId", naverMapsProperties.getClientId());
        model.addAttribute("isbn", isbn);

        return "map.html";
    }

    // 유저가 선택한 도서관에서 유저가 빌리고자 하는 책의 대출가능여부를 보여줍니다.
    @GetMapping("/search/isAvailable")
    @ResponseBody
    public String checkAvailable(String libCode, String isbn) {
        boolean result = searchService.isBookAvailable(libCode, isbn);

        if (result)
            return "대출 가능";
        return "대출 불가";
    }

    // 도서관들의 주소를 위도,경도 좌표로 변환 후 DB에 저장합니다.
}
