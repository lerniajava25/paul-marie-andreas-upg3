package se.lernia.jakarta.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetNotFoundExceptionMapperTest {

    @Test
    void shouldReturn404WhenPetIsNotFound() {

        PetNotFoundExceptionMapper mapper =
                new PetNotFoundExceptionMapper();

        PetNotFoundException exception =
                new PetNotFoundException(5L);

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());

        ErrorResponse error =
                (ErrorResponse) response.getEntity();

        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("Pet with id 5 was not found", error.getMessage());
    }
}