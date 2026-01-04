// TestAddVsUpdate.java
package com.monsite.inventaire.test;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;

public class TestAddVsUpdate {
    public static void main(String[] args) {
        ProductService service = new ProductService();
        
        // Test 1: Nouveau produit
        Product nouveau = new Product();
        nouveau.setCode("NEW_TEST");
        nouveau.setName("Nouveau Test");
        
        try {
            service.addProduct(nouveau);
            System.out.println("✅ addProduct() réussi pour nouveau produit");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        
        // Test 2: Même produit -> doit échouer
        try {
            service.addProduct(nouveau);
            System.out.println("❌ addProduct() aurait dû échouer (produit existe)");
        } catch (Exception e) {
            System.out.println("✅ addProduct() échoue correctement: " + e.getMessage());
        }
        
        // Test 3: Update produit existant
        nouveau.setPrice(99.99);
        try {
            service.updateProduct(nouveau);
            System.out.println("✅ updateProduct() réussi");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        
        // Test 4: Update produit inexistant -> doit échouer
        Product inexistant = new Product();
        inexistant.setCode("INEXISTANT");
        
        try {
            service.updateProduct(inexistant);
            System.out.println("❌ updateProduct() aurait dû échouer");
        } catch (Exception e) {
            System.out.println("✅ updateProduct() échoue correctement: " + e.getMessage());
        }
    }
}