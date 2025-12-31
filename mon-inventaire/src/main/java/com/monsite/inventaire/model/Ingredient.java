package com.monsite.inventaire.model;

public class Ingredient {
    
    private String name;
    private boolean isAllergen;
    private boolean isVegan;
    
    // --- Constructors ---
    public Ingredient() {
        // Constructeur par défaut
    }
    
    public Ingredient(String name, boolean isAllergen, boolean isVegan) {
        this.name = name;
        this.isAllergen = isAllergen;
        this.isVegan = isVegan;
    }

    // --- Getters and Setters ---
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isAllergen() {
        return isAllergen;
    }
    
    public void setAllergen(boolean isAllergen) {
        this.isAllergen = isAllergen;
    }
    
    public boolean isVegan() {
        return isVegan;
    }
    
    public void setVegan(boolean isVegan) {
        this.isVegan = isVegan;
    }
    
    @Override
    public String toString() {
        return name + " [Allergène: " + isAllergen + ", Vegan: " + isVegan + "]";
    }
}