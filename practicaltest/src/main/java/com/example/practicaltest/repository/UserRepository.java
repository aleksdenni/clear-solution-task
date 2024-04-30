package com.example.practicaltest.repository;

import com.example.practicaltest.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public User save(final User user) {
        if (users.stream()
                .anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } else
            users.add(user);
        ResponseEntity.status(HttpStatus.CREATED);
        return user;
    }

    public String delete(String email) {
        if (users.stream()
                .anyMatch(x -> x.getEmail().equals(email))) {
            users.removeIf(x -> x.getEmail().equals(email));
            ResponseEntity.status(HttpStatus.ACCEPTED);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return null;
    }

    public List<User> findByBirthDate(LocalDate fromDate, LocalDate toDate) {
        return users.stream()
                .filter(x -> x.getBirthDate().isAfter(fromDate) && x.getBirthDate().isBefore(toDate))
                .toList();
    }
}
