package se.lernia.jakarta.exception;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(Long id) {
        super("Pet with id " + id + " was not found");
    }
}