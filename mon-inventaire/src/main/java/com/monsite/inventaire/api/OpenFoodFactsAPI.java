package com.monsite.inventaire.api;

import com.monsite.inventaire.model.Product;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Gère les requêtes HTTP vers l'API OpenFoodFacts et le parsing JSON.
 */
public class OpenFoodFactsAPI {
    
    private static final String API_BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    /**
     * Recherche un produit par son code-barres (EAN).
     * @param barcode Le code-barres à rechercher.
     * @return L'objet Product correspondant, ou null si non trouvé ou erreur.
     */
    public Product getProductByBarcode(String barcode) {
        String url = API_BASE_URL + barcode + ".json";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // 1. Vérification du statut HTTP
            if (response.statusCode() != 200) {
                System.err.println("Erreur API, statut: " + response.statusCode());
                return null;
            }
            
            // 2. Parsing JSON avec Gson
            String jsonResponse = response.body();
            // L'API retourne un objet enveloppant (wrapper) la vraie donnée Product. 
            // Nous devons donc créer une structure intermédiaire (ApiResponseWrapper) 
            // ou extraire directement l'objet 'product' du JSON.
            // Pour l'exemple, supposons que nous extrayons la partie 'product' du JSON brut.
            
            // NOTE: Une classe intermédiaire (Wrapper) est souvent plus propre, mais ici, 
            // nous simplifions l'extraction si possible ou créons une structure de réponse
            // si nécessaire, basée sur la structure JSON réelle de l'API.

            // Par simplicité, assumons ici que nous avons une classe interne/externe
            // qui correspond à la réponse complète de l'API:
            class ApiResponseWrapper {
                String status;
                String status_verbose;
                Product product; // C'est ici que Gson va injecter l'objet Product
            }

            ApiResponseWrapper wrapper = gson.fromJson(jsonResponse, ApiResponseWrapper.class);
            
            if (wrapper != null && wrapper.product != null) {
                return wrapper.product;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}