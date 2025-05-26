
package com.eventsystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.eventsystem.exception.CapaciteMaxAtteinteException;
import com.eventsystem.exception.EvenementDejaExistantException;
import com.eventsystem.model.Concert;
import com.eventsystem.model.Conference;
import com.eventsystem.model.Evenement;
import com.eventsystem.model.Organisateur;
import com.eventsystem.model.Participant;
import com.eventsystem.observer.EvenementObservable;
import com.eventsystem.observer.ParticipantObserver;
import com.eventsystem.service.GestionEvenements;
import com.eventsystem.service.NotificationService;
import com.eventsystem.util.JsonSerializer;


// Classe principale pour démontrer le système de gestion d'événements
public class Main {
    public static void main(String[] args) throws IOException {
        // Obtenir l'instance Singleton de GestionEvenements
        GestionEvenements gestion = GestionEvenements.getInstance();

        // Créer un observable pour les notifications
        EvenementObservable observable = new EvenementObservable();

        // Créer des participants
        Participant p1 = new Participant("P001", "Alice", "alice@example.com");
        Participant p2 = new Participant("P002", "Bob", "bob@example.com");

        // Créer un organisateur
        Organisateur org = new Organisateur("O001", "Charlie", "charlie@example.com");

        // Implémenter un service de notification (exemple simple)
        NotificationService notificationService = new NotificationService() {
            @Override
            public void envoyerNotification(String message) {
                System.out.println("Notification envoyée : " + message);
            }

            @Override
            public CompletableFuture<Void> envoyerNotificationAsync(String message) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(1000); // Simuler un délai
                        System.out.println("Notification asynchrone envoyée : " + message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        // Implémenter l'observer pour les participants
        ParticipantObserver observer1 = message -> {
            System.out.println("Participant " + p1.getNom() + " notifié : " + message);
            notificationService.envoyerNotification("Message pour " + p1.getNom() + " : " + message);
        };
        ParticipantObserver observer2 = message -> {
            System.out.println("Participant " + p2.getNom() + " notifié : " + message);
            notificationService.envoyerNotification("Message pour " + p2.getNom() + " : " + message);
        };

        // Ajouter les observers
        observable.addObserver(observer1);
        observable.addObserver(observer2);

        try {
            // Créer des événements
            Conference conf = new Conference("CONF001", "Conférence IA", LocalDateTime.now(),
                    "Paris", 100, "Intelligence Artificielle", Arrays.asList("Dr. Smith"));
            Concert concert = new Concert("CONC001", "Concert Rock", LocalDateTime.now().plusDays(1),
                    "Lyon", 500, "The Rockers", "Rock");

            // Ajouter les événements au gestionnaire
            gestion.ajouterEvenement(conf);
            gestion.ajouterEvenement(concert);

            // Associer l'organisateur à un événement
            org.ajouterEvenementOrganise(conf);

            // Inscrire des participants
            conf.ajouterParticipant(p1);
            conf.ajouterParticipant(p2);

            // Afficher les détails
            System.out.println("\nDétails de la conférence :");
            conf.afficherDetails();

            // Simuler une annulation avec notification
            System.out.println("\nAnnulation de l'événement :");
            observable.setMessage("L'événement " + conf.getNom() + " a été annulé.");
            conf.annuler();

            // Envoi asynchrone de notification (bonus)
            System.out.println("\nEnvoi asynchrone de notifications :");
            notificationService.envoyerNotificationAsync("Mise à jour importante pour l'événement CONF001.");

            // Sauvegarder les événements en JSON
            List<Evenement> evenements = gestion.rechercherParNom("");
            JsonSerializer.saveEvents(evenements, "src/main/resources/events.json");
            System.out.println("\nÉvénements sauvegardés dans events.json");

            // Charger les événements depuis JSON
            List<Evenement> loadedEvents = JsonSerializer.loadEvents("src/main/resources/events.json");
            System.out.println("\nÉvénements chargés depuis JSON :");
            loadedEvents.forEach(Evenement::afficherDetails);

        } catch (EvenementDejaExistantException | CapaciteMaxAtteinteException  e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
