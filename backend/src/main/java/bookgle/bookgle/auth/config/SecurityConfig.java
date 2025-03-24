package bookgle.bookgle.auth.config;

import bookgle.bookgle.auth.security.JwtLogoutFilter;
import bookgle.bookgle.auth.security.JwtFilter;
import bookgle.bookgle.auth.security.JwtUtil;
import bookgle.bookgle.auth.security.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;

    // 패스워드 인코더 bean 등록
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf off
        http.csrf((csrf) -> csrf.disable());
        // 폼 로그인 off
        http.formLogin((auth) -> auth.disable());
        // http 베이직 인증 방식 off
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/user/mypage").authenticated()
                .requestMatchers("/register", "/login").anonymous()
                .anyRequest().permitAll());

        // jwt filter 등록
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // custom login Filter 등록
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, tokenProperties), UsernamePasswordAuthenticationFilter.class);

        // custom logout filter 등록
        http.addFilterBefore(new JwtLogoutFilter(), LogoutFilter.class);

        // session 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        // 폼으로 로그인 (기본 설정)
//        http.formLogin((formLogin) -> formLogin.loginPage("/login")
//                .defaultSuccessUrl("/user/mypage")
////                .failureUrl("/fail")
//        );
//        // 로그아웃 (기본 설정)
//        http.logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }
}
