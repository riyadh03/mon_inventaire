package com.monsite.inventaire.service;
import java.util.List;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.model.User;

public class RecommendationService {
    
    public List<Product> getProductsForUser(User user) {
        List<Product> allProducts = productService.getAllProducts();
        
        return allProducts.stream()
            .filter(p -> isSafeForUser(p, user))
            .collect(Collectors.toList());
    }
    
    private boolean isSafeForUser(Product product, User user) {
        // 1. Vérifier allergies
        if (user.hasAllergies() && product.containsAllergens(user.getAllergies())) {
            return false;
        }
        
        // 2. Vérifier préférence vegan
        if (user.isPrefersVegan() && !product.isVegan()) {
            return false;
        }
        
        return true;
    }
}
