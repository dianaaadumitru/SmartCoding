package com.example.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roleId;

    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
