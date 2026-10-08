package org.example.pet.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.pet.dto.PetDTO;
import org.example.pet.service.PetService;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;


    //API Resource
    @Path("/pets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public class PetResource {

        @Inject
        private PetService petService;

        @Context
        private UriInfo uriInfo;

        //Get all pets
        @GET
        public List<PetDTO> getAllPets() {
            return petService.getAllPets();
        }

        //Get a pet by ID
    @GET
    @Path("/{id}")
    public PetDTO getPet(@PathParam("id") long id) {
        PetDTO pet = petService.getPet(id);
        if (pet == null) {
            throw new NotFoundException("Pet with id " + id + " was not found");
        }
        return pet;
    }
    // Add a new pet
    @POST
    public Response createPet(@Valid PetDTO pet) {
        long id = petService.addPet(pet);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                        .path(Long.toString(id)).build())
                .entity(pet)
                .build();
    }

    //Delete a pet
    @DELETE
    @Path("/{id}")
    public Response deletePet(@PathParam("id") long id) {
        if (!petService.deletePet(id)) {
            throw new NotFoundException("Pet with id " + id + " was not found");
        }
        return Response.noContent().build();
    }

        //Feed a pet
        @PUT
        @Path("/{id}/feed")
        public PetDTO feedPet(@PathParam("id") long id) {
            PetDTO pet = petService.feedPet(id);
            if (pet == null) {
                throw new NotFoundException("Pet with id " + id + " was not found");
            }
            return pet;
        }

        //Play with a pet
        @PUT
        @Path("/{id}/play")
        public PetDTO playWithPet(@PathParam("id") long id) {
            PetDTO pet = petService.playWithPet(id);
            if (pet == null) {
                throw new NotFoundException("Pet with id " + id + " was not found");
            }
            return pet;
        }

    }
