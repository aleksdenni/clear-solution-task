package com.example.practicaltest.mapper;

import com.example.practicaltest.dto.request.UserRequestDto;
import com.example.practicaltest.dto.response.UserResponseDto;
import com.example.practicaltest.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel(UserRequestDto userRequestDto) {
        return User.builder()
                .email(userRequestDto.getEmail())
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .birthDate(userRequestDto.getBirthDate())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .address(userRequestDto.getAddress())
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
