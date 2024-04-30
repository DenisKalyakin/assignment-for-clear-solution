package com.example.assignmentforclearsolution.service.address;

import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.mapper.AddressMapper;
import com.example.assignmentforclearsolution.model.Address;
import com.example.assignmentforclearsolution.repository.AddressRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public Address save(Address address) {
        Optional<Address> existingAddress = addressRepository
                .findAddressByCountryNameAndCityNameAndStreetNameAndNumberOfHouse(
                address.getCountryName(),
                address.getCityName(),
                address.getStreetName(),
                address.getNumberOfHouse()
        );
        return existingAddress.orElseGet(() -> addressRepository.save(address));
    }

    @Override
    public Address parseAddressFromUserRegistrationRequestDto(
            UserRegistrationRequestDto requestDto
    ) {
        Address address = new Address();
        address.setCountryName(requestDto.getCountryName());
        address.setCityName(requestDto.getCityName());
        address.setStreetName(requestDto.getStreetName());
        address.setNumberOfHouse(requestDto.getNumberOfHouse());
        return address;
    }

    @Override
    public Address mapToModel(AddressRequestDto requestDto) {
        return addressMapper.toModel(requestDto);
    }
}
