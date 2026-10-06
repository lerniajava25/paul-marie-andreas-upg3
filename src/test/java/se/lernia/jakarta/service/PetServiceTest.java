package se.lernia.jakarta.service;

import org.junit.jupiter.api.Test;
import se.lernia.jakarta.dto.PetDTO;
import se.lernia.jakarta.exception.PetNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PetServiceTest {

    //Testar att första pet får id = 1
    @Test
    void createPetId() {
        PetService petService = new PetService();

        PetDTO pet = new PetDTO(
                null,
                "Bosse",
                "Dog",
                70,
                50
        );

        PetDTO createdPet = petService.createPet(pet);

        assertEquals(1L, createdPet.getId());
    }

    //Testar att ett pet kan hämtas med rätt id
    @Test
    void getPetById() {
        PetService petService = new PetService();

        PetDTO pet = new PetDTO(
                null,
                "Misse",
                "Cat",
                50,
                70
        );

        petService.createPet(pet);
        PetDTO foundPet = petService.getPetById(1L);

        assertEquals("Misse", foundPet.getName());
    }

    //Testar att PetNotFoundException kastas om id inte finns
    @Test
    void getPetByIdNotFound() {
        PetService petService = new PetService();

        assertThrows(
                PetNotFoundException.class,
                () -> petService.getPetById(10L)
        );
    }

    //Testar att alla sparade pets hämtas
    @Test
    void getAllPets() {
        PetService petService = new PetService();

        PetDTO dog = new PetDTO(
                null,
                "Bosse",
                "Dog",
                50,
                70
        );

        PetDTO cat = new PetDTO(
                null,
                "Misse",
                "Cat",
                50,
                70
        );

        petService.createPet(dog);
        petService.createPet(cat);

        List<PetDTO> allPets = petService.getAllPets();

        assertEquals(2, allPets.size());
    }

    //Testar att ett pet raderas
    @Test
    void deletePetById() {
        PetService petService = new PetService();

        PetDTO dog = new PetDTO(
                null,
                "Bosse",
                "Dog",
                50,
                70
        );

        PetDTO cat = new PetDTO(
                null,
                "Misse",
                "Cat",
                50,
                70
        );

        petService.createPet(dog);
        petService.createPet(cat);

        petService.deletePetById(1L);

        assertThrows(
                PetNotFoundException.class,
                () -> petService.getPetById(1L)
        );
    }

    //Testar att PetNotFoundException kastas om pet som ska raderas inte finns
    @Test
    void deletePetByIdNotFound() {
        PetService petService = new PetService();

        assertThrows(
                PetNotFoundException.class,
                () -> petService.deletePetById(10L)
        );
    }

    //Testar att feedPet sänker hunger med 10
    @Test
    void feedPet() {
        PetService petService = new PetService();

        PetDTO dog = new PetDTO(
                null,
                "Bosse",
                "Dog",
                50,
                70
        );

        petService.createPet(dog);
        PetDTO fedPet = petService.feedPet(1L);

        assertEquals(40, fedPet.getHungerLevel());
    }

    //Testar att feedPet inte sänker hunger under 0
    @Test
    void feedPetLevel() {
        PetService petService = new PetService();

        PetDTO cat = new PetDTO(
                null,
                "Misse",
                "Cat",
                5,
                70
        );

        petService.createPet(cat);
        PetDTO fedPet = petService.feedPet(1L);

        assertEquals(0, fedPet.getHungerLevel());
    }

    //Testar att PetNotFoundException kastas om pet som ska matas inte finns
    @Test
    void feedPetLevelNotFound() {
        PetService petService = new PetService();

        assertThrows(
                PetNotFoundException.class,
                () -> petService.feedPet(2L)
        );
    }

    //Testar att playWithPet ökar happiness med 10
    @Test
    void petHappiness() {
        PetService petService = new PetService();

        PetDTO dog = new PetDTO(
                null,
                "Bosse",
                "Dog",
                50,
                70
        );

        petService.createPet(dog);
        PetDTO happiness = petService.playWithPet(1L);

        assertEquals(80, happiness.getHappiness());
    }

    //Testar att happiness inte går över 100
    @Test
    void happinessLevel() {
        PetService petService = new PetService();

        PetDTO dog = new PetDTO(
                null,
                "Bosse",
                "Dog",
                50,
                95
        );

        petService.createPet(dog);
        PetDTO happiness = petService.playWithPet(1L);

        assertEquals(100, happiness.getHappiness());
    }

    //Testar att PetNotFoundException kastas om pet som ska lekas med inte finns
    @Test
    void happinessLevelNotFound() {
        PetService petService = new PetService();

        assertThrows(
                PetNotFoundException.class,
                () -> petService.playWithPet(2L)
        );
    }

    //Testar att samtidiga createPet-anrop får unika id:n
    @Test
    void createPetConcurrent() throws InterruptedException {
        PetService petService = new PetService();

        Set<Long> ids = ConcurrentHashMap.newKeySet();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                PetDTO pet = new PetDTO(
                        null,
                        "Bosse",
                        "Dog",
                        50,
                        95
                );

                PetDTO createNewPet = petService.createPet(pet);
                ids.add(createNewPet.getId());
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(10, ids.size());
    }
}
