package pl.kurs.personapp.exceptionhandling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadPositionException extends RuntimeException {

    public BadPositionException(String message) {
        super(message);
    }

    public BadPositionException(String message, Throwable cause) {
        super(message, cause);
    }
}
