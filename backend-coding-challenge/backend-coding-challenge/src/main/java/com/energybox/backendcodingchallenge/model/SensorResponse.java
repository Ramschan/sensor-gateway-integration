package com.energybox.backendcodingchallenge.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SensorResponse {
    private Long sensorId;
    private HttpStatus Status;
}
