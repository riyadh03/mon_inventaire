// NutritionalValue.java - Version JPA
//NutritionalValue.java - Version JPA

package com.monsite.inventaire.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "nutritional_value")
public class NutritionalValue {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 // ⚠️ CORRECTION: Utiliser columnDefinition pour MySQL
 @Column(name = "energy_kcal", columnDefinition = "DOUBLE")
 private double energy;
 
 @Column(name = "sugars_g", columnDefinition = "DOUBLE")
 private double sugars;
 
 @Column(name = "fat_g", columnDefinition = "DOUBLE")
 private double fat;
 
 @Column(name = "proteins_g", columnDefinition = "DOUBLE")
 private double proteins;
 
 @Column(name = "salt_g", columnDefinition = "DOUBLE")
 private double salt;
 
 @OneToOne(mappedBy = "nutritionalValue")
 private Product product;
 
 public double getEnergy() { return energy; }
 public void setEnergy(double energy) { this.energy = energy; }
 
 // OU
 public double getCalories() { return energy; }
 public void setCalories(double calories) { this.energy = calories; }
 
 public double getSugars() { return sugars; }
 public void setSugars(double sugars) { this.sugars = sugars; }
 
 public double getFat() { return fat; }
 public void setFat(double fat) { this.fat = fat; }
 
 public double getProteins() { return proteins; }
 public void setProteins(double proteins) { this.proteins = proteins; }
 
 public double getSalt() { return salt; }
 public void setSalt(double salt) { this.salt = salt; }
 
 
 // ... reste identique
}