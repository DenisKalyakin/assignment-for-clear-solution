package com.example.assignmentforclearsolution.service.user;

import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.BirthdayDateRangeDto;
import com.example.assignmentforclearsolution.dto.user.NamesRequestDto;
import com.example.assignmentforclearsolution.dto.user.PhoneNumberRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import java.util.List;

public interface UserService {
    UserResponseDto registration(UserRegistrationRequestDto registrationRequestDto);

    UserResponseDto updateNames(Long id, NamesRequestDto namesRequestDto);

    UserResponseDto updateUserAddress(Long id, AddressRequestDto requestDto);

    UserResponseDto updateUserPhoneNumber(Long id, PhoneNumberRequestDto requestDto);

    UserResponseDto update(Long id, UserWithoutAddressRequestDto requestDto);

    void deleteById(Long id);

    List<UserResponseDto> searchByBirthRange(BirthdayDateRangeDto dateRangeDto);
}
