package com.energybox.backendcodingchallenge.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Data
@Node
public class Sensor {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private String locationCode;
    @Relationship(type = "HAS_TYPE", direction = Relationship.Direction.OUTGOING)
    private Set<SensorType> types = new HashSet<>();
    @Relationship(type = "CONNECTED_TO", direction = Relationship.Direction.OUTGOING)
    private Gateway gateway;
    @Relationship(type = "HAS_LAST_READING", direction = Relationship.Direction.OUTGOING)
    private Map<SensorType, LastReading> lastReadings = new HashMap<>();

    public Sensor(String name, String locationCode){
        this.name = name;
        this.locationCode = locationCode;
    }

    // Method to add a reading
    public void addReading(SensorType type, LastReading reading) {
        lastReadings.put(type, reading);
    }
}
