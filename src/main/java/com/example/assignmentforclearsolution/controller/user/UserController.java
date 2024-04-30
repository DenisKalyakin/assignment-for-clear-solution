package com.example.assignmentforclearsolution.controller.user;


import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.BirthdayDateRangeDto;
import com.example.assignmentforclearsolution.dto.user.NamesRequestDto;
import com.example.assignmentforclearsolution.dto.user.PhoneNumberRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.example.assignmentforclearsolution.service.user.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/{id}/name")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateFirstAndLastNames(
            @PathVariable Long id,
            @RequestBody
            @Valid NamesRequestDto namesRequestDto
    ) {
        return userService.updateNames(id, namesRequestDto);
    }

    @PatchMapping("/{id}/address")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserAddress(
            @RequestBody @Valid AddressRequestDto requestDto,
            @PathVariable Long id
    ) {
        return userService.updateUserAddress(id, requestDto);
    }

    @PatchMapping("/{id}/phone")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserPhoneNumber(
            @RequestBody @Valid PhoneNumberRequestDto requestDto,
            @PathVariable Long id
    ) {
        return userService.updateUserPhoneNumber(id, requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid UserWithoutAddressRequestDto requestDto
    ) {
        return userService.update(id, requestDto);
    }

    @GetMapping("/birth-range")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> searchByBirthRange(
            @RequestBody @Valid BirthdayDateRangeDto dateRangeDto
    ) {
        return userService.searchByBirthRange(dateRangeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
