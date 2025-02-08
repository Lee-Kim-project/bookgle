package bookgle.bookgle.search.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaruProperties {
    @Value("${api.key}")
    private String apiKey;
}
