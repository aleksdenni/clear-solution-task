package com.example.practicaltest.dto.request;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class PatchUserRequestDto {

    String firstName;

    String lastName;

    @Past(message = "Birth date should be valid")
    LocalDate birthDate;

    String address;

    String phoneNumber;
}
