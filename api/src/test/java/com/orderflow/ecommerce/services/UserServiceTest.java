package com.orderflow.ecommerce.services;

import com.orderflow.ecommerce.auxiliar.Factory;
import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import com.orderflow.ecommerce.entities.User;
import com.orderflow.ecommerce.exceptions.DuplicateResourceValidationException;
import com.orderflow.ecommerce.mappers.UserMapper;
import com.orderflow.ecommerce.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    private Long existingId, nonExistingId, dependentId;
    private String existingUserEmail, nonExistingUserEmail, existingTaxId;
    private User user;
    private UserResponse userResponse;
    private UserRequest userRequest;
    private PageImpl<User> page;

    @BeforeEach
    void setUp() throws Exception {
        user = Factory.createUser();
        userResponse = Factory.createUserResponse();
        userRequest = Factory.createUserRequest();

        existingId = user.getId();
        nonExistingId = 2L;
        dependentId = 3L;
        existingUserEmail = user.getEmail();
        nonExistingUserEmail = "newuser@gmail.com";
        existingTaxId = user.getTaxId();

        page = new PageImpl<>(List.of(user));

        Mockito.when(repository.findByEmailIgnoreCase(existingUserEmail)).thenReturn(Optional.of(user));

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(repository.findById(dependentId)).thenReturn(Optional.of(user));
        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.existsByEmail(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(repository.existsByTaxId(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(repository.existsByEmailAndIdNot(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(false);
        Mockito.when(repository.existsByTaxIdAndIdNot(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(false);


        Mockito.when(repository.getReferenceById(existingId)).thenReturn(user);

        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(NoSuchElementException.class);

        Mockito.when(userMapper.toResponse(ArgumentMatchers.any(User.class))).thenReturn(userResponse);
        Mockito.when(userMapper.toEntity(ArgumentMatchers.any(UserRequest.class))).thenReturn(user);
    }


    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<UserResponse> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnUserResponseWhenIdExists() {
        UserResponse result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowNoSuchElementExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.findById(nonExistingId);
        });
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    public void findByEmailShouldReturnUserResponseWhenEmailIsValid() {
        UserResponse result = service.findByEmail(existingUserEmail);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByEmailShouldThrowNoSuchElementExceptionWhenUserNotFound() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.findByEmail(nonExistingUserEmail);
        });
        Mockito.verify(repository).findByEmailIgnoreCase(nonExistingUserEmail);
    }

    @Test
    void insertShouldSaveWhenNotDuplicated() {
        UserResponse result = service.insert(userRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(captor.capture());
        User saved = captor.getValue();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Bob", result.name());
        Assertions.assertEquals("bob@gmail.com", result.email());
        Mockito.verify(repository, times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void insertShouldThrowDuplicateResourceExceptionWhenEmailDuplicated() {

        Mockito.when(repository.existsByEmail(existingUserEmail)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceValidationException.class, () -> service.insert(userRequest));

        Mockito.verify(repository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    void insertShouldThrowDuplicateResourceExceptionWhenTaxIdDuplicated() {
        Mockito.when(repository.existsByTaxId(existingTaxId)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceValidationException.class, () -> service.insert(userRequest));

        Mockito.verify(repository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    void updateShouldReturnUserResponseWhenIdExistsAndNotDuplicated() {
        UserResponse result = service.update(existingId, userRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(captor.capture());
        User saved = captor.getValue();
        Assertions.assertEquals("Bob", result.name());
        Assertions.assertEquals("bob@gmail.com", result.email());
        Mockito.verify(repository, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    void updateShouldThrowDuplicateResourceExceptionWhenEmailUsedByAnother() {
        UserRequest userRequest = createUserRequestForUpdate();
        Mockito.when(repository.existsByEmailAndIdNot(existingUserEmail, existingId)).thenReturn(true);
        Assertions.assertThrows(DuplicateResourceValidationException.class, () -> service.update(existingId, userRequest));
        Mockito.verify(repository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    void updateShouldThrowDuplicateResourceExceptionWhenTaxIdEUsedByAnother() {
        UserRequest userRequest = createUserRequestForUpdate();
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(user);
        Mockito.when(repository.existsByEmailAndIdNot(existingUserEmail, existingId)).thenReturn(false);
        Mockito.when(repository.existsByTaxIdAndIdNot(existingTaxId, existingId)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceValidationException.class, () -> service.update(existingId, userRequest));

        Mockito.verify(repository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    public void updateShouldThrowNoSuchElementExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            service.update(nonExistingId, userRequest);
        });
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdDoesNotExist() {
        Mockito.doNothing().when(repository).deleteById(nonExistingId);
        Assertions.assertDoesNotThrow(() -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository, times(1)).deleteById(existingId);
    }


    private UserRequest createUserRequestForUpdate() {
        return new UserRequest(
                "Robert",
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
                "10000-000"
        );
    }
}
