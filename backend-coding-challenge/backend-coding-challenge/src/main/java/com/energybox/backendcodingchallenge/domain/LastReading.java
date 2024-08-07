package com.energybox.backendcodingchallenge.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.LocalDateTime;

@Data
@Node
public class LastReading {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime timestamp;
    private Double reading;       // reading

    public LastReading( LocalDateTime  timestamp, Double reading) {
        this.timestamp = timestamp;
        this.reading = reading;
    }
}
