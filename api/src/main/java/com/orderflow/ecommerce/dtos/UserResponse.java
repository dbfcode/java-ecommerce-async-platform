package com.orderflow.ecommerce.dtos;

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
) {}


