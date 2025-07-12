package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import AiService.AIQuestionService;
import controller.ExamsController;
import controller.QuestionsController;
import dao.Exam_QuestionsDAO;
import dao.ExamsDAO;
import dao.QuestionsDAO;
import model.Answers;
import model.Exam_Questions;
import model.Exams;
import model.Questions;
import service.ExportPDFService;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField txtNumberOfExams;
    public JTextField txtTitle;
    public JComboBox comboBoxLevel;
    public  JTable examTable;
    public JTable questionTable;
    public JComboBox cbExamList;
    public JComboBox examComboBox;
    public JComboBox sectionCombobox;
    public JFileChooser fileChooser;
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainFrame() {
    	
    	
    	//
    	Action exam_action = new ExamsController(this);
    	Action question_action = new QuestionsController(this);
    	fileChooser = new JFileChooser();
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        setTitle("Ứng dụng Quản lý đề thi tiếng Nhật");

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // === MENU BÊN TRÁI ===
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(45, 56, 85));
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Logo
     // Logo (scaled)
        try {
            ImageIcon icon = new ImageIcon("src/icon/ExamManager.jpg");
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // chỉnh kích thước tại đây
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
            menuPanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("Không thể load logo: src/icon/ExamManager.jpg");
        }


        // === BUTTON MENU ===

     // Tạo từng nút riêng biệt
        
        
        
        // Tương tự nút 2
     JButton btnManageExam = new JButton("Quản lý Đề thi");
     btnManageExam.setMaximumSize(new Dimension(200, 50));
     btnManageExam.setAlignmentX(Component.CENTER_ALIGNMENT);
     btnManageExam.setBackground(new Color(45, 56, 85));
     btnManageExam.setForeground(Color.WHITE);
     btnManageExam.setFocusPainted(false);
     btnManageExam.setBorderPainted(false);
     btnManageExam.setHorizontalAlignment(SwingConstants.LEFT);
     btnManageExam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
        
     JButton btnManageQuestion = new JButton("Quản lý Câu hỏi");
     btnManageQuestion.setMaximumSize(new Dimension(200, 50));
     btnManageQuestion.setAlignmentX(Component.CENTER_ALIGNMENT);
     btnManageQuestion.setBackground(new Color(45, 56, 85));
     btnManageQuestion.setForeground(Color.WHITE);
     btnManageQuestion.setFocusPainted(false);
     btnManageQuestion.setBorderPainted(false);
     btnManageQuestion.setHorizontalAlignment(SwingConstants.LEFT);
     btnManageQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
   



     // Nút 3
     JButton btnExportExam = new JButton("Xuất đề thi ra file");
     btnExportExam.setMaximumSize(new Dimension(200, 50));
     btnExportExam.setAlignmentX(Component.CENTER_ALIGNMENT);
     btnExportExam.setBackground(new Color(45, 56, 85));
     btnExportExam.setForeground(Color.WHITE);
     btnExportExam.setFocusPainted(false);
     btnExportExam.setBorderPainted(false);
     btnExportExam.setHorizontalAlignment(SwingConstants.LEFT);
     btnExportExam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
     btnExportExam.setIcon(createScaledIcon("/images/export.png"));

        // Thêm vào menu
        menuPanel.add(btnManageExam); 
        menuPanel.add(btnManageQuestion);
        menuPanel.add(btnExportExam);

        contentPane.add(menuPanel, BorderLayout.WEST);

     // === MAIN PANEL CHÍNH ===
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

   
 //===================================================== ExamManagerPanel ===================================================================
        JPanel examPanel = new JPanel(null); // Layout tự do
        examPanel.setLayout(null);
        examPanel.setBackground(Color.WHITE);
        
       
        
     // === Tiêu đề đỏ trên cùng ===
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(231, 76, 60));
        titlePanel.setBounds(0, 0, 800, 40);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel lblMainTitle = new JLabel("DANH SÁCH ĐỀ THI");
        lblMainTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblMainTitle.setForeground(Color.WHITE);
        titlePanel.add(lblMainTitle);
        examPanel.add(titlePanel);

        JLabel lblTitle = new JLabel("Tiêu đề:");
        lblTitle.setBounds(30, 63, 100, 25);
        examPanel.add(lblTitle);

        txtTitle = new JTextField();
        txtTitle.setBounds(107, 63, 150, 25);
        examPanel.add(txtTitle);

        JLabel lblLevel = new JLabel("Trình độ:");
        lblLevel.setBounds(30, 107, 77, 25);
        examPanel.add(lblLevel);
         comboBoxLevel = new JComboBox<>(new String[] {
                "N1", "N2", "N3","N4","N5"
            });
        comboBoxLevel.setBounds(107, 109, 150, 21);
        examPanel.add(comboBoxLevel);
        
        //
        
        
        // === Buttons ===
        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(exam_action);
        btnAdd.setBounds(320, 60, 100, 30);
        examPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.addActionListener(exam_action);
        btnUpdate.setBounds(320, 100, 100, 30);
        examPanel.add(btnUpdate);

        JButton btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(exam_action);
        btnDelete.setBounds(320, 140, 100, 30);
        examPanel.add(btnDelete);

        // === Label danh sách đề thi ===
        JLabel lblExamList = new JLabel("Danh sách đề thi:");
        lblExamList.setBounds(30, 180, 200, 25);
        examPanel.add(lblExamList);

        // === Table danh sách đề thi ===
     // Trong constructor hoặc method khởi tạo giao diện
        ExamsDAO examsDAO = new ExamsDAO();
        ArrayList<Exams> examList = examsDAO.selectAll();
        String[] examColumns = { "ID", "Tiêu đề", "Trình độ" };
        Object[][] examData = convertExamListToData(examList);

         examTable = new JTable(examData, examColumns);
        JScrollPane scrollExam = new JScrollPane(examTable);
        DefaultTableModel model = new DefaultTableModel(examData, examColumns) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    examTable.setModel(model);
        scrollExam.setBounds(30, 210, 723, 336);
        examPanel.add(scrollExam);
        examTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = examTable.getSelectedRow();
                if (selectedRow != -1) {
                    String title = examTable.getValueAt(selectedRow, 1).toString();
                    String level = examTable.getValueAt(selectedRow, 2).toString();

                    txtTitle.setText(title);
                    comboBoxLevel.setSelectedItem(level);
                }
            }
        });

        
        
        

//===============================================================================================
        
//=============================================== ===============QuestionManagerPanel ======================================================
        JPanel questionManagerPanel = new JPanel(null); // Layout tự do
        questionManagerPanel.setLayout(null);
        questionManagerPanel.setBackground(Color.WHITE);
        
        // === Tiêu đề đỏ trên cùng ===
        JPanel questionTitlePanel = new JPanel();
        questionTitlePanel.setBackground(new Color(231, 76, 60));
        questionTitlePanel.setBounds(0, 0, 800, 40);
        questionTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel lblQuestionTitle = new JLabel("DANH SÁCH CÂU HỎI");
        lblQuestionTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblQuestionTitle.setForeground(Color.WHITE);
        questionTitlePanel.add(lblQuestionTitle);
        questionManagerPanel.add(questionTitlePanel);
        
        // === ComboBox chọn đề thi ===
        JLabel lblSelectExam = new JLabel("Chọn đề thi: ");
        lblSelectExam.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSelectExam.setBounds(30, 60, 82, 25);
        questionManagerPanel.add(lblSelectExam);

        examComboBox = new JComboBox<>();
        examComboBox.setBounds(120, 63, 216, 21);
        questionManagerPanel.add(examComboBox);
      
        for (Exams e : examList) {
            examComboBox.addItem(e); // do Exam.toString() return title nên sẽ hiển thị đúng
        }
        
        
        
        
        // === Label danh sách câu hỏi ===
        JLabel lblQuestionList = new JLabel("Danh sách câu hỏi:");
        lblQuestionList.setBounds(30, 132, 200, 25);
        lblQuestionList.setFont(new Font("Arial", Font.PLAIN, 14));
        questionManagerPanel.add(lblQuestionList);
         
        
        //=== Chọn phần Nghe Từ vựng ngữ pháp đọc hiểu
        JLabel lblChnPhn = new JLabel("Chọn phần: ");
        lblChnPhn.setFont(new Font("Arial", Font.PLAIN, 14));
        lblChnPhn.setBounds(369, 60, 82, 25);
        questionManagerPanel.add(lblChnPhn);
        
        sectionCombobox = new JComboBox<>(new String[] {
        		"Nghe", "Từ vựng", "Ngữ pháp", "Đọc hiểu"
            });
        sectionCombobox.setBounds(461, 63, 140, 21);
        questionManagerPanel.add(sectionCombobox);
        
        
       

        
        
        JButton btnLc = new JButton("Lọc");
        btnLc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Exams selectedExam = (Exams) examComboBox.getSelectedItem();
            	if (selectedExam != null) {
                String selectedSection = (String) sectionCombobox.getSelectedItem();  // Lấy mục đã chọn
                int examID = selectedExam.getId();
                LoadTableQuestionData(examID,selectedSection);
            	}// Gọi hàm lọc với điều kiện
            }
        });

        btnLc.setBounds(632, 58, 100, 30);
        questionManagerPanel.add(btnLc);
        
        
        // === Bảng câu hỏi ===
        String[] questionColumns = {"ID", "Nội dung", "Đúng", "File âm thanh","File hình ảnh"};
        
        Object[][] questionData = {}; // Tạm thời rỗng

        questionTable = new JTable(questionData, questionColumns);
        JScrollPane questionScrollPane = new JScrollPane(questionTable);
        questionScrollPane.setBounds(30, 167, 739, 361);
        questionManagerPanel.add(questionScrollPane);

     // === Nút thêm, cập nhật, xóa câu hỏi ===
        JButton btnAddQuestionManual = new JButton("Thêm thủ công");
        btnAddQuestionManual.setBounds(209, 130, 140, 30); // Di chuyển xuống dưới bảng cho gọn
        questionManagerPanel.add(btnAddQuestionManual);
       
        
        
        
        JButton btnAddQuestionAI = new JButton("Thêm bằng AI");
        btnAddQuestionAI.setBounds(359, 130, 140, 30);
        questionManagerPanel.add(btnAddQuestionAI);
        btnAddQuestionAI.addActionListener(question_action);
        
        btnAddQuestionAI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String imagePath = file.getAbsolutePath();              
                    Exams selectedExam = (Exams) examComboBox.getSelectedItem();
       			    int examID = 0;
                	if (selectedExam != null) {
                    examID = selectedExam.getId();
                	}
                    String section = (String)sectionCombobox.getSelectedItem();

                    try {
                        AIQuestionService aiService = new AIQuestionService();
                        aiService.addQuestionFromImage(imagePath, examID, section);
                        JOptionPane.showMessageDialog(null, "Thêm câu hỏi từ ảnh thành công!");
                        LoadTableQuestionData(examID,section);                                                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Lỗi khi thêm câu hỏi từ ảnh.");
                    }
                }
            }
        });
        JButton btnUpdateQuestion = new JButton("Cập nhật");
        btnUpdateQuestion.setBounds(509, 130, 150, 30);
        questionManagerPanel.add(btnUpdateQuestion);
        btnUpdateQuestion.addActionListener(question_action);
        
        
        JButton btnDeleteQuestion = new JButton("Xóa");
        btnDeleteQuestion.setBounds(669, 130, 100, 30);
        questionManagerPanel.add(btnDeleteQuestion);
        btnDeleteQuestion.addActionListener(question_action);
        
        
        
       
        


        btnAddQuestionManual.addActionListener(e -> {
            String selectedSection = sectionCombobox.getSelectedItem().toString();
            JFrame frame = new JFrame("Tạo câu hỏi");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new AddQuestionPanel(this));
            frame.pack();
            frame.setLocationRelativeTo(btnAddQuestionManual);
            frame.setVisible(true);
        });
        
        btnUpdateQuestion.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một câu hỏi để chỉnh sửa.");
                return;
            }

            // Lấy questionOrder từ cột 0 (Câu số)
            int questionOrder = (int) questionTable.getValueAt(selectedRow, 0);

            // Giả định có examId (cần lấy từ ngữ cảnh, ví dụ: biến toàn cục hoặc tham số)
            Exams selectedExam = (Exams) examComboBox.getSelectedItem();
            String selectedSection = (String) sectionCombobox.getSelectedItem();  // Lấy mục đã chọn
            int examId = selectedExam.getId();
           
            Exam_QuestionsDAO dao = new Exam_QuestionsDAO();

            // Lấy questionID từ questionOrder và examId
            int questionID = dao.getQuestionIDByOrder(examId, questionOrder); // Cần thêm phương thức này vào DAO

            if (questionID == -1) {
                JOptionPane.showMessageDialog(null, "Không thể tìm thấy ID câu hỏi dựa trên thứ tự.");
                return;
            }

            JFrame frame = new JFrame("Chỉnh sửa câu hỏi");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            EditQuestionPanel editPanel = new EditQuestionPanel(this, questionID); // Truyền questionID
            frame.getContentPane().add(editPanel);
            frame.pack();
            frame.setLocationRelativeTo(btnAddQuestionManual);
            frame.setVisible(true);
        });

        
   

         
        
//==============================================================================================================================================
        
        
        
//============================================ ExportExamPanel ===================================
        JPanel exportPanel = new JPanel(null); // Layout tự do
        exportPanel.setBackground(Color.WHITE);

        // === Tiêu đề đỏ trên cùng ===
        JPanel exportTitlePanel = new JPanel();
        exportTitlePanel.setBackground(new Color(231, 76, 60));
        exportTitlePanel.setBounds(0, 0, 800, 40);
        exportTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JLabel lblexportTitle = new JLabel("XUẤT ĐỀ THI");
        lblexportTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblexportTitle.setForeground(Color.WHITE);
        exportTitlePanel.add(lblexportTitle);
        exportPanel.add(exportTitlePanel);

        // === Label "Chọn đề thi" và JComboBox ===
        JLabel lblSelectExam2 = new JLabel("Chọn đề thi:");
        lblSelectExam2.setBounds(30, 60, 100, 25);
        exportPanel.add(lblSelectExam2);
           
        cbExamList = new JComboBox<>();
        cbExamList.setBounds(140, 60, 200, 25);
        exportPanel.add(cbExamList);
        for (Exams e : examList) {
        	cbExamList.addItem(e); // do Exam.toString() return title nên sẽ hiển thị đúng
        }
        
        
        
        
        JLabel lblNhpSLng = new JLabel("Nhập số lượng đề:");
        lblNhpSLng.setBounds(383, 60, 140, 25);
        exportPanel.add(lblNhpSLng);
        
        txtNumberOfExams = new JTextField();
        txtNumberOfExams.setBounds(494, 60, 150, 25);
        exportPanel.add(txtNumberOfExams);

        
        
        // === Checkboxes ===
        JCheckBox cbShuffleQuestions = new JCheckBox("Xáo thứ tự câu hỏi");
        cbShuffleQuestions.setBounds(140, 100, 200, 25);
        cbShuffleQuestions.setBackground(Color.WHITE);
        exportPanel.add(cbShuffleQuestions);

        JCheckBox cbShuffleAnswers = new JCheckBox("Xáo đáp án");
        cbShuffleAnswers.setBounds(140, 130, 200, 25);
        cbShuffleAnswers.setBackground(Color.WHITE);
        exportPanel.add(cbShuffleAnswers);

        JCheckBox cbSeparateAnswerFile = new JCheckBox("Tạo file đáp án riêng");
        cbSeparateAnswerFile.setBounds(140, 160, 200, 25);
        cbSeparateAnswerFile.setBackground(Color.WHITE);
        exportPanel.add(cbSeparateAnswerFile);

        // === Buttons: PDF, DOCX, Back ===
        JButton btnExportPDF = new JButton("Xuất ra PDF");
        btnExportPDF.setBounds(140, 221, 140, 30);
        exportPanel.add(btnExportPDF);

        btnExportPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                  
                	
                	Exams selectedExam = (Exams) cbExamList.getSelectedItem();
                   
                   
                    
                    // Lấy số lượng đề cần xuất
                    int numberOfExams = Integer.parseInt(txtNumberOfExams.getText());

                    // Lấy trạng thái các checkbox
                    //Xáo câu hỏi từng đề
                    boolean shuffleQuestions = cbShuffleQuestions.isSelected();
                    //Xáo
                    boolean shuffleAnswers = cbShuffleAnswers.isSelected();
                    boolean separateAnswerFile = cbSeparateAnswerFile.isSelected();

                    // Gọi hàm expor
                    ExportPDFService export = new ExportPDFService ();
                    export.exportExamAndAnswerPDFs(selectedExam, numberOfExams, shuffleQuestions, shuffleAnswers, separateAnswerFile);
                    JOptionPane.showMessageDialog(null, "Xuất ĐỀ THI THÀNH CÔNG");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Số lượng đề không hợp lệ.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi xuất đề thi.");
                }
            }
        });

        
        
        
        
//==============================================================================================================================================
        // Thêm các panel con vào CardLayout
       
        mainPanel.add(examPanel, "ExamManager");
        
        
        mainPanel.add(questionManagerPanel, "QuestionManager");
        
       
        
      
        
        
        mainPanel.add(exportPanel, "ExportExam");
        
     
        // Thêm vào contentPane
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // === GẮN SỰ KIỆN CHUYỂN PANEL ===
        btnManageExam.addActionListener(e -> cardLayout.show(mainPanel, "ExamManager"));
        btnManageQuestion.addActionListener(e -> cardLayout.show(mainPanel, "QuestionManager"));
        btnExportExam.addActionListener(e -> cardLayout.show(mainPanel, "ExportExam"));
        
        
        
        
        
      

    }

    private Object[][] convertExamListToData(ArrayList<Exams> examList) {
        Object[][] data = new Object[examList.size()][3];
        for (int i = 0; i < examList.size(); i++) {
            Exams e = examList.get(i);
            data[i][0] = e.getId();
            data[i][1] = e.getTitle();
            data[i][2] = e.getLevel();
        }
        return data;
    }


	private ImageIcon createScaledIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Không thể load icon: " + path);
            return null;
        }
    }
	
	public void cleanForm() {
		txtTitle.setText("");
		comboBoxLevel.setSelectedIndex(0); // hoặc chọn mặc định là N1/N5 tùy bạn
		examTable.clearSelection();
	}
	
	public void LoadTableQuestionData(int examID, String sectionFilter) {
	    Exam_QuestionsDAO dao = new Exam_QuestionsDAO();
	    List<Questions> list = dao.selectByExamId(examID);

	    // Lọc danh sách theo section
	    List<Questions> filteredList = new ArrayList<>();
	    for (Questions q : list) {
	        if (q.getSection().equalsIgnoreCase(sectionFilter)) {
	            filteredList.add(q);
	        }
	    }

	    // Cập nhật cột tiêu đề, thay "ID" bằng "Câu số"
	    String[] columnName = {"Câu số", "Nội dung", "Đúng", "File âm thanh", "File hình ảnh"};
	    Object[][] data = new Object[filteredList.size()][5];

	    for (int i = 0; i < filteredList.size(); i++) {
	        Questions q = filteredList.get(i);
	        // Lấy questionOrder từ Exam_QuestionsDAO
	        int questionOrder = dao.getQuestionOrder(examID, q.getId());
	        if (questionOrder == -1) {
	            questionOrder = i + 1; // Giá trị mặc định nếu không tìm thấy
	        }
	        data[i][0] = questionOrder; // Hiển thị questionOrder thay vì ID
	        data[i][1] = q.getContent();

	        String correctAnswer = "";
	        for (Answers a : q.getAnswers()) {
	            if (a.isCorrect()) {
	                correctAnswer = a.getContent();
	                break;
	            }
	        }

	        data[i][2] = correctAnswer;
	        data[i][3] = q.getAudioPath();
	        data[i][4] = q.getImgPath();
	    }

	    DefaultTableModel model = new DefaultTableModel(data, columnName) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };

	    questionTable.setModel(model);
	}
	
	
	
	public void loadTableData() {
	    ExamsDAO dao = new ExamsDAO();
	    ArrayList<Exams> list = dao.selectAll();

	    String[] columnNames = {"ID", "Tiêu đề", "Trình độ"};
	    Object[][] data = new Object[list.size()][3];
	    for (int i = 0; i < list.size(); i++) {
	        Exams e = list.get(i);
	        data[i][0] = e.getId();
	        data[i][1] = e.getTitle();
	        data[i][2] = e.getLevel();
	    }
	    
	    DefaultTableModel model = new DefaultTableModel(data, columnNames) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
	    examTable.setModel(model);
	}
	public void loadExamComboBox() {  
	    examComboBox.removeAllItems();
	
	    ExamsDAO examDAO = new ExamsDAO(); 
	    List<Exams> examList = examDAO.selectAll(); 

	 
	    for (Exams e : examList) {
	        examComboBox.addItem(e); 
	    }
	}
}
