package bookgle.bookgle.exception.handler;

import bookgle.bookgle.dto.ExceptionResponseBodyDto;
import bookgle.bookgle.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponseBodyDto> handleServiceException(ServiceException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus().getHttpStatus())
                .body(new ExceptionResponseBodyDto(LocalDateTime.now(), e.getStatus().getHttpStatus().value(), e.getStatus().getHttpStatus().getReasonPhrase(), e.getMessage(), request.getRequestURI()));
    }
}

