package util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import java.util.ArrayList;
import java.util.List;

public class ColorDetector {
    static {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private Mat image;

    public ColorDetector(String imagePath) {
        // Load the image
        image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            throw new IllegalArgumentException("Image could not be loaded: " + imagePath);
        }
    }

    public void detectColors() {
        // Convert the image to HSV color space
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

        // Define color ranges for red
        Scalar lowerRed1 = new Scalar(0, 120, 70);
        Scalar upperRed1 = new Scalar(10, 255, 255);
        Scalar lowerRed2 = new Scalar(170, 120, 70);
        Scalar upperRed2 = new Scalar(180, 255, 255);

        // Define color range for blue
        Scalar lowerBlue = new Scalar(100, 150, 50);
        Scalar upperBlue = new Scalar(140, 255, 255);

        // Create masks for red and blue
        Mat redMask1 = new Mat();
        Mat redMask2 = new Mat();
        Mat blueMask = new Mat();

        Core.inRange(hsvImage, lowerRed1, upperRed1, redMask1);
        Core.inRange(hsvImage, lowerRed2, upperRed2, redMask2);
        Core.inRange(hsvImage, lowerBlue, upperBlue, blueMask);

        // Combine red masks
        Mat redMask = new Mat();
        Core.add(redMask1, redMask2, redMask);

        // Detect and draw contours for red and blue
        drawContours(redMask, new Scalar(0, 0, 255)); // Red color for red detection
        drawContours(blueMask, new Scalar(255, 0, 0)); // Blue color for blue detection

        // Display the result
        HighGui.imshow("Detected Colors", image);
        HighGui.waitKey(0);
    }

    private void drawContours(Mat mask, Scalar color) {
        // Find contours in the mask
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Draw bounding rectangles for each contour
        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(image, boundingRect.tl(), boundingRect.br(), color, 2);
        }
    }
}
