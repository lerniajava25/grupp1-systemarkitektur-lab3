package org.example.pet.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.pet.dto.PetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class PetService {
    private final ConcurrentHashMap<Long, PetDTO> pets = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private final ReentrantLock lock = new ReentrantLock();
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
        lock.lock();
        try {
            return pets.remove(id) != null;
        } finally {
            lock.unlock();
        }
    }

    // Feed a pet
    public PetDTO feedPet(long id) {
        lock.lock();
        try {
            PetDTO pet = pets.get(id);
            if (pet == null) {
                return null;
            }

            PetDTO updatedPet = new PetDTO(
                    pet.getName(),
                    pet.getSpecies(),
                    Math.max(0, pet.getHungerLevel() - 10),
                    pet.getHappiness()
            );
            pets.put(id, updatedPet);
            return updatedPet;
        } finally {
            lock.unlock();
        }
    }

    // Play with a pet
    public PetDTO playWithPet(long id) {
        lock.lock();
        try {
            PetDTO pet = pets.get(id);
            if (pet == null) {
                return null;
            }

            PetDTO updatedPet = new PetDTO(
                    pet.getName(),
                    pet.getSpecies(),
                    pet.getHungerLevel(),
                    Math.min(100, pet.getHappiness() + 10)
            );
            pets.put(id, updatedPet);
            return updatedPet;
        } finally {
            lock.unlock();
        }
    }
}
