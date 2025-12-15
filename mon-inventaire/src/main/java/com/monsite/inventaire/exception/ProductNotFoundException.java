package com.monsite.inventaire.exception;

// Exception levée lorsqu'un produit recherché n'est pas trouvé
public class ProductNotFoundException extends Exception {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
}