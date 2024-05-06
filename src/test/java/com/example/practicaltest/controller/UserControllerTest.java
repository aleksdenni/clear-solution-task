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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private String baseUri;
    private User user;
    private UserRequestDto userRequest;
    private UserResponseDto userResponse;
    private PatchUserRequestDto patchUserRequestDto;

    @BeforeEach
    public void setUp() {

        baseUri = "/api/v1/users";

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

        String userRequestJson = objectMapper.writeValueAsString(userRequest);

        when(userService.createUser(any(UserRequestDto.class)))
                .thenReturn(userResponse);
        mockMvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", equalTo(userRequest.getEmail())))
                .andExpect(jsonPath("$.firstName", equalTo(userRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(userRequest.getLastName())))
                .andExpect(jsonPath("$.birthDate", equalTo(userRequest.getBirthDate().toString())));
    }

    @Test
    void getUsers() throws Exception {
        List<User> foundUsers = new ArrayList<>(List.of(user));
        when(userService.getUsers(any(LocalDate.class), any(LocalDate.class))).thenReturn(foundUsers);

        mockMvc.perform(get(baseUri + "/search")
                        .param("fromBirthDate", "1999-01-01")
                        .param("toBirthDate", "2001-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",equalTo(1)))
                .andExpect(jsonPath("$[0].email").value("test@email.com"));
    }

    @Test
    void testPatchUser() throws Exception{
        userResponse = objectMapper.updateValue(userResponse, patchUserRequestDto);

        when(userService.patchUser(anyString(), any(PatchUserRequestDto.class))).thenReturn(userResponse);
        
        String patchRequestJson = objectMapper.writeValueAsString(patchUserRequestDto);

        mockMvc.perform(patch(baseUri + "/test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchRequestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName").value("patchedTestName"));

    }

    @Test
    void testUpdateUser() throws Exception {
        String userRequestJson = objectMapper.writeValueAsString(userRequest);

        when(userService.updateUser(anyString(), any(UserRequestDto.class)))
                .thenReturn(userResponse);

        mockMvc.perform(put(baseUri + "/test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName", equalTo(userRequest.getFirstName())));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete(baseUri + "/test@email.com"))
                .andExpect(status().isNoContent());
    }
}
