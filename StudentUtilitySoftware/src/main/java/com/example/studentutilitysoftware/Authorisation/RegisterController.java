package com.example.studentutilitysoftware.Authorisation;

import com.example.studentutilitysoftware.DataBase.DatabaseConnection;
import com.example.studentutilitysoftware.Home;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {
    @FXML
    private Button Registerbtn;

    @FXML
    private TextField FullUserNametf;

    @FXML
    private PasswordField Passwordpf;

    @FXML
    private TextField UserNametf;

    @FXML
    protected void RegisterUser() {
        String fullName = FullUserNametf.getText().trim();
        String userName = UserNametf.getText().trim();
        String password = Passwordpf.getText().trim();

        if (fullName.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO users (full_name, username, password) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "User registered successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to register user. Please try again.");
            }

            preparedStatement.close();
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1105, 700);
            Stage stage = (Stage) Registerbtn.getScene().getWindow();
            stage.setTitle("REGISTER FORM!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Database connection failed: " + e.getMessage());
        }
    }

    private void clearFields() {
        FullUserNametf.clear();
        UserNametf.clear();
        Passwordpf.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
