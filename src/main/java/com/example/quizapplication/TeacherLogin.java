package com.example.quizapplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Student;
import model.Teacher;
import org.controlsfx.control.Notifications;

import javax.security.auth.login.LoginException;
import java.io.IOException;
public class TeacherLogin {
    public TextField usernameField;
    public PasswordField passwordField;

    public void teacherlogin(ActionEvent actionEvent) {
        Teacher s = new Teacher(this.usernameField.getText() ,this.passwordField.getText() );
        try{
            s.login();
            System.out.println(s);

            try {
                FXMLLoader fxmlLoader =  new FXMLLoader(getClass().
                        getResource("teacher_home.fxml"));
                Parent root = fxmlLoader.load();
                TeacherHome controller = fxmlLoader.getController();

                Stage stage = (Stage)passwordField.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }catch (Exception ex){
            if(ex instanceof LoginException){
                Notifications.create()
                        .title("Login Failed..")
                        .text("Email or password incorrect")
                        .showError();
            }
        }
    }
    public void backteacherpage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/Home_page.fxml"));
            Parent teacherLoginPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(teacherLoginPage));
            stage.setTitle("Teacher Login");
            stage.show();
        }    catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogin(ActionEvent actionEvent) {

    }


    public void signuppage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/teachersignup.fxml"));
            Parent teacherLoginPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(teacherLoginPage));
            stage.setTitle("Teacher Login");
            stage.show();
        }    catch (IOException e) {
            e.printStackTrace();
        }
    }
}

