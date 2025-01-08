package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.ArrayList;

public class StudentResult {
    private Integer studentId;
    private String studentName;
    private Integer remarks;

    public StudentResult(Integer studentId, String studentName, Integer remarks) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.remarks = remarks;
    }
    public StudentResult() {
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getRemarks() {
        return remarks;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setRemarks(Integer remarks) {
        this.remarks = remarks;
    }
     public static ArrayList<StudentResult>getresult(String quizTitle) {
        ArrayList<StudentResult> students=new ArrayList<>();
        String query = "SELECT " +
                "s.id AS student_id, " +
                "s.firstName || ' ' || s.lastName AS student_name, " +
                "qr.RIGHT_ANSWERS AS right_answer " + // Sum of correct answers
                "FROM students s " +
                "JOIN QUIZ_RESULTS qr ON s.id = qr.student_id " +
                "JOIN quizs q ON q.quiz_id = qr.quiz_id " +
                "WHERE q.title = ? ";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:quiz.db");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, quizTitle);

            System.out.println("Executing query: " + preparedStatement.toString());  // Debugging

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No data found for quiz title: " + quizTitle);  // Debugging
            }

            while (resultSet.next()) {
                StudentResult  s= new StudentResult();
                s.setStudentId(resultSet.getInt(1));
                s.setStudentName(resultSet.getString(2));
                s.setRemarks(resultSet.getInt(3));



             //   System.out.println("Student ID: " + studentId + ", Student Name: " + studentName + ", Right Answers: " + rightAnswerCount);  // Debugging
                students.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}

