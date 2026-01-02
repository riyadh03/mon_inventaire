package com.monsite.inventaire.controller.admin;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.model.Command;
import com.monsite.inventaire.service.ProductService;
import com.monsite.inventaire.service.CommandService;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardController {
    
    @FXML private Label totalProductsLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalCommandsLabel;
    @FXML private Label stockAlertLabel;
    
    @FXML private PieChart categoryChart;
    @FXML private BarChart<String, Number> salesChart;
    
    private ProductService productService;
    private CommandService commandService;
    
    @FXML
    public void initialize() {
        productService = new ProductService();
        commandService = new CommandService();
        
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        // Statistiques produits
        List<Product> products = productService.getAllProductsAsList();
        totalProductsLabel.setText(String.valueOf(products.size()));
        
        // Produits en faible stock
        long lowStockCount = products.stream()
            .filter(p -> p.getQuantity() < 10)
            .count();
        stockAlertLabel.setText(String.valueOf(lowStockCount));
        
        // Statistiques par catégorie (PieChart)
        Map<String, Long> categoryCount = products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.counting()
            ));
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        categoryCount.forEach((category, count) -> 
            pieChartData.add(new PieChart.Data(category + " (" + count + ")", count))
        );
        categoryChart.setData(pieChartData);
        
        // Statistiques commandes (à implémenter)
        // totalCommandsLabel.setText(...);
        // totalUsersLabel.setText(...);
    }
    
    @FXML
    private void handleRefresh() {
        loadDashboardData();
    }
    
    @FXML
    private void handleViewProducts() {
        // Navigation vers ProductManagement
    }
    
    @FXML
    private void handleViewUsers() {
        // Navigation vers UserManagement
    }
}