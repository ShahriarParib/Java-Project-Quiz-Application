package com.example.quizapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Student;

import java.io.IOException;
import java.util.EventObject;

public class Studentdashboard {
    public Label studentwelcome;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;

        // Set the student's first name in the studentwelcome label
        studentwelcome.setText("WELCOME " + this.student.getFirstName().toUpperCase());
        studentwelcome.setStyle("-fx-font-weight: bold italic; -fx-font-size: 35px; -fx-text-alignment: center;");
    }

    @FXML
    private void attemptedquiz(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("attemptedquiz.fxml"));
            Parent root = fxmlLoader.load();
            Attemptedquiz controller = fxmlLoader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Attempted Quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show a user-friendly error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the Attempted Quiz page.");
            alert.setContentText("An error occurred while loading the page. Please try again later.");
            alert.showAndWait();
        }
    }




    public void logoutstudent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Do you really want to logout?");
        alert.setContentText("Press 'Exit' to leave or 'Cancel' to stay.");

        // Customizing the buttons
        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(exitButton, cancelButton);

        // Handling the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == exitButton) {
                try {
                    // Load the Home Page (or any other page)
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/Home_page.fxml"));
                    Parent homePage = loader.load();

                    // Get the current stage and set the scene to the new page
                    Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(homePage));
                    stage.setTitle("Home Page");

                    // Show the new scene
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void quizhome(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/studenthome.fxml"));
            Parent root = loader.load();
            Student_home controller = loader.getController();
            controller.setStudent(this.student);
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
