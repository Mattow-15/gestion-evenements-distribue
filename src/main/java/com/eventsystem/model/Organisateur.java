package com.eventsystem.model;

import java.util.ArrayList;
import java.util.List;

// Classe représentant un organisateur, héritant de Participant
public class Organisateur extends Participant {
    private List<Evenement> evenementsOrganises;

    // Constructeur
    public Organisateur(String id, String nom, String email) {
        super(id, nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }

    // Ajoute un événement organisé
    public void ajouterEvenementOrganise(Evenement evenement) {
        evenementsOrganises.add(evenement);
    }

    // Getters et setters
    public List<Evenement> getEvenementsOrganises() { return evenementsOrganises; }
}