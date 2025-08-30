package com.rhythmo.rhythmobackend.controller;

import com.rhythmo.rhythmobackend.model.Activity;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActivityModelAssembler implements RepresentationModelAssembler<Activity, EntityModel<Activity>> {
    @Override
    public @NonNull EntityModel<Activity> toModel(@NonNull Activity activity) {
        return EntityModel.of(activity,
                linkTo(methodOn(ActivityController.class).getById(activity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getById(activity.getAuthor().getId())).withRel("author"));
    }
}