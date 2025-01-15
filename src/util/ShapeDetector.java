package util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import main.Player;
import main.Side;
import main.SimpleObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShapeDetector {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static File uploadFile;
    private static File resultFile;
    private Side side;
    private ArrayList<Player> players1;
    private ArrayList<Player> players2;
    private SimpleObject ball;
    private Mat imageSourceMat;

    public ShapeDetector() {
    }

    public ShapeDetector(File file, Side side) {
        setUploadFile(file);
        setSide(side);
        setImageSourceMat();
        detectShapes();
        saveResult();
    }

    public void setSide(Side s) {
        side = s;
    }

    public Side getSide(Side s) {
        return side;
    }

    public void setUploadFile(File f) {
        if (f.exists()) {
            uploadFile = f;
        } else {
            System.out.println("uploaded File does not exist");
        }
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setResultFile(File f) {
        if (f.exists()) {
            resultFile = f;
        } else {
            System.out.println("result File does not exist");
        }
    }

    public File getResultFile() {
        return resultFile;
    }

    public void setImageSourceMat() {
        imageSourceMat = Imgcodecs.imread(getUploadFile().getAbsolutePath());
    }

    public void detectShapes() {

        if (imageSourceMat.empty()) {
            System.out.println("Could not load the image.");
            return;
        }

        // Convert to HSV for color filtering
        Mat hsv = new Mat();
        Imgproc.cvtColor(imageSourceMat, hsv, Imgproc.COLOR_BGR2HSV);

        // Create masks for blue, red, and black
        Mat blueMask = new Mat();
        Mat redMask = new Mat();
        Mat blackMask = new Mat();

        // Blue range
        Core.inRange(hsv, new Scalar(100, 150, 50), new Scalar(140, 255, 255), blueMask);

        // Red range (two ranges to handle hue wrapping)
        Mat redMask1 = new Mat();
        Mat redMask2 = new Mat();
        Core.inRange(hsv, new Scalar(0, 150, 50), new Scalar(10, 255, 255), redMask1);
        Core.inRange(hsv, new Scalar(170, 150, 50), new Scalar(180, 255, 255), redMask2);
        Core.add(redMask1, redMask2, redMask);

        // Black range (low saturation and value)
        Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(180, 255, 50), blackMask);

        // Process each mask
        processMask(blackMask, imageSourceMat, "Black Ball", new Scalar(0, 0, 0)); // Black
        processMask(blueMask, imageSourceMat, "Blue Player", new Scalar(255, 0, 0)); // Blue
        processMask(redMask, imageSourceMat, "Red Player", new Scalar(0, 0, 255)); // Red
    }

    public void saveResult() {
        // Save the output image
        String outputPath = "../assets/resultat.png"; // Update the path as needed

        boolean isSaved = Imgcodecs.imwrite(outputPath, imageSourceMat);
        if (isSaved) {
            System.out.println("Shapes detected and saved to " + outputPath);
        } else {
            System.out.println("Failed to save the image at " + outputPath);
        }
        setResultFile(new File(outputPath));
    }

    private static void processMask(Mat mask, Mat imageSourceMat, String label, Scalar color) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println("Number of " + label + "s found: " + contours.size());

        for (MatOfPoint contour : contours) {
            // Approximate the contour to a polygon
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            MatOfPoint2f approx = new MatOfPoint2f();
            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approx, epsilon, true);

            // Check if the contour is roughly circular
            double area = Imgproc.contourArea(contour);
            Rect boundingRect = Imgproc.boundingRect(contour);
            double radius = boundingRect.width / 2.0;
            boolean isCircle = Math.abs(1 - (boundingRect.width / (double) boundingRect.height)) <= 0.2 &&
                    Math.abs(1 - (area / (Math.PI * Math.pow(radius, 2)))) <= 0.2;

            if (isCircle) {
                Imgproc.rectangle(imageSourceMat, boundingRect, color, 2);

                Imgproc.putText(imageSourceMat, label,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);

                Imgproc.putText(imageSourceMat, "x: " + boundingRect.x + " " + "y: " + boundingRect.y,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8*3),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);

                Imgproc.putText(imageSourceMat, "width: " + boundingRect.width + " " + "height: " + boundingRect.height,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8*6),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);
            }
        }
    }
}
