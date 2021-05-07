package ru.mephi.iw.exceptions;

public class IwRuntimeException extends RuntimeException {
    public IwRuntimeException(String message) {
        super(message);
    }

    public IwRuntimeException(Exception ex) {
        super(ex);
    }

    public IwRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}
