package com.example.backend.jupyter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JupyterSessionDto {
    private String sessionId;

    private String kernelId;
}
