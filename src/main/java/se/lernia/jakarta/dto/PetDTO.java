package se.lernia.jakarta.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PetDTO {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Species must not be blank")
    private String species;

    @Min(value = 0, message = "Hunger level must be at least 0")
    @Max(value = 100, message = "Hunger level must not exceed 100")
    private int hungerLevel;

    @Min(value = 0, message = "Happiness must be at least 0")
    @Max(value = 100, message = "Happiness must not exceed 100")
    private int happiness;

    public PetDTO() {
    }

    public PetDTO(Long id, String name, String species,
                  int hungerLevel, int happiness) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.hungerLevel = hungerLevel;
        this.happiness = happiness;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
}