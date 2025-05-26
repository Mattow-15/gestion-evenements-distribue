package com.eventsystem.model;

import java.time.LocalDateTime;

// Classe représentant un concert, héritant de Evenement
public class Concert extends Evenement {
    private String artiste;
    private String genreMusical;

    // Constructeur
    // ... même chose, constructeur sans args
    public Concert() {
        super(null, null, null, null, 0);
    }

    public Concert(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                   String artiste, String genreMusical) {
        super(id, nom, date, lieu, capaciteMax);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
    }

    // Implémentation de la méthode abstraite pour afficher les détails
    @Override
    public void afficherDetails() {
        System.out.println("Concert: " + getNom() + " | Artiste: " + artiste);
        System.out.println("Genre: " + genreMusical + " | Date: " + getDate() + " | Lieu: " + getLieu());
        System.out.println("Participants inscrits: " + getParticipants().size() + "/" + getCapaciteMax());
    }

    // Getters et setters
    public String getArtiste() { return artiste; }
    public void setArtiste(String artiste) { this.artiste = artiste; }
    public String getGenreMusical() { return genreMusical; }
    public void setGenreMusical(String genreMusical) { this.genreMusical = genreMusical; }
}