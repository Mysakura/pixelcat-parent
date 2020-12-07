package com.pixelcat.core.exception;

public class PixelCatException extends RuntimeException {


    public PixelCatException() {
        super();
    }

    public PixelCatException(String message) {
        super(message);
    }

    public PixelCatException(String message, Throwable cause) {
        super(message, cause);
    }
}
