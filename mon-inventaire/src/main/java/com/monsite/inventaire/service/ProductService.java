package com.monsite.inventaire.service;

import com.monsite.inventaire.model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.monsite.inventaire.dao.ProductDAO;
import com.monsite.inventaire.exception.ProductNotFoundException;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;
    private final ObservableList<Product> observableProductList;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.observableProductList = FXCollections.observableArrayList();
        refreshFromDatabase(); // Charger initialement depuis DB
    }

    public ObservableList<Product> getAllProducts() {
        return observableProductList;
    }

    public void addProduct(Product product) {
        productDAO.save(product);
        refreshFromDatabase(); // Rafraîchir la liste après ajout
    }

    public void deleteProduct(String code) throws ProductNotFoundException {
        if (productDAO.findByCode(code) == null) {
            throw new ProductNotFoundException("Produit introuvable pour suppression");
        }
        productDAO.delete(code);
        refreshFromDatabase(); // Rafraîchir la liste après suppression
    }

    private void refreshFromDatabase() {
        List<Product> dbProducts = productDAO.getAll();
        observableProductList.setAll(dbProducts); // Mettre à jour l'ObservableList
    }
	
    public Product getProduct(String code) throws ProductNotFoundException {
        Product product = productDAO.findByCode(code);
        if (product == null) {
            throw new ProductNotFoundException("Produit avec le code " + code + " non trouvé");
        }
        return product;
    }

    public void updateProduct(Product product) throws ProductNotFoundException {
        if (productDAO.findByCode(product.getCode()) == null) {
            throw new ProductNotFoundException("Produit introuvable pour mise à jour");
        }
        productDAO.update(product);
    }
    
 // ✅ Méthode de test pour vérifier la connexion
    public boolean testConnection() {
        try {
            List<Product> products = productDAO.getAll();
            System.out.println("✅ Service OK - " + products.size() + " produits trouvés");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Erreur Service: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Importe depuis API et sauvegarde
     */
    public Product importAndSave(String barcode, double price, int quantity) 
            throws Exception {
        
        // 1. Vérifier si produit existe déjà
        Product existing = productDAO.findByCode(barcode);
        if (existing != null) {
            throw new Exception("Produit existe déjà: " + existing.getName());
        }
        
        // 2. Importer depuis API
        Product imported = apiService.importProduct(barcode);
        
        // 3. Compléter avec données admin
        imported.setPrice(price);
        imported.setQuantity(quantity);
        
        // 4. Sauvegarder en DB
        productDAO.save(imported);
        
        System.out.println("✅ Produit importé et sauvegardé: " + imported.getName());
        return imported;
    }
    
    /**
     * Décrémente le stock après commande
     */
    public boolean decreaseStock(String productCode, int quantity) {
        Product product = productDAO.findByCode(productCode);
        if (product == null || product.getQuantity() < quantity) {
            return false;
        }
        
        product.setQuantity(product.getQuantity() - quantity);
        productDAO.update(product);
        return true;
    }
    
    /**
     * Vérifie disponibilité
     */
    public boolean isAvailable(String productCode, int quantity) {
        Product product = productDAO.findByCode(productCode);
        return product != null && product.getQuantity() >= quantity;
    }
}

   
}
