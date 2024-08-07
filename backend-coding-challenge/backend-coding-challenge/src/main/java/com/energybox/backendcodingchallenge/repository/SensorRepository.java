package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.LastReading;
import com.energybox.backendcodingchallenge.domain.Sensor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorRepository extends Neo4jRepository<Sensor, Long> {

    @Query("MATCH (s:Sensor)-[:CONNECTED_TO]->(g:Gateway) WHERE g.id = $gatewayId RETURN s")
    List<Sensor> findAllByGatewayId(Long gatewayId);

    @Query("MATCH (s:Sensor) WHERE $typeName IN s.types RETURN s")
    List<Sensor> findSensorsByTypeName(@Param("typeName") String typeName);

}
