package main;

public class SimpleObject {
    private int x;
    private int y;
    private int width;
    private int height;

    public SimpleObject(){}

    public SimpleObject(int x,int y,int w,int h){
        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
    }
    // Getter for x
    public int getX() {
        return x;
    }

    // Setter for x
    public void setX(int x) {
        this.x = x;
    }

    // Getter for y
    public int getY() {
        return y;
    }

    // Setter for y
    public void setY(int y) {
        this.y = y;
    }

    // Getter for width
    public int getWidth() {
        return width;
    }

    // Setter for width
    public void setWidth(int width) {
        this.width = width;
    }

    // Getter for height
    public int getHeight() {
        return height;
    }

    // Setter for height
    public void setHeight(int height) {
        this.height = height;
    }
}
