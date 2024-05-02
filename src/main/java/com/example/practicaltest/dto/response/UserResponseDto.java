package com.example.practicaltest.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@RequiredArgsConstructor
public class UserResponseDto {

    @NotBlank String email;

    @NotBlank String firstName;

    @NotBlank String lastName;

    @NotNull LocalDate birthDate;

    String address;

    String phoneNumber;
}