package com.monsite.inventaire.controller;

import com.monsite.inventaire.model.User;
import com.monsite.inventaire.model.Allergen;
import com.monsite.inventaire.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, Boolean> veganColumn;

    private UserService userService;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        userService = new UserService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        veganColumn.setCellValueFactory(new PropertyValueFactory<>("prefersVegan"));

        loadUsers();
    }

    private void loadUsers() {
        userList = FXCollections.observableArrayList(userService.getAllUsers());
        userTable.setItems(userList);
    }

    @FXML
    private void handleAddUser() {
        try {
            // Exemple : ajouter un utilisateur par défaut
            User newUser = new User(userService.generateId(), "Nouvel utilisateur", false);
            userService.addUser(newUser);
            loadUsers();
            showAlert("Succès", "Utilisateur ajouté avec succès", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Exemple : on inverse la préférence vegan
            selectedUser.setPrefersVegan(!selectedUser.isPrefersVegan());
            userService.updateUser(selectedUser);
            loadUsers();
            showAlert("Succès", "Utilisateur mis à jour", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.deleteUser(selectedUser.getId());
            loadUsers();
            showAlert("Succès", "Utilisateur supprimé", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
