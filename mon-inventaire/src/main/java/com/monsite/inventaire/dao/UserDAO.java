package com.monsite.inventaire.dao;

import com.monsite.inventaire.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final List<User> users = new ArrayList<>();

    // Récupérer tous les utilisateurs
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    // Chercher un utilisateur par ID
    public User findById(int id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    // Ajouter un utilisateur
    public void save(User user) {
        users.add(user);
    }

    // Mettre à jour un utilisateur
    public void update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return;
            }
        }
    }

    // Supprimer un utilisateur
    public void delete(int id) {
        users.removeIf(u -> u.getId() == id);
    }
}
