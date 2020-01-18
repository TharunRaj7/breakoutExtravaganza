package breakout;

import javafx.scene.image.ImageView;

public class Brick {
    private final int width = 60;
    private final int height = 30;
    private int xValue;
    private int yValue;
    private int hits;
    private ImageView myNode;
    private boolean hasPowerUp;
    private String powerUpType; // only initialized and used if hasPowerUp is set to true

    public Brick(int hits, int xValue, int yValue, String imageFile) {
        this.hits = hits;
        this.xValue = xValue;
        this.yValue = yValue;
        this.hasPowerUp = hasPowerUp;
        ImageView image = new ImageView(imageFile);
        image.setX(xValue);
        image.setY(yValue);
        image.setFitWidth(this.width);
        image.setFitHeight(this.height);
        this.myNode = image;
        assignPowerUp();
    }

    //assign random powerup on initialization
    private void assignPowerUp() {
        if (hasPowerUp){
            //carry out logic
        }
        return;
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
