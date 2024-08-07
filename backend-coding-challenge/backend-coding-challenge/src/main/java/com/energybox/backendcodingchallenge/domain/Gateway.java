package com.energybox.backendcodingchallenge.domain;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;


@Data
@Node
public class Gateway {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;

    public Gateway(String name){
        this.name = name;
    }
}
