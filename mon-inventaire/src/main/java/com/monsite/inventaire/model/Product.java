// Product.java - Version avec annotations JPA
package com.monsite.inventaire.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product") // Nom de la table en DB
public class Product {
    
    @Id
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "brand", length = 255)
    private String brand;
    
    @Column(name = "category", length = 100)
    private String category;
    
    @Column(name = "quantity")
    private int quantity = 0;
    
    @Column(name = "price", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private double price = 0.0; 
    
    @Column(name = "origins_country", length = 100)
    private String originsCountry;
    
    @Column(name = "nutri_score", length = 1)
    private String nutriScore;
    
    @Column(name = "eco_score", length = 1)
    private String ecoScore;
    
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
    
    @Column(name = "labels", columnDefinition = "TEXT")
    private String labels;
    
    @Column(name = "vegan")
    private boolean vegan = false;
    
    // Allergen tags comme collection simple (stocké en JSON ou texte)
    @ElementCollection
    @CollectionTable(name = "product_allergen_tags", 
                     joinColumns = @JoinColumn(name = "product_code"))
    @Column(name = "allergen_tag")
    private List<String> allergenTags = new ArrayList<>();
    
    // Relation avec NutritionalValue (OneToOne)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "nutritional_value_id")
    private NutritionalValue nutritionalValue;
    
    // Relation avec Ingredient (OneToMany)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Ingredient> ingredients = new ArrayList<>();
    
    // Constructeurs
    public Product() {
        // Constructeur par défaut requis par JPA
    }
    
    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    // Getters et Setters (TOUS)
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        if (quantity >= 0) this.quantity = quantity; 
    }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { 
        if (price >= 0) this.price = price; 
    }
    
    public String getOriginsCountry() { return originsCountry; }
    public void setOriginsCountry(String originsCountry) { this.originsCountry = originsCountry; }
    
    public String getNutriScore() { return nutriScore; }
    public void setNutriScore(String nutriScore) { this.nutriScore = nutriScore; }
    
    public String getEcoScore() { return ecoScore; }
    public void setEcoScore(String ecoScore) { this.ecoScore = ecoScore; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getLabels() { return labels; }
    public void setLabels(String labels) { 
        this.labels = labels;
        if (labels != null) {
            this.vegan = labels.toLowerCase().contains("vegan");
        }
    }
    
    public boolean isVegan() { return vegan; }
    public void setVegan(boolean vegan) { this.vegan = vegan; }
    
    public List<String> getAllergenTags() { return allergenTags; }
    public void setAllergenTags(List<String> allergenTags) { 
        this.allergenTags = allergenTags != null ? allergenTags : new ArrayList<>();
    }
    
    public NutritionalValue getNutritionalValue() { return nutritionalValue; }
    public void setNutritionalValue(NutritionalValue nutritionalValue) { 
        this.nutritionalValue = nutritionalValue; 
    }
    
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { 
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }
    
    // Méthodes utilitaires
    public void addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            ingredient.setProduct(this);
            ingredients.add(ingredient);
        }
    }
    
    public void addAllergenTag(String tag) {
        if (tag != null && !allergenTags.contains(tag)) {
            allergenTags.add(tag);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Stock: %d, Prix: %.2f€)", code, name, quantity, price);
    }
}