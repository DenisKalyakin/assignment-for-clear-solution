package com.example.assignmentforclearsolution.service.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.model.Address;
import com.example.assignmentforclearsolution.repository.AddressRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    @DisplayName("Verification of saving a new address that is not in the db")
    void save_NewAddress_Success() {
        Address address = new Address();
        address.setCountryName("Country");
        address.setCityName("City");
        address.setStreetName("Street");
        address.setNumberOfHouse("123");

        Address expected = address;
        expected.setId(1L);

        when(addressRepository.findAddressByCountryNameAndCityNameAndStreetNameAndNumberOfHouse(
                address.getCountryName(),
                address.getCityName(),
                address.getStreetName(),
                address.getNumberOfHouse()))
                .thenReturn(Optional.empty());
        when(addressRepository.save(address)).thenReturn(expected);

        Address actual = addressService.save(address);

        assertEquals(expected, actual);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    @DisplayName("Checking the receipt (not saving) of an address that is already in the database")
    void save_ExistingAddress_Success() {
        Address address = new Address();
        address.setCountryName("Country");
        address.setCityName("City");
        address.setStreetName("Street");
        address.setNumberOfHouse("123");

        Address expected = address;
        expected.setId(1L);

        when(addressRepository.findAddressByCountryNameAndCityNameAndStreetNameAndNumberOfHouse(
                address.getCountryName(),
                address.getCityName(),
                address.getStreetName(),
                address.getNumberOfHouse()))
                .thenReturn(Optional.of(expected));

        Address actual = addressService.save(address);

        assertEquals(expected, actual);
        verify(addressRepository, times(0)).save(address);
    }

    @Test
    @DisplayName("Checking the correctness of the parsing of the address from UserRegistrationDto")
    void parseAddressFromUserRegistrationRequestDto_Success() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setEmail("jezza@gmail.uk")
                .setFirstName("Jeremy")
                .setLastName("Clarkson")
                .setBirthDate(LocalDate.of(1960, 4, 11))
                .setCountryName("England")
                .setCityName("Doncaster")
                .setStreetName("Baker st.")
                .setNumberOfHouse("221B")
                .setPhoneNumber("123123123");

        Address expected = new Address();
        expected.setCountryName("England");
        expected.setCityName("Doncaster");
        expected.setStreetName("Baker st.");
        expected.setNumberOfHouse("221B");

        Address actual = addressService.parseAddressFromUserRegistrationRequestDto(requestDto);

        assertEquals(expected, actual);
    }
}
