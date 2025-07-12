package controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import dao.AnswersDAO;
import dao.Exam_QuestionsDAO;
import dao.QuestionsDAO;
import model.Answers;
import model.Exam_Questions;
import model.Exams;
import model.Questions;
import view.AddQuestionPanel;
import view.EditQuestionPanel;
import view.MainFrame;

public class AnswersController implements Action {

    public AddQuestionPanel addView;
    public EditQuestionPanel editview;
    public AnswersDAO answersdao;
    public QuestionsDAO questiondao;

    public AnswersController(AddQuestionPanel addView) {
        this.addView = addView;
        this.answersdao = new AnswersDAO();
        this.questiondao = new QuestionsDAO();
    }

    
    
    public AnswersController(EditQuestionPanel editview) {
		// TODO Auto-generated constructor stub
    	 this.editview = editview;
    	 this.answersdao = new AnswersDAO();
         this.questiondao = new QuestionsDAO();
	}



	@Override
    public void actionPerformed(ActionEvent e) {
        String cm = e.getActionCommand();       
        if (cm.equals("Thêm câu hỏi")) {
            String questionContent = addView.txtQuestion.getText().trim();
            String answerA = addView.txtAnswerA.getText().trim();
            String answerB = addView.txtAnswerB.getText().trim();
            String answerC = addView.txtAnswerC.getText().trim();
            String answerD = addView.txtAnswerD.getText().trim();
            String section = (String) addView.viewmain.sectionCombobox.getSelectedItem();

            boolean isCorrectA = addView.cbAnswerA.isSelected();
            boolean isCorrectB = addView.cbAnswerB.isSelected();
            boolean isCorrectC = addView.cbAnswerC.isSelected();
            boolean isCorrectD = addView.cbAnswerD.isSelected();

            String imgPath = addView.getImgPath();     
            String audioPath = addView.getAudioPath(); 

            if (questionContent.isEmpty() || answerA.isEmpty() || answerB.isEmpty()
                    || answerC.isEmpty() || answerD.isEmpty()) {
                JOptionPane.showMessageDialog(addView, "Vui lòng nhập đầy đủ thông tin câu hỏi và 4 đáp án.");
                return;
            }

            // Kiểm tra chỉ được chọn tối đa 1 đáp án đúng
            int correctAnswerCount = (isCorrectA ? 1 : 0) + (isCorrectB ? 1 : 0) + (isCorrectC ? 1 : 0) + (isCorrectD ? 1 : 0);
            if (correctAnswerCount == 0) {
                JOptionPane.showMessageDialog(addView, "Phải chọn ít nhất 1 đáp án đúng.");
                return;
            }
            if (correctAnswerCount > 1) {
                JOptionPane.showMessageDialog(addView, "Chỉ được chọn đúng 1 đáp án. Vui lòng bỏ chọn các đáp án dư thừa.");
                return;
            }

            try {
                // 1. Lưu câu hỏi
                Questions q = new Questions();
                q.setContent(questionContent);
                q.setAudioPath(audioPath);
                q.setImgPath(imgPath);
                q.setSection(section);

                int questionID = questiondao.insert(q);

                // 2. Lưu các đáp án
                Answers A = new Answers(questionID, answerA, isCorrectA);
                Answers B = new Answers(questionID, answerB, isCorrectB);
                Answers C = new Answers(questionID, answerC, isCorrectC);
                Answers D = new Answers(questionID, answerD, isCorrectD);
                
                answersdao.insert(A);
                answersdao.insert(B);
                answersdao.insert(C);
                answersdao.insert(D);

                // 3. Lưu quan hệ đề - câu hỏi
                Exams selectedExam = (Exams) addView.viewmain.examComboBox.getSelectedItem();
                if (selectedExam != null) {
                    int examID = selectedExam.getId();

                    // Lấy số câu hỏi từ ô nhập
                    int questionOrder;
                    try {
                        questionOrder = Integer.parseInt(addView.txtQuestionNumber.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(addView, "Câu hỏi số không hợp lệ. Vui lòng nhập số nguyên.");
                        return;
                    }

                    // Kiểm tra trùng lặp questionOrder
                    Exam_QuestionsDAO eqDAO = new Exam_QuestionsDAO();
                    if (eqDAO.isQuestionOrderExists(examID, questionOrder, section)) {
                        JOptionPane.showMessageDialog(addView, "Thứ tự câu hỏi " + questionOrder + " đã tồn tại trong section này. Vui lòng chọn một số khác.");
                        return;
                    }

                    Exam_Questions eq = new Exam_Questions();
                    eq.setExamId(examID);
                    eq.setQuestionId(questionID);
                    eq.setQuestionOrder(questionOrder);
                    eqDAO.insert(eq);
                } else {
                    JOptionPane.showMessageDialog(addView, "Vui lòng chọn đề thi.");
                    return;
                }

                JOptionPane.showMessageDialog(addView, "Thêm câu hỏi và đáp án thành công!");
                if (selectedExam != null) {
                    String selectedSection = (String) addView.viewmain.sectionCombobox.getSelectedItem();
                    int examID = selectedExam.getId();
                    addView.viewmain.LoadTableQuestionData(examID, selectedSection);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addView, "Lỗi khi thêm câu hỏi: " + ex.getMessage());
                ex.printStackTrace();
            }
        }else if (cm.equals("Cập nhật câu hỏi")) {     	
            int questionID = editview.questionID; 
            String questionContent = editview.txtQuestion.getText().trim();
            String answerA = editview.txtAnswerA.getText().trim();
            String answerB = editview.txtAnswerB.getText().trim();
            String answerC = editview.txtAnswerC.getText().trim();
            String answerD = editview.txtAnswerD.getText().trim();
            String section = (String) editview.viewmain.sectionCombobox.getSelectedItem();

            boolean isCorrectA = editview.cbAnswerA.isSelected();
            boolean isCorrectB = editview.cbAnswerB.isSelected();
            boolean isCorrectC = editview.cbAnswerC.isSelected();
            boolean isCorrectD = editview.cbAnswerD.isSelected();

            String imgPath = editview.getImgPath();     
            String audioPath = editview.getAudioPath(); 

            if (questionContent.isEmpty() || answerA.isEmpty() || answerB.isEmpty()
                    || answerC.isEmpty() || answerD.isEmpty()) {
                JOptionPane.showMessageDialog(editview, "Vui lòng nhập đầy đủ thông tin câu hỏi và đáp án.");
                return;
            }

            // Kiểm tra chỉ được chọn tối đa 1 đáp án đúng
            int correctAnswerCount = (isCorrectA ? 1 : 0) + (isCorrectB ? 1 : 0) + (isCorrectC ? 1 : 0) + (isCorrectD ? 1 : 0);
            if (correctAnswerCount == 0) {
                JOptionPane.showMessageDialog(editview, "Phải chọn ít nhất 1 đáp án đúng.");
                return;
            }
            if (correctAnswerCount > 1) {
                JOptionPane.showMessageDialog(editview, "Chỉ được chọn đúng 1 đáp án. Vui lòng bỏ chọn các đáp án dư thừa.");
                return;
            }

            // Cập nhật câu hỏi
            Questions q = new Questions();
            q.setId(questionID);
            q.setContent(questionContent);
            q.setSection(section);
            q.setAudioPath(audioPath);
            q.setImgPath(imgPath);
            questiondao.update(q); 
            // Cập nhật đáp án
            
            List<Answers> answersList = answersdao.selectByQuestionId(questionID);

            // Cập nhật nội dung mới từ form vào các đối tượng Answers
            Answers A = new Answers(answersList.get(0).getId(), questionID, answerA, isCorrectA);
            Answers B = new Answers(answersList.get(1).getId(), questionID, answerB, isCorrectB);
            Answers C = new Answers(answersList.get(2).getId(), questionID, answerC, isCorrectC);
            Answers D = new Answers(answersList.get(3).getId(), questionID, answerD, isCorrectD);

            // Gọi update sau khi gán nội dung mới
            answersdao.update(A);
            answersdao.update(B);
            answersdao.update(C);
            answersdao.update(D);

            // Cập nhật lại thứ tự câu hỏi trong đề (nếu có)
            int questionOrder;
            try {
                questionOrder = Integer.parseInt(editview.txtQuestionNumber.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editview, "Câu hỏi số không hợp lệ. Vui lòng nhập số nguyên.");
                return;
            }

            Exams selectedExam = (Exams) editview.viewmain.examComboBox.getSelectedItem();
            if (selectedExam != null) {
                int examID = selectedExam.getId();

                // Kiểm tra trùng lặp questionOrder (ngoại trừ bản ghi hiện tại)
                Exam_QuestionsDAO eqDAO = new Exam_QuestionsDAO();
                if (eqDAO.isQuestionOrderExistsExceptCurrent(examID, questionID, questionOrder, section)) {
                    JOptionPane.showMessageDialog(editview, "Thứ tự câu hỏi " + questionOrder + " đã tồn tại trong section này. Vui lòng chọn một số khác.");
                    return;
                }

                Exam_Questions eq = new Exam_Questions();
                eq.setExamId(examID);
                eq.setQuestionId(questionID);
                eq.setQuestionOrder(questionOrder);

                eqDAO.update(eq); // Cập nhật thứ tự câu hỏi
            }
            
            if (selectedExam != null) {
                String selectedSection = (String) editview.viewmain.sectionCombobox.getSelectedItem();
                int examID = selectedExam.getId();
                editview.viewmain.LoadTableQuestionData(examID, selectedSection);
            }

            JOptionPane.showMessageDialog(editview, "Cập nhật câu hỏi thành công!");
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
