package com.monsite.inventaire.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestJavaFXMinimal extends Application {
    public void start(Stage stage) {
        System.out.println("=== TEST JAVAFX MINIMAL ===");
        
        // Juste un Label simple
        Label label = new Label("✅ JavaFX fonctionne !");
        Scene scene = new Scene(label, 300, 200);
        
        stage.setScene(scene);
        stage.setTitle("Test JavaFX");
        stage.show();
        
        System.out.println("JavaFX initialisé avec succès");
    }
    
    public static void main(String[] args) {
        System.out.println("Lancement test minimal...");
        launch(args);
    }
}