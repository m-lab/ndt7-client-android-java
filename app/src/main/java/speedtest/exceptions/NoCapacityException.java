package speedtest.exceptions;

public class NoCapacityException extends Exception {
    public NoCapacityException(String errorMessage) {
        super(errorMessage);
    }
}