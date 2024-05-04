package com.example.practicaltest.controller;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.model.User;
import com.example.practicaltest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> getUsers(@RequestParam LocalDate fromBirthDate,
                               @RequestParam LocalDate toBirthDate) {
        return userService.getUsers(fromBirthDate, toBirthDate);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserResponseDto> patchUser(@PathVariable String email, @Valid @RequestBody PatchUserRequestDto patchUserRequestDto){
        return userService.patchUser(email, patchUserRequestDto);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String email, @Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(email, userRequestDto);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}