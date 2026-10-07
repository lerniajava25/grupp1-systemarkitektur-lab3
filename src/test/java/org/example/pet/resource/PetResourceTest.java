package org.example.pet.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.example.pet.dto.PetDTO;
import org.example.pet.service.PetService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PetResourceTest {

    @Test
    void resourceCanBeCreated() {
        PetResource resource = new PetResource();

        assertNotNull(resource);
    }

    @Test
    void createPetReturnsCreated() throws Exception {
        PetResource resource = new PetResource();
        PetService service = new PetService();
        UriInfo uriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = mock(UriBuilder.class);
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        when(uriBuilder.path(Long.toString(1))).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(null);

        var uriField = PetResource.class.getDeclaredField("uriInfo");
        uriField.setAccessible(true);
        uriField.set(resource, uriInfo);

        var field = PetResource.class.getDeclaredField("petService");
        field.setAccessible(true);
        field.set(resource, service);

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 80);

        Response response = resource.createPet(pet);

        assertEquals(201, response.getStatus());
    }

    @Test
    void deletePetReturnsNoContent() throws Exception {
        PetResource resource = new PetResource();
        PetService service = new PetService();

        var field = PetResource.class.getDeclaredField("petService");
        field.setAccessible(true);
        field.set(resource, service);

        long id = service.addPet(
                new PetDTO("Luna", "Cat", 50, 80)
        );

        Response response = resource.deletePet(id);

        assertEquals(204, response.getStatus());
    }

    @Test
    void deleteUnknownPetReturnsNotFound() throws Exception {
        PetResource resource = new PetResource();
        PetService service = new PetService();

        var field = PetResource.class.getDeclaredField("petService");
        field.setAccessible(true);
        field.set(resource, service);

        assertThrows(
                jakarta.ws.rs.NotFoundException.class,
                () -> resource.deletePet(999)
        );
    }

    @Test
    void validationExceptionReturnsBadRequest() {
        ValidationExceptionMapper mapper = new ValidationExceptionMapper();

        Response response = mapper.toResponse(
                new jakarta.validation.ConstraintViolationException(
                        java.util.Set.of()
                )
        );

        assertEquals(400, response.getStatus());
    }
}