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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final String MIN_USER_AGE = "18";  //@Value("${min.user.age}")

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    public UserResponseDto createUser(UserRequestDto userRequest) {
        if (LocalDate.now().minusYears(Integer.parseInt(MIN_USER_AGE)).isBefore(userRequest.getBirthDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public List<User> getUsers(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDate(fromDate, toDate);
    }

    public UserResponseDto patchUser(String email, PatchUserRequestDto patchUserRequestDto) {
        final User savedUser = userRepository.patch(patchUserRequestDto, email);
        return userMapper.toResponseDto(savedUser);
    }

    public UserResponseDto updateUser(String email, UserRequestDto userRequest) {
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.update(user, email);
        return userMapper.toResponseDto(savedUser);
    }

    public void deleteUser(String email) {
    }
}
