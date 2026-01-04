
package com.monsite.inventaire;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("=== DÉMARRAGE SYSTÈME D'INVENTAIRE ===");
        
        // Charger la vue principale
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        
        // Configurer la scène
        Scene scene = new Scene(root, 1200, 800);
        
        // Configurer la fenêtre
        primaryStage.setTitle("Système d'Inventaire - Open Food Facts");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        
        // Afficher
        primaryStage.show();
        
        System.out.println("✅ Application démarrée avec succès");
    }
    
    @Override
    public void stop() {
        System.out.println("=== ARRÊT APPLICATION ===");
        // Fermer Hibernate si besoin
        com.monsite.inventaire.utils.HibernateUtil.shutdown();
    }
    
    public static void main(String[] args) {
        System.out.println("=== INITIALISATION ===");
        launch(args);
    }
}