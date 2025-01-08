package com.example.quizapplication;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Quiz;
import model.Quizresult;
import model.Student;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class Attemptedquiz implements Initializable {
    public VBox list;
    public Label total;
    public Label aq;
    public Label ra;
    public Label wa;
    public PieChart attemptedchart;
    public PieChart rightwrongchart;
    private Student student;
    public void setStudent(Student student)
    {
        this.student=student;
        setlist();
    }
    private void setlist(){
        Map<Quizresult, Quiz> map = Quizresult.getQuizzes(student);
        Set<Quizresult> quizzesData = map.keySet();

        for(Quizresult quizResult : quizzesData) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("attemptedquizlayout.fxml"));
            try {
                Parent root = fxmlLoader.load();
                Attemptedquizlayout attempedQuizListItemFXML = fxmlLoader.getController();
                attempedQuizListItemFXML.setClicKListener(new AttemptedQuizItemClicKListener() {


                    @Override
                    public void itemclicked(Integer nof, Integer noa) {

                        int attemped = noa;
                        int nonAtemped = nof - attemped;
                        int right = quizResult.getRightanswers();
                        int wrong = attemped - right;

                        total.setText(nof.toString());
                        aq.setText(attemped + "");
                        ra.setText(right + "");
                        wa.setText((noa - quizResult.getRightanswers()) + "");


                        ObservableList<PieChart.Data> attempedData = attemptedchart.getData();
                        attempedData.clear();
                        attempedData.add(new PieChart.Data("Attemped Questions (" + attemped + ")", attemped));
                        attempedData.add(new PieChart.Data("Not Attemped Questions (" + nonAtemped + ")", nonAtemped));


                        ObservableList<PieChart.Data> answerData =rightwrongchart.getData();
                        answerData.clear();
                        answerData.add(new PieChart.Data("Right Answers (" + right + ")", right));
                        answerData.add(new PieChart.Data("Wrong Answers (" + wrong + ")", wrong));


                    }
                });
                attempedQuizListItemFXML.setData(map.get(quizResult), quizResult);
                this.list.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void backfromattempted(ActionEvent actionEvent) {
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
    }
}
