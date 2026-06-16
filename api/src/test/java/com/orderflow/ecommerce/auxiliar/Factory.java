package com.orderflow.ecommerce.auxiliar;

import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import com.orderflow.ecommerce.entities.User;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class Factory {

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Bob");
        user.setEmail("bob@gmail.com");
        user.setPassword("Shh#secret0");
        user.setTaxId("12345678900");
        user.setStateRegistration("098765432");
        user.setPhone("11999999999");
        user.setBirthDate(LocalDate.of(2000, 1, 20));
        user.setTaxpayer(false);
        user.setGoogleId("google-id-000");
        user.setStreet("Rua A");
        user.setComplement("Casa");
        user.setNumber("123");
        user.setNeighborhood("Bairro");
        user.setCity("Cidade");
        user.setCountry("País");
        user.setState("SP");
        user.setZipCode("10000-000");
        return user;
    }

    public static UserRequest createUserRequest() {
        return new UserRequest(
                "Bob",
                "bob@gmail.com",
                "Shh#secret0",
                "12345678900",
                "098765432",
                "11999999999",
                LocalDate.of(2000, 1, 20),
                false,
                "google-id-000",
                "Rua A",
                "Casa",
                "123",
                "Bairro",
                "Cidade",
                "País",
                "SP",
                "10000-000");
    }

    public static UserResponse createUserResponse() {
        return new UserResponse(
                1L,
                "Bob",
                "bob@gmail.com",
                "Shh#secret0",
                "12345678900",
                "098765432",
                "11999999999",
                LocalDate.of(2000, 1, 20),
                false,
                "google-id-000",
                "Rua A",
                "Casa",
                "123",
                "Bairro",
                "Cidade",
                "País",
                "SP",
                "10000-000");
    }

}
