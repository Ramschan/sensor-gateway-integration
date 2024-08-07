package com.energybox.backendcodingchallenge.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GatewayResponse {
    private HttpStatus status;
    private Long gateWayId;
}
