package bookgle.bookgle.auth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class JwtLogoutFilter extends LogoutFilter {

    public JwtLogoutFilter() {
        super(new CustomLogoutSuccessHandler(), new CustomLogoutHandler());
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        super.doFilter(request, response, chain);
//    }

    // 쿠키 삭제
    private static class CustomLogoutHandler implements LogoutHandler {
        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            Cookie jwtCookie = new Cookie("Token", null);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0);  // 즉시 만료
            response.addCookie(jwtCookie);
        }
    }

    // 로그아웃 성공 후 처리 (리다이렉트 등)
    private static class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
//            SecurityContextHolder.clearContext();
            response.sendRedirect("/test/logout");  // 로그아웃 후 로그인 페이지로 리다이렉트
        }
    }
}

