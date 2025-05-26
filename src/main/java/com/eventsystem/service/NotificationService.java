package com.eventsystem.service;

import java.util.concurrent.CompletableFuture;

// Interface pour le service de notification
public interface NotificationService {
    // Envoie une notification synchrone
    void envoyerNotification(String message);

    // Envoie une notification asynchrone (bonus)
    CompletableFuture<Void> envoyerNotificationAsync(String message);
}