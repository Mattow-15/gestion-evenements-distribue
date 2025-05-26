package com.eventsystem.model;

import com.eventsystem.observer.ParticipantObserver;

// Classe représentant un participant
public class Participant implements ParticipantObserver {
    private String id;
    private String nom;
    private String email;

    // Constructeur
    public Participant(String id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
    }
    public void update(String message) {
        System.out.println("Notification pour " + nom + " (" + email + "): " + message);
        // Envoyer un email réel dans une version finale
    }
    // Getters et setters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}