package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.JDBCUtil;
import model.Answers;
import model.Questions;

public class QuestionsDAO {

    public static QuestionsDAO getInstance() {
        return new QuestionsDAO();
    }

    public int insert(Questions q) {
        int generatedId = -1;

        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO questions (Content, AudioPath, ImagePath, section) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, q.getContent());
            pst.setString(2, q.getAudioPath());
            pst.setString(3, q.getImgPath());
            pst.setString(4, q.getSection());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
               
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
                rs.close();
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public int update(Questions q) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE questions SET Content = ?, AudioPath = ?, ImagePath = ?, section = ? WHERE Id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, q.getContent());
            pst.setString(2, q.getAudioPath());
            pst.setString(3, q.getImgPath());
            pst.setString(4, q.getSection());
            pst.setInt(5, q.getId());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM questions WHERE Id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Questions> selectAll() {
        List<Questions> list = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM questions";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Questions q = new Questions();
                q.setId(rs.getInt("Id"));
                q.setContent(rs.getString("Content"));
                q.setAudioPath(rs.getString("AudioPath"));
                q.setImgPath(rs.getString("ImagePath"));
                q.setSection(rs.getString("section"));
                List<Answers> allAnswers = AnswersDAO.getInstance().selectByQuestionId(q.getId());
                q.setAnswers(allAnswers); 
                list.add(q);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Questions selectById(int id) {
        Questions q = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM questions WHERE Id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                q = new Questions();
                q.setId(rs.getInt("Id"));
                q.setContent(rs.getString("Content"));
                q.setAudioPath(rs.getString("AudioPath"));
                q.setImgPath(rs.getString("ImagePath"));
                q.setSection(rs.getString("section"));
                List<Answers> allAnswers = AnswersDAO.getInstance().selectByQuestionId(q.getId());
                q.setAnswers(allAnswers); 
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return q;
    }

    public List<Questions> selectByCondition(String condition) {
        List<Questions> list = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM questions WHERE " + condition;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Questions q = new Questions();
                q.setId(rs.getInt("Id"));
                q.setContent(rs.getString("Content"));
                q.setAudioPath(rs.getString("AudioPath"));
                q.setImgPath(rs.getString("ImagePath"));
                q.setSection(rs.getString("section"));
                list.add(q);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	

}
