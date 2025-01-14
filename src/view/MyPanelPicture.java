package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyPanelPicture extends JPanel {
    private BufferedImage image;

    public MyPanelPicture() {
    }

    public MyPanelPicture(File f) {
        this.setLayout(new FlowLayout());

        try {
            image = ImageIO.read(f);
            resizeIfNeeded();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(f.getAbsolutePath());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    public void resizeImage(int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        image = resizedImage;
    }

    /**
     * Checks if the image is larger than the screen dimensions.
     * If it is, resizes the image to fit the screen while maintaining aspect ratio.
     */
    public void resizeIfNeeded() {
        if (image == null) return;

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Get image dimensions
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // Check if resizing is needed
        if (imageWidth > screenWidth || imageHeight > screenHeight) {
            // Calculate the scaling factor to fit the screen
            double widthScale = (double) screenWidth / imageWidth;
            double heightScale = (double) screenHeight / imageHeight;
            double scale = Math.min(widthScale, heightScale); // Maintain aspect ratio

            // Calculate new dimensions
            int newWidth = (int) (imageWidth * scale);
            int newHeight = (int) (imageHeight * scale);

            // Resize the image
            resizeImage(newWidth, newHeight);
        }
    }
}
