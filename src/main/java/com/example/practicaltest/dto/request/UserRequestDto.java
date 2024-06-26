package com.example.practicaltest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
@Value
@Builder
@RequiredArgsConstructor
public class UserRequestDto {

    @NotEmpty()
    @Email(regexp = "^(?=.{10,63}$)(?!.*\\s)"
            + "[a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+"
            + "(?:[.\\-][a-zA-Z0-9!#$%&'*+\\-=?^_`{|}~]+)*"
            + "@[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*"
            + "(?:\\.[a-z0-9](?:[a-z0-9\\-]*[a-z0-9])*)+",
            message = "Email should be valid")
    String email;

    @NotEmpty String firstName;

    @NotEmpty String lastName;

    @Past
    @NotNull
    LocalDate birthDate;

    String address;

    String phoneNumber;

}
