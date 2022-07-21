package ru.kata.spring.boot_security.demo.configs;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class PostConstructInit {

    private final UserService userService;
    private final RoleService roleService;

    public PostConstructInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        List<User> users = userService.findAllUsers();

        if (users.isEmpty()) {
            Role admin = new Role("ROLE_ADMIN");
            Role user = new Role("ROLE_USER");
            Collection<Role> adminRole = new HashSet<>();
            Collection<Role> userRole = new HashSet<>();
            Collection<Role> anyRole = new HashSet<>();
            adminRole.add(admin);
            userRole.add(user);
            anyRole.add(admin);
            anyRole.add(user);
            roleService.saveRole(admin);
            roleService.saveRole(user);
            userService.addUser(new User("Georgiy", "Ivanovich", 25, "admin@mail.ru",
                    "admin@mail.ru", adminRole));
            userService.addUser(new User("Ivan", "Ivanov", 27, "user@mail.ru",
                    "user@mail.ru", userRole));
            userService.addUser(new User("Aleksey", "Alekseevich", 45, "alex@mail.ru",
                    "alex@mail.ru", userRole));
            userService.addUser(new User("Vasiliy", "Utkin", 40, "vasya@mail.ru",
                    "vasya@mail.ru", userRole));
            userService.addUser(new User("Valeriy", "Zmishenko", 54, "valera@mail.ru",
                    "valera@mail.ru", anyRole));
        }
    }
}
