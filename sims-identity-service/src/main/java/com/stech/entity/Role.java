package com.stech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "USER_ROLES")
@SequenceGenerator(name = "user_roles_seq", sequenceName = "USER_ROLES_SEQ", allocationSize = 1)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_seq")
    private int roleId;
    @Column(nullable = false)
    private String roleName;
}
