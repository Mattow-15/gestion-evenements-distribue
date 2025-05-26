package com.eventsystem;

import com.eventsystem.exception.EvenementDejaExistantException;
import com.eventsystem.model.Conference;
import com.eventsystem.model.Evenement;
import com.eventsystem.service.GestionEvenements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Classe de test unitaire pour GestionEvenements
public class GestionEvenementsTest {
    private GestionEvenements gestion;
    private Conference conference1;
    private Conference conference2;

    // Configuration exécutée avant chaque test
    @BeforeEach
    void setUp() {
        // Obtenir l'instance Singleton
        gestion = GestionEvenements.getInstance();
        
        // Créer des événements pour les tests
        conference1 = new Conference("CONF001", "Conférence IA", LocalDateTime.now(),
                "Paris", 100, "Intelligence Artificielle", Arrays.asList("Dr. Smith"));
        conference2 = new Conference("CONF002", "Conférence Blockchain", LocalDateTime.now().plusDays(1),
                "Lyon", 50, "Blockchain", Arrays.asList("Dr. Jones"));
    }

    // Teste l'ajout d'un événement
    @Test
    void testAjouterEvenement() throws EvenementDejaExistantException {
        gestion.ajouterEvenement(conference1);
        Evenement result = gestion.rechercherEvenement("CONF001");
        assertNotNull(result);
        assertEquals("Conférence IA", result.getNom());
    }

    // Teste l'exception lorsqu'on ajoute un événement avec un ID déjà existant
    @Test
    void testAjouterEvenementDejaExistant() throws EvenementDejaExistantException {
        gestion.ajouterEvenement(conference1);
        assertThrows(EvenementDejaExistantException.class, () -> {
            gestion.ajouterEvenement(conference1);
        });
    }

    // Teste la suppression d'un événement
    @Test
    void testSupprimerEvenement() throws EvenementDejaExistantException {
        gestion.ajouterEvenement(conference1);
        gestion.supprimerEvenement("CONF001");
        Evenement result = gestion.rechercherEvenement("CONF001");
        assertNull(result);
    }

    // Teste la recherche d'un événement par ID
    @Test
    void testRechercherEvenement() throws EvenementDejaExistantException {
        gestion.ajouterEvenement(conference1);
        gestion.ajouterEvenement(conference2);
        Evenement result = gestion.rechercherEvenement("CONF002");
        assertNotNull(result);
        assertEquals("Conférence Blockchain", result.getNom());
    }

    // Teste la recherche par nom avec Streams
    @Test
    void testRechercherParNom() throws EvenementDejaExistantException {
        gestion.ajouterEvenement(conference1);
        gestion.ajouterEvenement(conference2);
        List<Evenement> result = gestion.rechercherParNom("Blockchain");
        assertEquals(1, result.size());
        assertEquals("Conférence Blockchain", result.get(0).getNom());
    }
}