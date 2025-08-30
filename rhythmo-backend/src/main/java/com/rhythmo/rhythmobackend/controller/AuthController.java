package com.rhythmo.rhythmobackend.controller;

import com.rhythmo.rhythmobackend.constant.ApiPath;
import com.rhythmo.rhythmobackend.dto.LoginRequest;
import com.rhythmo.rhythmobackend.dto.LoginResponse;
import com.rhythmo.rhythmobackend.dto.RegisterRequest;
import com.rhythmo.rhythmobackend.model.User;
import com.rhythmo.rhythmobackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.AUTH)
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private UserModelAssembler userModelAssembler;

    @PostMapping("/register")
    public ResponseEntity<EntityModel<User>> create(@Valid @RequestBody RegisterRequest registerRequest) {
        EntityModel<User> userModel = userModelAssembler.toModel(authService.register(registerRequest));
        return ResponseEntity
                .created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
