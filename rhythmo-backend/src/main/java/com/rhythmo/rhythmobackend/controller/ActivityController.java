package com.rhythmo.rhythmobackend.controller;

import com.rhythmo.rhythmobackend.constant.ApiPath;
import com.rhythmo.rhythmobackend.dto.ActivityRequest;
import com.rhythmo.rhythmobackend.exception.NoSuchEntityException;
import com.rhythmo.rhythmobackend.model.Activity;
import com.rhythmo.rhythmobackend.model.User;
import com.rhythmo.rhythmobackend.repository.ActivityRepository;
import com.rhythmo.rhythmobackend.repository.UserRepository;
import com.rhythmo.rhythmobackend.service.UserFromJwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(ApiPath.ACTIVITY)
@RequiredArgsConstructor
public class ActivityController {
    private final UserRepository userRepository;
    private final UserFromJwtService userFromJwt;
    private final ActivityRepository activityRepository;
    private final ActivityModelAssembler activityModelAssembler;


    @PostMapping
    public ResponseEntity<EntityModel<Activity>> create(@Valid @RequestBody ActivityRequest activityRequest, @AuthenticationPrincipal Jwt authorJwt) {
        EntityModel<Activity> activityModel = activityModelAssembler.toModel(
                activityRepository.save(
                        new Activity(activityRequest.name(), activityRequest.type(), activityRequest.url(), userFromJwt.usingRepo(authorJwt))
                )
        );
        return ResponseEntity
                .created(activityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(activityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<Activity> getById(@PathVariable("id") Long id) {
        return activityModelAssembler.toModel(
                activityRepository
                        .findById(id)
                        .orElseThrow(() -> new NoSuchEntityException(id))
        );
    }

    @GetMapping("/user/{id}")
    public CollectionModel<EntityModel<Activity>> getUsersActivities(@PathVariable("id") Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchEntityException(id));
        return CollectionModel.of(
                activityRepository.findByAuthor(user).stream()
                        .map(activityModelAssembler::toModel)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal Jwt userJwt) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(id));
        User user = userFromJwt.usingRepo(userJwt);
        if (!activity.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can not delete not yours activity.");
        }
        activityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
