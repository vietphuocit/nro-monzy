package com.monzy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {

  public static void main(String[] args) {
    String inputDirectory = "x4"; // Đường dẫn đến thư mục chứa các tệp ảnh gốc
    String outputDirectory = "x1"; // Đường dẫn đến thư mục chứa các tệp ảnh sau khi chỉnh kích
    String errorDirectory = "error"; // Đường dẫn đến thư mục chứa các tệp ảnh sau khi chỉnh kích
    // thước
    double scaleFactor = 0.25; // Tỷ lệ giảm kích thước (75%)
    File folder = new File(inputDirectory);
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          String inputFilePath = inputDirectory + File.separator + fileName;
          String outputFilePath = outputDirectory + File.separator + fileName;
          String errorFilePath = errorDirectory + File.separator + fileName;

          try {
            // Đọc tệp ảnh gốc vào BufferedImage
            BufferedImage originalImage = ImageIO.read(new File(inputFilePath));

            // Tính toán kích thước mới
            int newWidth = (int) (originalImage.getWidth() * scaleFactor);
            int newHeight = (int) (originalImage.getHeight() * scaleFactor);

            // Kiểm tra kích thước mới
            if (newWidth <= 0 || newHeight <= 0) {
              System.err.println("Lỗi: Kích thước mới không hợp lệ cho tệp " + fileName);
              // Di chuyển tệp ảnh gốc đến thư mục lỗi
              File errorFolder = new File(errorDirectory);
              if (!errorFolder.exists()) {
                errorFolder.mkdirs();
              }
              ImageIO.write(originalImage, "png", new File(errorFilePath));
              continue;
            }

            // Tạo một BufferedImage mới với kích thước mới
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            // Tạo đối tượng Graphics2D để vẽ ảnh mới
            Graphics2D g2d = resizedImage.createGraphics();

            // Thực hiện vẽ ảnh mới từ ảnh gốc
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // Tạo thư mục đầu ra nếu chưa tồn tại
            File outputFolder = new File(outputDirectory);
            if (!outputFolder.exists()) {
              outputFolder.mkdirs();
            }

            // Ghi ảnh mới ra tệp đích
            ImageIO.write(resizedImage, "png", new File(outputFilePath));
          } catch (IOException ex) {
            System.out.println("Lỗi khi đọc/ghi tệp ảnh: " + ex.getMessage());
          } catch (IllegalArgumentException ex) {
            System.out.println("Lỗi: Kích thước không hợp lệ cho tệp " + fileName);
          }
        }
      }
    } else {
      System.out.println("Không tìm thấy tệp trong thư mục đầu vào.");
    }
  }
}
