package com.example.quizapplication;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class Progresslayout implements Initializable {
    public Circle circle;
    public Label number;

    public void setNumber(Integer number) {
        this.number.setText(number.toString());
    }
    public void setDefaultcolour()
    {
        circle.setFill(Color.web("#DAE0E2"));
        number.setTextFill(Color.valueOf("black"));
    }
    public void currentquestiontcolour()
    {
        circle.setFill(Color.web("#0fa337"));
        number.setTextFill(Color.valueOf("black"));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
