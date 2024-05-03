package com.example.assignmentforclearsolution.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignmentforclearsolution.dto.address.AddressDto;
import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.dto.user.BirthdayDateRangeDto;
import com.example.assignmentforclearsolution.dto.user.NamesRequestDto;
import com.example.assignmentforclearsolution.dto.user.PhoneNumberRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "databases/users/add-user-and-adrs-to-users-and-addresses-table.sql"
                    )
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    private static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "databases/users/delete-all-from-users-and-addresses-table.sql"
                    )
            );
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully updating the name of the user.")
    void updateFirstAndLastNames_ValidRequest_Success() {
        NamesRequestDto requestDto = new NamesRequestDto()
                .setFirstName("Richard")
                .setLastName("Hammond");
        UserResponseDto expected = new UserResponseDto()
                .setEmail("jezza@gmail.uk")
                .setFirstName("Richard")
                .setLastName("Hammond")
                .setBirthDate(LocalDate.of(1960, 4, 11))
                .setAddress(new AddressDto())
                .setPhoneNumber("123123123");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        patch("/users/1/name")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserResponseDto actual = objectMapper.readValue(contentAsString, UserResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully updating the address of the user.")
    void updateUserAddress_ValidRequest_Success() {
        AddressRequestDto requestDto = new AddressRequestDto()
                .setCountryName("England")
                .setCityName("Bristol")
                .setStreetName("Baker st.")
                .setNumberOfHouse("14");
        AddressDto addressDto = new AddressDto()
                .setCountryName("England")
                .setCityName("Bristol")
                .setStreetName("Baker st.")
                .setNumberOfHouse("14");
        UserResponseDto expected = new UserResponseDto()
                .setEmail("jezza@gmail.uk")
                .setFirstName("Jeremy")
                .setLastName("Clarkson")
                .setBirthDate(LocalDate.of(1960, 4, 11))
                .setAddress(addressDto)
                .setPhoneNumber("123123123");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        patch("/users/1/address")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserResponseDto actual = objectMapper.readValue(contentAsString, UserResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected.getAddress().getCityName(), actual.getAddress().getCityName());
        assertEquals(expected.getAddress().getNumberOfHouse(),
                actual.getAddress().getNumberOfHouse());
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully updating the phone number of the user.")
    void updatePhoneNumber_ValidRequest_Success() {
        PhoneNumberRequestDto requestDto = new PhoneNumberRequestDto()
                .setPhoneNumber("88888888");
        UserResponseDto expected = new UserResponseDto()
                .setEmail("jezza@gmail.uk")
                .setFirstName("Jeremy")
                .setLastName("Clarkson")
                .setBirthDate(LocalDate.of(1960, 4, 11))
                .setAddress(new AddressDto())
                .setPhoneNumber("88888888");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        patch("/users/1/phone")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserResponseDto actual = objectMapper.readValue(contentAsString, UserResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully updating the user without address.")
    void update_ValidRequest_Success() {
        UserWithoutAddressRequestDto requestDto = new UserWithoutAddressRequestDto()
                .setEmail("wilman@example.com")
                .setFirstName("Andy")
                .setLastName("Wilman")
                .setBirthDate(LocalDate.of(1962, 8, 16))
                .setPhoneNumber("44444444");
        UserResponseDto expected = new UserResponseDto()
                .setEmail("wilman@example.com")
                .setFirstName("Andy")
                .setLastName("Wilman")
                .setBirthDate(LocalDate.of(1962, 8, 16))
                .setAddress(new AddressDto())
                .setPhoneNumber("44444444");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/users/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserResponseDto actual = objectMapper.readValue(contentAsString, UserResponseDto.class);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id", "address"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully search by birth range.")
    void searchByBirthRange_ValidRequest_Success() {
        BirthdayDateRangeDto requestDto = new BirthdayDateRangeDto()
                .setBirthdayDateFrom(LocalDate.of(1968, 1, 1))
                .setBirthdayDateTo(LocalDate.now());
        List<UserResponseDto> expected = new ArrayList<>();
        expected.add(new UserResponseDto()
                .setFirstName("Richard")
                .setLastName("Hammond")
                .setBirthDate(LocalDate.of(1969, 12, 19)));
        expected.add(new UserResponseDto()
                .setFirstName("Denis")
                .setLastName("Kaliakin")
                .setBirthDate(LocalDate.of(1990, 8, 29)));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        get("/users/birth-range")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), UserResponseDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertTrue(Arrays.stream(actual)
                .allMatch(u -> u.getBirthDate().isAfter(requestDto.getBirthdayDateFrom())
                && u.getBirthDate().isBefore(requestDto.getBirthdayDateTo())));
    }

    @Test
    @DisplayName("Successfully delete user by id")
    @Sql(scripts = {
            "classpath:databases/users/add-user-to-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/users/delete-user-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_WithAvailableId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        delete("/users/9")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        int expected = 204;
        int actual = result.getResponse().getStatus();
        assertEquals(expected, actual);
    }
}
