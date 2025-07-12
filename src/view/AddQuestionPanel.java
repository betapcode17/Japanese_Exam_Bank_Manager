package view;

import javax.swing.*;

import AiService.AIAnswerSuggester;
import controller.AnswersController;
import controller.QuestionsController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionPanel extends JPanel {
    public JTextField txtQuestion;

    public JLabel lblA, lblB, lblC, lblD;
    public JTextField txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    public JCheckBox cbAnswerA, cbAnswerB, cbAnswerC, cbAnswerD;
    private JLabel lblAudioPath, lblImgPath;
    public JButton btnChooseAudio, btnChooseImage, btnSave, btnCancel;
    public JFileChooser fileChooser;
    public JTextField txtQuestionNumber;
    public MainFrame viewmain;
    JTextArea textArea;
    public AddQuestionPanel(MainFrame viewmain) {
    	 this.viewmain = viewmain;
        AddEditQuestionPanel();
    }
	private void AddEditQuestionPanel() {
    	
    	
    	Action action = (Action) new AnswersController(this);
         fileChooser = new JFileChooser();
    	
        setLayout(null);
        setPreferredSize(new Dimension(782, 715));
        setBackground(Color.WHITE);
         
        // Tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 782, 50);
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        JLabel lblHeader = new JLabel("Tạo câu hỏi");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 16));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        add(headerPanel);
         
        
        //Câu hỏi số
        JLabel lblCuS = new JLabel("Câu số :");
        lblCuS.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCuS.setBounds(30, 79, 68, 25);
        add(lblCuS);
        
        txtQuestionNumber = new JTextField();
        txtQuestionNumber.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
        txtQuestionNumber.setBounds(89, 76, 92, 25);
        add(txtQuestionNumber);
        
        
        // Câu hỏi
        JLabel lblQuestion = new JLabel("Nội dung câu hỏi (Tiếng Nhật):");
        lblQuestion.setBounds(461, 79, 300, 25);
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblQuestion);
       
        txtQuestion = new JTextField();
        txtQuestion.setBounds(30, 122, 731, 30);
        
        txtQuestion.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));

        add(txtQuestion);
        
        // Đáp án A
        lblA = new JLabel("Đáp án A:");
        lblA.setBounds(30, 185, 100, 25);
        lblA.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblA);

         
        
        txtAnswerA = new JTextField();
        txtAnswerA.setBounds(110, 185, 300, 25);
        txtAnswerA.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
        add(txtAnswerA);

        cbAnswerA = new JCheckBox("Đáp án đúng");
        cbAnswerA.setBounds(430, 185, 150, 25);
        cbAnswerA.setBackground(Color.WHITE);
        cbAnswerA.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cbAnswerA);

        // Đáp án B
        lblB = new JLabel("Đáp án B:");
        lblB.setBounds(30, 224, 100, 25);
        lblB.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblB);

        txtAnswerB = new JTextField();
        txtAnswerB.setBounds(110, 220, 300, 25);
        txtAnswerB.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
        add(txtAnswerB);

        cbAnswerB = new JCheckBox("Đáp án đúng");
        cbAnswerB.setBounds(430, 224, 126, 25);
        cbAnswerB.setBackground(Color.WHITE);
        cbAnswerB.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cbAnswerB);

        // Đáp án C
        lblC = new JLabel("Đáp án C:");
        lblC.setBounds(30, 263, 100, 25);
        lblC.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblC);

        txtAnswerC = new JTextField();
        txtAnswerC.setBounds(110, 259, 300, 25);
        txtAnswerC.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
        add(txtAnswerC);

        cbAnswerC = new JCheckBox("Đáp án đúng");
        cbAnswerC.setBounds(430, 259, 126, 25);
        cbAnswerC.setBackground(Color.WHITE);
        cbAnswerC.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cbAnswerC);

        // Đáp án D
        lblD = new JLabel("Đáp án D:");
        lblD.setBounds(30, 298, 100, 25);
        lblD.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblD);

        txtAnswerD = new JTextField();
        txtAnswerD.setBounds(110, 294, 300, 25);
        txtAnswerD.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
        add(txtAnswerD);

        cbAnswerD = new JCheckBox("Đáp án đúng");
        cbAnswerD.setBounds(430, 294, 150, 25);
        cbAnswerD.setBackground(Color.WHITE);
        cbAnswerD.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cbAnswerD);

        String[] sections = {"Nghe", "Từ vựng", "Ngữ pháp", "Đọc hiểu"};

        // Âm thanh
        JLabel lblAudio = new JLabel("Thêm âm thanh (nếu có):");
        lblAudio.setBounds(30, 350, 200, 25);
        lblAudio.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblAudio);
        
        lblAudioPath = new JLabel("Chưa chọn file");
        lblAudioPath.setBounds(420, 350, 300, 25);
        lblAudioPath.setFont(new Font("Arial", Font.ITALIC, 12));
        lblAudioPath.setForeground(Color.DARK_GRAY);
        add(lblAudioPath);
        
        btnChooseAudio = new JButton("Chọn file âm thanh");
        btnChooseAudio.setBounds(220, 350, 180, 25);
        btnChooseAudio.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnChooseAudio);
        btnChooseAudio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    
                  
                    String newPath = utils.AudioFile.saveAudio(file);
                    
                    if (newPath != null) {
                       
                        lblAudioPath.setText(newPath);
                        System.out.println(lblAudioPath.getText());
                    } else {
                        lblAudioPath.setText(""); 
                        System.out.println("Không thể lưu file âm thanh.");
                    }
                }
            }
        });
       
        
        // Ảnh
        JLabel lblImage = new JLabel("Thêm ảnh (nếu có):");
        lblImage.setBounds(30, 390, 200, 25);
        lblImage.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lblImage);

        
        lblImgPath = new JLabel("Chưa chọn file");
        lblImgPath.setBounds(420, 390, 300, 25);
        lblImgPath.setFont(new Font("Arial", Font.ITALIC, 12));
        lblImgPath.setForeground(Color.DARK_GRAY);
        add(lblImgPath);
        
        btnChooseImage = new JButton("Chọn file ảnh");
        btnChooseImage.setBounds(220, 390, 180, 25);
        btnChooseImage.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnChooseImage);

        btnChooseImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    
                    // Gọi ImgFile để lưu file vào src/img
                    String newPath = utils.ImgFile.saveImage(file);
                    
                    if (newPath != null) {
                        // Cập nhật lblImgPath với đường dẫn mới
                        lblImgPath.setText(newPath);
                        System.out.println(lblImgPath.getText());
                    } else {
                        lblImgPath.setText(""); // Nếu có lỗi, xóa đường dẫn
                        System.out.println("Không thể lưu file ảnh.");
                    }
                }
            }
        });
        
       
        
        // Nút lưu và hủy
        btnSave = new JButton("Thêm câu hỏi");
        btnSave.setBounds(180, 440, 150, 35);
        btnSave.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnSave);
        btnSave.addActionListener(action);
        
        
        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(350, 440, 150, 35);
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(btnCancel);
        btnCancel.addActionListener(action);
        
        JButton btnGip = new JButton("Gợi ý đáp án bằng AI");
        btnGip.setFont(new Font("Arial", Font.PLAIN, 13));
        btnGip.setBounds(30, 510, 169, 35);
        add(btnGip);
        btnGip.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					List<String> answers = new ArrayList<String>();
					answers.add(txtAnswerA.getText());
					answers.add(txtAnswerB.getText());
					answers.add(txtAnswerC.getText());
					answers.add(txtAnswerD.getText());
                    String suggestion = AIAnswerSuggester.suggestAnswer(txtQuestion.getText(),answers);
                    textArea.setText(suggestion);
                } catch (Exception e2) {
                    textArea.setText("Lỗi khi lấy gợi ý: " + e2.getMessage());
                }
			}
		});
        

       
        textArea = new JTextArea();
        textArea.setBackground(SystemColor.inactiveCaptionBorder);
        textArea.setBounds(30, 580, 742, 128);
        textArea.setLineWrap(true); 
        textArea.setWrapStyleWord(true); 
        textArea.setEditable(false); 
        textArea.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        add(textArea);
        
        
        
    }
    public String getImgPath() {
        return lblImgPath.getText();
    }

    public String getAudioPath() {
        return lblAudioPath.getText();
    }
    public void cleanForm() {
    	txtQuestion.setText("");
		txtAnswerA.setText("");
		txtAnswerB.setText("");
		txtAnswerC.setText("");
		txtAnswerD.setText("");
		txtQuestionNumber.setText("");
	}
}
