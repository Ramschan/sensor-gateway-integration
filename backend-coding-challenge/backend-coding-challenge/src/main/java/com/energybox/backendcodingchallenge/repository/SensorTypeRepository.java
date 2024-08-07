package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.SensorType;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SensorTypeRepository extends Neo4jRepository<SensorType, String>  {
    SensorType findByName(String name);
}
