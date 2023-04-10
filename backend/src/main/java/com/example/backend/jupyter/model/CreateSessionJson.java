package com.example.backend.jupyter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSessionJson {
    private final String path;
    private final String type;

    private final String name;

    private final Kernel kernel;

    @JsonCreator
    public CreateSessionJson(@JsonProperty("path") String path, @JsonProperty("type") String type,
                             @JsonProperty("name") String name, @JsonProperty("kernel") Kernel kernel) {

        this.path = path;
        this.type = type;
        this.name = name;
        this.kernel = kernel;
    }
}


