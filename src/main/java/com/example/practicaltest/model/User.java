package com.example.practicaltest.model;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class User {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Past
    @NotNull
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}