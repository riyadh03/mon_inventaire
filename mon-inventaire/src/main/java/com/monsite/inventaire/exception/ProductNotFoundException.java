// ProductNotFoundException.java
package com.monsite.inventaire.exception;

public class ProductNotFoundException extends Exception {
    private static final long serialVersionUID = 1L; // ⚠️ AJOUTÉ
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}