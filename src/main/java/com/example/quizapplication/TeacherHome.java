package com.example.quizapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import model.Teacher;
import java.io.IOException;

public class TeacherHome {
    public Label welcometeacher; // Label to display the teacher's name
    private Teacher teacher;


    // Handle navigation to "Add Student" page
    public void handleAddStudent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/add_student.fxml"));
            Parent addStudentPage = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addStudentPage));
            stage.setTitle("Add Student");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle navigation to "Add Quiz" page
    public void handleAddQuiz(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/add_quiz.fxml"));
            Parent addQuizPage = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addQuizPage));
            stage.setTitle("Add Quiz");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle logout confirmation and redirect to Home page
    public void logoutteacher(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Do you really want to logout?");
        alert.setContentText("Press 'Exit' to leave or 'Cancel' to stay.");

        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(exitButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == exitButton) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/Home_page.fxml"));
                    Parent homePage = loader.load();

                    Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(homePage));
                    stage.setTitle("Home Page");

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void quizdetails(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/quiztreeview.fxml"));
            Parent addQuizPage = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addQuizPage));
            stage.setTitle("Add Quiz");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void studentresults(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/studentresult.fxml"));
            Parent addQuizPage = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addQuizPage));
            stage.setTitle("Add Quiz");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
