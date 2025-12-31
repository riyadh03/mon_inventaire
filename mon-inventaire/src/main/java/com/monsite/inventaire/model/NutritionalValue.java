package com.monsite.inventaire.model;

public class NutritionalValue {
    
    private double energy;
    private double sugars;
    private double fat;
    private double proteins;
    private double salt;
    
    // --- Constructors ---
    public NutritionalValue() {
        // Constructeur par défaut
    }
    
    public NutritionalValue(double energy, double sugars, double fat, 
                           double proteins, double salt) {
        this.energy = energy;
        this.sugars = sugars;
        this.fat = fat;
        this.proteins = proteins;
        this.salt = salt;
    }
    
    // --- Getters and Setters ---
    public double getEnergy() {
        return energy;
    }
    
    public void setEnergy(double energy) {
        this.energy = energy;
    }
    
    public double getSugars() {
        return sugars;
    }
    
    public void setSugars(double sugars) {
        this.sugars = sugars;
    }
    
    public double getFat() {
        return fat;
    }
    
    public void setFat(double fat) {
        this.fat = fat;
    }
    
    public double getProteins() {
        return proteins;
    }
    
    public void setProteins(double proteins) {
        this.proteins = proteins;
    }
    
    public double getSalt() {
        return salt;
    }
    
    public void setSalt(double salt) {
        this.salt = salt;
    }
    
    @Override
    public String toString() {
        return String.format("Énergie: %.1f, Sucres: %.1f, Lipides: %.1f, Protéines: %.1f, Sel: %.1f",
            energy, sugars, fat, proteins, salt);
    }
}