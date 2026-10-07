package org.example.pet.service;

import org.junit.jupiter.api.Test;
import org.example.pet.dto.PetDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetServiceTest {


    @Test
    void canCreatePet() {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 80);

        long id = service.addPet(pet);

        PetDTO savedPet = service.getPet(id);

        assertEquals("Luna", savedPet.name());
        assertEquals("Cat", savedPet.species());
        assertEquals(50, savedPet.hungerLevel());
        assertEquals(80, savedPet.happiness());

        PetDTO fedPet = service.feedPet(id);

        assertEquals(40, fedPet.hungerLevel());

        PetDTO playedPet = service.playWithPet(id);

        assertEquals(90, playedPet.happiness());

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

        assertEquals(0, updatedPet.hungerLevel());
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

        assertEquals(100, updatedPet.happiness());
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

    @Test
    void feedingUnknownPetReturnsNull() {
        PetService service = new PetService();

        PetDTO result = service.feedPet(999);

        assertEquals(null, result);
    }

    @Test
    void playingWithUnknownPetReturnsNull() {
        PetService service = new PetService();

        PetDTO result = service.playWithPet(999);

        assertEquals(null, result);
    }

    @Test
    void deletingUnknownPetReturnsFalse() {
        PetService service = new PetService();

        boolean result = service.deletePet(999);

        assertEquals(false, result);
    }

    @Test
    void feedingPetDoesNotReduceHungerBelowZero() {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 5, 50);
        long id = service.addPet(pet);

        PetDTO result = service.feedPet(id);

        assertEquals(0, result.hungerLevel());
    }

    @Test
    void playingWithPetDoesNotIncreaseHappinessAbove100() {
        PetService service = new PetService();

        PetDTO pet = new PetDTO("Luna", "Cat", 50, 95);
        long id = service.addPet(pet);

        PetDTO result = service.playWithPet(id);

        assertEquals(100, result.happiness());
    }
}
