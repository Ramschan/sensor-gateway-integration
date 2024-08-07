package com.energybox.backendcodingchallenge.exception;

public class SensorAlreadyConnectedException extends RuntimeException{
    public SensorAlreadyConnectedException(String message) {
        super(message);
    }
}
