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
    @Value("${min.user.age}")
    private String minUserAge;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequest) {
        checkAgeOfMajority(userRequest.getBirthDate());
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public List<User> getUsers(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDate(fromDate, toDate);
    }

    /**
     * @param email               email of the user being updated
     * @param patchUserRequestDto new data of the user without email
     *                            and with changeable fields for patch
     * @return patched user
     */
    public UserResponseDto patchUser(String email, PatchUserRequestDto patchUserRequestDto) {
        if (patchUserRequestDto.getBirthDate() != null) {
            checkAgeOfMajority(patchUserRequestDto.getBirthDate());
        }
        final User user = userMapper.toModel(patchUserRequestDto);
        final User savedUser = userRepository.patch(email, user);
        return userMapper.toResponseDto(savedUser);
    }

    /**
     * @param email       email of the user being updated
     * @param userRequest new data of the user with all fields for update
     * @return updated user
     */
    public UserResponseDto updateUser(String email, UserRequestDto userRequest) {
        checkAgeOfMajority(userRequest.getBirthDate());
        final User user = userMapper.toModel(userRequest);
        final User savedUser = userRepository.update(email, user);
        return userMapper.toResponseDto(savedUser);
    }

    public void deleteUser(String email) {
        userRepository.delete(email);
    }

    public void checkAgeOfMajority(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Must be valid date");
        }
        if (LocalDate.now().minusYears(Integer.parseInt(minUserAge)).isBefore(birthDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Must be " + minUserAge + " years or older");
        }
    }
}