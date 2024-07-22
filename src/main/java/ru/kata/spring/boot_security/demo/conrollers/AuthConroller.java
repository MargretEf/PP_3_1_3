package ru.kata.spring.boot_security.demo.conrollers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AuthConroller {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private RoleRepository roleRepository;
    private PeopleService peopleService;
@Autowired
    public AuthConroller(PersonValidator personValidator, RegistrationService registrationService,RoleRepository roleRepository,PeopleService peopleService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.roleRepository = roleRepository;
        this.peopleService = peopleService;
    }
    @GetMapping("/users")
    @Transactional
    public String showAllUsers(Model model) {
        model.addAttribute("users", peopleService.getAllUsers());
        return "users";
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
        return "redirect:/admin/users";
    }

    @GetMapping("/show")
    public String showEditUser(Model model, @RequestParam(value = "id") long id) {
    User user = peopleService.getUser(id);
            model.addAttribute("showUser", user);
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
        registrationService.register(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/remove")
    public String removeUserId (@RequestParam(value = "id") long id, Model model) {
        peopleService.deleteUser(id);
        return "redirect:/admin/users";
    }

}
