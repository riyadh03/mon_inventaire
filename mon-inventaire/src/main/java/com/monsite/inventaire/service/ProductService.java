// ProductService.java - Version COMPLÈTE
package com.monsite.inventaire.service;

import com.monsite.inventaire.dao.ProductDAO;
import com.monsite.inventaire.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductDAO productDAO;
    private final ObservableList<Product> observableProductList;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
        this.observableProductList = FXCollections.observableArrayList();
        
        System.out.println("✅ ProductService initialisé avec Hibernate");
        refreshFromDatabase();
    }
    
    public ObservableList<Product> getAllProducts() {
        return observableProductList;
    }
    
    public void refreshFromDatabase() {
        List<Product> products = productDAO.getAll();
        observableProductList.setAll(products);
        System.out.println("🔄 " + products.size() + " produits chargés");
    }
    
    // ⚠️ CORRECTION: Méthode principale (utilise Hibernate save)
    public void saveProduct(Product product) {
        System.out.println("💾 Service: Sauvegarde " + product.getName());
        productDAO.save(product);
        refreshFromDatabase();
    }
    
    // ⚠️ CORRECTION: Alias pour addProduct (pour compatibilité)
    public void addProduct(Product product) {
        System.out.println("➕ Ajout nouveau produit: " + product.getName());
        
        // Vérifier si existe déjà
        Product existing = getProductByCode(product.getCode());
        if (existing != null) {
            throw new RuntimeException("Produit existe déjà: " + product.getCode());
        }
        
        productDAO.save(product); // Hibernate fera INSERT
        refreshFromDatabase();
    }
    
    public void updateProduct(Product product) {
        System.out.println("✏️ Mise à jour produit: " + product.getName());
        
        // Vérifier si existe
        Product existing = getProductByCode(product.getCode());
        if (existing == null) {
            throw new RuntimeException("Produit non trouvé: " + product.getCode());
        }
        
        productDAO.save(product); // Hibernate fera UPDATE (car existe)
        refreshFromDatabase();
    }
    
    public void deleteProduct(String code) {
        System.out.println("🗑️ Service: Suppression " + code);
        productDAO.delete(code);
        refreshFromDatabase();
    }
    
    public Product getProductByCode(String code) {
        return productDAO.findByCode(code);
    }
    
    // Méthode pour obtenir l'énergie/calories
    public double getProductEnergy(Product product) {
        if (product.getNutritionalValue() != null) {
            return product.getNutritionalValue().getEnergy(); // ou getCalories()
        }
        return 0.0;
    }
    
    // Autres utilitaires
    public int getProductCount() {
        return observableProductList.size();
    }
    
    public double getInventoryValue() {
        return observableProductList.stream()
            .mapToDouble(p -> p.getPrice() * p.getQuantity())
            .sum();
    }
    
    public List<Product> getLowStockProducts() { 
    	return observableProductList
    			.stream() 
    			.filter(p -> p.getQuantity() < 10) 
    			.collect(Collectors.toList()); 
    	}
}