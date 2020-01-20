package breakout;

import javafx.scene.image.ImageView;

public class Alien {
    private double xValue;
    private double yValue;
    private double mySpeed;
    private ImageView myNode;



    public Alien (double xValue, double yValue, double speed) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.mySpeed = speed;
        this.myNode = new ImageView("alien.gif");
        this.myNode.setY(yValue); this.myNode.setX(xValue);
        this.myNode.setFitWidth(25); this.myNode.setFitHeight(25);
    }

    public void updateYValue(int yValue){
        this.yValue = yValue;
    }
    public ImageView getNode (){
        return this.myNode;
    }
    public double getYValue(){
        return this.yValue;
    }
    public void setyValue(double yValue) {
        this.yValue = yValue;
        this.myNode.setY(yValue);
    }
    public double getXValue(){
        return this.xValue;
    }
    public double getSpeed() {return this.mySpeed;}
}
