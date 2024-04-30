package com.example.assignmentforclearsolution.mapper;

import com.example.assignmentforclearsolution.config.MapperConfig;
import com.example.assignmentforclearsolution.dto.user.UserResponseDto;
import com.example.assignmentforclearsolution.dto.user.UserWithoutAddressRequestDto;
import com.example.assignmentforclearsolution.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    User toModel(UserWithoutAddressRequestDto requestDto);
}
