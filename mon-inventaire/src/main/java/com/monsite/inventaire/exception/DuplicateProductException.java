package com.monsite.inventaire.exception;

// Exception levée lorsqu'on tente d'ajouter un produit qui existe déjà
public class DuplicateProductException extends Exception {
    
    // Constructeur qui prend un message d'erreur
    public DuplicateProductException(String message) {
        super(message);
    }
}