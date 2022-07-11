package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
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
            userService.saveUser(new User("Georgiy", "Ivanovich", 25, "georgiy@gmail.com",
                    bCryptPasswordEncoder.encode("admin"), adminRole));
            userService.saveUser(new User("Misha", "Mikhailov", 24, "mikhailov@mail.ru",
                    bCryptPasswordEncoder.encode("user"), userRole));
            userService.saveUser(new User("Andrey", "Andreev", 18, "andreev@mail.ru",
                    bCryptPasswordEncoder.encode("andrew"), userRole));
            userService.saveUser(new User("Vasiliy", "Pupkin", 50, "pupkin@gmail.com",
                    bCryptPasswordEncoder.encode("vasyap"), userRole));
            userService.saveUser(new User("Valeriy", "Zmishenko", 54, "valera@mail.ru",
                    bCryptPasswordEncoder.encode("valera"), anyRole));
        }

        return "redirect:/login";
    }
}
