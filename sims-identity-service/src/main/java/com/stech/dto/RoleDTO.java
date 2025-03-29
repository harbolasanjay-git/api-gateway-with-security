package com.stech.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDTO {
    public List<String> roles;
}
