package com.example.assignmentforclearsolution.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PhoneNumberRequestDto {
    @NotBlank
    private String phoneNumber;
}
