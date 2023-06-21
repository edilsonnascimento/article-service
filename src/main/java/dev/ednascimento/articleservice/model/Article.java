package dev.ednascimento.articleservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Article(
        @NotNull(message = "Name is mandatory")
        Integer id,
        @NotBlank(message = "Name is mandatory")
        String title,
        @NotBlank(message = "Name is mandatory")
        String body) {}