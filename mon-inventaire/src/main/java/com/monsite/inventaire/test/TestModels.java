package com.monsite.inventaire.test;

import com.monsite.inventaire.model.User;
import com.monsite.inventaire.model.Allergen;
import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.model.Command;
import com.monsite.inventaire.model.CommandLine;


public class TestModels {
    public static void main(String[] args) {
        System.out.println("=== TEST NOUVEAUX MODÈLES ===");
        
        // Créer un utilisateur
        User user = new User(1, "Alice", true);
        user.addAllergy(new Allergen("Arachide"));
        
        // Créer des produits
        Product produit1 = new Product("P001", "Lait végétal", "Alpro", 
            "Boisson", 10, "France", "A", "B");
        
        Product produit2 = new Product("P002", "Biscuits", "BN", 
            "Biscuits", 5, "France", "C", "D");
        
        // Créer une commande
        Command commande = new Command(user);
        
        // Ajouter des lignes
        CommandLine ligne1 = new CommandLine(produit1, 2);
        ligne1.setUnitPrice(2.50);
        
        CommandLine ligne2 = new CommandLine(produit2, 3);
        ligne2.setUnitPrice(1.80);
        
        commande.addLine(ligne1);
        commande.addLine(ligne2);
        
        // Afficher
        System.out.println("Utilisateur: " + user.getName());
        System.out.println("Vegan: " + user.isPrefersVegan());
        System.out.println("Allergies: " + user.getAllergies().size());
        
        System.out.println("\nCommande créée:");
        System.out.println("Total: " + commande.getTotal() + "€");
        System.out.println("Articles: " + commande.getTotalItems());
        
        for (CommandLine line : commande.getLines()) {
            System.out.println("  - " + line);
        }
        
        System.out.println("\n✅ Modèles Command/CommandLine fonctionnels !");
    }
}