// QuickTest.java - Pour vérifier les méthodes
package com.monsite.inventaire.test;

import java.util.List;

import com.monsite.inventaire.model.NutritionalValue;
import com.monsite.inventaire.model.Product;

public class QuickTest {
    public static void main(String[] args) {
        // Test 1: Product methods
        Product p = new Product();
        p.setCode("TEST");
        p.setName("Test");
        
        // Test setters/getters
        p.setAllergenTags(List.of("test1", "test2"));
        System.out.println("Allergènes: " + p.getAllergenTags());
        
        // Test 2: NutritionalValue
        NutritionalValue nv = new NutritionalValue();
        nv.setEnergy(100.0); // ou setCalories()
        p.setNutritionalValue(nv);
        
        // Test getter
        if (p.getNutritionalValue() != null) {
            double energy = p.getNutritionalValue().getEnergy(); // ou getCalories()
            System.out.println("Énergie: " + energy);
        }
        
        System.out.println("✅ Tests de méthodes OK");
    }
}