package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.JDBCUtil;
import model.Exam_Questions;
import model.Questions;

public class Exam_QuestionsDAO {

    public static Exam_QuestionsDAO getInstance() {
        return new Exam_QuestionsDAO();
    }

    public int insert(Exam_Questions eq) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO exam_questions (examId, questionId, questionOrder) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, eq.getExamId());
            pst.setInt(2, eq.getQuestionId());
            pst.setInt(3, eq.getQuestionOrder());

            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Questions> selectByExamId(int examId) {
        List<Questions> list = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT q.* FROM exam_questions eq " +
                         "JOIN questions q ON eq.questionId = q.Id " +
                         "WHERE eq.examId = ? ORDER BY eq.questionOrder";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, examId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Questions q = new Questions();
                q.setId(rs.getInt("Id"));
                q.setContent(rs.getString("Content"));
                q.setAudioPath(rs.getString("AudioPath"));
                q.setImgPath(rs.getString("ImagePath"));
                q.setSection(rs.getString("Section"));

                // Nếu cần lấy luôn câu trả lời đúng/sai
                q.setAnswers(AnswersDAO.getInstance().selectByQuestionId(q.getId()));

                list.add(q);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int deleteByExamId(int examId) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM exam_questions WHERE examId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, examId);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public int getQuestionOrder(int examId, int questionId) {
        int order = -1;
        String sql = "SELECT QuestionOrder FROM exam_questions WHERE ExamID = ? AND QuestionID = ?";

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examId);
            ps.setInt(2, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                order = rs.getInt("QuestionOrder");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return order;
    }
    public int update(Exam_Questions eq){
    	int result = 0;
		try {
			 Connection con = JDBCUtil.getConnection();
			 String sql = "UPDATE exam_questions SET QuestionOrder = ? WHERE ExamID = ? AND QuestionID = ?";
			 PreparedStatement stmt = con.prepareStatement(sql);
			 stmt.setInt(1, eq.getQuestionOrder());
		     stmt.setInt(2, eq.getExamId());
		     stmt.setInt(3, eq.getQuestionId());
		     result = stmt.executeUpdate();
		} catch (Exception ex) {
			// TODO: handle exception
			 ex.printStackTrace();
		}
		 return result;
    }
    public int getMaxQuestionOrderByExam(int examId, String section) {
        int maxOrder = 0;
        String sql = "SELECT MAX(eq.QuestionOrder) " +
                     "FROM exam_questions eq " +
                     "JOIN questions q ON eq.QuestionID = q.ID " +
                     "WHERE eq.ExamID = ? AND q.Section = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, examId);
            ps.setString(2, section);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxOrder = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxOrder;
    }

	public int getQuestionIDByOrder(int examId, int questionOrder) {
    int questionID = -1;
    String sql = "SELECT QuestionID FROM exam_questions WHERE ExamID = ? AND QuestionOrder = ?";

    try (Connection con = JDBCUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, examId);
        ps.setInt(2, questionOrder);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            questionID = rs.getInt("QuestionID");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return questionID;
}

	public boolean isQuestionOrderExists(int examID, int questionOrder, String section) {
	    String sql = "SELECT COUNT(*) FROM exam_questions eq " +
	                 "JOIN questions q ON eq.QuestionID = q.ID " +
	                 "WHERE eq.ExamID = ? AND eq.QuestionOrder = ? AND q.Section = ?";
	    try (Connection con = JDBCUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, examID);
	        ps.setInt(2, questionOrder);
	        ps.setString(3, section);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Trả về true nếu có bản ghi tồn tại trong cùng section
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}

	public boolean isQuestionOrderExistsExceptCurrent(int examId, int questionId, int questionOrder, String section) {
	    String sql = "SELECT COUNT(*) FROM exam_questions eq " +
	                 "JOIN questions q ON eq.QuestionID = q.ID " +
	                 "WHERE eq.ExamID = ? AND eq.QuestionOrder = ? AND q.Section = ? AND eq.QuestionID != ?";
	    try (Connection con = JDBCUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, examId);
	        ps.setInt(2, questionOrder);
	        ps.setString(3, section);
	        ps.setInt(4, questionId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Trả về true nếu có bản ghi khác tồn tại trong cùng section
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return false;
	}
}
