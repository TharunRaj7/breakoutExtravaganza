/**
 * This class provides the functions to handle the different powerUps in the game. I think it is well designed because all the logic to handle the
 * powerUps is located here instead of the GameDriver class.
 */
package breakout;

import javafx.application.Platform;

import static breakout.GameDriver.MOVER_SIZE;

public class PowerUps {


    public void powerUpHandler(String powerUpType, GameDriver game) {
        if(powerUpType.equals("Lives")){
            livesPowerUp(game);
        }
        else if (powerUpType.equals("PaddleRoids")){
           paddleRoidsPowerUp(game);
        }
        else if (powerUpType.equals("BallAcid")){
            ballAcidPowerUp(game);
        }
    }

    private void livesPowerUp(GameDriver game){
        if(game.getLives() < 5){
            Thread thread = new Thread(() -> {
                try {
                    Platform.runLater(() -> {game.setLives(game.getLives() + 1);
                        game.getLivesDisp().setText("lives: " + game.getLives());
                        game.getPowerUpDisp().setText("Extra Life Obtained!"); game.getPowerUpDisp().setVisible(true);});
                    Thread.sleep(1500);
                    Platform.runLater(() -> {game.getPowerUpDisp().setVisible(false);});
                } catch (InterruptedException exc) {
                    // should not be able to get here...
                    throw new Error("Unexpected interruption");
                }
            });
            thread.start();
        }
    }
    private void paddleRoidsPowerUp(GameDriver game){
        if(!game.isPaddleRoidsActivated()){
            game.setPaddleRoidsActivated(true);
            game.getMyPaddle().setWidth(game.getMyPaddle().getWidth() + 100);
            game.getMyPaddle().setX(game.getMyPaddle().getX() - 50);
            game.getPowerUpDisp().setText("Paddle Roids Activated!"); game.getPowerUpDisp().setVisible(true);

            Thread thread1 = new Thread(() -> {
                try {
                    Platform.runLater(() -> {game.getMyPaddle().setWidth(MOVER_SIZE + 100);});
                    Thread.sleep(5000);
                    Platform.runLater(() -> {game.getMyPaddle().setWidth(MOVER_SIZE);
                        game.getMyPaddle().setX(game.getMyPaddle().getX() + 50);
                        game.setPaddleRoidsActivated(true); game.getPowerUpDisp().setVisible(false);});
                } catch (InterruptedException exc) {
                    // should not be able to get here...
                    throw new Error("Unexpected interruption");
                }
            });
            thread1.start();
        }
    }
    private void ballAcidPowerUp(GameDriver game){
        if (!game.isBallAcidActivated()){
            Thread thread2 = new Thread(() -> {
                try {
                    Platform.runLater(() -> {game.setBouncer_speed(game.getBouncer_speed() - 85);
                        game.setBallAcidActivated(true);
                        game.getPowerUpDisp().setText("Ball Acid Activated!"); game.getPowerUpDisp().setVisible(true);});
                    Thread.sleep(5000);
                    Platform.runLater(() -> {game.setBouncer_speed(game.getBouncer_speed() + 85);
                        game.setBallAcidActivated(false);
                        game.getPowerUpDisp().setVisible(false);});
                } catch (InterruptedException exc) {
                    // should not be able to get here...
                    throw new Error("Unexpected interruption");
                }
            });
            thread2.start();

        }
    }

}
