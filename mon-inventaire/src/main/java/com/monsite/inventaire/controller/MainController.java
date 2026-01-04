package com.monsite.inventaire.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {
    
    @FXML private StackPane contentPane;
    @FXML private Label statusLabel;
    
    @FXML
    public void initialize() {
        statusLabel.setText("Système initialisé - Prêt");
    }
    
    @FXML
    private void showProductManagement() {
        loadView("/fxml/admin/ProductManagement.fxml");
        statusLabel.setText("Gestion des produits");
    }
    
    @FXML
    private void showCommandManagement() {
        loadView("/fxml/admin/CommandManagement.fxml");
        statusLabel.setText("Gestion des commandes");
    }
    
    @FXML
    private void showUserManagement() {
        loadView("/fxml/admin/UserManagement.fxml");
        statusLabel.setText("Gestion des clients");
    }
    
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            statusLabel.setText("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}