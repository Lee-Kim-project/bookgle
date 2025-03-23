package bookgle.bookgle.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class TokenProperties {
    @Value("${jwt.secret-key}")
    private String secret;

    @Value("${jwt.token.expiry}")
    private long expiry;
}
