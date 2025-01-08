package model;

import constants.databaseconstants;
import  model.Quiz;
import model.Questions;
import model.Student;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Quizresult {

    private  Integer id;
    private Quiz quiz;
    private Student student;

    private Integer rightanswers;
    private Timestamp timestamp;

    public static  class MetaData{
        public static final String TABLE_NAME ="QUIZ_RESULTS";
        public static final String ID ="id";
        public static final String QUIZ_ID ="QUIZ_ID";
        public static final String STUDENT_ID ="STUDENT_ID";
        public static final String RIGHT_ANSWERS ="RIGHT_ANSWERS";
        public static final String TIMESTAMP ="date_time";
    }


    {
        timestamp =new Timestamp(new Date().getTime());
    }

    public Quizresult() {
    }

    public Integer getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Student getStudent() {
        return student;
    }

    public Integer getRightanswers() {
        return  rightanswers;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setInteger(Integer rightanswers) {
        this.rightanswers = rightanswers;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Quizresult(Integer id, Quiz quiz, Student student, Integer rightanswers) {
        this.id = id;
        this.quiz = quiz;
        this.student = student;
        this.rightanswers =rightanswers;

    }

    public Quizresult(Quiz quiz, Student student,Integer rightanswers) {
        this.quiz = quiz;
        this.rightanswers = rightanswers;
        this.student = student;

    }
    public static void createTable(){

        String raw = "CREATE table %s (\n" +
                "        %s Integer not null PRIMARY key AUTOINCREMENT,\n" +
                "        %s int not null,\n" +
                "        %s int not null ,\n" +
                "        %s int not null ,\n" +
                "        %s timestamp NOT null ,\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s),\n" +
                "        FOREIGN KEY (%s) REFERENCES %s(%s)\n" +
                "        )";
        String query  = String.format(raw,
                MetaData.TABLE_NAME ,
                MetaData.ID,
                MetaData.STUDENT_ID,
                MetaData.QUIZ_ID,
                MetaData.RIGHT_ANSWERS,
                MetaData.TIMESTAMP,
                MetaData.STUDENT_ID,
                Quiz.MetaData.TABLE_NAME ,
                Quiz.MetaData.QUIZ_ID ,
                MetaData.STUDENT_ID ,
                Student.mydata.TABLE_NAME,
                Student.mydata.ID
        );

        System.err.println(query);
        try {
            System.out.println(query);
            String connectionurl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionurl);
            PreparedStatement ps= connection.prepareStatement(query);
            boolean b=ps.execute();
            System.out.println("model.Questions.createTable()");
            System.out.println(b);
            connection.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public boolean save(Map<Questions , String> userAnswers){
        String raw = "INSERT INTO %s (%s , %s , %s , %s ) values \n" +
                "( ?, ? , ?  , CURRENT_TIMESTAMP)";
        String query  = String.format(raw,
                MetaData.TABLE_NAME ,
                MetaData.STUDENT_ID,
                MetaData.QUIZ_ID,
                MetaData.RIGHT_ANSWERS,
                MetaData.TIMESTAMP);

        System.err.println(query);
        try{
            String connectionUrl = databaseconstants.CONNECTION_URL;
            Class.forName(databaseconstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1 , this.getStudent().getId());
            ps.setInt(2 , this.getQuiz().getQuizId());
            ps.setInt(3 , this.getRightanswers());
            int result =  ps.executeUpdate();
            if(result > 0){
                ResultSet keys = ps.getGeneratedKeys();
                if(keys.next()){
                    this.setId(keys.getInt(1));
                    // now we will save details..
                    System.out.println(this);
           return         saveQuizResultDetails(userAnswers);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;

        }
        return false;
    }
    public static Map<Quizresult , Quiz> getQuizzes(Student student){

        Map<Quizresult ,  Quiz > data = new HashMap<>();
        String raw = "SELECT %s.%s ,  " +
                "%s.%s ," +
                " %s.%s as quiz_id , " +
                "%s.%s FROM %s " +
                "JOIN %s on " +
                "%s.%s = %s.%s WHERE %s = ?";

        String query = String.format(raw ,
                MetaData.TABLE_NAME,
                MetaData.ID,
                MetaData.TABLE_NAME,
                MetaData.RIGHT_ANSWERS,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.QUIZ_ID,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.TITLe,
                MetaData.TABLE_NAME,
                Quiz.MetaData.TABLE_NAME,
                MetaData.TABLE_NAME,
                MetaData.QUIZ_ID ,
                Quiz.MetaData.TABLE_NAME,
                Quiz.MetaData.QUIZ_ID ,
                MetaData.STUDENT_ID
        );

        try{
            String connectionUrl = databaseconstants.CONNECTION_URL;
            Class.forName(databaseconstants.DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1 , student.getId());
            ResultSet result  =  ps.executeQuery();

            while (result.next()){
                Quizresult quizResult = new Quizresult();
                quizResult.setId(result.getInt(1));
                quizResult.setInteger(result.getInt(2));

                Quiz quiz = new Quiz();
                quiz.setQuizId(result.getInt(3));
                quiz.setTitle(result.getString(4));

                data.put(quizResult , quiz);
            }


        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return data;
    }
    public Integer getNumberOfAttempedQuestions() {
        String raw = "SELECT COUNT(*) FROM %s  WHERE  %s = ?";
        String query = String.format(raw, Quizresultdetails.MetaData.TABLE_NAME
                , Quizresultdetails.MetaData.QUIZ_RESULT_ID
        );


        try {
            Class.forName(databaseconstants.DRIVER_CLASS);
            Connection connection = DriverManager

                    .getConnection(databaseconstants.CONNECTION_URL);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.getId());
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                return result.getInt(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private boolean saveQuizResultDetails(Map<Questions,String>userAnswers)
    {
      return   Quizresultdetails.quizresultdetails(this,userAnswers);
    }


    }




