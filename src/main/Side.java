package main;

public class Side extends SimpleObject {
    private boolean hasBall;
    private String color;
    private String position;

    // Default constructor
    public Side() {
        super(); // Call SimpleObject's default constructor
    }

    // Parameterized constructor
    public Side(int x, int y, int width, int height, boolean hasBall, String color, String position) {
        super(); // Initialize fields from SimpleObject
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.hasBall = hasBall;
        this.color = color;
        this.position = position;
    }

    // Getter for hasBall
    public boolean isHasBall() {
        return hasBall;
    }

    // Setter for hasBall
    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    // Getter for color
    public String getColor() {
        return color;
    }

    // Setter for color
    public void setColor(String color) {
        this.color = color;
    }

    // Getter for position
    public String getPosition() {
        return position;
    }

    // Setter for position
    public void setPosition(String position) {
        this.position = position;
    }
}
