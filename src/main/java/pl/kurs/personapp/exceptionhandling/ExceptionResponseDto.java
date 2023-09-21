package pl.kurs.personapp.exceptionhandling;

import java.time.Instant;
import java.util.List;

public class ExceptionResponseDto {

    private List<String> errorMessages;
    private String errorCode;
    private Instant timestamp;

    public ExceptionResponseDto(List<String> errorMessages, String errorCode, Instant timestamp) {
        this.errorMessages = errorMessages;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}


