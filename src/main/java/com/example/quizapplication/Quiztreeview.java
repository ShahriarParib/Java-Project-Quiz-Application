package com.example.quizapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import model.Questions;
import model.Quiz;
import model.Student;
import model.Teacher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class Quiztreeview implements Initializable {



        @FXML
    private TreeView treeview;
    private void renderTreeView(){
        Map<Quiz, List<Questions>> data = Quiz.getAll();
        Set<Quiz> quizzes = data.keySet();

        TreeItem root = new TreeItem("Quizzes");
        for(Quiz q : quizzes){
            TreeItem quizTreeItem = new TreeItem(q);

            List<Questions> questions = data.get(q);
            for(Questions question : questions){
                TreeItem questionTreeItem = new TreeItem(question);
                questionTreeItem.getChildren().add(new TreeItem("A : " + question.getOption1()));
                questionTreeItem.getChildren().add(new TreeItem("B : " +question.getOption2()));
                questionTreeItem.getChildren().add(new TreeItem("C : " +question.getOption3()));
                questionTreeItem.getChildren().add(new TreeItem("D : " +question.getOption4()));
                questionTreeItem.getChildren().add(new TreeItem("Ans : " +question.getAnswer()));
                quizTreeItem.getChildren().add(questionTreeItem);
            }

            quizTreeItem.setExpanded(true);
            root.getChildren().add(quizTreeItem);
        }

        root.setExpanded(true);
        this.treeview.setRoot(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderTreeView();

    }

    public void handleback(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/teacher_home.fxml"));
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

