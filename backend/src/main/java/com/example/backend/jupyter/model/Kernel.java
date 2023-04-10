package com.example.backend.jupyter.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Kernel {
    private String name;

    private String id;

    private String lastActivity;

    private String executeState;

    private String connections;
}
