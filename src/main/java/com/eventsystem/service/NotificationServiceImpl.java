package com.eventsystem.service;

import java.util.concurrent.CompletableFuture;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public void envoyerNotification(String message) {
        System.out.println("Notification envoyée: " + message);
    }

    @Override
    public CompletableFuture<Void> envoyerNotificationAsync(String message) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Simulation de délai
                Thread.sleep(2000);
                System.out.println("Notification asynchrone envoyée: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}