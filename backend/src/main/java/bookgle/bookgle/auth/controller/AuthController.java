package bookgle.bookgle.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/register")
    public String register(Authentication auth) {
        // 로그인 안 되어 있는 경우
        // Security 설정에서 인증을 꺼버린 경우 auth == null이 된다. 그렇지 않은 경우 인증되지 않은 사용자인 경우 anonymousUser가 된다.
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            return "register.html";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        return "login.html";
    }
}
