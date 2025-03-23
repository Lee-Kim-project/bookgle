package bookgle.bookgle.user.controller;

import bookgle.bookgle.dto.UserRegisterDto;
import bookgle.bookgle.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Transactional
    @PostMapping("/user/register")
    public String registerUser(UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
        return "redirect:/login";
    }

    @GetMapping("/user/mypage")
    public String myPage(Authentication auth) {
        // 로그인이 안 되어 있는 경우
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "login.html";
        }
        System.out.println(auth.getName());
        return "myPage.html";
    }
}
