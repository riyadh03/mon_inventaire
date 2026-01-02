package com.monsite.inventaire.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Command {
    private int id;
    private User user;              // Utilisateur qui commande
    private LocalDate date;         // Date de la commande
    private double total;           // Total calculé
    private List<CommandLine> lines = new ArrayList<>(); // Lignes de commande
    
    // --- Constructors ---
    public Command() {
        this.date = LocalDate.now();
    }
    
    public Command(User user) {
        this.user = user;
        this.date = LocalDate.now();
    }
    
    public Command(int id, User user, LocalDate date, double total) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.total = total;
    }
    

	// --- Getters and Setters ---
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    S
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public List<CommandLine> getLines() {
        return lines;
    }
    
    public void setLines(List<CommandLine> lines) {
        this.lines = lines;
    }
    
    // --- Business Methods ---
    public void addLine(CommandLine line) {
        this.lines.add(line);
        calculateTotal();
    }
    
    public void removeLine(CommandLine line) {
        this.lines.remove(line);
        calculateTotal();
    }
    
    private void calculateTotal() {
        this.total = lines.stream()
            .mapToDouble(line -> line.getQuantity() * line.getUnitPrice())
            .sum();
    }
    
    public int getTotalItems() {
        return lines.stream()
            .mapToInt(CommandLine::getQuantity)
            .sum();
    }
    
    @Override
    public String toString() {
        return String.format("Commande #%d - %s - %.2f€ (%d articles)", 
            id, date, total, getTotalItems());
    }

}