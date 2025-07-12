package model;

import java.util.List;

public class Questions {
	    private int id;
	    private String content;
	    private String audioPath;
	    private String imgPath;
	    private String section;
	    private List<Answers> answers;
		
		public String getImgPath() {
			return imgPath;
		}
		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
		public String getSection() {
			return section;
		}
		public void setSection(String sectionID) {
			this.section = sectionID;
		}
		public Questions(int id, String content, String audioPath, String imgPath, String sectionID,
				List<Answers> answers) {
			super();
			this.id = id;
			this.content = content;
			this.audioPath = audioPath;
			this.imgPath = imgPath;
			this.section = sectionID;
			this.answers = answers;
		}
		public Questions() {}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getAudioPath() {
			return audioPath;
		}
		public void setAudioPath(String audioPath) {
			this.audioPath = audioPath;
		}
		public List<Answers> getAnswers() {
			return answers;
		}
		public void setAnswers(List<Answers> answers) {
			this.answers = answers;
		}
		
		public String getCorrectAnswer(List<Answers> answers) {
			// TODO Auto-generated method stub
			String correctAns ="";
			for (Answers ans : answers) {
				if(ans.isCorrect()) {
					 correctAns =  ans.getContent();
				}
			}
			return correctAns;
			
		}
		
	    
}
