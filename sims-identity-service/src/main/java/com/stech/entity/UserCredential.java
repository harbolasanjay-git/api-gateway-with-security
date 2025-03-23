package com.stech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_CREDENTIAL")
@SequenceGenerator(name = "user_credential_seq", sequenceName = "USER_CREDENTIAL_SEQ", allocationSize = 1)
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_credential_seq")
    private int id;
    private String name;
    private String email;
    private String password;
}
