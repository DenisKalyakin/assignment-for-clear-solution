package com.example.assignmentforclearsolution.service.address;

import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.model.Address;

public interface AddressService {

    Address save(Address address);

    Address parseAddressFromUserRegistrationRequestDto(UserRegistrationRequestDto requestDto);

    Address mapToModel(AddressRequestDto requestDto);
}
