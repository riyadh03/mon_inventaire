// DuplicateProductException.java
package com.monsite.inventaire.exception;

public class DuplicateProductException extends Exception {
    private static final long serialVersionUID = 1L; // ⚠️ AJOUTÉ
    
    public DuplicateProductException(String message) {
        super(message);
    }
    
    public DuplicateProductException(String message, Throwable cause) {
        super(message, cause);
    }
}