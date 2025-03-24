package bookgle.bookgle.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ServiceException extends RuntimeException{
    final private ExceptionStatus status;
}
