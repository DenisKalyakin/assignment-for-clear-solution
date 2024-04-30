package com.example.assignmentforclearsolution.service.user;

import com.example.assignmentforclearsolution.dto.user.BirthdayDateRangeDto;
import com.example.assignmentforclearsolution.dto.user.PhoneNumberRequestDto;
import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.NamesRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${min.registration.age}")
    private Long minAgeForRegistration;
    private final AddressService addressService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Override
    public UserResponseDto registration(UserRegistrationRequestDto registrationRequestDto) {
        checkUserAge(registrationRequestDto.getBirthDate());
        checkExistingEmail(registrationRequestDto.getEmail());
        Address address = addressService
                .parseAddressFromUserRegistrationRequestDto(registrationRequestDto);
        Address savedAddress = addressService.save(address);
        User user = parseUserFromUserRegistrationRequestDto(registrationRequestDto);
        user.setAddress(savedAddress);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateNames(Long id, NamesRequestDto namesRequestDto) {
        User user = findUserById(id);
        user.setFirstName(namesRequestDto.getFirstName());
        user.setLastName(namesRequestDto.getLastName());
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUserAddress(Long id, AddressRequestDto requestDto) {
        Address address = addressService.mapToModel(requestDto);
        Address savedAddress = addressService.save(address);
        User user = findUserById(id);
        user.setAddress(savedAddress);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUserPhoneNumber(Long id, PhoneNumberRequestDto requestDto) {
        User user = findUserById(id);
        user.setPhoneNumber(requestDto.getPhoneNumber());
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto update(Long id, UserWithoutAddressRequestDto requestDto) {
        checkUserAge(requestDto.getBirthDate());
        checkExistingEmail(requestDto.getEmail());
        User userById = findUserById(id);
        User user = userMapper.toModel(requestDto);
        user.setId(id);
        user.setAddress(userById.getAddress());
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public List<UserResponseDto> searchByBirthRange(BirthdayDateRangeDto dateRangeDto) {
        checkDateRange(dateRangeDto.getBirthdayDateFrom(), dateRangeDto.getBirthdayDateTo());
        return userRepository
                .getAllByBirthDateBetween(dateRangeDto.getBirthdayDateFrom(),
                        dateRangeDto.getBirthdayDateTo())
                .stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id: " + id)
        );
    }

    private User parseUserFromUserRegistrationRequestDto(
            UserRegistrationRequestDto registrationRequestDto
    ) {
        User user = new User();
        user.setEmail(registrationRequestDto.getEmail());
        user.setFirstName(registrationRequestDto.getFirstName());
        user.setLastName(registrationRequestDto.getLastName());
        user.setBirthDate(registrationRequestDto.getBirthDate());
        user.setPhoneNumber(registrationRequestDto.getPhoneNumber());
        return user;
    }

    @SneakyThrows
    private void checkUserAge(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Value must be earlier than current date");
        }
        if (birthDate.plusYears(minAgeForRegistration).isAfter(LocalDate.now())) {
            throw new RegistrationException("Age is less than 18.");
        }
    }

    @SneakyThrows
    private void checkExistingEmail(String email) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new RegistrationException("Such email already exists");
        }
    }

    @SneakyThrows
    private void checkDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new InvalidDateException(
                    "Date 'from' should be before than 'to', or equal 'to'!"
            );
        }
    }
}
