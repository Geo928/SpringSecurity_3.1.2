package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class IndexController {
    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value ="/")
    public String openStartPage() {
        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Role admin = new Role("ROLE_ADMIN");
            Role user = new Role("ROLE_USER");
            Set<Role> adminRole = new HashSet<>();
            Set<Role> userRole = new HashSet<>();
            Set<Role> anyRole = new HashSet<>();
            adminRole.add(admin);
            userRole.add(user);
            anyRole.add(admin);
            anyRole.add(user);
            userService.saveUser(new User("Maha", "Smirnova", 33, "admin", bCryptPasswordEncoder.encode("admin"), adminRole));
            userService.saveUser(new User("Misha", "Krokodilov", 24, "user", bCryptPasswordEncoder.encode("user"), userRole));
            userService.saveUser(new User("Dima", "Borisov", 18, "dimab", bCryptPasswordEncoder.encode("dimab"), userRole));
            userService.saveUser(new User("Vasya", "Pupkin", 16, "vasyap", bCryptPasswordEncoder.encode("vasyap"), userRole));
            userService.saveUser(new User("Kostya", "Gradov", 52, "kostyag", bCryptPasswordEncoder.encode("kostyag"), anyRole));
        }
        return "index";
    }
}
