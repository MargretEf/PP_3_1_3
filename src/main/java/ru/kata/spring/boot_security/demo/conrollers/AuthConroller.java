package ru.kata.spring.boot_security.demo.conrollers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AuthConroller {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private PeopleRepository peopleRepository;
    private RoleRepository roleRepository;
@Autowired
    public AuthConroller(PersonValidator personValidator, RegistrationService registrationService,PeopleRepository peopleRepository,RoleRepository roleRepository) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.peopleRepository = peopleRepository;
        this.roleRepository = roleRepository;
    }
    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", peopleRepository.findAll());
        return "users";
    }
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("person", user);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("allRoles", roles);

        return "auth/registration";
    }
    @PostMapping("/registration")
    public String perfomregistration(@ModelAttribute("person") User user, BindingResult bindingResult){
        personValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRole()) {
            roles.add(roleRepository.findById(role.getId()).get());
        }
        user.setRole(roles);
registrationService.register(user);
        return "redirect:/admin/login";
    }

    @GetMapping("/show")
    public String showEditUser(Model model, @RequestParam(value = "id") long id) {
        Optional<User> userOptional = peopleRepository.findById(id);
            model.addAttribute("showUser", userOptional.get());
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("allRoles", roles);
            return "auth/edit";
        }

    @PostMapping("/edit")
    public String showEditUser(@ModelAttribute("showUser") User user) {
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRole()) {
            roles.add(roleRepository.findById(role.getId()).orElse(null));
        }
        user.setRole(roles);
        peopleRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/remove")
    public String removeUserId (@RequestParam(value = "id") long id, Model model) {
        peopleRepository.deleteById(id);
        return "redirect:/admin/users";
    }

}
