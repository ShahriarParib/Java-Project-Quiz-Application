package com.example.quizapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import model.Quiz;
import model.Student;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SequencedMap;
import java.util.Set;

public class QuizList implements Initializable {
   @FXML
     private FlowPane quizlistcontainer;
   Map<Quiz,Integer>quizes=null;
private newscreenlistener screenlistener;
    private Set<Quiz>keys;
    public void setScreenlistener(newscreenlistener screenlistener) {
        this.screenlistener = screenlistener;

    }
    public void setcards()
    {

        for (Quiz quiz:keys) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quizlayout.fxml"));

            try {
                Node node = fxmlLoader.load();
                Quizlayout quizlayout= fxmlLoader.getController();
                quizlayout.setQuiz(quiz);
                quizlayout.setStudent(this.student);
                quizlayout.setQuizcount(quizes.get(quiz) +"");
                quizlayout.setScreenlistener(this.screenlistener);
                quizlistcontainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        quizes = Quiz.getAllWithQuestionCount();
        keys = quizes.keySet();

    }




    }




