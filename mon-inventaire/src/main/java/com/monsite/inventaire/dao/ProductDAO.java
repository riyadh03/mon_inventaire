// ProductDAO_Hibernate.java - Version avec Hibernate
package com.monsite.inventaire.dao;

import com.monsite.inventaire.model.Product;
import com.monsite.inventaire.utils.HibernateUtil;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDAO {
    
    public ProductDAO() {
        System.out.println("✅ ProductDAO Hibernate initialisé");
    }
    
    // Récupérer tous les produits
    public List<Product> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            List<Product> products = query.list();
            System.out.println("📥 " + products.size() + " produits chargés via Hibernate");
            return products;
        } catch (Exception e) {
            System.err.println("❌ Erreur getAll: " + e.getMessage());
            return List.of();
        }
    }
    
    // Chercher par code
    public Product findByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product product = session.get(Product.class, code);
            
            if (product != null) {
                // ⚠️ FORCER le chargement des collections LAZY
                Hibernate.initialize(product.getAllergenTags());
                Hibernate.initialize(product.getIngredients());
                
                // Si nutritionalValue est LAZY aussi
                if (product.getNutritionalValue() != null) {
                    Hibernate.initialize(product.getNutritionalValue());
                }
            }
            
            return product;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur findByCode: " + e.getMessage());
            return null;
        }
    }
    
 // Méthode intelligente: INSERT si nouveau, UPDATE si existe
    public void save(Product product) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Vérifier explicitement
            boolean exists = session.get(Product.class, product.getCode()) != null;
            
            if (!exists) {
                session.persist(product);
                System.out.println("🆕 INSERT: " + product.getCode());
            } else {
                session.merge(product);
                System.out.println("✏️ UPDATE: " + product.getCode());
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erreur sauvegarde: " + e.getMessage(), e);
        }
    }
    
 // Version alternative: Toujours utiliser merge (plus simple)
    public void saveOrUpdate(Product product) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // merge fait: INSERT si nouveau, UPDATE si existe
            session.merge(product);
            
            transaction.commit();
            System.out.println("💾 saveOrUpdate (merge): " + product.getCode());
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Erreur saveOrUpdate: " + e.getMessage());
        }
    }
    
    // Pour INSERT explicite
    public void insert(Product product) {
        if (exists(product.getCode())) {
            throw new RuntimeException("Produit existe déjà: " + product.getCode());
        }
        save(product); // Appelle save qui fera INSERT
    }
    
 // Pour UPDATE explicite
    public void update(Product product) {
        if (!exists(product.getCode())) {
            throw new RuntimeException("Impossible de mettre à jour, produit non trouvé: " + product.getCode());
        }
        save(product); // Appelle save qui fera UPDATE
    }
    // Vérifier existence
    public boolean exists(String code) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Product product = session.get(Product.class, code);
			return product != null;
		} catch (Exception e) {
			System.err.println("❌ Erreur exists: " + e.getMessage());
			return false;
		}
	}
    
    // Supprimer
    public void delete(String code) {
        Transaction transaction = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Product product = session.get(Product.class, code);
            if (product != null) {
            	session.remove(product);
                System.out.println("🗑️ Produit supprimé: " + code);
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Erreur delete: " + e.getMessage());
        }
    }
    
    // Rechercher par nom
    public List<Product> searchByName(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery(
                "FROM Product p WHERE LOWER(p.name) LIKE LOWER(:keyword) OR LOWER(p.brand) LIKE LOWER(:keyword)", 
                Product.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } catch (Exception e) {
            System.err.println("❌ Erreur searchByName: " + e.getMessage());
            return List.of();
        }
    }
    
    // Tester la connexion et migration
    public void testConnection() {
        System.out.println("=== TEST HIBERNATE ===");
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Hibernate va automatiquement:
            // 1. Se connecter à MySQL
            // 2. Vérifier la structure des tables
            // 3. Créer/modifier les colonnes si besoin (hbm2ddl.auto=update)
            
            Long count = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                               .uniqueResult();
            
            System.out.println("✅ Hibernate fonctionnel");
            System.out.println("📊 Produits en DB: " + (count != null ? count : 0));
            
        } catch (Exception e) {
            System.err.println("❌ Erreur test Hibernate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}