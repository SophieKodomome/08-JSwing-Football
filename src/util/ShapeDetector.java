package util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import main.Player;
import main.Side;
import main.SimpleObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;

public class ShapeDetector {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static File uploadFile;
    private static File resultFile;
    private Side teamSide;
    private ArrayList<Player> players;
    private SimpleObject ball;
    private Mat imageSourceMat;

    public ShapeDetector() {
    }

    public ShapeDetector(File file, Side side) {
        setUploadFile(file);
        setTeamSide(side);
        setImageSourceMat();
        setSideMeasure();
        detectShapes();
        saveResult();
        // printImageSize();
    }

    public void setBall(SimpleObject s) {
        ball = s;
    }

    public SimpleObject getBall() {
        return ball;
    }

    public void setTeamSide(Side s) {
        teamSide = s;
    }

    public void setSideMeasure() {
        if (teamSide.getPosition() == "Up") {
            teamSide.setY(0);
            teamSide.setHeight(imageSourceMat.rows() / 2);
        } else {
            teamSide.setY(imageSourceMat.rows() / 2);
            teamSide.setHeight(imageSourceMat.rows());
        }
    }

    public Side getTeamSide() {
        return teamSide;
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
        processMask(blackMask, imageSourceMat, "Ball", new Scalar(0, 0, 0)); // Search the ball

        teamSide.checkBallSide(ball);

        processMask(blueMask, imageSourceMat, "Blue", new Scalar(255, 0, 0)); // Blue
        processMask(redMask, imageSourceMat, "Red", new Scalar(0, 0, 255)); // Red

        Player goalie = new Player();
        Player defender = new Player();
        Player striker = new Player();

        if (players != null && !players.isEmpty()) {
            if (teamSide.getPosition() == "Up") {
                players.sort(Comparator.comparingInt(Player::getY));
            } else {
                players.sort(Comparator.comparingInt(Player::getY).reversed());
            }
            // Sort players by their proximity to the field's Y or height boundary

            Player player = new Player();
            player = players.get(0);

            if (player.getColor() == teamSide.getColor() && player.getY() >= teamSide.getY()
                    && player.getHeight() <= teamSide.getHeight()) {

                goalie = player;
                goalie.setRole("Goalie");
            }
            for (int i = 0; i < players.size(); i++) {
                if (goalie.getColor() == players.get(i).getColor() && goalie.getRole() != players.get(i).getRole()) {
                    defender = players.get(i);
                    defender.setRole("defender");
                    break;
                }
            }
        }
        Player closestPlayer = Player.findClosestPlayer(ball, players);

        if (closestPlayer != null) {
            if (closestPlayer.getColor() != teamSide.getColor() && closestPlayer.getY() >= teamSide.getY()
                    && closestPlayer.getHeight() <= teamSide.getHeight()) {
                striker = closestPlayer;
                striker.setRole("Striker");
                striker.setStatus(" ");

                if (teamSide.getPosition().equals("Up")) {
                    if (striker.getY() + striker.getHeight() < defender.getY()) {
                        striker.setStatus("Offside");
                    }
                } else {
                    if (striker.getY() > defender.getY() + defender.getHeight()) {
                        striker.setStatus("Offside");
                    }
                }
            }
        }

        Imgproc.putText(imageSourceMat, striker.getColor() + striker.getRole() + " " + striker.getStatus(),
                new Point(striker.getX() + striker.getWidth(), striker.getY() + 8 * 9),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);

        Imgproc.putText(imageSourceMat, goalie.getColor() + goalie.getRole(),
                new Point(goalie.getX() + goalie.getWidth(), goalie.getY() + 8 * 9),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);

        Imgproc.putText(imageSourceMat, defender.getColor() + defender.getRole(),
                new Point(defender.getX() + defender.getWidth(), defender.getY() + 8 * 9),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);

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

    private void processMask(Mat mask, Mat imageSourceMat, String label, Scalar color) {
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

                Imgproc.putText(imageSourceMat, "x: " + boundingRect.x + " " + "y: " + boundingRect.y,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8 * 3),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);

                Imgproc.putText(imageSourceMat,
                        "width: " + boundingRect.width + " " + "height: " + boundingRect.height,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8 * 6),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);

                if (label == "Ball") {

                    setBall(
                            new SimpleObject(
                                    boundingRect.x,
                                    boundingRect.y,
                                    boundingRect.width,
                                    boundingRect.height));

                    Imgproc.putText(imageSourceMat, label,
                            new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8),
                            Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);
                } else {
                    Player player = new Player();
                    player.setColor(label);
                    player.setX(boundingRect.x);
                    player.setY(boundingRect.y);
                    player.setHeight(boundingRect.height);
                    player.setWidth(boundingRect.width);

                    if (players == null)
                        players = new ArrayList<>();
                    players.add(player);
                }

                Imgproc.putText(imageSourceMat, label,
                        new Point(boundingRect.x + boundingRect.width, boundingRect.y + 8),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, color, 1);

            }
        }
    }

    public void printImageSize() {
        if (imageSourceMat == null || imageSourceMat.empty()) {
            System.out.println("Image is not loaded or is empty.");
            return;
        }

        int width = imageSourceMat.cols(); // Width of the image
        int height = imageSourceMat.rows(); // Height of the image
        // int channels = imageSourceMat.channels(); // Number of color channels

        System.out.println("Image Dimensions: ");
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        // System.out.println("Channels: " + channels);
    }
}