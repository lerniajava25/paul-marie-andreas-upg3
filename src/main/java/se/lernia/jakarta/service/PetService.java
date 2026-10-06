package se.lernia.jakarta.service;

import se.lernia.jakarta.dto.PetDTO;
import se.lernia.jakarta.exception.PetNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PetService {

    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();

    private final AtomicLong nextId = new AtomicLong(1);

    private Long getNextId() {
        return nextId.getAndIncrement();
    }

    public PetDTO createPet(PetDTO pet) {
        Long id = getNextId();
        pet.setId(id);
        pets.put(id, pet);
        return pet;
    }

    public PetDTO getPetById(Long id) {
        PetDTO pet = pets.get(id);

        if (pet == null) {
            throw new PetNotFoundException(id);
        }

        return pet;
    }

    public List<PetDTO> getAllPets() {
        return new ArrayList<>(pets.values());
    }

    public void deletePetById(Long id) {
        PetDTO pet = pets.remove(id);

        if (pet == null) {
            throw new PetNotFoundException(id);
        }
    }

    public PetDTO feedPet(Long id) {
        return pets.compute(id, (key, pet) -> {

            if (pet == null) {
                throw new PetNotFoundException(id);
            }

            int hungerLevel = pet.getHungerLevel();
            int newHungerLevel = Math.max(0, hungerLevel - 10);

            pet.setHungerLevel(newHungerLevel);

            return pet;
        });
    }

    public PetDTO playWithPet (Long id) {
        return pets.compute(id, (key, pet) -> {

            if (pet == null) {
                throw new PetNotFoundException(id);
            }

            int happiness = pet.getHappiness();
            int newHappiness = Math.min(100, happiness + 10);

            pet.setHappiness(newHappiness);

            return pet;
        });
    }

}