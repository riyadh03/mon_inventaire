

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("=== DÉMARRAGE APPLICATION ===");
        
        // 1. Charger la vue principale
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        
        // 2. Créer la scène
        Scene scene = new Scene(root, 900, 600);
        
        // 3. Ajouter le CSS
        scene.getStylesheets().add(getClass().getResource("/fxml/styles.css").toExternalForm());
        
        // 4. Configurer la fenêtre
        primaryStage.setTitle("Gestion Inventaire Alimentaire - Admin");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // 5. Afficher
        primaryStage.show();
        
        System.out.println("✅ Application démarrée avec succès");
    }
    
    public static void main(String[] args) {
        // Log de démarrage
        System.out.println("=== LANCEMENT APPLICATION JAVA ===");
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JavaFX version: " + System.getProperty("javafx.version"));
        
        // Lancer l'application JavaFX
        launch(args);
    }
}