package se.lernia.jakarta.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PetNotFoundExceptionMapper
        implements ExceptionMapper<PetNotFoundException> {

    @Override
    public Response toResponse(PetNotFoundException exception) {

        ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}