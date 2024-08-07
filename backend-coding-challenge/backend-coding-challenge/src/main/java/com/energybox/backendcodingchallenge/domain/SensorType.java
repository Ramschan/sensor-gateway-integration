package com.energybox.backendcodingchallenge.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@Node
@Data
public class SensorType {
    @Id
    @Property(name = "name")
    private String name;  // electricity, humidity

    public SensorType(String name) {
        this.name = name;
    }
}
