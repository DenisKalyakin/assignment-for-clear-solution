package com.example.assignmentforclearsolution.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(name = "addresses")
@SQLDelete(sql = "UPDATE addresses SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country_name", nullable = false)
    private String countryName;
    @Column(name = "city_name", nullable = false)
    private String cityName;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "number_of_house")
    private String numberOfHouse;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
