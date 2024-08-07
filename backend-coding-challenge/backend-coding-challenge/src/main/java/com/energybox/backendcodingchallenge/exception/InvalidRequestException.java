package com.energybox.backendcodingchallenge.exception;

/**
 * This is used to raise the exception when we receive invalid request from client.
 */
public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
