package com.monsite.inventaire.model;

import java.util.List;

public class Product {
    
    // Core fields from OpenFoodFacts API
    private String code;            // barcode
    private String name;            // product_name
    private String brand;           // brands
    private String category;        // categories
    private String quantity;
    private String originsCountry;  // origins
    private String nutriScore;      // nutriscore_grade
    private String ecoScore;        // ecoscore_grade
    
    // Relationships
    private NutritionalValue nutritionalValue; // 1 Product -> 1 NutritionalValue
    private List<Ingredient> ingredients;       // 1 Product -> * Ingredients

    // --- Constructor ---
    public Product(String code, String name, String brand) {
        this.code = code;
        this.name = name;
        this.brand = brand;
    }
    
    // --- Getters and Setters (Abbreviated for report clarity) ---
    // ...
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
    
    public String getCode() {
        return code;
    }
    // ... (autres getters et setters)
}