package bookgle.bookgle.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "검색한 책이 존재하지 않습니다.");

    final int statusCode;
    final String message;
    ExceptionStatus(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.message = message;
    }
}
