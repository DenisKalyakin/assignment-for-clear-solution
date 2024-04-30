package com.example.assignmentforclearsolution.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Accessors(chain = true)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email(message = "Email should be valid.")
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @DateTimeFormat
    private LocalDate birthDate;
    @NotBlank
    private String countryName;
    @NotBlank
    private String cityName;
    private String streetName;
    private String numberOfHouse;
    private String phoneNumber;
}
