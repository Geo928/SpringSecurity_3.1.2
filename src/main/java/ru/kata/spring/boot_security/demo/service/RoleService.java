package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    Role findBiId(int id);

    Role findByRole(String role);

    List<Role> findAllRoles();

    void saveRole(Role role);

    void saveAll(Collection<Role> roles);
}
