package ru.kata.spring.boot_security.demo.security;
import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class PersonDetails implements UserDetails{
    private final User user;

    public PersonDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

    }


    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public String getLastname() {
        return user.getLastname();
    }

    public Long getAge() {
        return user.getAge();
    }

    public String getEmail() {
        return user.getEmail();
    }
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //тот аккаунт не заблкирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //этот аккаунт пороль не вышел срок
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //он работает
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getPerson() {
        return this.user;
    }
}

