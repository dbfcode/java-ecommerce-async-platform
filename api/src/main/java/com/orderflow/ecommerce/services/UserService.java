package com.orderflow.ecommerce.services;

import com.orderflow.ecommerce.dtos.FieldMessage;
import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import com.orderflow.ecommerce.entities.User;
import com.orderflow.ecommerce.exceptions.DuplicateResourceValidationException;
import com.orderflow.ecommerce.mappers.UserMapper;
import com.orderflow.ecommerce.repositories.UserRepository;
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

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(findUserById(id));
    }

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        return userMapper.toResponse(repository.findByEmailIgnoreCase(email).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado")));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse insert(UserRequest request) {
        validateUser(null, request.email(), request.taxId());
        User entity = userMapper.toEntity(request);
        return userMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User entity = findUserById(id);
        validateUser(id, request.email(), request.taxId());

        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setPassword(request.password());
        entity.setTaxId(request.taxId());
        entity.setStateRegistration(request.stateRegistration());
        entity.setPhone(request.phone());
        entity.setBirthDate(request.birthDate());
        entity.setTaxpayer(request.taxpayer());
        entity.setGoogleId(request.googleId());
        entity.setStreet(request.street());
        entity.setComplement(request.complement());
        entity.setNumber(request.number());
        entity.setNeighborhood(request.neighborhood());
        entity.setCity(request.city());
        entity.setCountry(request.country());
        entity.setState(request.state());
        entity.setZipCode(request.zipCode());

        return userMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.delete(findUserById(id));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Integridade violada");
        }
    }

    private void validateUser(Long id, String email, String taxId) {

        List<FieldMessage> errors = new ArrayList<>();

        if(id != null){
            if (repository.existsByEmailAndIdNot(email, id)) errors.add(new FieldMessage("email", "Email já cadastrado para outro usuário!"));
            if (repository.existsByTaxIdAndIdNot(taxId, id)) errors.add(new FieldMessage("taxId", "CPF/CNPJ já cadastrado para outro usuário!"));
        } else {
            if (repository.existsByEmail(email)) errors.add(new FieldMessage("email", "Email já cadastrado!"));
            if (repository.existsByTaxId(taxId)) errors.add(new FieldMessage("taxId", "CPF/CNPJ já cadastrado!"));
        }

        if (!errors.isEmpty()) throw new DuplicateResourceValidationException(errors, "Informação já existe para outro usuário");

    }

    private User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    }
}
