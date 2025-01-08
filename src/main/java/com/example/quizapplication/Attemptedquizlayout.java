package com.example.quizapplication;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Quiz;
import model.Quizresult;

import java.net.URL;
import java.util.ResourceBundle;

public class Attemptedquizlayout implements Initializable {
    public Label title;
    public VBox item;
    private Quiz quiz;
    private Quizresult quizresult;
private AttemptedQuizItemClicKListener ClicKListener;
    public void setData(Quiz quiz,Quizresult quizresult) {
        this.quiz = quiz;
        this.quizresult=quizresult;
        this.title.setText(this.quiz.getTitle());
    }

    public void setClicKListener(AttemptedQuizItemClicKListener clicKListener) {
        ClicKListener = clicKListener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loadData(MouseEvent mouseEvent) {
        System.out.println("Item Clicked...");
        Integer numberOfAttempedQuestions =quizresult .getNumberOfAttempedQuestions();
        Integer numberOfQuestions = quiz.getNumberOfQuestions();
        System.out.println(numberOfAttempedQuestions);
        System.out.println(numberOfQuestions);
        ClicKListener.itemclicked(numberOfQuestions , numberOfAttempedQuestions);
    }
    }

