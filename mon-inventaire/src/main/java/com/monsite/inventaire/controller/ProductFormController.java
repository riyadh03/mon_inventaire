package com.monsite.inventaire.controller;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;
import com.monsite.inventaire.service.OpenFoodFactsService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class ProductFormController {

    @FXML private TextField searchField;      // Champ de recherche
    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField nutriScoreField;
    @FXML private TextField ecoScoreField;
    @FXML private TextArea allergensArea;
    @FXML private Label statusLabel;
    @FXML private ComboBox<Product> productSelector; // Pour choisir parmi plusieurs résultats
    
    private ProductService productService;
    private OpenFoodFactsService apiService;
    private Product editingProduct;
    
    @FXML
    public void initialize() {
        productService = new ProductService();
        apiService = new OpenFoodFactsService();
        
        // Configurer le ComboBox
        productSelector.setCellFactory(lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                setText(empty || product == null ? "" : 
                    product.getName() + " - " + product.getBrand());
            }
        });
        
        productSelector.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                setText(empty || product == null ? "Sélectionner un produit" : 
                    product.getName());
            }
        });
        
        // Quand un produit est sélectionné dans la liste
        productSelector.setOnAction(e -> {
            Product selected = productSelector.getSelectionModel().getSelectedItem();
            if (selected != null) {
                populateForm(selected);
            }
        });
    }
    
    /**
     * Bouton "Rechercher" (nom ou code-barres)
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir un nom ou code-barres", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            setFormEnabled(false);
            statusLabel.setText("Recherche en cours...");
            
            // Recherche dans OpenFoodFacts
            List<Product> results = apiService.searchProducts(searchTerm);
            
            if (results.isEmpty()) {
                statusLabel.setText("❌ Aucun produit trouvé");
                showAlert("Information", "Aucun produit trouvé pour: " + searchTerm, 
                         Alert.AlertType.INFORMATION);
                return;
            }
            
            // Mettre à jour le ComboBox
            productSelector.getItems().clear();
            productSelector.getItems().addAll(results);
            productSelector.setVisible(true);
            
            if (results.size() == 1) {
                // Si un seul résultat, le sélectionner automatiquement
                productSelector.getSelectionModel().select(0);
                statusLabel.setText("✅ 1 produit trouvé");
            } else {
                statusLabel.setText("✅ " + results.size() + " produits trouvés - Sélectionnez");
            }
            
        } catch (Exception e) {
            statusLabel.setText("❌ Erreur de recherche");
            showAlert("Erreur", "Recherche impossible: " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        } finally {
            setFormEnabled(true);
        }
    }
    
    /**
     * Remplit le formulaire avec un produit
     */
    private void populateForm(Product product) {
        barcodeField.setText(product.getCode());
        nameField.setText(product.getName());
        brandField.setText(product.getBrand());
        categoryField.setText(product.getCategory());
        nutriScoreField.setText(product.getNutriScore());
        ecoScoreField.setText(product.getEcoScore());
        
        // Allergènes
        if (!product.getAllergenTags().isEmpty()) {
            allergensArea.setText(String.join(", ", product.getAllergenTags()));
        } else {
            allergensArea.setText("Aucun allergène détecté");
        }
        
        // Vegan
        if (product.isVegan()) {
            allergensArea.appendText("\n✅ Produit Vegan");
        }
        
        // Focus sur les champs à remplir par l'admin
        priceField.requestFocus();
        statusLabel.setText("Remplissez le prix et la quantité");
    }
    
    /**
     * Bouton "Sauvegarder dans l'inventaire"
     */
    @FXML
    private void handleSave() {
        try {
            // Validation
            String barcode = barcodeField.getText().trim();
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            
            if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                throw new IllegalArgumentException("Nom, prix et quantité sont obligatoires");
            }
            
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            
            if (price <= 0) {
                throw new IllegalArgumentException("Le prix doit être supérieur à 0");
            }
            
            if (quantity < 0) {
                throw new IllegalArgumentException("La quantité ne peut pas être négative");
            }
            
            // Vérifier si le produit existe déjà
            Product existing = productService.getProduct(barcode);
            if (existing != null && editingProduct == null) {
                throw new Exception("Ce produit existe déjà dans l'inventaire");
            }
            
            // Créer ou mettre à jour le produit
            Product product;
            if (editingProduct == null) {
                product = new Product();
                product.setCode(barcode);
                product.setName(name);
                product.setBrand(brandField.getText());
                product.setCategory(categoryField.getText());
                product.setOriginsCountry(""); // À ajuster si disponible
                product.setNutriScore(nutriScoreField.getText());
                product.setEcoScore(ecoScoreField.getText());
                product.setPrice(price);
                product.setQuantity(quantity);
                
                // Ajouter les données de l'API (si disponibles)
                Product apiProduct = productSelector.getSelectionModel().getSelectedItem();
                if (apiProduct != null) {
                    product.setImageUrl(apiProduct.getImageUrl());
                    product.setLabels(apiProduct.getLabels());
                    product.setVegan(apiProduct.isVegan());
                    product.setNutritionalValue(apiProduct.getNutritionalValue());
                    product.getIngredients().addAll(apiProduct.getIngredients());
                    product.getAllergenTags().addAll(apiProduct.getAllergenTags());
                }
                
                productService.addProduct(product);
                statusLabel.setText("✅ Produit ajouté à l'inventaire");
                showAlert("Succès", "Produit ajouté avec succès", Alert.AlertType.INFORMATION);
                
            } else {
                // Modification
                editingProduct.setName(name);
                editingProduct.setBrand(brandField.getText());
                editingProduct.setCategory(categoryField.getText());
                editingProduct.setNutriScore(nutriScoreField.getText());
                editingProduct.setEcoScore(ecoScoreField.getText());
                editingProduct.setPrice(price);
                editingProduct.setQuantity(quantity);
                
                productService.updateProduct(editingProduct);
                statusLabel.setText("✅ Produit mis à jour");
                showAlert("Succès", "Produit mis à jour", Alert.AlertType.INFORMATION);
            }
            
            // Réinitialiser le formulaire
            clearForm();
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix et la quantité doivent être des nombres valides", 
                     Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Réinitialiser le formulaire
     */
    private void clearForm() {
        searchField.clear();
        barcodeField.clear();
        nameField.clear();
        brandField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
        nutriScoreField.clear();
        ecoScoreField.clear();
        allergensArea.clear();
        productSelector.getItems().clear();
        productSelector.setVisible(false);
        statusLabel.setText("");
        editingProduct = null;
    }
    
    private void setFormEnabled(boolean enabled) {
        searchField.setDisable(!enabled);
        barcodeField.setDisable(!enabled);
        nameField.setDisable(!enabled);
        brandField.setDisable(!enabled);
        categoryField.setDisable(!enabled);
        quantityField.setDisable(!enabled);
        priceField.setDisable(!enabled);
        nutriScoreField.setDisable(!enabled);
        ecoScoreField.setDisable(!enabled);
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}