package com.monsite.inventaire.service;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.model.NutritionalValue;
import com.monsite.inventaire.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OpenFoodFactsService {
    
    private static final Gson gson = new Gson();
    
    // 🔥 NOUVEAU : Classe pour la réponse normalisée
    private static class NormalizedProduct {
        String code;
        String product_name;
        String brands;
        String categories;
        String origins;
        String nutriscore_grade;
        String ecoscore_grade;
        String image_url;
        String labels;
        JsonArray allergens_tags;
        JsonArray ingredients;
        JsonObject nutriments;
        
        // Méthode pour convertir en Product
        Product toProduct() {
            Product product = new Product();
            product.setCode(code != null ? code : "");
            product.setName(product_name != null ? product_name : "");
            product.setBrand(brands != null ? brands : "");
            product.setCategory(categories != null ? categories : "");
            product.setOriginsCountry(origins != null ? origins : "");
            product.setNutriScore(nutriscore_grade != null ? nutriscore_grade : "");
            product.setEcoScore(ecoscore_grade != null ? ecoscore_grade : "");
            product.setImageUrl(image_url != null ? image_url : "");
            product.setLabels(labels != null ? labels : "");
            
            // Vegan
            if (labels != null && labels.toLowerCase().contains("vegan")) {
                product.setVegan(true);
            }
            
            return product;
        }
    }
    
    /**
     * Recherche un produit (nom ou code-barres) avec normalisation
     */
    public List<Product> searchProducts(String searchTerm) throws Exception {
        List<NormalizedProduct> normalizedResults;
        
        if (isValidBarcode(searchTerm)) {
            System.out.println("🔎 Recherche par code-barres: " + searchTerm);
            normalizedResults = searchByBarcodeNormalized(searchTerm);
        } else {
            System.out.println("🔎 Recherche par nom: " + searchTerm);
            normalizedResults = searchByNameNormalized(searchTerm);
        }
        
        // 🔥 CONVERTIR TOUS LES RÉSULTATS NORMALISÉS EN PRODUCT
        return convertToProducts(normalizedResults);
    }
    
    /**
     * Recherche par code-barres avec normalisation
     */
    private List<NormalizedProduct> searchByBarcodeNormalized(String barcode) throws Exception {
        // API V2 avec champs normalisés
        String url = String.format(
            "https://world.openfoodfacts.org/api/v2/product/%s?" +
            "fields=code,product_name,brands,categories,origins," +
            "nutriscore_grade,ecoscore_grade,image_url,labels," +
            "allergens_tags,ingredients,nutriments",
            barcode
        );
        
        String jsonResponse = callAPI(url);
        JsonObject root = gson.fromJson(jsonResponse, JsonObject.class);
        
        List<NormalizedProduct> results = new ArrayList<>();
        
        if (root.get("status").getAsInt() == 1) {
            NormalizedProduct np = gson.fromJson(
                root.getAsJsonObject("product"), 
                NormalizedProduct.class
            );
            results.add(np);
        }
        
        return results;
    }
    
    /**
     * Recherche par nom avec normalisation
     */
    private List<NormalizedProduct> searchByNameNormalized(String productName) throws Exception {
        // API V1 avec normalisation des champs
        String url = String.format(
            "https://world.openfoodfacts.org/cgi/search.pl?" +
            "search_terms=%s&search_simple=1&json=1&page_size=10&" +
            "fields=code,product_name,brands,categories,origins," +
            "nutriscore_grade,ecoscore_grade,image_url,labels," +
            "allergens_tags,ingredients,nutriments",
            java.net.URLEncoder.encode(productName, "UTF-8")
        );
        
        String jsonResponse = callAPI(url);
        JsonObject root = gson.fromJson(jsonResponse, JsonObject.class);
        
        List<NormalizedProduct> results = new ArrayList<>();
        
        if (root.has("products")) {
            JsonArray productsArray = root.getAsJsonArray("products");
            
            for (int i = 0; i < productsArray.size() && i < 10; i++) {
                JsonObject productJson = productsArray.get(i).getAsJsonObject();
                
                // Vérifier les champs minimaux
                if (productJson.has("code") && productJson.has("product_name")) {
                    NormalizedProduct np = gson.fromJson(productJson, NormalizedProduct.class);
                    results.add(np);
                }
            }
        }
        
        return results;
    }
    
    /**
     * Convertit les résultats normalisés en objets Product
     */
    private List<Product> convertToProducts(List<NormalizedProduct> normalizedProducts) {
        List<Product> products = new ArrayList<>();
        
        for (NormalizedProduct np : normalizedProducts) {
            Product product = np.toProduct();
            
            // Extraire données additionnelles
            extractAdditionalData(product, np);
            
            // Définir valeurs par défaut (admin devra remplir)
            product.setQuantity(0);
            product.setPrice(0.0);
            
            products.add(product);
        }
        
        return products;
    }
    
    /**
     * Extrait les données additionnelles (allergènes, nutrition, ingrédients)
     */
    private void extractAdditionalData(Product product, NormalizedProduct np) {
        // Allergènes
        if (np.allergens_tags != null) {
            for (int i = 0; i < np.allergens_tags.size(); i++) {
                String tag = np.allergens_tags.get(i).getAsString();
                if (tag.startsWith("en:")) {
                    tag = tag.substring(3);
                }
                product.addAllergenTag(tag);
            }
        }
        
        // Valeurs nutritionnelles
        if (np.nutriments != null) {
            NutritionalValue nv = new NutritionalValue();
            nv.setEnergy(getSafeDouble(np.nutriments, "energy-kcal_100g"));
            nv.setSugars(getSafeDouble(np.nutriments, "sugars_100g"));
            nv.setFat(getSafeDouble(np.nutriments, "fat_100g"));
            nv.setProteins(getSafeDouble(np.nutriments, "proteins_100g"));
            nv.setSalt(getSafeDouble(np.nutriments, "salt_100g"));
            product.setNutritionalValue(nv);
        }
        
        // Ingrédients
        if (np.ingredients != null) {
            for (int i = 0; i < np.ingredients.size(); i++) {
                JsonObject ing = np.ingredients.get(i).getAsJsonObject();
                
                String text = getSafeString(ing, "text");
                boolean isVegan = "yes".equals(getSafeString(ing, "vegan"));
                
                // Détection allergènes simplifiée
                boolean isAllergen = containsAllergen(text);
                
                Ingredient ingredient = new Ingredient(text, isAllergen, isVegan);
                product.addIngredient(ingredient);
            }
        }
    }
    
    /**
     * Vérifie si c'est un code-barres valide
     */
    private boolean isValidBarcode(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        String cleaned = input.trim();
        return cleaned.matches("\\d{8,13}");
    }
    
    /**
     * Appel API générique
     */
    private String callAPI(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("User-Agent", "MonInventaireApp/1.0");
        
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Erreur API: HTTP " + responseCode);
        }
        
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        
        return response.toString();
    }
    
    private String getSafeString(JsonObject json, String key) {
        if (json != null && json.has(key) && !json.get(key).isJsonNull()) {
            return json.get(key).getAsString();
        }
        return "";
    }
    
    private double getSafeDouble(JsonObject json, String key) {
        if (json != null && json.has(key) && !json.get(key).isJsonNull()) {
            try {
                return json.get(key).getAsDouble();
            } catch (Exception e) {
                return 0.0;
            }
        }
        return 0.0;
    }
    
    private boolean containsAllergen(String text) {
        if (text == null) return false;
        String lowerText = text.toLowerCase();
        String[] commonAllergens = {"peanut", "milk", "gluten", "soy", "egg", "nuts", "arachide"};
        for (String allergen : commonAllergens) {
            if (lowerText.contains(allergen)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Test de normalisation
     */
//    public static void main(String[] args) {
//        try {
//            OpenFoodFactsService service = new OpenFoodFactsService();
//            
//            System.out.println("=== TEST NORMALISATION ===");
//            
//            // Test 1: Code-barres
//            System.out.println("\n1. Recherche par code-barres:");
//            List<Product> results1 = service.searchProducts("3017620422003");
//            System.out.println("Résultats: " + results1.size());
//            if (!results1.isEmpty()) {
//                Product p = results1.get(0);
//                System.out.println("Format: " + p.getClass().getSimpleName());
//                System.out.println("Champs: code=" + p.getCode() + ", name=" + p.getName());
//                System.out.println("Allergènes: " + p.getAllergenTags().size());
//                System.out.println("Nutrition: " + (p.getNutritionalValue() != null));
//            }
//            
//            // Test 2: Nom
//            System.out.println("\n2. Recherche par nom:");
//            List<Product> results2 = service.searchProducts("pasta");
//            System.out.println("Résultats: " + results2.size());
//            if (!results2.isEmpty()) {
//                for (int i = 0; i < Math.min(2, results2.size()); i++) {
//                    Product p = results2.get(i);
//                    System.out.println((i+1) + ". Même format? " + 
//                        (p.getCode() != null && p.getName() != null));
//                }
//            }
//            
//            // Vérifier que les deux retours sont identiques
//            System.out.println("\n✅ VÉRIFICATION NORMALISATION:");
//            boolean sameFormat = !results1.isEmpty() && !results2.isEmpty() &&
//                results1.get(0).getClass().equals(results2.get(0).getClass());
//            System.out.println("Même classe Product: " + sameFormat);
//            
//        } catch (Exception e) {
//            System.out.println("❌ Erreur: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}