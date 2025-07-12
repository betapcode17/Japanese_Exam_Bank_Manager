package controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.security.PublicKey;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import dao.Exam_QuestionsDAO;
import dao.QuestionsDAO;
import model.Exams;
import model.Questions;
import view.MainFrame;

public class QuestionsController implements Action{

	public MainFrame view;
	public QuestionsDAO dao;
	public Exam_QuestionsDAO exam_questiondao;
	
	
	


	public QuestionsController(MainFrame view) {
		// TODO Auto-generated constructor stub
		super();
		this.view = view;
		this.dao = new QuestionsDAO();
		this.exam_questiondao = new Exam_QuestionsDAO();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
	    String cm = e.getActionCommand();
	    if (cm.equals("Xóa")) {
	        int selectedRow = view.questionTable.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
	            return;
	        }
	        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa câu hỏi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
	        if (confirm != JOptionPane.YES_OPTION) return;
	        int questionOrder = (int) view.questionTable.getValueAt(selectedRow, 0);
	        Exams selectedExam = (Exams) view.examComboBox.getSelectedItem();
        	if (selectedExam != null) {
            String selectedSection = (String) view.sectionCombobox.getSelectedItem();  // Lấy mục đã chọn
            int examID = selectedExam.getId();
	        int questionID = exam_questiondao.getQuestionIDByOrder(examID, questionOrder); // Cần thêm phương thức này vào DAO
	        QuestionsDAO dao = new QuestionsDAO();
	        int result = dao.delete(questionID);

	        if (result > 0) {
	            JOptionPane.showMessageDialog(view, "Xóa thành công!"); 
                view.LoadTableQuestionData(examID,selectedSection);
            	}
	        } else {
	            JOptionPane.showMessageDialog(view, "Xóa thất bại!");
	        }
	    }
	}

	
	
	

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

}
