package com.stech.repository;

import com.stech.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    List<Role> findByRoleNameIn(List<String> roles);
}
