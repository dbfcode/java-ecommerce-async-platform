package com.orderflow.ecommerce.dtos;

import com.orderflow.ecommerce.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String name,
        String email,
        String password,
        String taxId,
        String stateRegistration,
        String phone,
        LocalDate birthDate,
        Boolean taxpayer,
        String googleId,
        String street,
        String complement,
        String number,
        String neighborhood,
        String city,
        String country,
        String state,
        String zipCode
) {
    public UserResponse(User entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword(), entity.getTaxId(), entity.getStateRegistration(), entity.getPhone(), entity.getBirthDate(), entity.getTaxpayer(), entity.getGoogleId(), entity.getStreet(), entity.getComplement(), entity.getNumber(), entity.getNeighborhood(), entity.getCity(), entity.getCountry(), entity.getState(), entity.getZipCode());
    }
}


