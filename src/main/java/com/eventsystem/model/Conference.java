package com.eventsystem.model;

import java.time.LocalDateTime;
import java.util.List;

// Classe représentant une conférence, héritant de Evenement
public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    // Constructeur
    public Conference() {
        super(null, null, null, null, 0); // ou initialisez avec des valeurs par défaut
    }

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                      String theme, List<String> intervenants) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = intervenants;
    }

    // Implémentation de la méthode abstraite pour afficher les détails
    @Override
    public void afficherDetails() {
        System.out.println("Conférence: " + getNom() + " | Thème: " + theme);
        System.out.println("Date: " + getDate() + " | Lieu: " + getLieu());
        System.out.println("Intervenants: " + intervenants);
        System.out.println("Participants inscrits: " + getParticipants().size() + "/" + getCapaciteMax());
    }

    // Getters et setters
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public List<String> getIntervenants() { return intervenants; }
    public void setIntervenants(List<String> intervenants) { this.intervenants = intervenants; }
}