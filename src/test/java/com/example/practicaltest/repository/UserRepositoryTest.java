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
class UserRepositoryTest {

    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();

        user = User.builder()
                .email("test@email.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();
    }

    @Test
    void testSaveUser() {

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals("test@email.com", savedUser.getEmail());
        assertEquals(1, userRepository.getUsers().size());
    }

    @Test
    void testSaveUserWhenUserIsAlreadyExists() {

        userRepository.save(user);

        assertThrows(ResponseStatusException.class, ()
                -> userRepository.save(user));
    }

    @Test
    void testFindByBirthDate() {
        User user1 = user;

        User user2 = User.builder()
                .email("test2@email.com")
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
    void testPatchUser() {
        User existingUser = user;

        PatchUserRequestDto patchRequest = PatchUserRequestDto.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRepository.save(existingUser);

        User patchedUser = userRepository.patch("test@email.com", patchRequest);

        assertNotNull(patchedUser);
        assertEquals("UpdatedFirstName", patchedUser.getFirstName());
        assertEquals("UpdatedLastName", patchedUser.getLastName());
    }

    @Test
    void testUpdateUser() {
        User existingUser = user;

        userRepository.save(existingUser);

        User newUser = User.builder()
                .email("test@email.com")
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User updatedUser = userRepository.update("test@email.com", newUser);

        assertNotNull(updatedUser);
        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
    }

    @Test
    void testDeleteUser() {

        userRepository.save(user);

        assertDoesNotThrow(() -> {
            userRepository.delete("test@email.com");
        });

        assertEquals(0, userRepository.getUsers().size());
    }

    @Test
    void testDeleteUserWhenUserIsNotFound() {
        userRepository.save(user);

        assertThrows(ResponseStatusException.class, ()
                -> userRepository.delete("unknown@email.com"));
    }
}
