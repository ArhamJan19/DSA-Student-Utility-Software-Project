//package com.example.studentutilitysoftware.RemainingControllers;
//
//import com.example.studentutilitysoftware.ExpenseFeature.ExpenseController;
//import com.example.studentutilitysoftware.Home;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class SideBarController {
//    private BorderPane layout;
//    private String User;
//
//    @FXML
//    private Button Logoutbtn;
//    @FXML
//    private Button Compressbtn;
//    @FXML
//    private Button Decompressbtn;
//    @FXML
//    private Button Notesbtn;
//    @FXML
//    private Button Expensesbtn;
//    @FXML
//    private Button DashBoardbtn;
//
//
//    public void setLayout(BorderPane layout) {
//        this.layout = layout;
//    }
//
//    @FXML
//    protected void GotoDashBoard() {
//        loadView("/com/example/studentutilitysoftware/DashBoard.fxml",600, "DashBoard");
//    }
//
//    @FXML
//    protected void GotoCompressor() {
//        loadView("/com/example/studentutilitysoftware/FileCompressionUI.fxml",600, "Compressor");
//    }
//
//    @FXML
//    protected void GotoDecompressor() {
//        loadView("/com/example/studentutilitysoftware/FileDecompressionUI.fxml",600, "Decompressor");
//    }
//
//    @FXML
//    protected void GotoNotes() {
//        loadView("/com/example/studentutilitysoftware/Notes.fxml",875, "Notes");
//    }
//
//    @FXML
//    protected void GotoExpenses() {
//        loadView("/com/example/studentutilitysoftware/Expenses.fxml",754, "Expenses");
//    }
//
//    @FXML
//    protected void Logout() throws IOException {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Logout Confirmation");
//        alert.setHeaderText(null);
//        alert.setContentText("Are you sure you want to logout?");
//
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.OK) {
//                try {
//                    FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Home.fxml"));
//                    Scene scene = new Scene(fxmlLoader.load(), 1105, 700);
//                    Stage stage = (Stage) Logoutbtn.getScene().getWindow();
//                    stage.setMaxWidth(1105);
//                    stage.setMinWidth(1105);
//                    stage.setTitle("WELCOME!");
//                    stage.setScene(scene);
//                    stage.show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//    private void loadView(String fxmlPath , int View , String s) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//            layout.setRight(loader.load());
//            layout.setMinWidth(View);
//            layout.setMaxWidth(View);
//            if(s.equals("DashBoard")){
//                Stage stage = (Stage) DashBoardbtn.getScene().getWindow();
//                stage.setMinWidth(250 + View);
//                stage.setMaxWidth(250 + View);
//            } else if (s.equals("Compressor")) {
//                Stage stage = (Stage) Compressbtn.getScene().getWindow();
//                stage.setMinWidth(250 + View);
//                stage.setMaxWidth(250 + View);
//            }else if (s.equals("Decompressor")) {
//                Stage stage = (Stage) Decompressbtn.getScene().getWindow();
//                stage.setMinWidth(250 + View);
//                stage.setMaxWidth(250 + View);
//            }else if (s.equals("Notes")) {
//                Stage stage = (Stage) Notesbtn.getScene().getWindow();
//                stage.setMinWidth(250 + View);
//                stage.setMaxWidth(250 + View);
//            }else if (s.equals("Expenses")) {
//                ExpenseController expenseController = loader.getController();
//                expenseController.setUser(this.User);
//                loader.setController(expenseController);
//                Stage stage = (Stage) Expensesbtn.getScene().getWindow();
//                stage.setMinWidth(250 + View);
//                stage.setMaxWidth(250 + View);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error loading view: " + fxmlPath);
//        }
//    }
//
//    public void setUser(String username) {
//        this.User = username;
//    }
//}
package com.example.studentutilitysoftware.RemainingControllers;

import com.example.studentutilitysoftware.ExpenseFeature.ExpenseController;
import com.example.studentutilitysoftware.Home;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SideBarController {
    private BorderPane layout;
    private String User;
    private ExpenseController currentExpenseController; // Reference to current ExpenseController

    @FXML
    private Button Logoutbtn;
    @FXML
    private Button Compressbtn;
    @FXML
    private Button Decompressbtn;
    @FXML
    private Button Notesbtn;
    @FXML
    private Button Expensesbtn;
    @FXML
    private Button DashBoardbtn;
    @FXML
    private AnchorPane SideBarPane;
    @FXML
    private Label UserNameLabel;

    public void setLayout(BorderPane layout) {
        this.layout = layout;
    }

    private void Intialize(String user){
        UserNameLabel.setText(user);
    }

    @FXML
    protected void GotoDashBoard() {
        if (canSwitchFrames()) {
            loadView("/com/example/studentutilitysoftware/DashBoard.fxml", 600, "DashBoard");
        }
    }

    @FXML
    protected void GotoCompressor() {
        if (canSwitchFrames()) {
            loadView("/com/example/studentutilitysoftware/FileCompressionUI.fxml", 600, "Compressor");
        }
    }

    @FXML
    protected void GotoDecompressor() {
        if (canSwitchFrames()) {
            loadView("/com/example/studentutilitysoftware/FileDecompressionUI.fxml", 600, "Decompressor");
        }
    }

    @FXML
    protected void GotoNotes() {
        if (canSwitchFrames()) {
            loadView("/com/example/studentutilitysoftware/Notes.fxml", 875, "Notes");
        }
    }

    @FXML
    protected void GotoExpenses() {
        if (canSwitchFrames()) {
            loadView("/com/example/studentutilitysoftware/Expenses.fxml", 756, "Expenses");
        }
    }

    @FXML
    protected void Logout() throws IOException {
        if (canSwitchFrames()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Home.class.getResource("Home.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1105, 700);
                        Stage stage = (Stage) Logoutbtn.getScene().getWindow();
                        stage.setMaxWidth(1105);
                        stage.setMinWidth(1105);
                        stage.setTitle("WELCOME!");
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void loadView(String fxmlPath, int viewWidth, String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            layout.setRight(loader.load());
            layout.setMinWidth(viewWidth);
            layout.setMaxWidth(viewWidth);

            if (viewName.equals("Expenses")) {
                ExpenseController expenseController = loader.getController();
                expenseController.setUser(this.User);
                currentExpenseController = expenseController; // Store the controller reference
            }

            Stage stage = (Stage) layout.getScene().getWindow();
            if(viewName.equals("Expenses")){
                stage.setMinHeight(737);
                SideBarPane.setPrefHeight(737);
            }
            stage.setMinWidth(266 + viewWidth);
            stage.setMaxWidth(266 + viewWidth);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxmlPath);
        }
    }

    private boolean canSwitchFrames() {
        if (currentExpenseController != null && currentExpenseController.hasUnsavedExpenses()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Expenses");
            alert.setHeaderText("You have unsaved expenses.");
            alert.setContentText("Do you want to save them before switching?");

            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Discard");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    currentExpenseController.saveNewExpenses();
                    return true; // Allow frame switch after saving
                } else if (result.get() == discardButton) {
                    currentExpenseController.clearExpenses();
                    return true; // Allow frame switch after discarding
                } else {
                    return false; // Cancel frame switch
                }
            }
        }
        return true; // No unsaved expenses, allow frame switch
    }

    public void setUser(String username) {
        this.User = username;
        Intialize(username);
    }
}
