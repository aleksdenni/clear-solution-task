package com.example.practicaltest.controller;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.model.User;
import com.example.practicaltest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
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
    void testCreateUser() throws Exception {

        String userRequestJson = String.format(
                "{\"email\":\"%s\"," +
                        " \"firstName\":\"%s\"," +
                        " \"lastName\":\"%s\"," +
                        " \"birthDate\":\"%s\"}",
                userRequest.getEmail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getBirthDate().toString());

        when(userService.createUser(any(UserRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userResponse));
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", equalTo(userRequest.getEmail())))
                .andExpect(jsonPath("$.firstName", equalTo(userRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(userRequest.getLastName())))
                .andExpect(jsonPath("$.birthDate", equalTo(userRequest.getBirthDate().toString())));
    }

    @Test
    void testUpdateUser() throws Exception {
        String userRequestJson;
        userRequestJson = objectMapper.writeValueAsString(userRequest);

        when(userService.updateUser(any(String.class), any(UserRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse));

        mockMvc.perform(put("/api/v1/users/test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName", equalTo(userRequest.getFirstName())));
    }

}
