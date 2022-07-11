package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public AdminController(UserService userService, BCryptPasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String findAll(Model model, Principal principal) {
        List<User> list = userRepository.findAll();
        model.addAttribute("users", list);
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("thisUser", user);
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/add")
    public String addUser(Model model, Principal principal) {
        model.addAttribute("newUser", new User());
        model.addAttribute("role", new ArrayList<Role>());
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("activeUser", user);
        return "createUser";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user, @RequestParam(value = "role") String[] roles) {
        ArrayList<Role> rolesList = userService.getRoleCollectionToStringArray(roles);
        roleRepository.saveAll(rolesList);
        user.setRoles(rolesList);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        model.addAttribute("user", userRepository.findUserById(id));
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("activeUser", user);
        return "admin";
    }

    @PatchMapping("/update/{id}")
    public String update(@ModelAttribute("user") User user,  @RequestParam(value = "role") String[] roles,
                             Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userRepository.findUserById(id));
        ArrayList<Role> roleList = userService.getRoleCollectionToStringArray(roles);
        roleRepository.saveAll(roleList);
        if (user.getPassword() == null || user.getPassword().equals("")
                || user.getPassword().equals(userRepository.findUserById(id).getPassword())) {
            user.setPassword(userRepository.findUserById(id).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(roleList);
        userRepository.saveAndFlush(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

}
