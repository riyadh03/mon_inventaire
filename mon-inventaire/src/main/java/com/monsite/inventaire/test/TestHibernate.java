// TestHibernate.java
package com.monsite.inventaire.test;

import java.util.List;

import com.monsite.inventaire.dao.ProductDAO;
import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.utils.HibernateUtil;

public class TestHibernate {
    public static void main(String[] args) {
        System.out.println("=== TEST HIBERNATE COMPLET ===");
        
        ProductDAO dao = new ProductDAO();
        
        // 1. Tester connexion et migration
        System.out.println("\n1. Test connexion et migration auto...");
        dao.testConnection();
        
        // 2. Créer un produit
        System.out.println("\n2. Création produit avec Hibernate...");
        Product p = new Product();
        p.setCode("HIBERNATE_TEST");
        p.setName("Test Hibernate");
        p.setBrand("Hibernate ORM");
        p.setCategory("Test");
        p.setQuantity(777);
        p.setPrice(88.88);
        p.setOriginsCountry("France");
        p.setNutriScore("A");
        p.setEcoScore("B");
        p.setLabels("Bio,Vegan,Local");
        p.setVegan(true);
        p.addAllergenTag("test");
        p.addAllergenTag("demo");
        
        // 3. Sauvegarder (Hibernate fera CREATE ou UPDATE automatiquement)
        System.out.println("\n3. Sauvegarde via Hibernate...");
        dao.save(p);
        
        // 4. Rechercher
        System.out.println("\n4. Recherche...");
        Product found = dao.findByCode("HIBERNATE_TEST");
        if (found != null) {
            System.out.println("✅ TROUVÉ: " + found.getName());
            System.out.println("   NutriScore: " + found.getNutriScore());
            System.out.println("   Vegan: " + found.isVegan());
            System.out.println("   Allergènes: " + found.getAllergenTags());
        }
        
        // 5. Lister tous
        System.out.println("\n5. Liste complète:");
        List<Product> all = dao.getAll();
        System.out.println("Total produits: " + all.size());
        
        // 6. Vérifier phpMyAdmin
        System.out.println("\n6. ⚠️ VÉRIFIEZ PHPMYADIN:");
        System.out.println("   - La table 'product' a-t-elle été créée/modifiée?");
        System.out.println("   - Toutes les colonnes sont-elles présentes?");
        System.out.println("   - Le produit 'HIBERNATE_TEST' est-il visible?");
        
        // 7. Fermer Hibernate


HibernateUtil.shutdown();
        
        System.out.println("\n=== FIN TEST ===");
    }
}