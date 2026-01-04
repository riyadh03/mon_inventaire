package com.monsite.inventaire.controller.admin;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;
import com.monsite.inventaire.service.OpenFoodFactsService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.List;

public class ProductManagementController {
    
    // === COMPOSANTS TABLEAU ===
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> codeColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> brandColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> nutriScoreColumn;
    
    // === COMPOSANTS RECHERCHE/IMPORT ===
    @FXML private TextField searchField;
    @FXML private ComboBox<Product> searchResultsCombo;
    @FXML private Button importButton;
    @FXML private Label searchStatusLabel;
    @FXML private VBox apiResultsContainer; // ⚠️ AJOUTÉ
    @FXML private Label statsLabel; // ⚠️ AJOUTÉ
    
    // === COMPOSANTS DÉTAILS PRODUIT ===
    @FXML private ImageView productImageView;
    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField brandField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField nutriScoreField;
    @FXML private TextField ecoScoreField;
    @FXML private TextField originsField;
    @FXML private TextArea allergensArea;
    @FXML private Label veganLabel;
    @FXML private Label nutritionLabel;
    
    // === SERVICES ===
    private ProductService productService;
    private OpenFoodFactsService apiService;
    private Product currentProduct;
    
    @FXML
    public void initialize() {
        productService = new ProductService();
        apiService = new OpenFoodFactsService();
        
        setupTable();
        setupSearchCombo();
        loadProducts();
        clearForm();
        
        // ⚠️ AJOUTÉ: Initialiser la visibilité du container API
        apiResultsContainer.setVisible(false);
        apiResultsContainer.setManaged(false);
        
        // ⚠️ AJOUTÉ: Gérer la sélection dans le ComboBox
        searchResultsCombo.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                apiResultsContainer.setVisible(true);
                apiResultsContainer.setManaged(true);
            }
        });
    }
    
    private void setupTable() {
        // Configurer les colonnes
        codeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCode()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        brandColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBrand()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategory()));
        quantityColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getQuantity()));
        priceColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrice()));
        nutriScoreColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNutriScore()));
        
        // Sélection tableau -> afficher détails
        productTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> showProductDetails(newSelection)
        );
    }
    
    private void setupSearchCombo() {
        searchResultsCombo.setCellFactory(lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                setText(empty || product == null ? "" : 
                    product.getName() + " (" + product.getBrand() + ")");
            }
        });
        
        // ⚠️ CORRIGÉ: Pas de paramètre apiProduct
        searchResultsCombo.setOnAction(e -> {
            Product selected = searchResultsCombo.getSelectionModel().getSelectedItem();
            if (selected != null) {
                populateFormFromAPI(selected);
            }
        });
    }
    
    private void loadProducts() {
        productTable.setItems(productService.getAllProducts());
        updateStatsLabel(); // ⚠️ AJOUTÉ
    }
    
    // ⚠️ NOUVELLE MÉTHODE: Mettre à jour les statistiques
    private void updateStatsLabel() {
        int count = productService.getProductCount();
        double value = productService.getInventoryValue();
        statsLabel.setText(String.format("Produits: %d | Valeur totale: %.2f€", count, value));
    }
    
    // === GESTION RECHERCHE API ===
    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) return;
        
        searchStatusLabel.setText("Recherche en cours...");
        importButton.setDisable(true);
        
        new Thread(() -> {
            try {
                List<Product> results = apiService.searchProducts(searchTerm);
                
                javafx.application.Platform.runLater(() -> {
                    searchResultsCombo.getItems().setAll(results);
                    
                    if (results.isEmpty()) {
                        searchStatusLabel.setText("❌ Aucun résultat");
                        apiResultsContainer.setVisible(false);
                        apiResultsContainer.setManaged(false);
                    } else {
                        searchStatusLabel.setText("✅ " + results.size() + " produit(s) trouvé(s)");
                        apiResultsContainer.setVisible(true);
                        apiResultsContainer.setManaged(true);
                        searchResultsCombo.getSelectionModel().select(0);
                    }
                    importButton.setDisable(false);
                });
                
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    searchStatusLabel.setText("❌ Erreur: " + e.getMessage());
                    importButton.setDisable(false);
                });
            }
        }).start();
    }
    
    // ⚠️ NOUVELLE MÉTHODE: Gérer l'import
    @FXML
    public void handleImport() {
        Product selected = searchResultsCombo.getSelectionModel().getSelectedItem();
        if (selected != null) {
            populateFormFromAPI(selected);
        }
    }
    
    private void populateFormFromAPI(Product apiProduct) {
        currentProduct = null; // Nouveau produit
        
        codeField.setText(apiProduct.getCode());
        nameField.setText(apiProduct.getName());
        brandField.setText(apiProduct.getBrand());
        categoryField.setText(apiProduct.getCategory());
        originsField.setText(apiProduct.getOriginsCountry());
        nutriScoreField.setText(apiProduct.getNutriScore());
        ecoScoreField.setText(apiProduct.getEcoScore());
        
        // Allergènes
        if (!apiProduct.getAllergenTags().isEmpty()) {
            allergensArea.setText(String.join("\n", apiProduct.getAllergenTags()));
        }
        
        // Vegan
        veganLabel.setText(apiProduct.isVegan() ? "✅ VEGAN" : "❌ NON VEGAN");
        veganLabel.setStyle(apiProduct.isVegan() ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red;");
        
        // Image
        if (apiProduct.getImageUrl() != null && !apiProduct.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(apiProduct.getImageUrl(), 150, 150, true, true);
                productImageView.setImage(image);
            } catch (Exception e) {
                productImageView.setImage(null);
            }
        }
        
        // Nutrition
        if (apiProduct.getNutritionalValue() != null) {
            nutritionLabel.setText(String.format("Calories: %.0f kcal/100g", 
                apiProduct.getNutritionalValue().getEnergy()));
        }
        
        // Focus sur prix/quantité (à remplir par admin)
        priceField.requestFocus();
    }
    

    // === CRUD PRODUITS ===
    @FXML
    public void handleSave() {
        try {
            // Validation
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            
            if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                throw new IllegalArgumentException("Nom, prix et quantité obligatoires");
            }
            
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            
            if (price <= 0 || quantity < 0) {
                throw new IllegalArgumentException("Prix > 0 et quantité ≥ 0");
            }
            
            // 🔧 CORRECTION: Utiliser addProduct() ou updateProduct() selon le cas
            if (currentProduct == null) {
                // NOUVEAU produit
                handleAddProduct(code, name, price, quantity);
            } else {
                // MODIFICATION produit existant
                handleUpdateProduct(name, price, quantity);
            }
            
            loadProducts();
            clearForm();
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix et quantité doivent être des nombres", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    // 🔧 NOUVELLE MÉTHODE: Ajouter un nouveau produit
    private void handleAddProduct(String code, String name, double price, int quantity) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setBrand(brandField.getText());
        product.setCategory(categoryField.getText());
        product.setOriginsCountry(originsField.getText());
        product.setNutriScore(nutriScoreField.getText());
        product.setEcoScore(ecoScoreField.getText());
        product.setPrice(price);
        product.setQuantity(quantity);
        
        // Ajouter données API si disponibles
        Product apiProduct = searchResultsCombo.getSelectionModel().getSelectedItem();
        if (apiProduct != null) {
            copyApiDataToProduct(apiProduct, product);
        }
        
        // 🔧 CORRECTION: Utiliser addProduct() pour nouveau produit
        productService.addProduct(product);
        showAlert("Succès", "Produit ajouté", Alert.AlertType.INFORMATION);
    }
    
    // 🔧 NOUVELLE MÉTHODE: Mettre à jour un produit existant
    private void handleUpdateProduct(String name, double price, int quantity) {
        currentProduct.setName(name);
        currentProduct.setBrand(brandField.getText());
        currentProduct.setCategory(categoryField.getText());
        currentProduct.setOriginsCountry(originsField.getText());
        currentProduct.setNutriScore(nutriScoreField.getText());
        currentProduct.setEcoScore(ecoScoreField.getText());
        currentProduct.setPrice(price);
        currentProduct.setQuantity(quantity);
        
        // 🔧 CORRECTION: Utiliser updateProduct() pour modification
        productService.updateProduct(currentProduct);
        showAlert("Succès", "Produit mis à jour", Alert.AlertType.INFORMATION);
    }
    
    // 🔧 NOUVELLE MÉTHODE UTILITAIRE: Copier données API
    private void copyApiDataToProduct(Product source, Product target) {
        target.setImageUrl(source.getImageUrl());
        target.setLabels(source.getLabels());
        target.setVegan(source.isVegan());
        target.setNutritionalValue(source.getNutritionalValue());
        target.setIngredients(source.getIngredients());
        target.setAllergenTags(source.getAllergenTags());
    }
    
    @FXML
    public void handleDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Sélectionnez un produit à supprimer", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le produit ?");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer : " + selected.getName());
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                productService.deleteProduct(selected.getCode());
                loadProducts();
                clearForm();
                showAlert("Succès", "Produit supprimé", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    private void showProductDetails(Product product) {
        if (product == null) return;
        
        currentProduct = product;
        
        codeField.setText(product.getCode());
        nameField.setText(product.getName());
        brandField.setText(product.getBrand());
        categoryField.setText(product.getCategory());
        quantityField.setText(String.valueOf(product.getQuantity()));
        priceField.setText(String.valueOf(product.getPrice()));
        nutriScoreField.setText(product.getNutriScore());
        ecoScoreField.setText(product.getEcoScore());
        originsField.setText(product.getOriginsCountry());
        
        // Allergènes
        if (!product.getAllergenTags().isEmpty()) {
            allergensArea.setText(String.join("\n", product.getAllergenTags()));
        }
        
        // Vegan
        veganLabel.setText(product.isVegan() ? "✅ VEGAN" : "❌ NON VEGAN");
        
        // Image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(product.getImageUrl(), 150, 150, true, true);
                productImageView.setImage(image);
            } catch (Exception e) {
                productImageView.setImage(null);
            }
        }
    }
    @FXML
    public void clearForm() {
        currentProduct = null;
        codeField.clear();
        nameField.clear();
        brandField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
        nutriScoreField.clear();
        ecoScoreField.clear();
        originsField.clear();
        allergensArea.clear();
        veganLabel.setText("");
        nutritionLabel.setText("");
        productImageView.setImage(null);
        searchField.clear();
        searchResultsCombo.getItems().clear();
        searchResultsCombo.setVisible(false);
        searchStatusLabel.setText("");
        
        productTable.getSelectionModel().clearSelection();
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}