package com.monsite.inventaire.model;

public class Ingredient {
    
    private String name;
    private boolean isAllergen; // allergene
    private boolean isVegan;    // vegan
    
    // --- Constructor ---
    public Ingredient(String name, boolean isAllergen, boolean isVegan) {
        this.name = name;
        this.isAllergen = isAllergen;
        this.isVegan = isVegan;
    }

    // --- Getters and Setters (Abbreviated) ---
    // ...
    public boolean isAllergen() {
        return isAllergen;
    }
    
    public boolean isVegan() {
        return isVegan;
    }
    // ...
}