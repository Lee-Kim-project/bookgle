package bookgle.bookgle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class SearchController {
    private final WebClient webClient;
    private static final String BASE_URL = "http://data4library.kr/api";

    @GetMapping("/")
    String welcome() {
        return "main.html";
    }

    @PostMapping("/search")
    @ResponseBody
    String searchLibrary(String title) {
//        searchLibrary(title);
        return "hello";
    }
}
