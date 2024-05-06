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
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

        UserResponseDto response = userService.createUser(userRequest);

        assertNotNull(response);
        assertEquals("test@email.com", response.getEmail());
    }

    @Test
    void getUsers() {
        List<User> users = List.of(user);

        when(userRepository.findByBirthDate(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(users);

        List<User> response = userService.getUsers(
                LocalDate.of(1999, 1, 1),
                LocalDate.of(2000, 1, 1));

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testPatchUser() {
        user.setFirstName(patchUserRequestDto.getFirstName());
        User newUserData = user;

        when(userMapper.toModel(any(PatchUserRequestDto.class))).thenReturn(newUserData);
        when(userRepository.patch(anyString(), any(User.class)))
                .thenReturn(newUserData);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponse);

        assertDoesNotThrow(()
                -> userService.patchUser(userResponse.getEmail(), patchUserRequestDto));

        verify(userMapper).toResponseDto(user);
        verify(userRepository).patch(userResponse.getEmail(), newUserData);
        verify(userRepository, times(1)).patch(userResponse.getEmail(), newUserData);

        assertNotNull(newUserData);
        assertEquals("patchedTestName", newUserData.getFirstName());

    }

    @Test
    void testUpdateUser() {
        UserRequestDto userRequestForUpdate = UserRequestDto.builder()
                .email("test@email.com")
                .firstName("Updated")
                .lastName("Updated")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        user.setFirstName("Updated");
        user.setLastName("Updated");
        User newUserData = user;

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(newUserData);
        when(userRepository.update(anyString(), any(User.class))).thenReturn(newUserData);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponse);

        assertDoesNotThrow(()
                -> userService.updateUser(userResponse.getEmail(), userRequestForUpdate));

        assertNotNull(newUserData);
        assertEquals("Updated", newUserData.getFirstName());

    }

    @Test
    void testDeleteUser() {
        when(userRepository.delete("test@email.com")).thenReturn(null);

        assertDoesNotThrow(()
                -> userService.deleteUser("test@email.com"));

        verify(userRepository, times(1)).delete("test@email.com");
    }

    @Test
    void testDeleteWhenUserNotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(userRepository).delete("testWrong@email.com");

        assertThrows(ResponseStatusException.class, ()
                -> userService.deleteUser("testWrong@email.com"));
    }

}