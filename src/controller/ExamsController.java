package controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dao.ExamsDAO;
import model.Exams;
import view.MainFrame;

public class ExamsController implements Action{
    public MainFrame view;
    public ExamsDAO dao;
	
	

	public ExamsController(MainFrame view) {
		super();
		this.view = view;
		this.dao = new ExamsDAO();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cm = e.getActionCommand();
		if(cm.equals("Thêm")) {
			 String title = view.txtTitle.getText();
	            String level = (String) view.comboBoxLevel.getSelectedItem();
	            Exams exam = new Exams(title, level);
	            int result = dao.insert(exam);
	            if (result > 0) {
	                JOptionPane.showMessageDialog(view, "Thêm đề thi thành công!");
	                view.loadExamComboBox();
	                view.cleanForm();
	                view.loadTableData(); // cập nhật lại bảng
	            } else {
	                JOptionPane.showMessageDialog(view, "Thêm đề thi thất bại!");
	            }
	            
	            
	            
	            
		}else if(cm.equals("Cập nhật")) {
			
			int selectedRow = view.examTable.getSelectedRow();
		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần cập nhật!");
		        return;
		    }

		    int id = (int) view.examTable.getValueAt(selectedRow, 0); // Lấy ID của đề thi
		    String title = view.txtTitle.getText().trim();
		    String level = view.comboBoxLevel.getSelectedItem().toString();

		    if (title.isEmpty()) {
		        JOptionPane.showMessageDialog(view, "Tiêu đề không được để trống!");
		        return;
		    }

		    Exams exam = new Exams();
		    exam.setId(id);
		    exam.setTitle(title);
		    exam.setLevel(level);

		    int result = dao.update(exam);

		    if (result > 0) {
		        JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
		        view.loadExamComboBox();
		        view.cleanForm();
		        view.loadTableData();
		    } else {
		        JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
		    }
		}
		else if (cm.equals("Xóa")) {
			
		    int selectedRow = view.examTable.getSelectedRow();
		    System.out.println("Selected row = " + selectedRow);
		    if (selectedRow == -1) {
		        JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
		    if (confirm != JOptionPane.YES_OPTION) return;

		    int id = (int) view.examTable.getValueAt(selectedRow, 0);
		    System.out.println("Deleting ID = " + id);

		    Exams exam = new Exams();
		    exam.setId(id);
    
		    int result = dao.delete(exam);
		    System.out.println("Delete result = " + result);

		    if (result > 0) {
		        JOptionPane.showMessageDialog(view, "Xóa thành công!");
		        //load lại hết data
		        view.loadExamComboBox();
		        view.cleanForm();
		        view.loadTableData();
		        
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
