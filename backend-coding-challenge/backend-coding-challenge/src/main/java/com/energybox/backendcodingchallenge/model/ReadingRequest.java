package com.energybox.backendcodingchallenge.model;

import lombok.Data;

@Data
public class ReadingRequest {
    private Long sensorId;
    private Double reading;
    private String sensorType;
}
