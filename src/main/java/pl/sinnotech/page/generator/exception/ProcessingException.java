package pl.sinnotech.page.generator.exception;

public class ProcessingException extends Exception {

    private String message;

    public ProcessingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return getMessage();
    }

}
