package com.orderflow.ecommerce.services;

import com.orderflow.ecommerce.controllers.exceptions.FieldMessage;
import com.orderflow.ecommerce.dtos.UserDto;
import com.orderflow.ecommerce.entities.User;
import com.orderflow.ecommerce.exceptions.DuplicateResourceValidationException;
import com.orderflow.ecommerce.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        return new UserDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        return new UserDto(repository.findByEmailIgnoreCase(email).orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(UserDto::new);
    }

    @Transactional
    public UserDto insert(UserDto dto) {
        return new UserDto(saveEntity(null, dto));
    }

    @Transactional
    public UserDto update(Long id, UserDto dto) {
        try {
            return new UserDto(saveEntity(id, dto));
        }
        catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id, boolean verify) {
        try {
            if (verify) repository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Integrity violation");
        }
    }

    private User saveEntity(Long id, UserDto dto) {
        User entity = new User();
        if(id != null){ // if updating
            entity = repository.getReferenceById(id);
            validate(id, dto.email(), dto.taxId(), "Email já cadastrado para outro usuário!", "CPF/CNPJ já cadastrado para outro usuário!");
        } else {
            validate(null, dto.email(), dto.taxId(), "Email já cadastrado!", "CPF/CNPJ já cadastrado!");
        }

        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(dto.password());
        entity.setTaxId(dto.taxId());
        entity.setStateRegistration(dto.stateRegistration());
        entity.setPhone(dto.phone());
        entity.setBirthDate(dto.birthDate());
        entity.setTaxpayer(dto.taxpayer());
        entity.setGoogleId(dto.googleId());
        entity.setStreet(dto.street());
        entity.setComplement(dto.complement());
        entity.setNumber(dto.number());
        entity.setNeighborhood(dto.neighborhood());
        entity.setCity(dto.city());
        entity.setCountry(dto.country());
        entity.setState(dto.state());
        entity.setZipCode(dto.zipCode());

        return repository.save(entity);
    }

    private void validate(Long id, String email, String taxId, String emailMessage, String taxIdMessage) {
        List<FieldMessage> errors = new ArrayList<>();
        int ok = 0;
        if(id != null){
            if (repository.existsByEmailAndIdNot(email, id)) {
                ok = 1;
                errors.add(new FieldMessage("email", "Email já cadastrado para outro usuário!"));
            }
            if (repository.existsByTaxIdAndIdNot(taxId, id)) {
                ok = 1;
                errors.add(new FieldMessage("taxId", "CPF/CNPJ já cadastrado para outro usuário!"));
            }
        } else {
            if (repository.existsByEmail(email)) {
                ok = 1;
                errors.add(new FieldMessage("email", "Email já cadastrado!"));
            }
            if (repository.existsByTaxId(taxId)) {
                ok = 1;
                errors.add(new FieldMessage("taxId", "CPF/CNPJ já cadastrado!"));
            }
        }

        if (ok == 1)
            throw new DuplicateResourceValidationException(errors, "Duplicated information!");

    }
}
