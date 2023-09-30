package pl.kurs.personapp.exceptionhandling.exceptions;

public class ImportLockedException extends RuntimeException{

    public ImportLockedException(String message) {
        super(message);
    }

    public ImportLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
