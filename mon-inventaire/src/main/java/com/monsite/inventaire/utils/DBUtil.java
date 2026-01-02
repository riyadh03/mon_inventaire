package com.monsite.inventaire.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // URL de connexion JDBC
    private static final String URL = "jdbc:mysql://localhost:3306/inventaire_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";     // utilisateur MySQL
    private static final String PASSWORD = "";     // mot de passe (vide dans ton cas)

    // Méthode pour obtenir une connexion
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
