package com.monsite.inventaire.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void showProductTable() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/ProductTableView.fxml"));
        contentPane.getChildren().setAll(view);
    }

    @FXML
    private void showProductForm() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/ProductFormView.fxml"));
        contentPane.getChildren().setAll(view);
    }

    @FXML
    private void showUserView() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/UserView.fxml"));
        contentPane.getChildren().setAll(view);
    }

    @FXML
    private void quitApp() {
        Platform.exit();
    }
}
