package org.example.pet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PetDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Species is required")
        String species,

        @Min(value = 0, message = "Hunger level must be between 0 and 100")
        @Max(value = 100, message = "Hunger level must be between 0 and 100")
        int hungerLevel,

        @Min(value = 0, message = "Happiness level must be between 0 and 100")
        @Max(value = 100, message = "Happiness level must be between 0 and 100")
        int happiness
) {

}
