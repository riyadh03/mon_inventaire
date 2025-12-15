package com.monsite.inventaire.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;
import com.monsite.inventaire.dao.ProductDAO; // Pour l'initialisation du Service
import com.monsite.inventaire.exception.ProductNotFoundException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;

/**
 * Gère les interactions utilisateur et agit comme la couche d'accès à la logique métier (Service).
 */
public class MainController implements Initializable {
    
    // Déclaration des éléments UI (à relier dans Scene Builder)
    @FXML
    private TableView<Product> productTable;
    
    // Déclaration du Service Métier (Business Logic)
    private ProductService productService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation de la dépendance (pour un projet simple)
        // NOTE: En production, on utiliserait un framework d'injection.
        ProductDAO dummyDao = new ProductDAO(); // Remplacer par l'implémentation concrète
        this.productService = new ProductService(dummyDao);
        
        // Initialisation de la table (définition des colonnes, etc.)
        // Nous reviendrons sur cette configuration JavaFX plus tard.
        
        System.out.println("MainController initialized.");
    }
    
    // Exemple d'action utilisateur: Charger un produit (déclenché par un bouton FXML)
    @FXML
    private void handleLoadProduct() {
        // Exemple d'appel au Service, avec gestion des exceptions
        String barcode = "123456789"; // Exemple
        try {
            Product product = productService.getProduct(barcode);
            System.out.println("Product loaded: " + product.getName());
            // Mise à jour de l'UI ici (ex: afficher les détails)
        } catch (ProductNotFoundException e) {
            // Affichage d'une alerte à l'utilisateur
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product Search Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    // Ajoutez ici d'autres méthodes @FXML pour les autres actions (Ajouter, Supprimer, Filtrer...)
}