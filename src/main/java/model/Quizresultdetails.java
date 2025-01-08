package model;

import constants.databaseconstants;
import model.Questions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Set;

public class Quizresultdetails {
    private Integer id;
    private Questions question;
    private String userAnswer;
    private Quizresult quizResult;

    public static class MetaData {
        public static final String TABLE_NAME = "QUIZ_RESULT_DETAILS";
        public static final String ID = "ID";
        public static final String USER_ANSWER = "USER_ANSWER";
        public static final String QUESTION_ID = "QUESTION_ID";
        public static final String QUIZ_RESULT_ID = "QUIZ_RESULT_ID";
    }

    public Quizresultdetails(Integer id, Questions question, String userAnswer, Quizresult quizResult) {
        this.id = id;
        this.question = question;
        this.userAnswer = userAnswer;
        this.quizResult = quizResult;
    }

    public Quizresultdetails(Questions question, String userAnswer, Quizresult quizResult) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.quizResult = quizResult;
    }

    public Quizresultdetails() {
    }

    public Integer getId() {
        return id;
    }

    public Questions getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public Quizresult getQuizResult() {
        return quizResult;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public void setQuizResult(Quizresult quizResult) {
        this.quizResult = quizResult;
    }

    public static void createTable() {
        String raw = "CREATE table %s(\n" +
                "        %s INTEGER NOT null PRIMARY key AUTOINCREMENT,\n" +
                "        %s int not null ,\n" +
                "        %s int NOT NULL ,\n" +
                "        %s varchar(200) not null ,\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "        FOREIGN KEY (%s) REFERENCES questions(id)\n" +
                "        )";
        String query = String.format(raw,
                MetaData.TABLE_NAME,
                MetaData.ID,
                MetaData.QUIZ_RESULT_ID,
                MetaData.QUESTION_ID,
                MetaData.USER_ANSWER,
                MetaData.QUIZ_RESULT_ID,
                Quizresult.MetaData.TABLE_NAME,
                Quizresult.MetaData.ID,
                MetaData.QUESTION_ID,
                Questions.myData.Table_name,
                Questions.myData.QUESTIONID
        );
        System.out.println(query);

        try {
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("Table created: " + b);
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean quizresultdetails(Quizresult quizresult, Map<Questions, String> userAnswers) {
        String raw = "INSERT INTO QUIZ_RESULT_DETAILS (QUIZ_RESULT_ID, QUESTION_ID, USER_ANSWER) VALUES (?, ?, ?)";
        try {
            String connectionUrl = databaseconstants.CONNECTION_URL;
            Class.forName(databaseconstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(raw);

            Set<Questions> questions = userAnswers.keySet();
            for (Questions question : questions) {
                // Debug: print questionId to ensure it's populated
                System.out.println("Question ID: " + question.getQuestionId());
                // Check if questionId is valid (not null or invalid)
                if (question.getQuestionId() == null || question.getQuestionId() <= 0) {
                    System.out.println("Invalid question ID detected!");
                    return false; // Stop execution if invalid questionId
                }

                ps.setInt(1, quizresult.getId());
                ps.setInt(2, question.getQuestionId());
                ps.setString(3, userAnswers.get(question));
                ps.addBatch();
            }

            int[] result = ps.executeBatch();
            if (result.length > 0) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return false;
    }
}
