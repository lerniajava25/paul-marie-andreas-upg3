package se.lernia.jakarta.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.lernia.jakarta.dto.PetDTO;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationExceptionMapperTest {

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
    void shouldReturn400ForValidationError() {

        PetDTO pet = new PetDTO(
                1L,
                "",
                "Dog",
                50,
                80
        );

        Set<ConstraintViolation<PetDTO>> violations =
                validator.validate(pet);

        ConstraintViolationException exception =
                new ConstraintViolationException(violations);

        ValidationExceptionMapper mapper =
                new ValidationExceptionMapper();

        Response response = mapper.toResponse(exception);

        assertEquals(400, response.getStatus());

        ErrorResponse error =
                (ErrorResponse) response.getEntity();

        assertEquals(400, error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertEquals("Name must not be blank", error.getMessage());
    }
}