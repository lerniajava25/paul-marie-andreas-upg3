package se.lernia.jakarta.service;

import jakarta.enterprise.context.ApplicationScoped;
import se.lernia.jakarta.dto.PetDTO;
import se.lernia.jakarta.exception.PetNotFoundException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class PetService {

    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    private final AtomicLong nextId = new AtomicLong(1);

    private Long getNextId() {
        return nextId.getAndIncrement();
    }

    public PetDTO createPet(PetDTO pet) {
        Long id = getNextId();

        PetDTO createdPet = new PetDTO(
                id,
                pet.getName(),
                pet.getSpecies(),
                pet.getHungerLevel(),
                pet.getHappiness()
        );

        pets.put(id, createdPet);

        return new PetDTO(
                createdPet.getId(),
                createdPet.getName(),
                createdPet.getSpecies(),
                createdPet.getHungerLevel(),
                createdPet.getHappiness()
        );
    }

    public PetDTO getPetById(Long id) {
        PetDTO pet = pets.get(id);

        if (pet == null) {
            throw new PetNotFoundException(id);
        }

        return new PetDTO(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getHungerLevel(),
                pet.getHappiness()
        );
    }

    public List<PetDTO> getAllPets() {
        return pets.values()
                .stream()
                .map(pet -> new PetDTO(
                        pet.getId(),
                        pet.getName(),
                        pet.getSpecies(),
                        pet.getHungerLevel(),
                        pet.getHappiness()
                ))
                .toList();
    }

    public void deletePetById(Long id) {
        PetDTO pet = pets.remove(id);

        if (pet == null) {
            throw new PetNotFoundException(id);
        }
    }

    public PetDTO feedPet(Long id) {
        PetDTO updatedPet = pets.compute(id, (key, pet) -> {

            if (pet == null) {
                throw new PetNotFoundException(id);
            }

            int hungerLevel = pet.getHungerLevel();
            int newHungerLevel = Math.max(0, hungerLevel - 10);

            pet.setHungerLevel(newHungerLevel);

            return pet;
        });

        return new PetDTO(
                updatedPet.getId(),
                updatedPet.getName(),
                updatedPet.getSpecies(),
                updatedPet.getHungerLevel(),
                updatedPet.getHappiness()
        );
    }

    public PetDTO playWithPet(Long id) {
        PetDTO updatedPet = pets.compute(id, (key, pet) -> {

            if (pet == null) {
                throw new PetNotFoundException(id);
            }

            int happiness = pet.getHappiness();
            int newHappiness = Math.min(100, happiness + 10);

            pet.setHappiness(newHappiness);

            return pet;
        });

        return new PetDTO(
                updatedPet.getId(),
                updatedPet.getName(),
                updatedPet.getSpecies(),
                updatedPet.getHungerLevel(),
                updatedPet.getHappiness()
        );
    }
}
