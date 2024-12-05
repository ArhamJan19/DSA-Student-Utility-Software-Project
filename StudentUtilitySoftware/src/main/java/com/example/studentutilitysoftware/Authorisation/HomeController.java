package com.example.studentutilitysoftware.Authorisation;


import com.example.studentutilitysoftware.DataBase.DatabaseConnection;
import com.example.studentutilitysoftware.Home;
import com.example.studentutilitysoftware.RemainingControllers.SideBarController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HomeController {

    @FXML
    private Button Loginbtn;
    @FXML
    private Hyperlink RegisterLink;
    @FXML
    private TextField Usernametf;
    @FXML
    private TextField Passwordpf;

    @FXML
    protected void login() {
        String username = Usernametf.getText().trim();
        String password = Passwordpf.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and Password must not be empty!");
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                showAlert("Success", "Login successful!");
                navigateToDashboard(username);
            } else {
                showAlert("Error", "Invalid username or password!");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            showAlert("Error", "Database connection failed: " + e.getMessage());
        }
    }

    @FXML
    protected void GotoRegisterPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1105, 700);
        Stage stage = (Stage) RegisterLink.getScene().getWindow();
        stage.setTitle("REGISTER FORM!");
        stage.setScene(scene);
        stage.show();
    }

    private void navigateToDashboard(String Username) throws IOException {
        BorderPane borderPane = new BorderPane();
        FXMLLoader sidebarLoader = new FXMLLoader(Home.class.getResource("DashboardSideBar.fxml"));
        borderPane.setLeft(sidebarLoader.load());

        SideBarController sidebarController = sidebarLoader.getController();
        sidebarController.setLayout(borderPane);
        sidebarController.setUser(Username);
        sidebarLoader.setController(sidebarController);
        FXMLLoader dashboardLoader = new FXMLLoader(Home.class.getResource("Dashboard.fxml"));
        borderPane.setCenter(dashboardLoader.load());

        Scene scene = new Scene(borderPane, 1125, 737);


        Stage stage = (Stage) Loginbtn.getScene().getWindow();
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}