package utils;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImgFile {
    private static final String DESTINATION_DIR = "src/img/";

    public static String saveImage(File sourceFile) {
        try {
            
            File destinationDirectory = new File(DESTINATION_DIR);
            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }

          
            String fileName = sourceFile.getName();
            File destinationFile = new File(DESTINATION_DIR + fileName);

           
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

          
            return destinationFile.getAbsolutePath();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi sao chép file: " + ex.getMessage());
            return null;
        }
    }
}