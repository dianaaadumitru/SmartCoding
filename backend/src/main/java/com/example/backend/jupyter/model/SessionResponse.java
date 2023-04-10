package com.example.backend.jupyter.model;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {

    private String id;

    private String name;

    private String path;

    private String type;

    private Kernel kernel;

    private Notebook notebook;
}
