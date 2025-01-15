package util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ShapeDetector {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void detectShapes(String filePath) {
        Mat src = Imgcodecs.imread(filePath);

        if (src.empty()) {
            System.out.println("Could not load the image.");
            return;
        }

        // Convert to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // Apply edge detection
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println("Number of contours found: " + contours.size());

        for (MatOfPoint contour : contours) {
            // Approximate the contour to a polygon
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            MatOfPoint2f approx = new MatOfPoint2f();
            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approx, epsilon, true);

            // Classify the shape based on the number of vertices
            int vertices = approx.toArray().length;
            String shape = classifyShape(vertices);

            // Draw the shape and label
            Rect boundingRect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(src, boundingRect, new Scalar(0, 255, 0), 2);
            Imgproc.putText(src, shape, new Point(boundingRect.x, boundingRect.y - 10),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
        }

        // Save the output image
        String outputPath = "../assets/resultat.png"; // Update the path as needed
        boolean isSaved = Imgcodecs.imwrite(outputPath, src);
        if (isSaved) {
            System.out.println("Shapes detected and saved to " + outputPath);
        } else {
            System.out.println("Failed to save the image at " + outputPath);
        }
    }

    private static String classifyShape(int vertices) {
        switch (vertices) {
            case 3:
                return "Triangle";
            case 4:
                return "Rectangle/Square";
            case 5:
                return "Pentagon";
            case 6:
                return "Hexagon";
            default:
                return vertices > 6 ? "Circle" : "Unknown";
        }
    }
}
