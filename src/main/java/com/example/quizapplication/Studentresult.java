package com.example.quizapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.StudentResult;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Studentresult {

    @FXML
    private ComboBox<String> quizComboBox; // Updated to use ComboBox
    @FXML
    private Button searchButton;
    @FXML
    private TableView<StudentResult> studentResultsTable;
    @FXML
    private TableColumn<StudentResult, Integer> studentIdColumn;
    @FXML
    private TableColumn<StudentResult, String> studentNameColumn;
    @FXML
    private TableColumn<StudentResult, Integer> remarksColumn;

    @FXML
    public void initialize() {
        populateQuizList();
        setupComboBoxListener();
    }

    private void populateQuizList() {
        // Fetch all quiz titles from the database
        List<String> quizTitles = getAllQuizTitles();
        quizComboBox.setItems(FXCollections.observableArrayList(quizTitles));
    }

    public static ArrayList<String> getAllQuizTitles() {
        ArrayList<String> quizTitles = new ArrayList<>();
        String query = "SELECT title FROM quizs";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:quiz.db");
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Executing query: " + preparedStatement.toString()); // Debugging

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                quizTitles.add(title);
            }

            if (quizTitles.isEmpty()) {
                System.out.println("No quizzes found in the database."); // Debugging
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizTitles;
    }


    private void setupComboBoxListener() {
        quizComboBox.setOnAction(event -> {
            String selectedQuiz = quizComboBox.getSelectionModel().getSelectedItem();
            if (selectedQuiz != null) {
                quizComboBox.setValue(selectedQuiz); // Set selected quiz in the ComboBox
            }
        });
    }

    @FXML
    public void searchresult() {
        String quizTitle = quizComboBox.getValue();
        if (quizTitle == null || quizTitle.trim().isEmpty()) {
            showAlert("Error", "Please select or enter a quiz title to search.");
            return;
        }

        List<StudentResult> students = StudentResult.getresult(quizTitle);
        studentResultsTable.getItems().clear();
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<>("remarks"));
        studentResultsTable.getItems().addAll(students);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void handleBackAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/teacher_home.fxml"));
            Parent addStudentPage = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(addStudentPage));
            stage.setTitle("Add Student");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
