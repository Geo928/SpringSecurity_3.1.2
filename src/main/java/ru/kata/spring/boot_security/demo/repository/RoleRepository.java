package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(String role);

    Collection<Role> findAllByUsersId(long id);
}
