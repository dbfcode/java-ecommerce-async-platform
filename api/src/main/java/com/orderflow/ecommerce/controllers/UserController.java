package com.orderflow.ecommerce.controllers;

import com.orderflow.ecommerce.dtos.UserRequest;
import com.orderflow.ecommerce.dtos.UserResponse;
import com.orderflow.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAllPaged(pageable));
    }

    @GetMapping(params = "email")
    public ResponseEntity<UserResponse> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest request) {
        UserResponse response = service.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok().body(service.update(id, request));
    }
}
