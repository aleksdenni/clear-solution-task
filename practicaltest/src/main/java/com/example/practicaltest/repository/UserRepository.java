package com.example.practicaltest.repository;

import com.example.practicaltest.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
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

    public User findByEmail(String email) {
        return users.stream()
                .filter(x -> x.getEmail().equals(email))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with email " + email + " not found"));
    }

    public User update(User user, String email) {
        User userFound = findByEmail(email);
        int indexUserFound = users.indexOf(userFound);
        users.set(indexUserFound, user);
        return userFound; //ResponseEntity.status(HttpStatus.ACCEPTED).body(userFound);
    }

    public User patch(User user, String email) {
        User userFound = findByEmail(email);
        String userEmail;
        String firstName;
        String lastName;
        LocalDate birthDate;
        String address;
        String phoneNumber;
        if ((userEmail = user.getEmail()) != null) {
            userFound.setEmail(userEmail);
        }
        if ((firstName = user.getFirstName()) != null) {
            userFound.setFirstName(firstName);
        }
        if ((lastName = user.getLastName()) != null) {
            userFound.setLastName(lastName);
        }
        if ((birthDate = user.getBirthDate()) != null) {
            userFound.setBirthDate(birthDate);
        }
        if ((address = user.getAddress()) != null) {
            userFound.setAddress(address);
        }
        if ((phoneNumber = user.getPhoneNumber()) != null) {
            userFound.setPhoneNumber(phoneNumber);
        }
        return userFound;
    }
}
