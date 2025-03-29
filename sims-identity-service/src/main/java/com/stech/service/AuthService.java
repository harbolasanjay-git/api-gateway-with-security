package com.stech.service;

import com.stech.dto.RoleDTO;
import com.stech.dto.UserDTO;
import com.stech.entity.Role;
import com.stech.entity.UserCredential;
import com.stech.repository.RoleRepository;
import com.stech.repository.UserCredentialRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public String saveUser(UserDTO userDTO) {
        System.out.println("Auth Service execution started..............................................");
        System.out.println(userDTO.getId()+" "+userDTO.getName() +" " +userDTO.getEmail() +" "+userDTO.getPassword()+" "+userDTO.getRoles());
        UserCredential entity = new UserCredential();
        BeanUtils.copyProperties(userDTO, entity);
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        List<Role> roles = roleRepository.findByRoleNameIn(userDTO.getRoles());
        if(userDTO.getRoles().size()!=roles.size()){
            throw new RuntimeException("Roles are not available : "+userDTO.getRoles());
        }
        entity.setRoles(Set.copyOf(roles));
        repository.save(entity);
        return  "User added to the system!!";
    }

    @Transactional
    public String loadRoles(RoleDTO roleDTO) {
        List<Role> roleList = new ArrayList<>();
        for(String roleName: roleDTO.getRoles()) {
            Role role = new Role();
            role.setRoleName(roleName);
            roleList.add(role);
        }
        roleRepository.saveAll(roleList);
        return  "Roles added to the system!!";
    }

    public String generateToken(String userName) {
        return jwtService.generateToken(userName);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
