package com.onclass.capacity.infrastructure.entrypoints.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CapacityDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 90, message = "La descripción no debe superar los 90 caracteres")
    private String description;

    private List<Long> technologyIds;
}
