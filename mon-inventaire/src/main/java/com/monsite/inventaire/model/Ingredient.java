// Ingredient.java - Version JPA
package com.monsite.inventaire.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "ingredient")
public class Ingredient {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "is_allergen")
    private boolean isAllergen;
    
    @Column(name = "is_vegan")
    private boolean isVegan;
    
    @Column(name = "rank")
    private int rank;
    
    // ⚠️ CORRECTION: Pour MySQL
    @Column(name = "percent", columnDefinition = "DOUBLE")
    private double percent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code")
    private Product product;
    
    // Constructeurs
    public Ingredient() {}
    
    public Ingredient(String name, boolean isAllergen, boolean isVegan) {
        this.name = name;
        this.isAllergen = isAllergen;
        this.isVegan = isVegan;
    }
    
    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isAllergen() { return isAllergen; }
    public void setAllergen(boolean allergen) { isAllergen = allergen; }
    
    public boolean isVegan() { return isVegan; }
    public void setVegan(boolean vegan) { isVegan = vegan; }
    
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    
    public double getPercent() { return percent; }
    public void setPercent(double percent) { this.percent = percent; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    
    @Override
    public String toString() {
		return name + (isAllergen ? " (Allergène)" : "") + (isVegan ? " (Vegan)" : "");
	}
    
    
}