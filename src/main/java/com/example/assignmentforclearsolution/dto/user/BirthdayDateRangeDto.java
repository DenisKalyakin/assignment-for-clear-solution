package com.example.assignmentforclearsolution.dto.user;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Accessors(chain = true)
public class BirthdayDateRangeDto {
    @NotNull
    @DateTimeFormat
    private LocalDate birthdayDateFrom;
    @NotNull
    @DateTimeFormat
    private LocalDate birthdayDateTo;
}
