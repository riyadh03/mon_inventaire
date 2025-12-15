package com.monsite.inventaire.model;

public class NutritionalValue {
    
    private double energy;
    private double sugars;
    private double fat;     // graisses
    private double proteins;
    private double salt;
    
    // --- Constructor ---
    public NutritionalValue(double energy, double sugars, double fat, double proteins, double salt) {
        this.energy = energy;
        this.sugars = sugars;
        this.fat = fat;
        this.proteins = proteins;
        this.salt = salt;
    }
    
    // --- Getters and Setters (Abbreviated) ---
    // ...
    public double getEnergy() {
        return energy;
    }
    
    // ... (autres getters et setters)
}