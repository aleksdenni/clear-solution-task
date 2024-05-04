package com.example.practicaltest.service;

import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.mapper.UserMapper;
import com.example.practicaltest.model.User;
import com.example.practicaltest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

    @Test
    void testCreateUser() {
        UserRequestDto userRequest = UserRequestDto.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        User user = User.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        UserResponseDto userResponse = UserResponseDto.builder()
                .email("test@example.com")
                .firstName("testName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("testAddress 123")
                .phoneNumber("012-345-67-89")
                .build();

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(userResponse);

        ResponseEntity<UserResponseDto> response = userService.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

    @Test
    void testUpdateUser() {
        UserRequestDto userRequest = new UserRequestDto(
                "test@example.com", "Updated", "Updated", LocalDate.of(2000, 1, 1), null, null
        );

        User updatedUser = User.builder()
                .email("test@example.com")
                .firstName("Updated")
                .lastName("Updated")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .email("test@example.com")
                .firstName("Updated")
                .lastName("Updated")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        when(userMapper.toModel(any(UserRequestDto.class))).thenReturn(updatedUser);
        when(userRepository.update(any(User.class), anyString())).thenReturn(updatedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userService.updateUser("test@example.com", userRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated", response.getBody().getFirstName());

    }
}