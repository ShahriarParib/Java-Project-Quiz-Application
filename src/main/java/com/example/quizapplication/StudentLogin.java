package com.example.quizapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Student;
import org.controlsfx.control.Notifications;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class StudentLogin {
    public Text usernameLabel;
    public TextField usernameField;
    public PasswordField passwordField;



    @FXML
    private void studentlogin(ActionEvent event) {
        System.out.println("controllers.AdminLoginController.loginStudent()");
        Student s = new Student(this.usernameField.getText() ,this.passwordField.getText() );
        try{
            s.login();
            System.out.println(s);

            try {
                FXMLLoader fxmlLoader =  new FXMLLoader(getClass().
                        getResource("studentdashboard.fxml"));
                Parent root = fxmlLoader.load();
                Studentdashboard controller = fxmlLoader.getController();
                controller.setStudent(s);
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


    public void backtstudentpage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/Home_page.fxml"));
            Parent teacherLoginPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(teacherLoginPage));
            stage.setTitle("Student Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signuppage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapplication/studentsignup.fxml"));
            Parent teacherLoginPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(teacherLoginPage));
            stage.setTitle("Student Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
