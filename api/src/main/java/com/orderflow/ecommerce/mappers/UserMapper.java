package com.orderflow.ecommerce.mappers;

import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import com.orderflow.ecommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "password", source = "request.password")
    @Mapping(target = "taxId", source = "request.taxId")
    @Mapping(target = "stateRegistration", source = "request.stateRegistration")
    @Mapping(target = "phone", source = "request.phone")
    @Mapping(target = "birthDate", source = "request.birthDate")
    @Mapping(target = "taxpayer", source = "request.taxpayer")
    @Mapping(target = "googleId", source = "request.googleId")
    @Mapping(target = "street", source = "request.street")
    @Mapping(target = "complement", source = "request.complement")
    @Mapping(target = "number", source = "request.number")
    @Mapping(target = "neighborhood", source = "request.neighborhood")
    @Mapping(target = "city", source = "request.city")
    @Mapping(target = "country", source = "request.country")
    @Mapping(target = "state", source = "request.state")
    @Mapping(target = "zipCode", source = "request.zipCode")
    User toEntity(UserRequest request);
}