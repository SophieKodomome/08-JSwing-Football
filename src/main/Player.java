package main;

import java.util.ArrayList;

public class Player extends SimpleObject {
    private String color;
    private boolean hasBall;
    private String role;
    private String status;

    // Default constructor
    public Player() {
        super(); // Call SimpleObject's default constructor
    }

    // Parameterized constructor
    public Player(int x, int y, int width, int height, String color, boolean hasBall, String role, String status) {
        super(); // Initialize fields from SimpleObject
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.color = color;
        this.hasBall = hasBall;
        this.role = role;
        this.status = status;
    }

    // Getter for color
    public String getColor() {
        return color;
    }

    // Setter for color
    public void setColor(String color) {
        this.color = color;
    }

    // Getter for hasBall
    public boolean isHasBall() {
        return hasBall;
    }

    // Setter for hasBall
    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Setter for role
    public void setRole(String role) {
        this.role = role;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    
    public static Player findClosestPlayer(SimpleObject ball, ArrayList<Player> players) {
        if (ball == null || players == null || players.isEmpty()) {
            return null;
        }

        double ballCenterX = ball.getX() + ball.getWidth() / 2.0;
        double ballCenterY = ball.getY() + ball.getHeight() / 2.0;

        Player closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (Player player : players) {
            double playerCenterX = player.getX() + player.getWidth() / 2.0;
            double playerCenterY = player.getY() + player.getHeight() / 2.0;

            double distance = Math
                    .sqrt(Math.pow(playerCenterX - ballCenterX, 2) + Math.pow(playerCenterY - ballCenterY, 2));

            if (distance < minDistance) {
                minDistance = distance;
                player.setHasBall(true);
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }
}
