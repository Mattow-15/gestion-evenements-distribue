package com.eventsystem;

import com.eventsystem.exception.CapaciteMaxAtteinteException;
import com.eventsystem.model.Conference;
import com.eventsystem.model.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EvenementTest {
    private Conference conference;

    @BeforeEach
    void setUp() {
        conference = new Conference("CONF001", "Conférence IA", LocalDateTime.now(),
                "Paris", 2, "Intelligence Artificielle", Arrays.asList("Dr. Smith"));
    }

    @Test
    void testAjouterParticipant() throws CapaciteMaxAtteinteException {
        Participant p1 = new Participant("P001", "Alice", "alice@example.com");
        conference.ajouterParticipant(p1);
        assertEquals(1, conference.getParticipants().size());
    }

    @Test
    void testCapaciteMaxAtteinte() {
        Participant p1 = new Participant("P001", "Alice", "alice@example.com");
        Participant p2 = new Participant("P002", "Bob", "bob@example.com");
        Participant p3 = new Participant("P003", "Charlie", "charlie@example.com");
        assertDoesNotThrow(() -> {
            conference.ajouterParticipant(p1);
            conference.ajouterParticipant(p2);
        });
        assertThrows(CapaciteMaxAtteinteException.class, () -> conference.ajouterParticipant(p3));
    }
}