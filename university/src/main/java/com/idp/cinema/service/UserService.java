package com.idp.cinema.service;

import com.idp.cinema.model.*;
import com.idp.cinema.repository.ApplicationRepository;
import com.idp.cinema.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationRepository applicationRepository;
    private final UniversityService universityService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationRepository applicationRepository, UniversityService universityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationRepository = applicationRepository;
        this.universityService = universityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null)
            return user;
        else
            throw new UsernameNotFoundException(String.format("User with username - %s not found", username));
    }


    public UserView create(CreateUserRequest request) throws ValidationException {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ValidationException("Username exists!");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }
        if (request.getAuthorities() == null) {
            HashSet<String> authorities = new HashSet<>();
            authorities.add(Role.USER_STUDENT);
            request.setAuthorities(authorities);
        }

        User user = new User(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        return new UserView(user);
    }

    public UserView grantAdmin(String username) {
        User user = userRepository.findByUsername(username);
        Set<String> authorities = new HashSet<>();
        authorities.add(Role.USER_ADMIN);
        authorities.add(Role.USER_STUDENT);
        user.setAuthorities(authorities.stream().map(Role::new).collect(Collectors.toSet()));

        return new UserView(userRepository.save(user));
    }

    public UserView denyAdmin(String username) {
        User user = userRepository.findByUsername(username);
        Set<String> authorities = new HashSet<>();
        authorities.add(Role.USER_STUDENT);
        user.setAuthorities(authorities.stream().map(Role::new).collect(Collectors.toSet()));

        return new UserView(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String username) {
        List<Application> applications = applicationRepository.findAllByUsername(username);
        applications.forEach(application -> universityService.deleteApplication(application.getId(), application.getUsername()));
        userRepository.deleteByUsername(username);
    }

    public UserView updateUser(String username, CreateUserRequest request) throws ValidationException {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ValidationException("Username exists!");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords don't match!");
        }
        List<Application> applications = applicationRepository.findAllByUsername(username);

        User user = userRepository.findByUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());

        applications.forEach(application -> {
            application.setUsername(request.getUsername());
            applicationRepository.save(application);
        });
        return new UserView(userRepository.save(user));
    }
}
