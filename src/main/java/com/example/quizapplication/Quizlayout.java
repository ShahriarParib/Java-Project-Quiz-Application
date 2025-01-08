package com.example.quizapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.Quiz;
import model.Student;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Quizlayout implements Initializable {
    public Label quizcount;
    public Label quiztitle;
    public Button start;
    private Quiz quiz;
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.quiztitle.setText(this.quiz.getTitle());
    }

    private newscreenlistener screenlistener;

    public void setScreenlistener(newscreenlistener screenlistener) {
        this.screenlistener = screenlistener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setQuizcount(String value) {

        this.quizcount.setText(value);
    }




    public void startquiz(ActionEvent actionEvent) {
        // Get the logged-in student's id
        Integer studentId = this.student.getId(); // Assuming you have this method in your Student class

        // Get the quiz id
        Integer quizId = this.quiz.getQuizId(); // Assuming you have this method in your Quiz class

        // Check if the student has already attended the quiz
        if (Quiz.hasStudentAttendedQuiz(studentId, quizId)) {
            Notifications.create()
                    .title("Failure")
                    .text("You have already attended the exam")
                    .position(Pos.CENTER)
                    .hideAfter(Duration.seconds(2))
                    .showInformation();
        } else {
            // If not attended, allow the student to start the quiz and mark attendance
            Quiz.recordAttendance(studentId, quizId);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("questionscreen.fxml"));
            try {
                Node node = fxmlLoader.load();
                Questionscreen questionscreen = fxmlLoader.getController();
                questionscreen.setStudent(this.student);
                questionscreen.setQuiz(this.quiz);
                questionscreen.setScreenlistener(this.screenlistener);
                this.screenlistener.changescreen(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


