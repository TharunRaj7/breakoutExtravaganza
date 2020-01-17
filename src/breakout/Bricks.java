package breakout;

import javafx.scene.image.ImageView;

public class Bricks {
    private final int width = 40;
    public final int height = 20;
    private int hits;
    private ImageView node;
    private boolean hasPowerUp;
    private String powerUpType; // only initialized and used if hasPowerUp is set to true

    public Bricks(int hits, boolean hasPowerUp, String imageFile) {
        this.hits = hits;
        this.hasPowerUp = hasPowerUp;
        ImageView image = new ImageView(imageFile);
        this.node = image;
        assignPowerUp();
    }

    //assign random powerup on initialization
    private void assignPowerUp() {
        if (hasPowerUp){
            //carry out logic
        }
        return;
    }

    public String getPowerUpType() {
        return powerUpType;
    }

    //returns the node when adding to the root
    public ImageView getNode() {
        return node;
    }

    public boolean isHasPowerUp() {
        return hasPowerUp;
    }
}
