package com.example.assignmentforclearsolution.dto.user;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Accessors(chain = true)
public class UserWithoutAddressRequestDto {
    @Email(message = "Email should be valid.")
    private String email;
    private String firstName;
    private String lastName;
    @DateTimeFormat
    private LocalDate birthDate;
    private String phoneNumber;
}
