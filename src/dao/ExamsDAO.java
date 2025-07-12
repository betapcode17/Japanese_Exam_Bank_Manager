package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.JDBCUtil;
import model.Exams;

public class ExamsDAO implements DAOInterface<Exams>{

	public static ExamsDAO getInstance() {
		return new ExamsDAO();
	}
	
	
	@Override
	public int insert(Exams e) {
	    int result = 0;
	    try {
	        Connection con = JDBCUtil.getConnection();

	        String sql = "INSERT INTO exams (Title, Level) VALUES (?, ?)";
	        PreparedStatement pst = con.prepareStatement(sql);

	        pst.setString(1, e.getTitle());
	        pst.setString(2, e.getLevel());

	        result = pst.executeUpdate(); 

	        JDBCUtil.closeConnection(con);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return result;
	}


	@Override
	public int update(Exams e) {
		int result = 0;
		try {
			 Connection con = JDBCUtil.getConnection();
			 String sql = "UPDATE exams SET Title = ?, Level = ? WHERE ID = ?";
			 PreparedStatement pst = con.prepareStatement(sql);
			    pst.setString(1, e.getTitle());
	            pst.setString(2, e.getLevel());
	            pst.setInt(3, e.getId());

	            result = pst.executeUpdate();
		} catch (Exception ex) {
			// TODO: handle exception
			 ex.printStackTrace();
		}
		 return result;
	}

	@Override
	public int delete(Exams exam) {
	    try {
	        Connection con = JDBCUtil.getConnection();
	        String sql = "DELETE FROM exams WHERE ID = ?";
	        PreparedStatement pst = con.prepareStatement(sql);
	        pst.setInt(1, exam.getId());

	        return pst.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	@Override
	public ArrayList<Exams> selectAll() {
		// TODO Auto-generated method stub
		 ArrayList<Exams> list = new ArrayList<>();
		try {
			Connection con = JDBCUtil.getConnection();
			String sql = "SELECT * FROM exams";
			 PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Exams e = new Exams();
                e.setId(rs.getInt("ID"));
                e.setTitle(rs.getString("Title"));
                e.setLevel(rs.getString("Level"));
                list.add(e);
            }
		} catch (Exception ex) {
			// TODO: handle exception
			 ex.printStackTrace();
		}
		return list;
	}

	@Override
	public Exams selectByID(Exams t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Exams> selectByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}
   public void cleanForm() {
	   
   }
}
