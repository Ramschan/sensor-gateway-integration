package com.energybox.backendcodingchallenge.exception;

public class SensorTypeNotFoundException extends RuntimeException {
    public SensorTypeNotFoundException(String message) {
        super(message);
    }
}