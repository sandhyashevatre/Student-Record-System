package com.prodapt.learningspring.model.wordle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;


@Component
public class Classroom {
  private List<Student> students;
  private Connection cnx;

 

  public Classroom() {
      students = new ArrayList<>();
      setConnection(); 
  }

  public void setConnection() {
      try {
          Class.forName("com.mysql.jdbc.Driver");
          cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/entries", "SandhyaShevatre","sandhya@123");
      } catch (ClassNotFoundException | SQLException e) {
          e.printStackTrace();
      }
  }

 

  public List<Student> getStudents() throws SQLException {
      if (cnx == null) {
          throw new IllegalStateException("Connection is not initialized");
      }
      students = new ArrayList<>();
      String get_students = "SELECT * FROM Student";
      Statement stmt = cnx.createStatement();
      ResultSet rs = stmt.executeQuery(get_students);
      while (rs.next()) {
          Student s = new Student();
          s.setId(rs.getInt("id"));
          s.setName(rs.getString("name"));
          s.setScore(rs.getInt("score"));
          s.setRank(rs.getInt("ranking")); 
          students.add(s);
      }
      rank();
      return Collections.unmodifiableList(students);
  }

 

  private void rank() {
    Collections.sort(students, (s1, s2) -> -Integer.compare(s1.getScore(), s2.getScore()));
    for (int i = 0; i < students.size(); i++)
      students.get(i).setRank(i + 1);
    for (int i = 1; i < students.size(); i++) {
      if (students.get(i).getScore() == students.get(i-1).getScore())
        students.get(i).setRank(students.get(i-1).getRank());
    }
  }
  public void add(Student student) throws SQLException {
      String set_students = "INSERT INTO Student VALUES(?,?,?,?)";
      PreparedStatement insert = cnx.prepareStatement(set_students);
      Statement stmt = cnx.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Student ORDER BY id DESC LIMIT 1");
      int previd = 0;
      if(students.size() == 0)
          previd = 0;
      else {
          while(rs.next())
              previd = rs.getInt("id");
      }
      insert.setInt(1, previd+1);
      insert.setInt(2, student.getRank());
      insert.setString(3, student.getName());
      insert.setInt(4, student.getScore());
      insert.executeUpdate();
      students.add(student);
      rank();
  }

  public void remove(int id) throws SQLException {
    String delete_student = "DELETE FROM Student WHERE id = ?";
    PreparedStatement delete = cnx.prepareStatement(delete_student);
    delete.setInt(1, id);
    delete.executeUpdate();
    int idx=0;
    for(Student s:students) {
    	if(s.getId()==id)
    		idx = students.indexOf(s);
    }
    students.remove(idx);
    rank();
  }

  public void replace(int id, Student current) throws SQLException {
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).getId() == id) {
        String update_student = "UPDATE Student SET ranking = ?, score = ?,name = ? WHERE id = ?";
        PreparedStatement update = cnx.prepareStatement(update_student);
        update.setInt(1, current.getRank());
        update.setInt(2, current.getScore());
        update.setString(3, current.getName());
        update.setInt(4, id);
        update.execute();
        students.get(i).setName(current.getName());
        students.get(i).setScore(current.getScore());
      }
    }
    rank();
  }
  public Optional<Student> getById(int id) {
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).getId() == id)
        return Optional.of(students.get(i));
    }
    return Optional.empty();
  }
}