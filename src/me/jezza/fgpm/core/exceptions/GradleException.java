package me.jezza.fgpm.core.exceptions;

public class GradleException extends RuntimeException {

    public GradleException() {
    }

    public GradleException(String message) {
        super(message);
    }

    public GradleException(String message, Throwable cause) {
        super(message, cause);
    }

    public GradleException(Throwable cause) {
        super(cause);
    }
}
