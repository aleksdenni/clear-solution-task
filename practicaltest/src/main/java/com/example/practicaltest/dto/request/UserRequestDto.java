package com.example.practicaltest.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
@Value
@Builder
@RequiredArgsConstructor
public class UserRequestDto {

    @NotBlank
    @Email(regexp = "^(?=.{10,63}$)(?!.*\\s)"
            + "[a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+"
            + "(?:[.\\-][a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+)*"
            + "@[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*"
            + "(?:\\.[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*)+",
            message = "Email should be valid")
    String email;

    @NotBlank String firstName;

    @NotBlank String lastName;

    @Past
    @NotNull LocalDate birthDate;

    String address;

    String phoneNumber;

}
