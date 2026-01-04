// DBUtil.java - VERSION AVEC COMMIT FORCÉ
package com.monsite.inventaire.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static Connection connection = null;
    
    private static final String URL = "jdbc:mysql://localhost:3306/inventaire_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Votre mot de passe
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // 1. Charger driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // 2. Établir connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                // 3. CRITIQUE: Forcer auto-commit et vérifier
                connection.setAutoCommit(true);
                
                // 4. Vérifier
                if (!connection.getAutoCommit()) {
                    System.err.println("⚠️ ATTENTION: Auto-commit est FALSE! Correction...");
                    connection.setAutoCommit(true);
                }
                
                System.out.println("✅ Connexion MySQL (auto-commit=true)");
            }
            return connection;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur connexion MySQL: " + e.getMessage());
            return null;
        }
    }
    
    // NOUVELLE MÉTHODE: Commit explicite
    public static void commit() {
        try {
            if (connection != null && !connection.isClosed() && !connection.getAutoCommit()) {
                connection.commit();
                System.out.println("💾 Commit explicite effectué");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur commit: " + e.getMessage());
        }
    }
    
    // Test de connexion
    public static void testConnectionAndCommit() {
        try (Connection testConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("=== TEST CONNEXION COMPLET ===");
            System.out.println("Auto-commit: " + testConn.getAutoCommit());
            System.out.println("Read-only: " + testConn.isReadOnly());
            System.out.println("Valid: " + testConn.isValid(2));
            
            // Tester un insert simple
            testConn.setAutoCommit(true); // S'assurer
            try (var stmt = testConn.createStatement()) {
                stmt.execute("INSERT INTO product (code, name) VALUES ('TEST_COMMIT', 'Test Commit')");
                System.out.println("✅ Insert test exécuté");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Test échoué: " + e.getMessage());
        }
    }
}