package bookgle.bookgle.search.controller;

import bookgle.bookgle.search.config.NaverMapsProperties;
import bookgle.bookgle.search.domain.Book;
import bookgle.bookgle.search.domain.Library;
import bookgle.bookgle.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
//    @ResponseBody // html을 반환하지 않고, 데이터 자체를 반환함
    public String searchBooks(Model model, String title, String[] region) throws IOException {
        // 유저가 입력한 책 제목으로 isbn을 검색한다.
        List<Book> books = searchService.searchBooks(title);
        // 유저가 입력한 책 제목과 동일한 제목의 책 목록을 보여준다.
        model.addAttribute("books", books);
        model.addAttribute("booksCount", books.size());
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
        return "map.html";
    }
}
