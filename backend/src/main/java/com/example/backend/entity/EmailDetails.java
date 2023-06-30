package com.example.backend.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmailDetails {
    private String recipient;

    private String msgBody;

    private String subject;
}