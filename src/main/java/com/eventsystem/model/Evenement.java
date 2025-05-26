package com.eventsystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eventsystem.exception.CapaciteMaxAtteinteException;
import com.eventsystem.observer.EvenementObservable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


public abstract class Evenement {
    private String id;
    private String nom;
    private LocalDateTime date;
    private String lieu;
    private int capaciteMax;
    private List<Participant> participants = new ArrayList<>();
    private EvenementObservable observable = new EvenementObservable();

    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
    }

    // Une seule version de la méthode ajouterParticipant
    public void ajouterParticipant(Participant participant) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement : " + nom);
        }
        participants.add(participant);
        observable.addObserver(participant);
    }

    // Une seule version de la méthode annuler
    public void annuler() {
        observable.setMessage("L'événement '" + nom + "' a été annulé.");
        System.out.println("Événement " + nom + " annulé.");
    }

    public abstract void afficherDetails();

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public LocalDateTime getDate() { return date; }
    public String getLieu() { return lieu; }
    public int getCapaciteMax() { return capaciteMax; }
    public List<Participant> getParticipants() { return participants; }
}