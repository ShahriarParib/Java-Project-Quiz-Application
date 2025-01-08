package com.example.quizapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
public class HelloApplication extends Application {
//    checking
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home_page.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("HomePage");
        stage.show();
     /* Quiz.createTable();
        Questions.createTable();
       Student.createTable();
       Quizresult.createTable();

        Quizresultdetails.createTable();
        Teacher.createTable();
        Student.createAttendanceTablef();*/
    }

    public static void main(String[] args)
    {
        launch();
    }
}
