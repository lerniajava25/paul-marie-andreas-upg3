package se.lernia.jakarta.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation
                .byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    @Test
    void validPetShouldHaveNoValidationErrors() {

        PetDTO pet = new PetDTO(
                1L,
                "Bosse",
                "Dog",
                50,
                80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(violations.isEmpty());
    }

    @Test
    void blankNameShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L,
                "",
                "Dog",
                50,
                80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);


        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Name must not be blank"))
        );
    }

    @Test
    void hungerBelowZeroShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L, "Bosse", "Dog", -1, 80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Hunger level must be at least 0"))
        );
    }

    @Test
    void hungerAboveHundredShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L, "Bosse", "Dog", 101, 80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Hunger level must not exceed 100"))
        );
    }
    @Test
    void blankSpeciesShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L, "Bosse", "", 50, 80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Species must not be blank"))
        );
    }
    @Test
    void happinessBelowZeroShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L, "Bosse", "Dog", 50, -1
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Happiness must be at least 0"))
        );
    }

    @Test
    void happinessAboveHundredShouldHaveValidationError() {

        PetDTO pet = new PetDTO(
                1L, "Bosse", "Dog", 50, 101
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage()
                                .equals("Happiness must not exceed 100"))
        );
    }
    @Test
    void boundaryValuesShouldBeValid() {

        PetDTO petAtMinimum = new PetDTO(
                1L, "Bosse", "Dog", 0, 0
        );

        PetDTO petAtMaximum = new PetDTO(
                2L, "Misse", "Cat", 100, 100
        );

        Set<ConstraintViolation<PetDTO>> minimumViolations =
                validator.validate(petAtMinimum);

        Set<ConstraintViolation<PetDTO>> maximumViolations =
                validator.validate(petAtMaximum);

        assertTrue(minimumViolations.isEmpty());
        assertTrue(maximumViolations.isEmpty());
    }


}