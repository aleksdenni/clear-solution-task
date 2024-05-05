package com.example.practicaltest.service;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.mapper.UserMapper;
import com.example.practicaltest.model.User;
import com.example.practicaltest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "min.user.age=18")
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    private User user;
    private UserRequestDto userRequest;
    private UserResponseDto userResponse;
    private PatchUserRequestDto patchUserRequestDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@email.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userRequest = UserRequestDto.builder()
                .email("test@email.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        userResponse = UserResponseDto.builder()
                .email("test@email.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        patchUserRequestDto = PatchUserRequestDto.builder()
                .firstName("patchedTestName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();
    }

    @Test
    void testCreateUser() {

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponse);

        ResponseEntity<UserResponseDto> response = userService.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@email.com", response.getBody().getEmail());
    }

    @Test
    void getUsers() {
        List<User> users = Arrays.asList(user);

        when(userRepository.findByBirthDate(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(users);

        ResponseEntity<List<User>> response = userService.getUsers(
                LocalDate.of(1999, 1, 1),
                LocalDate.of(2000, 1, 1));

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testPatchUser() {
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponse);
        when(userRepository.patch(any(PatchUserRequestDto.class), any(String.class)))
                .thenReturn(user);

        ResponseEntity<UserResponseDto> actualPatchUserResult = userService
                .patchUser(userResponse.getEmail(), patchUserRequestDto);

        assertTrue(actualPatchUserResult.hasBody());
        assertTrue(actualPatchUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.ACCEPTED, actualPatchUserResult.getStatusCode());
        verify(userMapper).toResponseDto(any(User.class));
        verify(userRepository).patch(any(PatchUserRequestDto.class), any(String.class));
    }

    @Test
    void testUpdateUser() {
        UserRequestDto userRequest = new UserRequestDto(
                "test@email.com", "Updated", "Updated", LocalDate.of(2000, 1, 1), null, null
        );

        User updatedUser = User.builder()
                .email("test@email.com")
                .firstName("Updated")
                .lastName("Updated")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .email("test@email.com")
                .firstName("Updated")
                .lastName("Updated")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(updatedUser);
        when(userRepository.update(any(User.class), anyString())).thenReturn(updatedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userService.updateUser("test@email.com", userRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated", response.getBody().getFirstName());

    }

    @Test
    void testDeleteUser() {
        when(userRepository.delete("test@email.com")).thenReturn(null);

        assertDoesNotThrow(() -> {
            userService.deleteUser("test@email.com");
        });

        verify(userRepository, times(1)).delete("test@email.com");
    }

    @Test
    void testDeleteWhenUserNotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(userRepository).delete("testWrong@email.com");

        assertThrows(ResponseStatusException.class, () -> {
            userService.deleteUser("testWrong@email.com");
        });
    }

}