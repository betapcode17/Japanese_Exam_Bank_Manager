package service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dao.Exam_QuestionsDAO;
import model.Answers;
import model.Exams;
import model.Questions;

public class ExportPDFService {
    private static final String OUTPUT_DIR = "src/DeThiJLPT/";

    public void exportExamAndAnswerPDFs(Exams exam, int numberOfExams, boolean shuffleQuestions, 
                                       boolean shuffleAnswers, boolean separateAnswerFile) {
        try {
            String examTitle = "Exam_" + exam.getId();
            File baseDir = new File(OUTPUT_DIR);
            File examFolder = new File(baseDir, examTitle);
            File questionFolder = new File(examFolder, "CauHoi");
            File answerFolder = new File(examFolder, "DapAn");

            questionFolder.mkdirs();
            if (separateAnswerFile) answerFolder.mkdirs();

            Exam_QuestionsDAO dao = Exam_QuestionsDAO.getInstance();
            List<Questions> originalQuestions = dao.selectByExamId(exam.getId());
            if (originalQuestions == null || originalQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy câu hỏi cho đề thi: " + examTitle);
                return;
            }

            for (int i = 1; i <= numberOfExams; i++) {
                List<Questions> questionsForThisExam = new ArrayList<>();
                for (Questions q : originalQuestions) {
                    Questions tempQ = new Questions();
                    tempQ.setId(q.getId());
                    tempQ.setContent(q.getContent());
                    tempQ.setAudioPath(q.getAudioPath());
                    tempQ.setImgPath(q.getImgPath());
                    tempQ.setSection(q.getSection());

                    List<Answers> tempAnswers = new ArrayList<>();
                    for (Answers a : q.getAnswers()) {
                        Answers tempA = new Answers();
                        tempA.setId(a.getId());
                        tempA.setContent(a.getContent());
                        tempA.setCorrect(a.isCorrect());
                        tempAnswers.add(tempA);
                    }
                    tempQ.setAnswers(tempAnswers);
                    questionsForThisExam.add(tempQ);
                }

                // Nhóm câu hỏi theo section trước khi xáo trộn
                Map<String, List<Questions>> questionsBySection = new HashMap<>();
                for (Questions q : questionsForThisExam) {
                    String section = q.getSection();
                    questionsBySection.computeIfAbsent(section, k -> new ArrayList<>()).add(q);
                }

                // Xáo trộn câu hỏi trong từng section nếu được chọn
                if (shuffleQuestions) {
                    for (List<Questions> sectionQuestions : questionsBySection.values()) {
                        Collections.shuffle(sectionQuestions);
                    }
                }

                // Xáo trộn đáp án trong từng câu hỏi nếu được chọn
                if (shuffleAnswers) {
                    for (List<Questions> sectionQuestions : questionsBySection.values()) {
                        for (Questions q : sectionQuestions) {
                            List<Answers> shuffledAnswers = new ArrayList<>(q.getAnswers());
                            Collections.shuffle(shuffledAnswers);
                            q.setAnswers(shuffledAnswers);
                        }
                    }
                }

                // Tạo danh sách câu hỏi đã sắp xếp theo section
                List<Questions> sortedQuestions = new ArrayList<>();
                for (String section : questionsBySection.keySet()) {
                    sortedQuestions.addAll(questionsBySection.get(section));
                }

                // Xuất đề thi
                String questionFilePath = new File(questionFolder, examTitle + "_CauHoi_" + i + ".pdf").getAbsolutePath();
                exportExamToPDF(exam, questionsBySection, questionFilePath);

                // Mở file đề thi
                openFile(questionFilePath);

                // Xuất đáp án riêng nếu được chọn
                if (separateAnswerFile) {
                    String answerFilePath = new File(answerFolder, examTitle + "_DapAn_" + i + ".pdf").getAbsolutePath();
                    exportAnswerKeyToPDF(exam, sortedQuestions, originalQuestions, answerFilePath);

                    // Mở file đáp án
                    openFile(answerFilePath);
                }
            }

            JOptionPane.showMessageDialog(null, "Xuất " + numberOfExams + " đề PDF thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất file PDF: " + e.getMessage());
        }
    }

    private void exportExamToPDF(Exams exam, Map<String, List<Questions>> questionsBySection, String filePath) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        doc.open();

        // Fonts
        Font vietnameseFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12);
        Font japaneseFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\BIZ-UDMinchoM.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12);
        Font sectionFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 14, Font.BOLD);

        // Tiêu đề đề thi
        Paragraph title = new Paragraph("Đề Thi: " + exam.getTitle() + " (" + exam.getId() + ")", vietnameseFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        doc.add(title);

        // Đếm câu hỏi toàn cục
        int globalIndex = 1;

        // Xuất từng section
        for (String section : questionsBySection.keySet()) {
            // Tiêu đề section
            Paragraph sectionTitle = new Paragraph("Phần: " + section, sectionFont);
            sectionTitle.setSpacingBefore(15);
            sectionTitle.setSpacingAfter(10);
            doc.add(sectionTitle);

            // Danh sách câu hỏi trong section
            List<Questions> questions = questionsBySection.get(section);
            for (Questions q : questions) {
                Font qFont = getFontForText(q.getContent(), vietnameseFont, japaneseFont);
                doc.add(new Paragraph(globalIndex + ". " + q.getContent(), qFont));

                List<Answers> answers = q.getAnswers();
                for (int i = 0; i < answers.size(); i++) {
                    String ansText = (char) ('A' + i) + ". " + answers.get(i).getContent();
                    Font aFont = getFontForText(ansText, vietnameseFont, japaneseFont);
                    doc.add(new Paragraph("   " + ansText, aFont));
                }

                doc.add(new Paragraph(" "));
                globalIndex++;
            }
        }

        doc.close();
    }

    private void exportAnswerKeyToPDF(Exams exam, List<Questions> shuffledQuestions, List<Questions> originalQuestions, String filePath) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        doc.open();

        Font vietnameseFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12);
        Font japaneseFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\BIZ-UDMinchoM.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12);
        Font sectionFont = new Font(BaseFont.createFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 14, Font.BOLD);

        // Tiêu đề đáp án
        Paragraph title = new Paragraph("Đáp Án Thi: " + exam.getTitle() + " (" + exam.getId() + ")", vietnameseFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        doc.add(title);

        // Tạo ánh xạ từ ID câu hỏi sang đáp án đúng
        List<String> correctAnswers = new ArrayList<>();
        for (Questions q : originalQuestions) {
            List<Answers> answers = q.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                if (answers.get(i).isCorrect()) {
                    correctAnswers.add((char) ('A' + i) + "");
                    break;
                }
            }
        }

        // Nhóm câu hỏi theo section
        Map<String, List<Questions>> questionsBySection = new HashMap<>();
        for (Questions q : shuffledQuestions) {
            String section = q.getSection();
            questionsBySection.computeIfAbsent(section, k -> new ArrayList<>()).add(q);
        }

        // Đếm câu hỏi toàn cục
        int globalIndex = 1;

        // Xuất đáp án theo từng section
        for (String section : questionsBySection.keySet()) {
            // Tiêu đề section
            Paragraph sectionTitle = new Paragraph("Phần: " + section, sectionFont);
            sectionTitle.setSpacingBefore(15);
            sectionTitle.setSpacingAfter(10);
            doc.add(sectionTitle);

            // Danh sách đáp án trong section
            List<Questions> sectionQuestions = questionsBySection.get(section);
            for (Questions q : sectionQuestions) {
                int originalIndex = originalQuestions.indexOf(q);
                if (originalIndex == -1) {
                    for (int j = 0; j < originalQuestions.size(); j++) {
                        if (originalQuestions.get(j).getId() == q.getId()) {
                            originalIndex = j;
                            break;
                        }
                    }
                }
                String ansText = globalIndex + ". " + correctAnswers.get(originalIndex);
                Font font = getFontForText(ansText, vietnameseFont, japaneseFont);
                doc.add(new Paragraph(ansText, font));
                globalIndex++;
            }
        }

        doc.close();
    }

    private Font getFontForText(String text, Font vietFont, Font japanFont) {
        return text.matches(".*[\u3040-\u30FF\u4E00-\u9FFF]+.*") ? japanFont : vietFont;
    }

    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "File không tồn tại: " + filePath);
                return;
            }

            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd", "/c", "start", "", file.getAbsolutePath());
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", file.getAbsolutePath());
            } else {
                pb = new ProcessBuilder("xdg-open", file.getAbsolutePath());
            }

            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể mở file: " + filePath + "\nLỗi: " + e.getMessage());
        }
    }
}