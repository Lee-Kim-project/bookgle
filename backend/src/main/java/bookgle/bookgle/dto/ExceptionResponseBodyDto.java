package bookgle.bookgle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseBodyDto {
    private LocalDateTime timestamp;
    private int statusCode;
    private String error;
    private String errorMessage;
    private String path;
}
