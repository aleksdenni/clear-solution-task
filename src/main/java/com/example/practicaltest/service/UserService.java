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

    public ResponseEntity<UserResponseDto> createUser(UserRequestDto userRequest) {
        if (LocalDate.now().minusYears(Integer.parseInt(MIN_USER_AGE)).isBefore(userRequest.getBirthDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.save(user);
        UserResponseDto createdUser = userMapper.toResponseDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUser);
    }

    public ResponseEntity<List<User>> getUsers(LocalDate fromDate, LocalDate toDate) {
        List<User> foundUsers = userRepository.findByBirthDate(fromDate, toDate);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(foundUsers);
    }

    public ResponseEntity<UserResponseDto> patchUser(String email, PatchUserRequestDto patchUserRequestDto) {
        final User savedUser = userRepository.patch(patchUserRequestDto, email);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userMapper.toResponseDto(savedUser));
    }

    public ResponseEntity<UserResponseDto> updateUser(String email, UserRequestDto userRequest) {
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.update(user, email);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userMapper.toResponseDto(savedUser));
    }

    public void deleteUser(String email) {
        userRepository.delete(email);
    }
}
