package com.abhiram.stocktrader.exception;

/**
 * Exception thrown when a client sends
 * an invalid request.
 */
public class BadRequestException
        extends RuntimeException {

    public BadRequestException(
            String message) {

        super(message);
    }
}