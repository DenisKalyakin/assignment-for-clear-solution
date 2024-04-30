package com.example.assignmentforclearsolution.repository;

import com.example.assignmentforclearsolution.model.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByCountryNameAndCityNameAndStreetNameAndNumberOfHouse(
            String countryName,
            String cityName,
            String streetName,
            String numberOfHouse
    );
}
