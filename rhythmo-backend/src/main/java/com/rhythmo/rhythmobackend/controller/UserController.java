package com.rhythmo.rhythmobackend.controller;

import com.rhythmo.rhythmobackend.constant.ApiPath;
import com.rhythmo.rhythmobackend.dto.RegisterRequest;
import com.rhythmo.rhythmobackend.exception.NoSuchEntityException;
import com.rhythmo.rhythmobackend.model.User;
import com.rhythmo.rhythmobackend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/{id}")
    public EntityModel<User> getById(@PathVariable("id") Long id) {
        return userModelAssembler.toModel(
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new NoSuchEntityException(id))
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> getAll() {
        return CollectionModel.of(
                userRepository.findAll().stream()
                        .map(userModelAssembler::toModel)
                        .toList()
        );
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> update(@PathVariable("id") Long id, @Valid @RequestBody RegisterRequest updateForm) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(id));
        user.setUsername(updateForm.username());
        user.setEmail(updateForm.email());
        user.setPassword(passwordEncoder.encode(updateForm.password()));
        return ResponseEntity.ok(userModelAssembler.toModel(userRepository.save(user)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SUPPORTER') and #id == authentication.principal.id")
    @PatchMapping("/{id}/change-bg")
    public ResponseEntity<?> changeBg(@PathVariable("id") Long id, @RequestParam String bgColor) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchEntityException(id));
        user.setBgColor(bgColor);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SUPPORTER') and #id == authentication.principal.id")
    @PatchMapping("/{id}/change-profile-cover")
    public ResponseEntity<?> changeProfileCover(@PathVariable("id") Long id, @RequestParam String profileCoverUrl) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchEntityException(id));
        user.setProfileCoverUrl(profileCoverUrl);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
