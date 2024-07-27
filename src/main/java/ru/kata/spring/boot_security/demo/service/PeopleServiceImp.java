package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class PeopleServiceImp implements PeopleService {

   private final PeopleRepository peopleRepository;
   private final PasswordEncoder passwordEncoder;
   private final RoleRepository roleRepository;
    @Autowired
    public PeopleServiceImp(PeopleRepository peopleService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository =peopleService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {

        return peopleRepository.findAll();
    }

    @Transactional
    @Override
    public User getUser(long id) {

       return peopleRepository.getById(id);
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        peopleRepository.deleteById(id);
        ;

    }
    @Transactional
    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepository.save(user);
    }
    @Transactional
    @Override
    public void save(User user) {
        peopleRepository.save(user);
    }

    @Transactional
    @Override
    public void edit(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        var currentPassword = peopleRepository.findById(user.getId()).get().getPassword();
        if (password.equals(currentPassword)) {
            peopleRepository.save(user);
        } else {
            user.setPassword(encode);
            peopleRepository.save(user);
        }
    }
    @Transactional
    @Override
    public void setRole(User user){
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRole()) {
            roles.add(roleRepository.findById(role.getId()).get());
        }
        user.setRole(roles);
    }
}
