package com.eventsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eventsystem.exception.EvenementDejaExistantException;
import com.eventsystem.model.Evenement;


// Classe Singleton pour gérer les événements
public class GestionEvenements {
    private static GestionEvenements instance;
    private Map<String, Evenement> evenements;

    // Constructeur privé pour le Singleton
    private GestionEvenements() {
        evenements = new HashMap<>();
    }

    // Méthode statique pour obtenir l'instance unique
    public static synchronized GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    // Ajoute un événement
    public void ajouterEvenement(Evenement evenement) throws EvenementDejaExistantException {
        if (evenements.containsKey(evenement.getId())) {
            throw new EvenementDejaExistantException("L'événement avec l'ID " + evenement.getId() + " existe déjà.");
        }
        evenements.put(evenement.getId(), evenement);
    }

    // Supprime un événement
    public void supprimerEvenement(String id) {
        evenements.remove(id);
    }

    // Recherche un événement par ID
    public Evenement rechercherEvenement(String id) {
        return evenements.get(id);
    }

    // Recherche des événements par nom (exemple avec Stream)
    public List<Evenement> rechercherParNom(String nom) {
        return evenements.values().stream()
                .filter(e -> e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Map<String, Evenement> getTousLesEvenements() {
        return evenements;
    }

}