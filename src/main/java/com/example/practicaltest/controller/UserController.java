package com.example.practicaltest.controller;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.model.User;
import com.example.practicaltest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/search")
    public List<User> getUsers(@RequestParam LocalDate fromBirthDate,
                               @RequestParam LocalDate toBirthDate) {
        return userService.getUsers(fromBirthDate, toBirthDate);
    }

    @PatchMapping("/{email}")
    public UserResponseDto patchUser(@PathVariable String email, @RequestBody PatchUserRequestDto patchUserRequestDto){
        return userService.patchUser(email, patchUserRequestDto);
    }

    @PutMapping("/{email}")
    public UserResponseDto updateUser(@PathVariable String email, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(email, userRequestDto);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}