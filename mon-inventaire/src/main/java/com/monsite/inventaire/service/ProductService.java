package com.monsite.inventaire.service;

import java.util.List;

import com.monsite.inventaire.dao.ProductDAO;
import com.monsite.inventaire.exception.DuplicateProductException;
import com.monsite.inventaire.exception.ProductNotFoundException;
import com.monsite.inventaire.model.Product;

/**
 * Couche Service: Contient la logique métier, agit comme un intermédiaire entre le Controller (UI) et le DAO (BD).
 */
public class ProductService {

    // Le Service DÉPEND du DAO (on l'injectera ou on le créera ici)
    private final ProductDAO productDAO;

    // Constructeur pour lier le service au DAO
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // CREATE
    public void addProduct(Product product) throws DuplicateProductException {
        // Logique métier: Vérifier l'existence AVANT d'ajouter
        if (productDAO.exists(product.getCode())) {
             // Traduction du message d'erreur
            throw new DuplicateProductException("Product already exists!"); 
        }
        productDAO.save(product);
    }

    // READ (one)
    public Product getProduct(String code) throws ProductNotFoundException {
        Product product = productDAO.findByCode(code);
        if (product == null) {
            // Traduction du message d'erreur
            throw new ProductNotFoundException("Product not found!"); 
        }
        return product;
    }

    // READ (all)
    public List<Product> getAllProducts() {
        // Aucune logique métier complexe ici, on délègue directement au DAO
        return productDAO.findAll();
    }

    // UPDATE
    public void updateProduct(Product product) throws ProductNotFoundException {
        if (!productDAO.exists(product.getCode())) {
            throw new ProductNotFoundException("Cannot update: Product not found!");
        }
        productDAO.update(product);
    }

    // DELETE
    public void deleteProduct(String code) throws ProductNotFoundException {
        if (!productDAO.exists(code)) {
            throw new ProductNotFoundException("Cannot delete: Product not found!");
        }
        productDAO.deleteByCode(code);
    }
    
    // NOTE: D'autres méthodes de filtre/statistiques (Streams) seront ajoutées ici.
}