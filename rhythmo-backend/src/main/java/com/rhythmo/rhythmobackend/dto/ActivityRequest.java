package com.rhythmo.rhythmobackend.dto;

import com.rhythmo.rhythmobackend.model.Activity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record ActivityRequest(@NotBlank String name, @NotNull Activity.Type type, @URL String url) {}