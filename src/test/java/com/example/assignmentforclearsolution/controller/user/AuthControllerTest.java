package com.example.assignmentforclearsolution.controller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.assignmentforclearsolution.dto.address.AddressDto;
import com.example.assignmentforclearsolution.dto.user.UserRegistrationRequestDto;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("Successful registration")
    void registration_ValidRequest_Success() {
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

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/auth/register")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        UserResponseDto actual = objectMapper.readValue(contentAsString, UserResponseDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getAddress().getCityName(), actual.getAddress().getCityName());
    }
}
