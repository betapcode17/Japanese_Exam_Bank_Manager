package model;

import java.util.List;

public class Exams {
	  private int id;
	  private String title;
	  private String level;
	  private List<Exam_Questions> examQuestions;
	  public Exams(int id, String title, String level, List<Exam_Questions> examQuestions) {
		super();
		this.id = id;
		this.title = title;
		this.level = level;
		this.examQuestions = examQuestions;
	  }
	  public Exams(String title, String level) {
		super();
		this.title = title;
		this.level = level;
	}
	public Exams() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public List<Exam_Questions> getExamQuestions() {
		return examQuestions;
	}
	public void setExamQuestions(List<Exam_Questions> examQuestions) {
		this.examQuestions = examQuestions;
	}
	 @Override
	    public String toString() {
	        return title; // Giúp JComboBox hiển thị tên đề
	    }  
}
