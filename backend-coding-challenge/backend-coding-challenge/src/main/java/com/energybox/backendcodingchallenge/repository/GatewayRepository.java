package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface GatewayRepository  extends Neo4jRepository<Gateway, Long> {

    @Query("MATCH (g:Gateway)<-[:CONNECTED_TO]-(s:Sensor)-[:HAS_TYPE]->(t:SensorType {name: $typeName}) RETURN g")
    List<Gateway> findGatewaysWithSensorType(String typeName);
}
