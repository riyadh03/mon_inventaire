package com.monsite.inventaire.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    
    private int id;
    private String name;
    private List<Allergen> allergies;
    private boolean prefersVegan;

    // --- Constructors ---
    public User() {
        this.allergies = new ArrayList<>();
    }
    
    public User(int id, String name, boolean prefersVegan) {
        this.id = id;
        this.name = name;
        this.prefersVegan = prefersVegan;
        this.allergies = new ArrayList<>();
    }

    // --- Getters and Setters ---
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Allergen> getAllergies() {
        return allergies;
    }
    
    public void setAllergies(List<Allergen> allergies) {
        this.allergies = allergies;
    }
    
    // IMPORTANT: Pour JavaFX PropertyValueFactory
    public boolean isPrefersVegan() {
        return prefersVegan;
    }
    
    public void setPrefersVegan(boolean prefersVegan) {
        this.prefersVegan = prefersVegan;
    }

    public void addAllergy(Allergen allergen) {
        this.allergies.add(allergen);
    }
    
    public void removeAllergy(Allergen allergen) {
        this.allergies.remove(allergen);
    }
    
    @Override
    public String toString() {
        return String.format("User[%d] %s (Vegan: %s, Allergies: %d)", 
            id, name, prefersVegan, allergies.size());
    }
}