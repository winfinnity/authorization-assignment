package nl.rabobank.exceptions;

public class DuplicateAuthorizationException extends RuntimeException {
    public DuplicateAuthorizationException() {
        super("Authorization already exist");
    }
}
