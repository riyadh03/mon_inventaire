package com.monsite.inventaire.service;

import com.monsite.inventaire.model.User;
import com.monsite.inventaire.model.Allergen;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return;
            }
        }
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
    }

    public int generateId() {
        return users.size() + 1; // simple génération d'ID incrémental
    }

    public void addAllergyToUser(int userId, Allergen allergen) {
        for (User user : users) {
            if (user.getId() == userId) {
                user.getAllergies().add(allergen);
                return;
            }
        }
    }
}
