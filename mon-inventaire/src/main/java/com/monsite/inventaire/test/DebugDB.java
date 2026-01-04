// DebugDB.java - TEST DIRECT SQL
package com.monsite.inventaire.test;

import java.sql.*;

public class DebugDB {
    static final String URL = "jdbc:mysql://localhost:3306/inventaire_db";
    static final String USER = "root";
    static final String PASSWORD = "";
    
    public static void main(String[] args) {
        System.out.println("=== DEBUG COMPLET BASE DE DONNÉES ===");
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            
            // 1. Vérifier auto-commit
            boolean autoCommit = conn.getAutoCommit();
            System.out.println("1. Auto-commit: " + autoCommit);
            
            // 2. Lister toutes les tables
            System.out.println("\n2. Tables dans la base:");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println("   - " + tables.getString("TABLE_NAME"));
            }
            
            // 3. Voir structure table products
            System.out.println("\n3. Structure table 'products':");
            ResultSet columns = meta.getColumns(null, null, "products", "%");
            while (columns.next()) {
                System.out.printf("   %-20s %-15s\n", 
                    columns.getString("COLUMN_NAME"),
                    columns.getString("TYPE_NAME"));
            }
            
            // 4. Compter produits AVANT test
            System.out.println("\n4. Nombre de produits AVANT test:");
            String countSQL = "SELECT COUNT(*) as count FROM product";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    System.out.println("   COUNT: " + rs.getInt("count"));
                }
            }
            
            // 5. Insérer un produit DIRECTEMENT
            System.out.println("\n5. Insertion directe SQL:");
            String insertSQL = """
                INSERT INTO products (code, name, brand, quantity, price) 
                VALUES ('TEST123', 'Test Direct', 'Test Brand', 100, 9.99)
                """;
            
            try (Statement stmt = conn.createStatement()) {
                int rows = stmt.executeUpdate(insertSQL);
                System.out.println("   Rows inserted: " + rows);
            }
            
            // 6. Vérifier APRÈS insertion
            System.out.println("\n6. Nombre de produits APRÈS insertion directe:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    System.out.println("   COUNT: " + rs.getInt("count"));
                }
            }
            
            // 7. Lister tous les produits
            System.out.println("\n7. Liste complète des produits:");
            String selectSQL = "SELECT code, name, quantity, price FROM products";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {
                System.out.println("   CODE\t\tNAME\t\tQTY\tPRICE");
                System.out.println("   ----\t\t----\t\t---\t-----");
                while (rs.next()) {
                    System.out.printf("   %s\t%s\t%d\t%.2f\n",
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"));
                }
            }
            
            // 8. Rollback ou commit selon auto-commit
            if (!autoCommit) {
                System.out.println("\n⚠️ Auto-commit désactivé! Faire commit/rollback");
                conn.commit(); // ou conn.rollback();
            }
            
        } catch (SQLException e) {
            System.err.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DEBUG ===");
    }
}