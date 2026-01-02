package com.monsite.inventaire.test;
import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.service.OpenFoodFactsService;
import java.util.List;

public class TestOpenFoodFactsService {
    public static void main(String[] args) {
        try {
            OpenFoodFactsService service = new OpenFoodFactsService();
            
            // Test mixte
            String[] tests = {"3017620422003", "pasta", "chocolat", "1234567890123"};
            
            for (String test : tests) {
                System.out.println("\n=== TEST: '" + test + "' ===");
                List<Product> results = service.searchProducts(test);
                System.out.println("Résultats: " + results.size());
                
                if (!results.isEmpty()) {
                    Product first = results.get(0);
                    System.out.println("Premier: " + first.getName());
                    System.out.println("Code: " + first.getCode());
                    System.out.println("Marque: " + first.getBrand());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}