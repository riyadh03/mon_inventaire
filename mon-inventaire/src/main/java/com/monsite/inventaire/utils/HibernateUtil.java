// HibernateUtil.java - Version avec meilleure gestion d'erreur
package com.monsite.inventaire.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import com.monsite.inventaire.model.*;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Configuration simplifiée
                Configuration configuration = new Configuration();
                
                // Connexion MySQL
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/inventaire_db");
                configuration.setProperty("hibernate.connection.username", "root");
                configuration.setProperty("hibernate.connection.password", "");
                
                // Paramètres Hibernate
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // ⚠️ CORRIGÉ
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.format_sql", "true");
                configuration.setProperty("hibernate.connection.autocommit", "true"); // ⚠️ IMPORTANT
                
                // Désactiver certaines vérifications pour éviter erreurs
                configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
                
                // Ajouter les entités
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(Ingredient.class);
                configuration.addAnnotatedClass(NutritionalValue.class);
                // Ajouter d'autres classes si besoin
                
                // Construire SessionFactory avec meilleure gestion d'erreur
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
                
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                
                System.out.println("✅ Hibernate SessionFactory créée avec succès");
                System.out.println("   Mode: hbm2ddl.auto=update");
                System.out.println("   Dialect: MySQLDialect");
                
            } catch (Exception e) {
                System.err.println("❌ ERREUR CRITIQUE Hibernate:");
                System.err.println("   Message: " + e.getMessage());
                
                if (e.getCause() != null) {
                    System.err.println("   Cause: " + e.getCause().getMessage());
                }
                
                // Afficher la stack trace complète pour debug
                e.printStackTrace();
                
                // Essayer une configuration alternative plus simple
                System.out.println("\n🔄 Tentative configuration alternative...");
                sessionFactory = createSimpleSessionFactory();
            }
        }
        return sessionFactory;
    }
    
    // Configuration alternative plus simple
    private static SessionFactory createSimpleSessionFactory() {
        try {
            Configuration config = new Configuration()
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/inventaire_db")
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "validate") // ⚠️ Juste valider, pas créer
                .setProperty("hibernate.show_sql", "true")
                .addAnnotatedClass(Product.class);
            
            return config.buildSessionFactory();
            
        } catch (Exception e) {
            System.err.println("❌ Échec configuration alternative");
            return null;
        }
    }
    
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory fermée");
        }
    }
    
    // Test de connexion simple
    public static boolean testConnection() {
        try (var session = getSessionFactory().openSession()) {
            System.out.println("✅ Test connexion Hibernate: OK");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Test connexion Hibernate échoué: " + e.getMessage());
            return false;
        }
    }
}