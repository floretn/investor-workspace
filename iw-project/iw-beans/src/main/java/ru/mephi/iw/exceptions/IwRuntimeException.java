package ru.mephi.iw.exceptions;

public class IwRuntimeException extends RuntimeException {
    public IwRuntimeException(String message) {
        super(message);
    }
    public IwRuntimeException(String message, Exception ex) {
        super(message, ex);
    }
}
