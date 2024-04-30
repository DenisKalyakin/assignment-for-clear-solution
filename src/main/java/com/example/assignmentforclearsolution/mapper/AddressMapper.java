package com.example.assignmentforclearsolution.mapper;

import com.example.assignmentforclearsolution.config.MapperConfig;
import com.example.assignmentforclearsolution.dto.address.AddressDto;
import com.example.assignmentforclearsolution.dto.address.AddressRequestDto;
import com.example.assignmentforclearsolution.model.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address toModel(AddressRequestDto addressRequestDto);
}
