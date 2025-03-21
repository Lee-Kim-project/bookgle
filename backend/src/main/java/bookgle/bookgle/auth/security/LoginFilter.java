package bookgle.bookgle.auth.security;

import bookgle.bookgle.auth.config.TokenProperties;
import bookgle.bookgle.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // authenticationManager가 인증을 수행하기 위해 필요한 데이터를 토큰(dto)에 담는다.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // authenticationManager로 토큰 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        // 첫번째 권한 가져옴
        GrantedAuthority auth = authentication.getAuthorities().iterator().next();

        String role = auth.getAuthority();
        // jwt 생성
        String token = jwtUtil.createJwt(username, role, tokenProperties.getExpiry());

        // jwt를 쿠키에 삽입 (HttpOnly, Secure는 필요에 따라 설정)
        Cookie jwtCookie = new Cookie("Token", token);
        jwtCookie.setHttpOnly(true); // 자바스크립트에서 접근 불가 (보안 강화)
        jwtCookie.setPath("/"); // 모든 경로의 요청에서 자동으로 쿠키를 서버로 전송
        jwtCookie.setMaxAge((int) tokenProperties.getExpiry() / 1000); // 쿠키 만료 시간 (초 단위), 설정안하면 세션 쿠키로 설정됨
        response.addCookie(jwtCookie);

        // response header에 jwt 삽입
//        response.addHeader("Authorization", "Bearer " + token);

        // 로그인 성공 후 메인페이지로 리다이렉트
        response.sendRedirect("/");

        System.out.println("login success");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
