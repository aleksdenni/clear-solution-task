package com.example.practicaltest.repository;

import com.example.practicaltest.model.User;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Getter
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public User save(User user) {
        if (users.stream()
                .anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } else
            users.add(user);
        return findByEmail(user.getEmail());
    }

    public String delete(String email) {
        if (users.stream()
                .anyMatch(x -> x.getEmail().equals(email))) {
            users.removeIf(x -> x.getEmail().equals(email));
            return null;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<User> findByBirthDate(LocalDate fromDate, LocalDate toDate) {
        return users.stream()
                .filter(x -> !x.getBirthDate().isAfter(toDate) // including the date of toDate
                        && !x.getBirthDate().isBefore(fromDate)) // including the date of fromDate
                .toList();
    }

    public User findByEmail(String email) {
        return users.stream()
                .filter(x -> x.getEmail().equals(email))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with email " + email + " not found"));
    }

    public User update(String email, User newUserData) {
        if (!email.equals(newUserData.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " you can't change your email ");
        }
        int indexFoundUser = users.indexOf(findByEmail(email));
        users.set(indexFoundUser, newUserData);
        return users.get(indexFoundUser);
    }

    public User patch(String email, User newUserData) {
        User foundUser = findByEmail(email);
        newUserData.setEmail(email); // you can't change your email
        String firstName;
        String lastName;
        LocalDate birthDate;
        String address;
        String phoneNumber;
        if ((firstName = newUserData.getFirstName()) != null) {
            foundUser.setFirstName(firstName);
        }
        if ((lastName = newUserData.getLastName()) != null) {
            foundUser.setLastName(lastName);
        }
        if ((birthDate = newUserData.getBirthDate()) != null) {
            foundUser.setBirthDate(birthDate);
        }
        if ((address = newUserData.getAddress()) != null) {
            foundUser.setAddress(address);
        }
        if ((phoneNumber = newUserData.getPhoneNumber()) != null) {
            foundUser.setPhoneNumber(phoneNumber);
        }
        return foundUser;
    }
}
