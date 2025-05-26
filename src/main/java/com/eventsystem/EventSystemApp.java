package com.eventsystem;

import com.eventsystem.exception.EvenementDejaExistantException;
import com.eventsystem.model.*;
import com.eventsystem.service.GestionEvenements;
import com.eventsystem.service.NotificationService;
import com.eventsystem.service.NotificationServiceImpl;
import com.eventsystem.util.JsonSerializer;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class EventSystemApp extends JFrame {
    private static final String JSON_FILE = "data/events.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private GestionEvenements gestion = GestionEvenements.getInstance();
    private static final String DATA_FILE = "events.json";
    private Color primaryColor = new Color(70, 130, 180); // SteelBlue
    private Color secondaryColor = new Color(240, 240, 240); // White
    private Color accentColor = new Color(255, 165, 0); // Orange
    private Color textColor = new Color(50, 50, 50);        // Noir foncé (texte)

    private NotificationService notificationService = new NotificationServiceImpl();

    public EventSystemApp() {
        setTitle("Système de Gestion d'Événements");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Appliquer le style aux composants Swing
        UIManager.put("TabbedPane.background", secondaryColor);
        UIManager.put("TabbedPane.foreground", textColor);
        UIManager.put("TabbedPane.selected", primaryColor);
        UIManager.put("Panel.background", secondaryColor);
        UIManager.put("OptionPane.background", secondaryColor);
        UIManager.put("OptionPane.messageForeground", textColor);
        getContentPane().setBackground(secondaryColor);

        // Charger les événements depuis le fichier JSON
        chargerEvenements();

        JTabbedPane tabs = new JTabbedPane();
        // Style global
        tabs.setBackground(secondaryColor);
        tabs.setForeground(textColor);

        // Style des onglets individuels
        UIManager.put("TabbedPane.selected", primaryColor);
        UIManager.put("TabbedPane.selectHighlight", primaryColor);
        UIManager.put("TabbedPane.foreground", textColor);
        UIManager.put("TabbedPane.selectedForeground", primaryColor.darker());
        UIManager.put("TabbedPane.borderHighlightColor", primaryColor);

        SwingUtilities.updateComponentTreeUI(tabs);

        tabs.add("Créer Événement", creerOngletEvenements());
        tabs.add("Inscrire Participant", creerOngletParticipants());
        tabs.add("Actions", creerOngletActions());
        tabs.add("Liste Événements", creerOngletListeEvenements());

        add(tabs);
        setVisible(true);

        // Sauvegarder les événements lors de la fermeture
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                sauvegarderEvenements();
            }
        });

        // Charge les données au démarrage (ajoutez cette ligne)
        chargerDonnees();


        // Modifiez le WindowListener pour sauvegarder
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                sauvegarderDonnees();
                dispose();
            }
        });
    }

    private void chargerDonnees() {
        try {
            File file = new File(JSON_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Crée le dossier si inexistant
                file.createNewFile(); // Crée le fichier vide
                System.out.println("Fichier de données créé: " + JSON_FILE);
                return;
            }

            if (file.length() > 0) { // Vérifie que le fichier n'est pas vide
                List<Evenement> events = JsonSerializer.loadEvents(JSON_FILE);
                gestion.getTousLesEvenements().clear();
                events.forEach(ev -> {
                    try {
                        gestion.ajouterEvenement(ev);
                    } catch (EvenementDejaExistantException e) {
                        System.err.println("Événement déjà chargé: " + ev.getId());
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("ERREUR lors du chargement: " + e.getMessage());
        }
    }

    private void sauvegarderDonnees() {
        try {
            JsonSerializer.saveEvents(
                    new ArrayList<>(gestion.getTousLesEvenements().values()),
                    JSON_FILE
            );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerEvenements() {
        try {
            List<Evenement> events = JsonSerializer.loadEvents(JSON_FILE);
            events.forEach(ev -> {
                try {
                    gestion.ajouterEvenement(ev);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.println("Aucun fichier de données trouvé, création d'un nouveau.");
        }
    }

    private void sauvegarderEvenements() {
        try {
            // Crée le dossier data s'il n'existe pas
            new File("data").mkdirs();

            JsonSerializer.saveEvents(
                    new ArrayList<>(gestion.getTousLesEvenements().values()),
                    JSON_FILE
            );
            System.out.println("Données sauvegardées dans: " + new File(JSON_FILE).getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde:\n" + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel creerOngletEvenements() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Conférence", "Concert"});
        JTextField idField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField lieuField = new JTextField(20);
        JTextField capaciteField = new JTextField(20);
        JTextField themeField = new JTextField(20);
        JTextField intervenantsField = new JTextField(20);
        JTextField artisteField = new JTextField(20);
        JTextField genreField = new JTextField(20);

        // Configuration des champs
        themeField.setEnabled(false);
        intervenantsField.setEnabled(false);
        artisteField.setEnabled(false);
        genreField.setEnabled(false);

        typeCombo.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            themeField.setEnabled("Conférence".equals(type));
            intervenantsField.setEnabled("Conférence".equals(type));
            artisteField.setEnabled("Concert".equals(type));
            genreField.setEnabled("Concert".equals(type));
        });

        // Ajout des composants avec GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Type d'événement:"), gbc);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Lieu:"), gbc);
        gbc.gridx = 1;
        panel.add(lieuField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Capacité Max:"), gbc);
        gbc.gridx = 1;
        panel.add(capaciteField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Thème (Conférence):"), gbc);
        gbc.gridx = 1;
        panel.add(themeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Intervenants (séparés par ,):"), gbc);
        gbc.gridx = 1;
        panel.add(intervenantsField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Artiste (Concert):"), gbc);
        gbc.gridx = 1;
        panel.add(artisteField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Genre musical:"), gbc);
        gbc.gridx = 1;
        panel.add(genreField, gbc);

        JButton ajouterBtn = createButton("Créer l'événement");
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(ajouterBtn, gbc);

        ajouterBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String nom = nomField.getText();
                String lieu = lieuField.getText();
                int capacite = Integer.parseInt(capaciteField.getText());

                Evenement ev;
                if (typeCombo.getSelectedItem().equals("Conférence")) {
                    ev = new Conference(id, nom, LocalDateTime.now(), lieu, capacite,
                            themeField.getText(),
                            Arrays.asList(intervenantsField.getText().split(",")));
                } else {
                    ev = new Concert(id, nom, LocalDateTime.now(), lieu, capacite,
                            artisteField.getText(), genreField.getText());
                }
                gestion.ajouterEvenement(ev);


                JOptionPane.showMessageDialog(this, "Événement ajouté !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                clearFields(idField, nomField, lieuField, capaciteField, themeField, intervenantsField, artisteField, genreField);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel creerOngletParticipants() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(20);
        JTextField nomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JComboBox<String> eventList = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Nom:"), gbc);
        gbc.gridx = 1;
        panel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createLabel("Événement:"), gbc);
        gbc.gridx = 1;
        panel.add(eventList, gbc);

        JButton inscrireBtn = createButton("Inscrire");
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(inscrireBtn, gbc);

        inscrireBtn.addActionListener(e -> {
            String id = idField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            Participant p = new Participant(id, nom, email);

            String eventId = (String) eventList.getSelectedItem();
            Evenement ev = gestion.rechercherEvenement(eventId);
            if (ev != null) {
                try {
                    ev.ajouterParticipant(p);
                    JOptionPane.showMessageDialog(this, "Participant inscrit !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    clearFields(idField, nomField, emailField);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un événement", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                eventList.removeAllItems();
                for (String key : gestion.getTousLesEvenements().keySet()) {
                    eventList.addItem(key);
                }
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });

        return panel;
    }

    private JPanel creerOngletActions() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea eventList = new JTextArea();
        eventList.setEditable(false);
        eventList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        eventList.setBackground(new Color(240, 240, 240));

        JButton annulerBtn = createButton("Annuler un événement");
        JButton exporterBtn = createButton("Exporter en JSON");
        JButton importerBtn = createButton("Importer depuis JSON");

        JComboBox<String> combo = new JComboBox<>();
        combo.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Evenement ev : gestion.getTousLesEvenements().values()) {
                sb.append("ID: ").append(ev.getId()).append("\n");
                sb.append("Nom: ").append(ev.getNom()).append("\n");
                sb.append("Date: ").append(ev.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
                sb.append("Lieu: ").append(ev.getLieu()).append("\n");
                sb.append("Capacité: ").append(ev.getParticipants().size()).append("/").append(ev.getCapaciteMax()).append("\n");

                if (ev instanceof Conference) {
                    Conference conf = (Conference) ev;
                    sb.append("Type: Conférence\n");
                    sb.append("Thème: ").append(conf.getTheme()).append("\n");
                    sb.append("Intervenants: ").append(String.join(", ", conf.getIntervenants())).append("\n");
                } else if (ev instanceof Concert) {
                    Concert concert = (Concert) ev;
                    sb.append("Type: Concert\n");
                    sb.append("Artiste: ").append(concert.getArtiste()).append("\n");
                    sb.append("Genre: ").append(concert.getGenreMusical()).append("\n");
                }
                sb.append("\n--------------------------------\n\n");
            }
            eventList.setText(sb.toString());
        });

        annulerBtn.addActionListener(e -> {
            String id = (String) combo.getSelectedItem();
            if (id != null) {
                Evenement ev = gestion.rechercherEvenement(id);
                if (ev != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Êtes-vous sûr de vouloir annuler cet événement?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ev.annuler();
                        // Notification asynchrone
                        notificationService.envoyerNotificationAsync("L'événement " + ev.getNom() + " a été annulé");
                        JOptionPane.showMessageDialog(this, "Événement annulé et participants notifiés.",
                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        exporterBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exporter les événements");
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    JsonSerializer.saveEvents(List.copyOf(gestion.getTousLesEvenements().values()),
                            fileChooser.getSelectedFile().getPath());
                    JOptionPane.showMessageDialog(this, "Événements exportés avec succès!",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'export: " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        importerBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Importer des événements");
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    List<Evenement> events = JsonSerializer.loadEvents(fileChooser.getSelectedFile().getPath());
                    gestion.getTousLesEvenements().clear();
                    events.forEach(ev -> {
                        try {
                            gestion.ajouterEvenement(ev);
                        } catch (Exception ex) {
                            System.err.println("Erreur lors de l'import: " + ex.getMessage());
                        }
                    });
                    JOptionPane.showMessageDialog(this, "Événements importés avec succès!",
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'import: " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        combo.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                combo.removeAllItems();
                for (String key : gestion.getTousLesEvenements().keySet()) {
                    combo.addItem(key);
                }
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(secondaryColor);
        topPanel.add(new JLabel("Sélectionner un événement:"), BorderLayout.WEST);
        topPanel.add(combo, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.add(annulerBtn);
        buttonPanel.add(exporterBtn);
        buttonPanel.add(importerBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        return panel;
    }

    private JPanel creerOngletListeEvenements() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(secondaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTable table = new JTable();
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setBackground(secondaryColor);
        table.setForeground(textColor);
        table.setGridColor(primaryColor);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(textColor);

        // Modèle de table personnalisé
        EvenementTableModel model = new EvenementTableModel(
                new ArrayList<>(gestion.getTousLesEvenements().values()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor, 2),
                "Liste des événements",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                primaryColor));
        scrollPane.setBackground(secondaryColor);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Ajoutez un bouton de rafraîchissement
        JButton refreshBtn = new JButton("Rafraîchir");
        refreshBtn.addActionListener(e -> {
            model.setEvenements(new ArrayList<>(gestion.getTousLesEvenements().values()));
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(textColor); // Utilisation de la couleur de texte définie
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(primaryColor);
        button.setForeground(secondaryColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });

        return button;
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EventSystemApp();
        });
    }
}

class EvenementTableModel extends AbstractTableModel {
    private List<Evenement> evenements;
    private final String[] columnNames = {"ID", "Nom", "Type", "Date", "Lieu", "Capacité", "Inscrits"};

    public EvenementTableModel(List<Evenement> evenements) {
        this.evenements = evenements;
    }

    @Override
    public int getRowCount() {
        return evenements.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Evenement ev = evenements.get(rowIndex);
        switch (columnIndex) {
            case 0: return ev.getId();
            case 1: return ev.getNom();
            case 2: return (ev instanceof Conference) ? "Conférence" : "Concert";
            case 3: return ev.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            case 4: return ev.getLieu();
            case 5: return ev.getCapaciteMax();
            case 6: return ev.getParticipants().size();
            default: return null;
        }
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
        fireTableDataChanged(); // Notifie le tableau que les données ont changé
    }
}