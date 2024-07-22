package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.List;

@Service

public class PeopleServiceImp implements PeopleService {

   private PeopleRepository peopleRepository;
    @Autowired
    public PeopleServiceImp(PeopleRepository peopleService) {
        this.peopleRepository =peopleService;
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
}
