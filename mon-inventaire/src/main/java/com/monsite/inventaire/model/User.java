package com.monsite.inventaire.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    
    private int id;
    private String name;
    private List<Allergen> allergies;   // Utilisateur a -> List<Allergene>
    private boolean prefersVegan;       // prefereVegan

    // --- Constructor ---
    public User(int id, String name, boolean prefersVegan) {
        this.id = id;
        this.name = name;
        this.prefersVegan = prefersVegan;
        this.allergies = new ArrayList<>(); // Initialisation de la liste
    }

    // --- Getters and Setters (Abbreviated) ---
    // ...
    public List<Allergen> getAllergies() {
        return allergies;
    }
    
    public boolean prefersVegan() {
        return prefersVegan;
    }

    public void addAllergy(Allergen allergen) {
        this.allergies.add(allergen);
    }
    // ...
}