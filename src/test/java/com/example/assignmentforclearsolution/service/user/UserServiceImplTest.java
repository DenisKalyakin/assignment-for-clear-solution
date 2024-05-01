package com.example.assignmentforclearsolution.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.assignmentforclearsolution.dto.address.AddressDto;
import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.BirthdayDateRangeDto;
import com.example.assignmentforclearsolution.dto.user.NamesRequestDto;
import com.example.assignmentforclearsolution.dto.user.PhoneNumberRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import com.example.assignmentforclearsolution.exception.EntityNotFoundException;
import com.example.assignmentforclearsolution.exception.InvalidDateException;
import com.example.assignmentforclearsolution.exception.RegistrationException;
import com.example.assignmentforclearsolution.mapper.UserMapper;
import com.example.assignmentforclearsolution.model.Address;
import com.example.assignmentforclearsolution.model.User;
import com.example.assignmentforclearsolution.repository.UserRepository;
import com.example.assignmentforclearsolution.service.address.AddressService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AddressService addressService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "minAgeForRegistration", 18L);
    }

    @Test
    @DisplayName("Registration a new user")
    void registration_ValidRequest_ShouldRegisterUserSuccessfully() {

        Address address = new Address();
        address.setCountryName("England");
        address.setCityName("Doncaster");
        address.setStreetName("Baker st.");
        address.setNumberOfHouse("221B");

        User user = new User();
        user.setEmail("jezza@gmail.uk");
        user.setFirstName("Jeremy");
        user.setLastName("Clarkson");
        user.setBirthDate(LocalDate.of(1960, 4, 11));
        user.setPhoneNumber("123123123");
        user.setAddress(address);

        AddressDto addressDto = new AddressDto()
                .setCountryName("England")
                .setCityName("Doncaster")
                .setStreetName("Baker st.")
                .setNumberOfHouse("221B");

        UserResponseDto expected = new UserResponseDto()
                .setEmail("jezza@gmail.uk")
                .setFirstName("Jeremy")
                .setLastName("Clarkson")
                .setBirthDate(LocalDate.of(1960, 4, 11))
                .setAddress(addressDto)
                .setPhoneNumber("123123123");
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

        when(addressService.parseAddressFromUserRegistrationRequestDto(requestDto))
                .thenReturn(address);
        when(addressService.save(address)).thenReturn(address);
        when(userMapper.toUserResponseDto(user)).thenReturn(expected);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto actual = userService.registration(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Unsuccessful attempt to register a minor user")
    void registration_MinorUser_NotSuccess() {

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setBirthDate(LocalDate.now().minusYears(17));

        assertThrows(RegistrationException.class, () -> {
            userService.registration(requestDto);
        });
    }

    @Test
    @DisplayName("Unsuccessful attempt to register a user who not born)")
    void registration_WithNotValidBirthDate_NotSuccess() {

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setBirthDate(LocalDate.now().plusYears(1));

        assertThrows(InvalidDateException.class, () -> {
            userService.registration(requestDto);
        });
    }

    @Test
    @DisplayName("Unsuccessful attempt to register a user with existing email")
    void registration_WithExistingEmail_NotSuccess() {
        String email = "jezza@example.com";
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto()
                .setBirthDate(LocalDate.now().minusYears(19))
                .setEmail(email);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(RegistrationException.class, () -> {
            userService.registration(requestDto);
        });
    }

    @Test
    @DisplayName("Update user name and lastname by valid id")
    void updateNames_validRequest_Success() {
        Long userId = 1L;
        User user = new User();
        user.setFirstName("Jeremy");
        user.setLastName("Clarkson");

        NamesRequestDto requestDto = new NamesRequestDto()
                .setFirstName("James")
                .setLastName("May");

        UserResponseDto expected = new UserResponseDto()
                .setFirstName("James")
                .setLastName("May");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(expected);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto actual = userService.updateNames(userId, requestDto);

        assertEquals(expected, actual);
        assertEquals(requestDto.getFirstName(), user.getFirstName());
        assertEquals(requestDto.getLastName(), user.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Update user address by valid id")
    void updateUserAddress_ValidRequest_Success() {
        Address expectedAddress = new Address();
        expectedAddress.setCountryName("England");
        expectedAddress.setCityName("Bristol");
        expectedAddress.setStreetName("Baker st.");
        expectedAddress.setNumberOfHouse("14");

        AddressRequestDto requestDto = new AddressRequestDto()
                .setCountryName("England")
                .setCityName("Bristol")
                .setStreetName("Baker st.")
                .setNumberOfHouse("14");
        when(addressService.mapToModel(requestDto)).thenReturn(expectedAddress);
        when(addressService.save(expectedAddress)).thenReturn(expectedAddress);
        User user = new User();
        user.setAddress(new Address());
        AddressDto expectedAddressDto = new AddressDto()
                .setCountryName("England")
                .setCityName("Bristol")
                .setStreetName("Baker st.")
                .setNumberOfHouse("14");
        UserResponseDto expected = new UserResponseDto()
                .setAddress(expectedAddressDto);
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(expected);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto actual = userService.updateUserAddress(userId, requestDto);

        assertEquals(expected, actual);
        assertEquals(expectedAddress, user.getAddress());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Update user phone number by valid id")
    void updateUserPhoneNumber_ValidRequest_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPhoneNumber("00000000");

        PhoneNumberRequestDto requestDto = new PhoneNumberRequestDto()
                .setPhoneNumber("12345678");

        UserResponseDto expected = new UserResponseDto()
                .setPhoneNumber("12345678");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(expected);

        UserResponseDto actual = userService.updateUserPhoneNumber(userId, requestDto);

        assertEquals(expected, actual);
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Unsuccessful attempt to search by not valid date range")
    void searchByBirthRange_NotValidDateRange_NotSuccess() {
        BirthdayDateRangeDto requestDto = new BirthdayDateRangeDto()
                .setBirthdayDateFrom(LocalDate.now().plusDays(1))
                .setBirthdayDateTo(LocalDate.now());

        assertThrows(InvalidDateException.class, () -> {
            userService.searchByBirthRange(requestDto);
        });
    }

    @Test
    @DisplayName("Unsuccessful attempt to update by not valid id")
    void update_NotValidId_NotSuccess() {
        Long id = 1L;
        String email = "jezza@example.com";
        UserWithoutAddressRequestDto requestDto = new UserWithoutAddressRequestDto()
                .setBirthDate(LocalDate.of(1970, 1, 1))
                .setEmail(email);

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.update(id, requestDto);
        });
    }

    @Test
    @DisplayName("Search by valid date range")
    void searchByBirthRange_ValidDateRange_Success() {
        User james = new User();
        james.setBirthDate(LocalDate.of(1963, 1, 16));
        User hammond = new User();
        hammond.setBirthDate(LocalDate.of(1969, 12, 19));
        BirthdayDateRangeDto requestDto = new BirthdayDateRangeDto()
                .setBirthdayDateFrom(LocalDate.of(1962, 1, 1))
                .setBirthdayDateTo(LocalDate.of(1970, 1, 1));

        List<User> expected = List.of(james, hammond);
        when(userRepository.getAllByBirthDateBetween(requestDto.getBirthdayDateFrom(),
                requestDto.getBirthdayDateTo())).thenReturn(expected);

        List<UserResponseDto> actual = userService.searchByBirthRange(requestDto);

        assertEquals(expected.size(), actual.size());
        verify(userRepository, times(1))
                .getAllByBirthDateBetween(requestDto.getBirthdayDateFrom(),
                        requestDto.getBirthdayDateTo());
    }
}
