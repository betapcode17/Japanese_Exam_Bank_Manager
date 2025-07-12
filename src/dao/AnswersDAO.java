package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.JDBCUtil;
import model.Answers;

public class AnswersDAO {

    public static AnswersDAO getInstance() {
        return new AnswersDAO();
    }

    public int insert(Answers a) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO answers (QuestionId, Content, IsCorrect) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, a.getQuestionId());
            pst.setString(2, a.getContent());
            pst.setBoolean(3, a.isCorrect());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(Answers a) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE answers SET Content = ?, IsCorrect = ? WHERE ID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, a.getContent());
            pst.setBoolean(2, a.isCorrect());
            pst.setInt(3, a.getId());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteById(int id) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM answers WHERE Id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Answers> selectByQuestionId(int questionId) {
        List<Answers> list = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM answers WHERE QuestionId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, questionId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Answers a = new Answers();
                a.setId(rs.getInt("Id"));
                a.setQuestionId(rs.getInt("QuestionId"));
                a.setContent(rs.getString("Content"));
                a.setCorrect(rs.getBoolean("IsCorrect"));
                list.add(a);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Answers selectById(int id) {
        Answers a = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM answers WHERE Id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                a = new Answers();
                a.setId(rs.getInt("Id"));
                a.setQuestionId(rs.getInt("QuestionId"));
                a.setContent(rs.getString("Content"));
                a.setCorrect(rs.getBoolean("IsCorrect"));
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }
}
