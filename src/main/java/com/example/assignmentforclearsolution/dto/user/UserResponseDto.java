package com.example.assignmentforclearsolution.dto.user;

import com.example.assignmentforclearsolution.dto.address.AddressDto;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;
    private AddressDto address;
}
