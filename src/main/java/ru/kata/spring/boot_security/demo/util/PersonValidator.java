package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;
@Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz
        );
    }
//Проверяем есть ли пользователь,лучше создать пипол сервер и на нем создать проверяющий метод
    @Override
    public void validate(Object target, Errors errors) {
        User person =(User)target;

        try {
            personDetailsService.loadUserByUsername(person.getEmail());

        }catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("email", "Invalid email address");


    }
}
