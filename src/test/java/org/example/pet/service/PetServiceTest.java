package org.example.pet.service;

import org.junit.jupiter.api.Test;
import org.example.pet.dto.PetDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetServiceTest {

    @Test
    void serviceCanBeCreated() {
        PetService service = new PetService();
    }
    @Test
    void canCreatePet() {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 80);

        long id = service.addPet(pet);

        PetDTO savedPet = service.getPet(id);

        assertEquals("Luna", savedPet.getName());
        assertEquals("Cat", savedPet.getSpecies());
        assertEquals(50, savedPet.getHungerLevel());
        assertEquals(80, savedPet.getHappiness());

        PetDTO fedPet = service.feedPet(id);

        assertEquals(40, fedPet.getHungerLevel());

        PetDTO playedPet = service.playWithPet(id);

        assertEquals(90, playedPet.getHappiness());

        boolean deleted = service.deletePet(id);

        assertEquals(true, deleted);
        assertEquals(null, service.getPet(id));
    }

    @Test
    void unknownPetIdIsMissing() {
        PetService service = new PetService();

        assertEquals(null, service.getPet(999));
    }

    @Test
    void concurrentFeedPetDoesNotLoseUpdates() throws InterruptedException {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 100, 50);
        long id = service.addPet(pet);

        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> service.feedPet(id));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        PetDTO updatedPet = service.getPet(id);

        assertEquals(0, updatedPet.getHungerLevel());
    }

    @Test
    void concurrentPlayWithPetDoesNotLoseUpdates() throws InterruptedException {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 0);
        long id = service.addPet(pet);

        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> service.playWithPet(id));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        PetDTO updatedPet = service.getPet(id);

        assertEquals(100, updatedPet.getHappiness());
    }

    @Test
    void concurrentDeleteDoesNotBringPetBack() throws InterruptedException {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 50);
        long id = service.addPet(pet);

        Thread feedThread = new Thread(() -> service.feedPet(id));
        Thread playThread = new Thread(() -> service.playWithPet(id));
        Thread deleteThread = new Thread(() -> service.deletePet(id));

        feedThread.start();
        playThread.start();
        deleteThread.start();

        feedThread.join();
        playThread.join();
        deleteThread.join();

        assertEquals(null, service.getPet(id));
    }
}