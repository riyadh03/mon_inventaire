package com.monsite.inventaire.test;

import com.monsite.inventaire.dao.ProductDAO;
import com.monsite.inventaire.utils.DBUtil;
import java.sql.Connection;
import java.sql.Statement;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("=== TEST CONNEXION MYSQL ===");
        
        // Test 1 : Connexion simple
        try {
            Connection conn = DBUtil.getConnection();
            System.out.println("✅ Connexion MySQL réussie !");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Erreur connexion DB: " + e.getMessage());
            return;
        }
        
        // Test 2 : Vérifier tables existent
        try {
            ProductDAO dao = new ProductDAO();
            int count = dao.getAll().size();
            System.out.println("✅ Table Product accessible - " + count + " produits");
        } catch (Exception e) {
            System.out.println("❌ Erreur ProductDAO: " + e.getMessage());
        }
    }
}