package com.energybox.backendcodingchallenge.exception;

/**
 * This exception will be thrown where there is no Gateway found in database.
 */
public class GatewayNotFoundException extends RuntimeException {
    public GatewayNotFoundException(String message) {
        super(message);
    }
}
