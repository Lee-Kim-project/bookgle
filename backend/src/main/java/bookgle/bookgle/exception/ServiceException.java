package bookgle.bookgle.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    final private ExceptionStatus status;

    public ServiceException(ExceptionStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
