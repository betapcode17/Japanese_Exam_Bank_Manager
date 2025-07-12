package AiService;

import model.Questions;
import model.Answers;

import java.util.ArrayList;
import java.util.List;

public class QuestionTextParser {

	public static List<Questions> parseQuestions(String text) {
	    List<Questions> questionList = new ArrayList<>();
	    String[] lines = text.split("\\R"); // Tách từng dòng

	    List<String> currentBlock = new ArrayList<>();

	    for (String line : lines) {
	        if (line.contains("（　）")) {
	            if (!currentBlock.isEmpty()) {
	                // Xử lý block cũ
	                if (currentBlock.size() >= 6) {
	                    Questions q = new Questions();
	                    q.setContent(currentBlock.get(0).trim());
	                    q.setAudioPath("Chưa chọn file");
	                    q.setImgPath("Chưa chọn File");
	                    questionList.add(q);
	                }
	                currentBlock.clear();
	            }
	        }
	        currentBlock.add(line);
	    }

	    // Xử lý block cuối cùng nếu còn
	    if (!currentBlock.isEmpty() && currentBlock.size() >= 6) {
	        Questions q = new Questions();
	        q.setContent(currentBlock.get(0).trim());
	        q.setAudioPath("Chưa chọn file");
	        q.setImgPath("Chưa chọn File");
	        questionList.add(q);
	    }

	    return questionList;
	}


    public static List<Answers> parseAllAnswers(String text, List<Integer> insertedQuestionIds) {
        List<Answers> allAnswers = new ArrayList<>();
        String[] blocks = text.split("(?=\\n*[^\\n]+\\（　\\）[^\\n]*\\n)");
        int index = 0;

        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty()) continue;

            String[] lines = block.split("\\n");
            if (lines.length < 6) continue;

            int questionId = insertedQuestionIds.get(index++);
            int correctIndex;
            try {
                correctIndex = Integer.parseInt(lines[5].trim()) - 1; // chuyển 1-based về 0-based
            } catch (NumberFormatException e) {
                continue;
            }

            for (int i = 1; i <= 4; i++) {
                String answerText = lines[i].trim();
                boolean isCorrect = (i - 1 == correctIndex);
                allAnswers.add(new Answers(0, questionId, answerText, isCorrect));
            }
        }

        return allAnswers;
    }
}
