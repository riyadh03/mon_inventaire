package com.monsite.inventaire.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    
    // Champs de base (API + notre logique)
    private String code;
    private String name;
    private String brand;
    private String category;
    private int quantity;           // En unités (pas grammes)
    private String originsCountry;
    private String nutriScore;
    private String ecoScore;
    
    // Nouveaux champs pour l'API
    private double price;           // ❗ À saisir manuellement (API ne fournit pas)
    private String imageUrl;        // URL de l'image
    private String labels;          // "Vegan, Vegetarian, Gluten-Free"
    private boolean isVegan;        // Calculé à partir des labels/ingrédients
    private List<String> allergenTags = new ArrayList<>(); // ["peanuts", "sesame-seeds"]
    
    // Relations
    private NutritionalValue nutritionalValue;
    private List<Ingredient> ingredients = new ArrayList<>();

    // --- Constructors ---
    public Product() {}
    
    public Product(String code, String name, String brand) {
        this.code = code;
        this.name = name;
        this.brand = brand;
    }
    
    // Constructeur pour import API
    public Product(String code, String name, String brand, String category,
                   String originsCountry, String nutriScore, String ecoScore,
                   String imageUrl, String labels) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.originsCountry = originsCountry;
        this.nutriScore = nutriScore;
        this.ecoScore = ecoScore;
        this.imageUrl = imageUrl;
        this.labels = labels;
        this.isVegan = labels != null && labels.toLowerCase().contains("vegan");
        this.quantity = 0; // Par défaut
        this.price = 0.0;  // À définir manuellement
    }
    
    // --- Getters and Setters (ajouter les nouveaux) ---
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getOriginsCountry() { return originsCountry; }
    public void setOriginsCountry(String originsCountry) { this.originsCountry = originsCountry; }
    
    public String getNutriScore() { return nutriScore; }
    public void setNutriScore(String nutriScore) { this.nutriScore = nutriScore; }
    
    public String getEcoScore() { return ecoScore; }
    public void setEcoScore(String ecoScore) { this.ecoScore = ecoScore; }
    
    // NOUVEAUX GETTERS/SETTERS
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getLabels() { return labels; }
    public void setLabels(String labels) { 
        this.labels = labels;
        this.isVegan = labels != null && labels.toLowerCase().contains("vegan");
    }
    
    public boolean isVegan() { return isVegan; }
    public void setVegan(boolean vegan) { isVegan = vegan; }
    
    public List<String> getAllergenTags() { return allergenTags; }
    public void setAllergenTags(List<String> allergenTags) { this.allergenTags = allergenTags; }
    
    public void addAllergenTag(String allergen) {
        if (!allergenTags.contains(allergen)) {
            allergenTags.add(allergen);
        }
    }
    
    public boolean containsAllergen(String allergen) {
        return allergenTags.stream()
            .anyMatch(tag -> tag.toLowerCase().contains(allergen.toLowerCase()));
    }
    
    // Relations existantes
    public NutritionalValue getNutritionalValue() { return nutritionalValue; }
    public void setNutritionalValue(NutritionalValue nutritionalValue) { 
        this.nutritionalValue = nutritionalValue; 
    }
    
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    // --- Business Methods ---
    public boolean isSafeForUser(User user) {
        // 1. Vérifier allergies
        if (user != null && user.getAllergies() != null) {
            for (Allergen userAllergen : user.getAllergies()) {
                if (containsAllergen(userAllergen.getName())) {
                    return false;
                }
            }
        }
        
        // 2. Vérifier vegan
        if (user != null && user.isPrefersVegan() && !isVegan) {
            return false;
        }
        
        return true;
    }
    
    public double getTotalNutritionalValue(String type) {
        if (nutritionalValue == null) return 0.0;
        
        switch(type.toLowerCase()) {
            case "energy": return nutritionalValue.getEnergy() * quantity / 100.0;
            case "sugars": return nutritionalValue.getSugars() * quantity / 100.0;
            case "proteins": return nutritionalValue.getProteins() * quantity / 100.0;
            default: return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%.2f€, Nutri:%s, Vegan:%s)", 
            code, name, price, nutriScore, isVegan);
    }
}