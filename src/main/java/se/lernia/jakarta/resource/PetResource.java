package se.lernia.jakarta.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.lernia.jakarta.dto.PetDTO;
import se.lernia.jakarta.service.PetService;

import java.util.Comparator;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/pets")
public class PetResource {

    @Inject
    private PetService petService;

    @POST
    public Response createPet(@Valid PetDTO pet) {
         PetDTO createdPet = petService.createPet(pet);

         return Response
                 .status(Response.Status.CREATED)
                 .entity(createdPet)
                 .build();
    }

    @GET
    public List<PetDTO> getPets(
        @QueryParam("species") String species,
        @QueryParam("sortBy") String sortBy,
        @QueryParam("order") String order) {

        List<PetDTO> pets = petService.getAllPets();

        if (species != null && !species.isBlank()) {
            pets = pets.stream()
                    .filter(pet -> pet.getSpecies().equalsIgnoreCase(species))
                    .toList();
        }

        if (sortBy != null) {
            Comparator<PetDTO> comparator = switch (sortBy) {
                case "happiness" -> Comparator.comparing(PetDTO::getHappiness);
                case "hungerLevel"  -> Comparator.comparing(PetDTO::getHungerLevel);
                case "name"    -> Comparator.comparing(PetDTO::getName);
                default -> null;
            };

            if (comparator != null) {
                if ("desc".equalsIgnoreCase(order)) {
                    comparator = comparator.reversed();
                }

                pets = pets.stream()
                        .sorted(comparator)
                        .toList();
            }
        }

        return  pets;
    }

    @GET
    @Path("/{id}")
    public PetDTO getPet(@PathParam("id") Long id) {
        return petService.getPetById(id);
    }

    @PUT
    @Path("/{id}/feed")
    public PetDTO feedPet(@PathParam("id") Long id) {
        return petService.feedPet(id);
    }

    @PUT
    @Path("/{id}/play")
    public PetDTO playPet(@PathParam("id") Long id) {
        return petService.playWithPet(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deletePet(@PathParam("id") Long id) {
        petService.deletePetById(id);
        return Response.noContent().build();
    }
}
