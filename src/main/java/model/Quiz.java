package model;

import constants.databaseconstants;

import java.sql.*;
import java.util.*;

public class Quiz {
    // Properties

    private Integer quizId;
    private String title;

    // Constructers
    public Quiz(String title) {
        this.title = title;
    }

    public static class MetaData {

        public static final String TABLE_NAME = "quizs";
        public static final String QUIZ_ID = "quiz_id";
        static final String TITLe = "title";

    }

    public Quiz() {
    }

    // Getter Setters
    public Integer getQuizId() {
        return quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
    return this.title;
    }

    // Other Methods
    public static void createTable() {
        try {
            String raw = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR(50) );";
            String query = String.format(raw, MetaData.TABLE_NAME, MetaData.QUIZ_ID, MetaData.TITLe);
            System.err.println(query);
            String connectionUrl = "jdbc:sqlite:quiz.db";
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = connection.prepareStatement(query);
            boolean b = ps.execute();
            System.out.println("models.Quiz.createTable()");
            System.out.println(b);
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int save() {
        String raw = "Insert into %s (%s) values (?)";
        String query = String.format(raw, MetaData.TABLE_NAME, MetaData.TITLe);
        String connectionUrl = "jdbc:sqlite:quiz.db";

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, this.title);
                int i = ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

        return -1;
    }

    public boolean save(ArrayList<Questions> questions){
        boolean flag = true;
        this.quizId = this.save();

        for(Questions q : questions){
            flag = flag && q.save();
            System.out.println(flag);
        }
        return flag;
    }
    public static Map<Quiz, Integer> getAllWithQuestionCount() {
        Map<Quiz, Integer> quizes = new HashMap<>();
        Quiz key = null;

        String query = String.
                format("SELECT %s.%s , %s  ," +
                                " COUNT(*) as question_count  " +

                                "FROM %s join %s on %s.%s = %s.%s GROUP BY %s.%s",
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID,
                        MetaData.TITLe,
                        MetaData.TABLE_NAME,
                        Questions.myData.Table_name,
                        Questions.myData.Table_name,
                        Questions.myData.QUIZID,
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID,
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID
                );
        String connectionUrl ="jdbc:sqlite:quiz.db";
        System.out.println(query);
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setQuizId(result.getInt(1));
                    temp.setTitle(result.getString(2));
                    int count = result.getInt(3);
                    quizes.put(temp, count);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return quizes;
    }
    public List<Questions> getQuestions() {
        List<Questions> quizes = new ArrayList<>();

        String query = String.
                format("SELECT " +
                                " %s , %s , " +
                                "%s , %s ," +
                                " %s , %s , \n" +
                                "%s\n" +
                                "FROM %s  where %s = ?",

                        Questions.myData.QUESTIONID,
                        Questions.myData.QUESTION,
                        Questions.myData.OPTION1,
                        Questions.myData.OPTION2,
                        Questions.myData.OPTION3,
                        Questions.myData.OPTION4,
                        Questions.myData.ANSWER,
                         Questions.myData.Table_name,
                        Questions.myData.QUIZID


                );
        String connectionUrl = "jdbc:sqlite:quiz.db";
        System.out.println(query);
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, this.quizId);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Questions tempQuestion = new Questions();
                    tempQuestion.setQuestionId(result.getInt(1));
                    tempQuestion.setQuestion(result.getString(2));
                    tempQuestion.setOption1(result.getString(3));
                    tempQuestion.setOption2(result.getString(4));
                    tempQuestion.setOption3(result.getString(5));
                    tempQuestion.setOption4(result.getString(6));
                    tempQuestion.setAnswer(result.getString(7));
                    tempQuestion.setQuiz(this);
                    quizes.add(tempQuestion);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return quizes;
    }
    public Integer getNumberOfQuestions() {
        String raw = "SELECT count(*) from %s WHERE %s = ?";
        String query = String.format(raw, Questions.myData.Table_name
                , Questions.myData.QUIZID
        );


        try {
            Class.forName(databaseconstants.DRIVER_CLASS);
            Connection connection = DriverManager

                    .getConnection(databaseconstants.CONNECTION_URL);

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.quizId);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                return result.getInt(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch ( SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static Map<Quiz, List<Questions>> getAll() {
        Map<Quiz, List<Questions>> quizes = new HashMap<>();
        Quiz key = null;

        String query = String.
                format("SELECT %s.%s , %s  ," +
                                " %s.%s , %s , " +
                                "%s , %s ," +
                                " %s , %s , \n" +
                                "%s\n" +
                                "FROM %s join %s on %s.%s = %s.%s",
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID,
                        MetaData.TITLe,
                        Questions.myData.Table_name,
                        Questions.myData.QUESTIONID,

                        Questions.myData.QUESTION,
                        Questions.myData.OPTION1,
                        Questions.myData.OPTION2,
                        Questions.myData.OPTION3,
                        Questions.myData.OPTION4,
                        Questions.myData.ANSWER,
                        MetaData.TABLE_NAME,
                        Questions.myData.Table_name,
                        Questions.myData.Table_name,
                        Questions.myData.QUIZID,
                        MetaData.TABLE_NAME,
                        MetaData.QUIZ_ID
                );
        String connectionUrl = databaseconstants.CONNECTION_URL;
        System.out.println(query);
        try {
            Class.forName(databaseconstants.DRIVER_CLASS);
            try (Connection connection = DriverManager.getConnection(connectionUrl)) {

                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    Quiz temp = new Quiz();
                    temp.setQuizId(result.getInt(1));
                    temp.setTitle(result.getString(2));

                    Questions tempQuestion = new Questions();
                    tempQuestion.setQuestionId(result.getInt(3));
                    tempQuestion.setQuestion(result.getString(4));
                    tempQuestion.setOption1(result.getString(5));
                    tempQuestion.setOption2(result.getString(6));
                    tempQuestion.setOption3(result.getString(7));
                    tempQuestion.setOption4(result.getString(8));
                    tempQuestion.setAnswer(result.getString(9));

                    if (key != null && key.equals(temp)) {
                        quizes.get(key).add(tempQuestion);
                    } else {
                        ArrayList<Questions> value = new ArrayList<>();
                        value.add(tempQuestion);
                        quizes.put(temp, value);
                    }

                    key = temp;

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return quizes;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!(obj instanceof Quiz))
            return  false;
        Quiz t = (Quiz)obj;

        if(this.quizId == t.quizId){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, title);
    }

    public static boolean hasStudentAttendedQuiz(int studentId, int quizId) {
        String query = "SELECT COUNT(*) FROM student_quiz_attendance WHERE student_id = ? AND quiz_id = ?";
        String connectionUrl = "jdbc:sqlite:quiz.db";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ps.setInt(2, quizId);
            ResultSet result = ps.executeQuery();

            if (result.next() && result.getInt(1) > 0) {
                return true; // Student has attended the quiz
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Student has not attended the quiz
    }
    public static void recordAttendance(int studentId, int quizId) {
        String query = "INSERT INTO student_quiz_attendance (student_id, quiz_id) VALUES (?, ?)";
        String connectionUrl = "jdbc:sqlite:quiz.db";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, studentId);
            ps.setInt(2, quizId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}




