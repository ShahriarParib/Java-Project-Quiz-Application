package com.example.quizapplication;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Questions;
import model.Quiz;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Resultscreen implements Initializable {
    public PieChart attemptedchart;
    public PieChart scorechart;
    public VBox questioncontainer;
    public Label score;
    // not fxml variable
    private Map<Questions, String> userAnswers;
    private Integer numberOfRightAnswers = 0;
    private Quiz quiz;
    private List<Questions> questionList;
    private Integer notAttemped = 0;
    private Integer attemped = 0;

    public void setValues(Map<Questions, String> userAnswers,
                          Integer numberOfRightAnswers, Quiz quiz,
                          List<Questions> questionList) {
        this.userAnswers = userAnswers;
        this.numberOfRightAnswers = numberOfRightAnswers;
        this.quiz = quiz;
        this.questionList = questionList;
        this.attemped = this.userAnswers.keySet().size();
        this.notAttemped = this.questionList.size() - attemped;

        this.score.setText(String.format("YOUR MARK IS %d/%d", this.numberOfRightAnswers, this.questionList.size()));
        this.score.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-alignment: center;");
        this.score.setWrapText(true); // Ensures text wraps if the label width is constrained.


        setValuesToChart();
        renderQuestions();
    }

    private void setValuesToChart() {
        ObservableList<PieChart.Data> attempedData = this.attemptedchart.getData();
        attempedData.add(new PieChart.Data(
                String.format("Attemped (%d)", this.attemped), this.attemped));
        attempedData.add(new PieChart.Data(
                String.format("Not Attemped (%d)", this.notAttemped), this.notAttemped));

        ObservableList<PieChart.Data> scoreChartDatata = this.scorechart.getData();
        scoreChartDatata.add(new PieChart.Data(
                String.format("Right Answers (%d)", this.numberOfRightAnswers), this.numberOfRightAnswers));
        scoreChartDatata.add(new PieChart.Data(
                String.format("Wrong Answers (%d)", this.attemped - this.numberOfRightAnswers), this.attemped - this.numberOfRightAnswers));
    }

    private void renderQuestions() {
        for (int i = 0; i < this.questionList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("quizresultsinglequestion.fxml"));

            try {
                Node node = fxmlLoader.load();
                Quizresultsinglequestion controller = fxmlLoader.getController();
                controller.setValues(this.questionList.get(i), this.userAnswers.get(this.questionList.get(i)));
                questioncontainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
