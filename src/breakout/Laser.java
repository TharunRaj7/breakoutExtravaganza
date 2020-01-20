package breakout;

import javafx.scene.image.ImageView;

public class Laser {
    private int xValue;
    private int yValue;
    private int mySpeed;
    private ImageView myNode;

    public Laser (int xValue, int yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.mySpeed = 200;
        this.myNode = new ImageView("laserpower.gif");
    }

    public void updateYValue(){
        yValue += 10;
    }
    public int getyValue(){
        return this.yValue;
    }
}
