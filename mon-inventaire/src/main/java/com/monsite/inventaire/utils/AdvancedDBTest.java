package com.monsite.inventaire.utils;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.ProductService;
import com.monsite.inventaire.dao.ProductDAO;

public class AdvancedDBTest {
    public static void main(String[] args) {
        System.out.println("=== TEST AVANCÉ DB + SERVICE ===");
        
        // Test 1 : DAO direct
        System.out.println("\n1. Test ProductDAO direct:");
        ProductDAO dao = new ProductDAO();
        System.out.println("Produits en DB (DAO): " + dao.getAll().size());
        
        // Test 2 : Service
        System.out.println("\n2. Test ProductService:");
        ProductService service = new ProductService();
        System.out.println("Produits en DB (Service): " + service.getAllProducts().size());
        
        // Test 3 : Ajout produit
        System.out.println("\n3. Ajout nouveau produit:");
        Product newProduct = new Product(
            "PROD-" + System.currentTimeMillis(),  // Code unique
            "Yaourt Nature",
            "Danone",
            "Produits Laitiers",
            15,           // quantity en int
            "France",
            "A",
            "B"
        );
        
        try {
            service.addProduct(newProduct);
            System.out.println("✅ Produit ajouté: " + newProduct.getCode());
        } catch (Exception e) {
            System.out.println("❌ Erreur ajout: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test 4 : Vérifier ajout
        System.out.println("\n4. Vérification après ajout:");
        int newCount = service.getAllProducts().size();
        System.out.println("Nouveau total: " + newCount + " produits");
        
        // Test 5 : Recherche produit spécifique
        System.out.println("\n5. Recherche produit:");
        try {
            Product found = service.getProduct(newProduct.getCode());
            System.out.println("✅ Produit trouvé: " + found.getName());
            System.out.println("   Quantité: " + found.getQuantity());
            System.out.println("   NutriScore: " + found.getNutriScore());
        } catch (Exception e) {
            System.out.println("❌ Produit non trouvé");
        }
        
        // Test 6 : Vérifier dans MySQL directement
        System.out.println("\n6. Instruction SQL pour vérifier:");
        System.out.println("   Ouvrez MySQL et exécutez:");
        System.out.println("   USE inventaire_db;");
        System.out.println("   SELECT code, name, quantity FROM Product;");
    }
}