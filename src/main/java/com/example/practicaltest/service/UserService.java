package com.example.practicaltest.service;

import com.example.practicaltest.dto.request.PatchUserRequestDto;
import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.mapper.UserMapper;
import com.example.practicaltest.model.User;
import com.example.practicaltest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${min.user.age}")
    private String MIN_USER_AGE;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        if (LocalDate.now().minusYears(Integer.parseInt(MIN_USER_AGE)).isBefore(userRequest.getBirthDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.save(user);
        UserResponseDto createdUser = userMapper.toResponseDto(savedUser);
        return createdUser;
    }

    public List<User> getUsers(LocalDate fromDate, LocalDate toDate) {
        List<User> foundUsers = userRepository.findByBirthDate(fromDate, toDate);
        if (foundUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else return foundUsers;
    }

    public UserResponseDto patchUser(String email, PatchUserRequestDto patchUserRequestDto) {
        final User savedUser = userRepository.patch(email, patchUserRequestDto);
        return userMapper.toResponseDto(savedUser);
    }

    public UserResponseDto updateUser(String email, UserRequestDto userRequest) {
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.update(email, user);
        return userMapper.toResponseDto(savedUser);
    }

    public void deleteUser(String email) {
        userRepository.delete(email);
    }
}
