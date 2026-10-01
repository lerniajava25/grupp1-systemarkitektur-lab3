package org.example.pet.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.pet.dto.PetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    // Add a new pet
    public long addPet(PetDTO pet) {
        long id = nextId.getAndIncrement();
        pets.put(id, pet);
        return id;
    }

    // Get a pet by ID
    public PetDTO getPet(long id) {
        return pets.get(id);
    }

    // Get all pets
    public List<PetDTO> getAllPets() {
        return new ArrayList<>(pets.values());
    }

    // Delete a pet by ID
    public boolean deletePet(long id) {
        return pets.remove(id) != null;
    }

    // Feed a pet
    public PetDTO feedPet(long id) {
        return pets.computeIfPresent(id, (key, pet) ->
                new PetDTO(
                        pet.getName(),
                        pet.getSpecies(),
                        Math.max(0, pet.getHungerLevel() - 10),
                        pet.getHappiness()
                )
        );
    }

    // Play with a pet
    public PetDTO playWithPet(long id) {
        return pets.computeIfPresent(id, (key, pet) ->
                new PetDTO(
                        pet.getName(),
                        pet.getSpecies(),
                        pet.getHungerLevel(),
                        Math.min(100, pet.getHappiness() + 10)
                )
        );
    }
}
