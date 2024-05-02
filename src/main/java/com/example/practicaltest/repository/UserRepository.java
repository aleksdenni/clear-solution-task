package com.example.practicaltest.repository;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
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

    public User update(User user, String email) {
        int indexUserFound = users.indexOf(findByEmail(email));
        user.setEmail(email); // you can't change your email
        users.set(indexUserFound, user);
        return users.get(indexUserFound); //ResponseEntity.status(HttpStatus.ACCEPTED).body(userFound);
    }

    public User patch(PatchUserRequestDto patchUser, String email) {
        User userFound = findByEmail(email);
        String userEmail;
        String firstName;
        String lastName;
//        LocalDate birthDate;
        String address;
        String phoneNumber;
        if ((userEmail = patchUser.getEmail()) != null) {
            userFound.setEmail(userEmail);
        }
        if ((firstName = patchUser.getFirstName()) != null) {
            userFound.setFirstName(firstName);
        }
        if ((lastName = patchUser.getLastName()) != null) {
            userFound.setLastName(lastName);
        }
/*        if ((birthDate = patchUser.getBirthDate()) != null) {
            userFound.setBirthDate(birthDate);
        }*/
        if ((address = patchUser.getAddress()) != null) {
            userFound.setAddress(address);
        }
        if ((phoneNumber = patchUser.getPhoneNumber()) != null) {
            userFound.setPhoneNumber(phoneNumber);
        }
        return userFound;
    }
}
