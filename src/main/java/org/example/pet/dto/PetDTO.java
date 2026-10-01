package org.example.pet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PetDTO {
    //Fields
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Species is required")
    private String species;

    @Max(value = 100, message = "Hunger level must be between 0 and 100")
    @Min(value = 0, message = "Hunger level must be between 0 and 100")
    private int hungerLevel;
    @Max(value = 100, message = "Happiness level must be between 0 and 100")
    @Min(value = 0, message = "Happiness level must be between 0 and 100")
    private int happiness;

    //Constructor
    public PetDTO() {
    }
    public PetDTO(String name, String species, int hungerLevel, int happiness) {
        this.name = name;
        this.species = species;
        this.hungerLevel = hungerLevel;
        this.happiness = happiness;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public int getHappiness() {
        return happiness;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
}
