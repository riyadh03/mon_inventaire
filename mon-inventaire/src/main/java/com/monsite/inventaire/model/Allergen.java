package com.monsite.inventaire.model;

public class Allergen {
    
    private String name;
    
    // --- Constructors ---
    public Allergen() {
        // Constructeur par défaut (important)
    }
    
    public Allergen(String name) {
        this.name = name;
    }

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}