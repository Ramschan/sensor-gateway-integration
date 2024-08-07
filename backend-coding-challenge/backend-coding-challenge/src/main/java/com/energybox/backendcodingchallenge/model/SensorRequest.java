package com.energybox.backendcodingchallenge.model;

import lombok.Data;
import java.util.Set;

@Data
public class SensorRequest {
    private String name;
    private String locationCode;
    private Set<String> type;
}
