package com.example.quizapplication;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Student_home implements Initializable {

    public Button back;
    @FXML
    private Button backbutton;
    @FXML
    private StackPane stackpane;
    private Student student;
    private Questionscreen questionscreen; // Reference to the Questionscreen controller

    public void setQuestionscreen(Questionscreen questionscreen) {
        this.questionscreen = questionscreen;
    }

    public void setStudent(Student student) {
        this.student = student;
        addquizlist();
    }

    private void addscreentostackplane(Node node) {
        this.stackpane.getChildren().add(node);
    }

    private void addquizlist() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quiz_list.fxml")); // Correct file
        try {
            Node node = fxmlLoader.load();
            QuizList quizList = fxmlLoader.getController();
            quizList.setStudent(this.student);
            quizList.setScreenlistener(new newscreenlistener() {
                @Override
                public void changescreen(Node node) {
                    addscreentostackplane(node);
                }

                @Override
                public void removetopscreen() {
                    stackpane.getChildren().remove(stackpane.getChildren().size() - 1);
                }

                @Override
                public void handle(Event event) {
                    // Optional
                }
            });
            quizList.setcards();
            stackpane.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleback(ActionEvent actionEvent) {
            ObservableList<Node> nodes = this.stackpane.getChildren();

            if (nodes.size() == 1) {
                // Navigate back to the student dashboard, preserving the student object
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/studentdashboard.fxml"));
                    Parent studentDashboardPage = loader.load();

                    // Pass the student object to the new controller
                    Studentdashboard studentdashboard = loader.getController();
                    studentdashboard.setStudent(this.student);

                    // Navigate to the dashboard
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(studentDashboardPage));
                    stage.setTitle("Student Dashboard");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            if (nodes.size() == 2) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit Confirmation");
                alert.setHeaderText("Do you really want to exit?");
                alert.setContentText("Press 'Exit' to leave or 'Cancel' to stay.");

                // Customizing the buttons
                ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(exitButton, cancelButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == exitButton) {
                        // Stop the timer before exiting
                        if (questionscreen != null) {
                            questionscreen.cancelTimer();
                        }

                        // Remove the current screen
                        this.stackpane.getChildren().remove(nodes.size() - 1);
                    }
                });
            } else {
                // If there are more than 1 node, just remove the top screen
                this.stackpane.getChildren().remove(nodes.size() - 1);
            }
        }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
