package com.example.practicaltest.repository;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class testsTest {

    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();

        User user = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();
    }

    @Test
    void testSaveUser() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(1, userRepository.getUsers().size());
    }

    @Test
    void testSaveUserWhenUserIsAlreadyExists() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRepository.save(user);

        assertThrows(ResponseStatusException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRepository.save(user);

        assertDoesNotThrow(() -> {
            userRepository.delete("test@example.com");
        });

        assertEquals(0, userRepository.getUsers().size());
    }

    @Test
    void testDeleteUserWhenUserIsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            userRepository.delete("unknown@example.com");
        });
    }

    @Test
    void testFindByBirthDate() {
        User user1 = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User user2 = User.builder()
                .email("test2@example.com")
                .firstName("secondTestName")
                .lastName("secondTestLastName")
                .birthDate(LocalDate.of(1995, 1, 1))
                .address("testAddress 456")
                .phoneNumber("345-67-89-012")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> foundUsers = userRepository.findByBirthDate(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2000, 12, 31)
        );

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    void testUpdateUser() {
        User existingUser = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRepository.save(existingUser);

        User newUser = User.builder()
                .email("test@example.com")
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User updatedUser = userRepository.update(newUser, "test@example.com");

        assertNotNull(updatedUser);
        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
    }

    @Test
    void testPatchUser() {
        User existingUser = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRepository.save(existingUser);

        PatchUserRequestDto patchRequest = PatchUserRequestDto.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User patchedUser = userRepository.patch(patchRequest, "test@example.com");

        assertNotNull(patchedUser);
        assertEquals("UpdatedFirstName", patchedUser.getFirstName());
        assertEquals("UpdatedLastName", patchedUser.getLastName());
    }
}
