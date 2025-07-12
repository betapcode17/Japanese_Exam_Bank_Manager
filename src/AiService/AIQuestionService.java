package AiService;

import dao.*;
import model.*;
import service.GeminiConnect;

import java.util.ArrayList;
import java.util.List;

public class AIQuestionService {
    private QuestionsDAO questionsDAO = new QuestionsDAO();
    private AnswersDAO answersDAO = new AnswersDAO();
    private Exam_QuestionsDAO examQuestionsDAO = new Exam_QuestionsDAO();

    public void addQuestionFromImage(String imagePath, int examId, String section) {
    	String prompt = """
    			Hãy đọc kỹ nội dung trong ảnh và trích xuất thành câu hỏi và các đáp án trắc nghiệm.

    			Yêu cầu:
    			- Giữ nguyên ngôn ngữ gốc trong ảnh (ví dụ: tiếng Nhật).
    			- Xuất ra đúng định dạng sau:
    			  + Dòng đầu tiên là nội dung câu hỏi (có chứa dấu ngoặc: （　）).
    			  + Mỗi dòng tiếp theo là một đáp án, không cần thêm A, B, C, D.
    			  + Dòng cuối cùng là một con số (từ 1 đến 4) tương ứng với đáp án đúng.
    			  + Không thêm mô tả hoặc chú thích nào khác.

    			Ví dụ định dạng mong muốn:
    			クイズの答えが分からないので、何か（　）が欲しい。
    			ヒント  
    			マナー  
    			アドレス  
    			チェック  
    			1
    			""";
    	String aiText = GeminiConnect.generateAnswerFromImage( imagePath,prompt);

    	// Parse danh sách câu hỏi
    	List<Questions> questionList = QuestionTextParser.parseQuestions(aiText);

    	// Lưu từng câu hỏi và lấy ID
    	List<Integer> insertedQuestionIds = new ArrayList<>();
    	for (Questions q : questionList) {
    	    q.setSection(section);
    	    int questionId = questionsDAO.insert(q);
    	    insertedQuestionIds.add(questionId);

    	    Exam_Questions examQ = new Exam_Questions();
    	    examQ.setExamId(examId);
    	    examQ.setQuestionId(questionId);
    	    examQ.setQuestionOrder(examQuestionsDAO.getMaxQuestionOrderByExam(examId,section) + 1);
    	    examQuestionsDAO.insert(examQ);
    	}

    	// Parse và lưu danh sách đáp án
    	List<Answers> answerList = QuestionTextParser.parseAllAnswers(aiText, insertedQuestionIds);
    	for (Answers ans : answerList) {
    	    answersDAO.insert(ans);
    	}
    }
}
