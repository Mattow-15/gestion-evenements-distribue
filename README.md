# 🎉 Système de Gestion d'Événements Distribué

## 📌 Objectif du Projet

Ce projet a pour but de mettre en œuvre des concepts avancés de la **programmation orientée objet (POO)**, notamment :

- Héritage, polymorphisme et interfaces
- Design patterns (Observer, Factory, Singleton, Strategy)
- Gestion des exceptions personnalisées
- Manipulation de collections génériques
- Sérialisation/Désérialisation (JSON et/ou XML)
- Programmation événementielle et asynchrone

---

## 🛠 Fonctionnalités principales

- 👥 Inscription des participants à différents événements (conférences, concerts, etc.)
- 👨‍💼 Gestion des organisateurs et des événements organisés
- 🔔 Notifications en temps réel (annulation, modification, etc.)
- 💾 Persistance des données (JSON/XML)
- 🔄 Sérialisation/désérialisation des objets
- ✅ Tests unitaires avec JUnit
- 🚀 Programmation asynchrone avec `CompletableFuture` (bonus)

---

## 🧩 Modélisation des classes

### Classes principales :

- `Evenement` (abstraite) : contient `id`, `nom`, `date`, `lieu`, `capaciteMax`
- `Conference` : hérite de `Evenement`, ajoute `theme`, `intervenants`
- `Concert` : hérite de `Evenement`, ajoute `artiste`, `genreMusical`
- `Participant` : `id`, `nom`, `email`
- `Organisateur` : hérite de `Participant`, ajoute `evenementsOrganises`
- `GestionEvenements` : Singleton qui gère les événements (ajout, recherche, suppression)
- `NotificationService` : interface d'envoi de notifications

### Design Patterns utilisés :

- `Observer` : participants sont notifiés de changements d'événements
- `Factory` : création dynamique d’événements (Conférence ou Concert)
- `Singleton` : `GestionEvenements`
- `Strategy` : algorithme de filtrage ou tri d’événements (optionnel)

---

## 🔧 Technologies utilisées

- Langage : **Java**
- IDE : IntelliJ IDEA / Eclipse / VS Code
- Bibliothèques :
  - `Jackson` pour la sérialisation JSON
  - `JAXB` pour XML (optionnel)
  - `JUnit` pour les tests
  - `CompletableFuture` pour l’asynchronisme

---

## ✅ Comment exécuter le projet

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/Mattow-15/gestion-evenements-distribue.git
   cd gestion-evenements-distribue
2. Ouvrir dans votre IDE préféré

3. Compiler et exécuter le projet

4. Lancer les tests
