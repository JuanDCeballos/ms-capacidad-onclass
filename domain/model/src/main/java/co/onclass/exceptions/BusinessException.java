package co.onclass.exceptions;

import co.onclass.enums.ExceptionMessages;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionMessages exceptionMessages;

    public BusinessException(ExceptionMessages exceptionMessages) {
        super(exceptionMessages.getMessage());
        this.exceptionMessages = exceptionMessages;
    }

    public BusinessException(ExceptionMessages exceptionMessages, String message) {
        super(message);
        this.exceptionMessages = exceptionMessages;
    }
}
