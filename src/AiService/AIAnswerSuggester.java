package AiService;

import java.util.List;

import service.GeminiConnect;

public class AIAnswerSuggester {
    public static String suggestAnswer(String questionContent, List<String> answers) {
        // Validate the number of answers
        if (answers == null || answers.size() != 4) {
            return "Error: Vui lòng cung cấp đúng 4 đáp án.";
        }

        // Prompt in Vietnamese, including the question and answer options
        String prompt = """
                Bạn là một chuyên gia tiếng Nhật. Dựa trên câu hỏi tiếng Nhật và các đáp án sau đây, hãy cung cấp đáp án đúng và giải thích bằng tiếng Việt.

                Yêu cầu:
                - Dòng đầu tiên: Đáp án đúng (ví dụ: '選択肢1' nếu đó là đáp án đúng).
                - Dòng thứ hai: Giải thích chi tiết bằng tiếng Việt tại sao đáp án đó đúng.
                - Không thêm mô tả hoặc chú thích nào khác ngoài hai dòng trên.

                Ví dụ định dạng mong muốn:
                選択肢1
                Đáp án đúng là 選択肢1 vì đây là cách đọc chuẩn của kanji trong ngữ cảnh này.

                Câu hỏi: """ + questionContent + "\n" +
                "Đáp án:\n" +
                "1. " + answers.get(0) + "\n" +
                "2. " + answers.get(1) + "\n" +
                "3. " + answers.get(2) + "\n" +
                "4. " + answers.get(3);

        String aiSuggestAnswer = GeminiConnect.generateSuggestAnswer(prompt);
        return aiSuggestAnswer;
    }
}

