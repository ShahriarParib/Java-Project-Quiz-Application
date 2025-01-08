package com.example.quizapplication;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import model.Questions;
import model.Quiz;
import model.Quizresult;
import model.Student;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Questionscreen implements Initializable {
    public FlowPane progressplane;
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    private class Questionchecker
    {
        Property<String> question = new SimpleStringProperty();
        Property<String> option1 = new SimpleStringProperty();
        Property<String> option2 = new SimpleStringProperty();
        Property<String> option3 = new SimpleStringProperty();
        Property<String> option4 = new SimpleStringProperty();
        Property<String> answer = new SimpleStringProperty();

        public void setQuestion(Questions question) {
            this.question.setValue(question.getQuestion());
            this.option1.setValue(question.getOption1());
            this.option2.setValue(question.getOption2());
            this.option3.setValue(question.getOption3());
            this.option4.setValue(question.getOption4());
            this.answer.setValue(question.getAnswer());
        }
    }

    public Label title;
    public Label time;
    public Label question;
    public RadioButton option1;
    public RadioButton option2;
    public RadioButton option3;
    public RadioButton option4;
    public Button next;
    public Button submit;
    public ToggleGroup Options;
    private Quiz quiz;
    private Questions currentquestion;
    private Map<Questions,String> studentanswer=new HashMap<>();
    private Integer numberofrightanswers =0;
    private List<Questions> questionsList;
    int currentindex =0;
    private  Questionchecker questionchecker;
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.title.setText(this.quiz.getTitle());
        this.getdata();
    }
    private void getdata()
    {
        if(quiz!=null)
        {
            this.questionsList=quiz.getQuestions();

            Collections.shuffle(this.questionsList);
            checkprogress();
            setnextquestion();
            setTimer();

        }
    }
    private  newscreenlistener screenlistener;

    public void setScreenlistener(newscreenlistener screenlistener) {
        this.screenlistener = screenlistener;
    }

    private void checkprogress()
    {
        for(int i=0;i<this.questionsList.size();i++)
        {
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("progresslayout.fxml"));
            try
            {
                Node node =fxmlLoader.load();
                Progresslayout progresslayout=fxmlLoader.getController();
                progresslayout.setNumber(i+1);
                progresslayout.setDefaultcolour();
                progressplane.getChildren().add(node);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    //timer fields
    private long min, sec, hr, totalSec = 0; //250 4 min 10 sec
    private Timer timer;
    private String format(long value) {
        if (value < 10) {
            return 0 + "" + value;
        }

        return value + "";
    }

    public void convertTime() {

        min = TimeUnit.SECONDS.toMinutes(totalSec);
        sec = totalSec - (min * 60);
        hr = TimeUnit.MINUTES.toHours(min);
        min = min - (hr * 60);
        time.setText(format(hr) + ":" + format(min) + ":" + format(sec));

        totalSec--;
    }
    private void setTimer() {
        totalSec = this.questionsList.size() * 60;
        this.timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("After 1 sec...");
                        convertTime();
                        if (totalSec <= 0) {
                            timer.cancel();
                            time.setText("00:00:00");
                            // saveing data to database
                            submit(null);
                            Notifications.create()
                                    .title("Error")
                                    .text("TIme Up")
                                    .position(Pos.BOTTOM_RIGHT)
                                    .showError();
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.shownextquestion();
        this.hidesubmitquiz();
        this.questionchecker=new Questionchecker();
        bindfield();
        this.option1.setSelected(true);

    }
    private void bindfield()
    {
        this.question.textProperty().bind(this.questionchecker.question);
        this.option4.textProperty().bind(this.questionchecker.option4);
        this.option3.textProperty().bind(this.questionchecker.option3);
        this.option2.textProperty().bind(this.questionchecker.option2);
        this.option1.textProperty().bind(this.questionchecker.option1);
    }

    private void setnextquestion() {

        if (!(currentindex >= questionsList.size())) {
            Node circlenode= this.progressplane.getChildren().get(currentindex);
            Progresslayout controller=  (Progresslayout) circlenode.getUserData();
            controller.currentquestiontcolour();
            this.currentquestion = this.questionsList.get(currentindex);
            List<String>options=new ArrayList<>();

            options.add(this.currentquestion.getOption1());
            options.add(this.currentquestion.getOption2());
            options.add(this.currentquestion.getOption3());
            options.add(this.currentquestion.getOption4());
            Collections.shuffle(options);

            this.currentquestion.setOption1(options.get(0));
            this.currentquestion.setOption2(options.get(1));
            this.currentquestion.setOption3(options.get(2));
            this.currentquestion.setOption4(options.get(3));
            this.questionchecker.setQuestion(this.currentquestion);
            currentindex++;
        } else {
            hidenextquestion();
            showsubmitquiz();
        }
    }


    public void nextquestion(ActionEvent actionEvent) {

        boolean isRight = false;
        {
            // checking answer
            RadioButton selectedButton = (RadioButton) Options.getSelectedToggle();
            String userAnswer = selectedButton.getText();
            String rightAnswer = this.currentquestion.getAnswer();
            if (userAnswer.trim().equalsIgnoreCase(rightAnswer.trim())) {
                isRight = true;
                this.numberofrightanswers++;

            }

            // saving Answer to hashMap
            studentanswer.put(this.currentquestion, userAnswer);
        }
        Node circlenode = this.progressplane.getChildren().get(currentindex - 1);
        Progresslayout controller=  (Progresslayout) circlenode.getUserData();


        this.setnextquestion();
    }


    private void hidenextquestion()
    {
        this.next.setVisible(false);


    }
    private void shownextquestion()
    {
        this.next.setVisible(true);

    }
    private void hidesubmitquiz()
    {
        this.submit.setVisible(false);

    }
    private void showsubmitquiz()
    {
        this.submit.setVisible(true);

    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();

        }
    }

    public void submit(ActionEvent actionEvent) {
        System.out.println(this.studentanswer);


        Quizresult quizresult=new Quizresult(this.quiz,student,numberofrightanswers);
        boolean result=quizresult.save(this.studentanswer);
        if(result)
        {
            Notifications.create()
                    .title("Message")
                    .text("You have succesfully Attempted Quiz...")
                    .position(Pos.CENTER)
                    .hideAfter(javafx.util.Duration.seconds(2))
                    .showInformation();
            timer.cancel();
            openresultscreen();

        }
        else {
            Notifications.create()
                    .title("ERROR")
                    .text("Something went wrong")
                    .position(Pos.CENTER)
                    .showError();
        }
    }
    private void openresultscreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                getResource("resultscreen.fxml"));

        try {
            Node node = fxmlLoader.load();
            Resultscreen controller = fxmlLoader.getController();
            controller.setValues(this.studentanswer,numberofrightanswers,quiz,questionsList);
            this.screenlistener.removetopscreen();
            this.screenlistener.changescreen(node);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}