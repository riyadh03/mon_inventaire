package com.monsite.inventaire.dao;

import com.monsite.inventaire.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private final List<Product> products = new ArrayList<>();

    // Récupérer tous les produits
    public List<Product> getAll() {
        return new ArrayList<>(products);
    }

    // Chercher un produit par code-barres
    public Product findByCode(String code) {
        for (Product p : products) {
            if (p.getCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    // Ajouter un produit
    public void save(Product product) {
        products.add(product);
    }

    // Mettre à jour un produit
    public void update(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCode().equals(product.getCode())) {
                products.set(i, product);
                return;
            }
        }
    }

    // Supprimer un produit
    public void delete(String code) {
        products.removeIf(p -> p.getCode().equals(code));
    }
}
