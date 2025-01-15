package main;

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
}
