package com.monsite.inventaire.controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

// Initializable est l'interface standard qui assure que le contrôleur est prêt après le chargement du FXML.
public class MainController implements Initializable {

    // Cette méthode est appelée juste après que l'interface FXML a été entièrement chargée.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ici, nous pouvons initialiser des éléments de la vue :
        // - Charger des données initiales dans un tableau.
        // - Configurer des événements.
        System.out.println("MainController initialisé. L'application est prête.");
    }
    
    // Exemple de méthode qui sera appelée si un bouton est défini avec onAction="#handleQuit" dans le FXML
    // public void handleQuit() {
    //     System.exit(0);
    // }
}