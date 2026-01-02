package com.monsite.inventaire.model;

public class CommandLine {
    private int id;
    private Command command;    // Commande parente
    private Product product;    // Produit commandé
    private int quantity;       // Quantité
    private double unitPrice;   // Prix unitaire au moment de la commande
    
    // --- Constructors ---
    public CommandLine() {}
    
    public CommandLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = 0.0; // À déterminer par logique métier
    }
    
    public CommandLine(int id, Command command, Product product, 
                      int quantity, double unitPrice) {
        this.id = id;
        this.command = command;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    

	// --- Getters and Setters ---
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Command getCommand() {
        return command;
    }
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Recalculer le prix total si besoin
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    // --- Calculated Properties ---
    public double getLineTotal() {
        return quantity * unitPrice;
    }
    
    // Pour les statistiques nutritionnelles
    public double getTotalCalories() {
        if (product != null && product.getNutritionalValue() != null) {
            return quantity * product.getNutritionalValue().getEnergy();
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("%s x%d = %.2f€", 
            product != null ? product.getName() : "Produit", 
            quantity, getLineTotal());
    }
}