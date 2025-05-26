package com.eventsystem.observer;

import com.eventsystem.model.Participant;

import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les événements observables
public class EvenementObservable {
    private List<ParticipantObserver> observers = new ArrayList<>();
    private String message;

    // Ajoute un observer
    public void addObserver(ParticipantObserver observer) {
        observers.add(observer);
    }

    // Supprime un observer
    public void removeObserver(ParticipantObserver observer) {
        observers.remove(observer);
    }

    // Notifie tous les observers
    public void notifyObservers() {
        for (ParticipantObserver observer : observers) {
            observer.update(message);
        }
    }

    // Simule un changement (ex. : annulation d'événement)
    public void setMessage(String message) {
        this.message = message;
        notifyObservers();
    }
}