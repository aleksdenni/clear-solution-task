package com.example.practicaltest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
public class PatchUserRequestDto {

    @NotBlank
    @Email(regexp = "^(?=.{10,63}$)(?!.*\\s)"
            + "[a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+"
            + "(?:[.\\-][a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+)*"
            + "@[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*"
            + "(?:\\.[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*)+",
            message = "Email should be valid") String email;

    @NotBlank
    String firstName;
    @NotBlank
    String lastName;

    @Past
    @NotBlank
    LocalDate birthDate;

    @NotBlank
    String address;

    @NotBlank
    String phoneNumber;
}
