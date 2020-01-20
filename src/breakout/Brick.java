package breakout;

import javafx.scene.effect.*;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Brick {
    private final int width = 60;
    private final int height = 30;
    private int xValue;
    private int yValue;
    private int hits;
    private ImageView myNode;
    private boolean hasPowerUp;
    private String powerUpType;



    public Brick(int hits, int xValue, int yValue, String imageFile) {
        this.hits = hits;
        this.xValue = xValue;
        this.yValue = yValue;
        this.hasPowerUp = selectHasPowerUp();
        this.powerUpType = assignPowerUp();
        ImageView image = new ImageView(imageFile);
        if(this.hasPowerUp){
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0.4);
           // colorAdjust.setHue(-0.05);
            //colorAdjust.setContrast(0.8);
            image.setEffect(colorAdjust);
        }
        image.setX(xValue);
        image.setY(yValue);
        image.setFitWidth(this.width);
        image.setFitHeight(this.height);
        this.myNode = image;
    }

    //assign random powerup on initialization
    private String assignPowerUp() {
        if (hasPowerUp){
            String [] powerUpArray = {/*"Lives", "PaddleRoids", "BallAcid", */"Alien"};
            Random rand = new Random();
            return powerUpArray[rand.nextInt(powerUpArray.length)];
        }
        else{
            return "None";
        }
    }

    private boolean selectHasPowerUp(){
        boolean [] choiceArray = {true,false,false,false,false,false};
        Random rand = new Random();
        return choiceArray[rand.nextInt(choiceArray.length)];
    }

    public int getHits(){
        return this.hits;
    }

    public void brickHit(){
        this.hits--;
    }

    public String getPowerUpType() {
        return powerUpType;
    }

    //returns the node when adding to the root
    public ImageView getNode() {
        return myNode;
    }

    public boolean isHasPowerUp() {
        return hasPowerUp;
    }
}
