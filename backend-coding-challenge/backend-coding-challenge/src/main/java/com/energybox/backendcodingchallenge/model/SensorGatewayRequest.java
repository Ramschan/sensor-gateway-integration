package com.energybox.backendcodingchallenge.model;

import lombok.Data;

@Data
public class SensorGatewayRequest {
    private Long sensorId;
    private Long gatewayId;
}
