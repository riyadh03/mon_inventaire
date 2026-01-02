package com.monsite.inventaire.controller;

import com.monsite.inventaire.exception.ProductNotFoundException;
import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductTableController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> codeColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> brandColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, String> quantityColumn;
    @FXML
    private TableColumn<Product, String> nutriScoreColumn;
    @FXML
    private TableColumn<Product, String> ecoScoreColumn;

    private ProductService productService;

//    @FXML
//    public void initialize() {
//        productService = new ProductService();
//
//        // Configurer les colonnes
//        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
//        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
//        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        nutriScoreColumn.setCellValueFactory(new PropertyValueFactory<>("nutriScore"));
//        ecoScoreColumn.setCellValueFactory(new PropertyValueFactory<>("ecoScore"));
//        
//    	// Lier les données au tableau	
//        productTable.setItems(productService.getAllProducts());
//        
//        
//    }
    
    @FXML
    public void initialize() {
        System.out.println("\n=== INITIALISATION TABLEVIEW ===");
        
        try {
            // 1. Initialiser service
            productService = new ProductService();
            System.out.println("1. Service créé");
            
            // 2. Tester connexion DB
            System.out.println("2. Test connexion DB...");
            int dbCount = productService.getAllProducts().size(); // Si tu as cette méthode
            System.out.println("   Produits en DB: " + dbCount);
            
            // 3. Configurer colonnes
            System.out.println("3. Configuration des colonnes...");
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            nutriScoreColumn.setCellValueFactory(new PropertyValueFactory<>("nutriScore"));
            ecoScoreColumn.setCellValueFactory(new PropertyValueFactory<>("ecoScore"));
            System.out.println("   Colonnes configurées");
            
            // 4. Remplir TableView
            System.out.println("4. Remplissage TableView...");
            ObservableList<Product> products = productService.getAllProducts();
            System.out.println("   Données récupérées: " + products.size() + " produits");
            
            productTable.setItems(products);
            System.out.println("   TableView remplie");
            
            // 5. Vérifier affichage
            System.out.println("5. Vérification affichage:");
            System.out.println("   Lignes dans table: " + productTable.getItems().size());
            System.out.println("   Colonnes visibles: " + productTable.getColumns().size());
            
            // 6. Si table vide, ajouter un produit test
            if (products.isEmpty()) {
                System.out.println("6. Table vide - ajout produit test...");
                Product testProduct = new Product(
                    "DEMO-001", 
                    "Produit Démo", 
                    "Marque Démo",
                    "Catégorie Démo",
                    99,
                    "France",
                    "A",
                    "B"
                );
                productService.addProduct(testProduct);
                
                // Rafraîchir TableView
                productTable.setItems(productService.getAllProducts());
                System.out.println("   Produit démo ajouté et table rafraîchie");
            }
            
            System.out.println("✅ Initialisation TableView TERMINÉE");
            
        } catch (Exception e) {
            System.out.println("❌ ERREUR CRITIQUE: " + e.getMessage());
            e.printStackTrace();
            
            // Mode dégradé : créer des données fictives pour tester l'UI
            System.out.println("Mode dégradé : données fictives pour test UI");
            ObservableList<Product> fakeData = FXCollections.observableArrayList(
                new Product("FAKE-1", "Produit Test 1", "Marque A", "Cat 1", 10, "FR", "A", "B"),
                new Product("FAKE-2", "Produit Test 2", "Marque B", "Cat 2", 20, "UK", "C", "D")
            );
            productTable.setItems(fakeData);
        }
    }
    @FXML
    private void handleAdd() {
        System.out.println("Ajouter produit");
        // À compléter avec ProductFormController
    }

    @FXML
    private void handleEdit() {
        System.out.println("Modifier produit");
    }

    @FXML
    private void handleDelete() throws ProductNotFoundException {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productService.deleteProduct(selected.getCode());
        }
    }
}
