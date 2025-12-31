package com.monsite.inventaire.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    
    private String code;
    private String name;
    private String brand;
    private String category;
    private int quantity;
    private String originsCountry;
    private String nutriScore;
    private String ecoScore;
    
    private NutritionalValue nutritionalValue;
    private List<Ingredient> ingredients = new ArrayList<>();

    // --- Constructors ---
    public Product() {
        // Constructeur par défaut
    }
    
    public Product(String code, String name, String brand) {
        this.code = code;
        this.name = name;
        this.brand = brand;
    }
    
    public Product(String code, String name, String brand, String category,
                   int quantity, String originsCountry, String nutriScore, String ecoScore) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.quantity = quantity;
        this.originsCountry = originsCountry;
        this.nutriScore = nutriScore;
        this.ecoScore = ecoScore;
    }
    
    // --- Getters and Setters ---
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getOriginsCountry() {
        return originsCountry;
    }
    
    public void setOriginsCountry(String originsCountry) {
        this.originsCountry = originsCountry;
    }
    
    public String getNutriScore() {
        return nutriScore;
    }
    
    public void setNutriScore(String nutriScore) {
        this.nutriScore = nutriScore;
    }
    
    public String getEcoScore() {
        return ecoScore;
    }
    
    public void setEcoScore(String ecoScore) {
        this.ecoScore = ecoScore;
    }
    
    public NutritionalValue getNutritionalValue() {
        return nutritionalValue;
    }
    
    public void setNutritionalValue(NutritionalValue nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Quantité: %d, NutriScore: %s)", 
            code, name, quantity, nutriScore);
    }
}