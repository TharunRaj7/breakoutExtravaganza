package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */
    public static final String TITLE = "Space Breakout (Platinum Edtion)";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 200;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.BLACK;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final Paint MOVER_COLOR = Color.ROYALBLUE;
    public static final int MOVER_SIZE = 80;
    public static final Paint GROWER_COLOR = Color.BISQUE;

    // some things needed to remember during game
    private Scene myScene;
    private ImageView myBall;
    private int bouncer_speed;
    private int mover_speed;
    private Rectangle myPaddle;
    private Scene mySplash;
    double directionX = 1;
    double directionY = 1;
    ArrayList<Brick> gameBricks;
    private Group root;
    private int currentLevel;
    private int score = 0;
    private int lives = 3;
    private Label levelDisp;
    private Label scoreDisp;
    private Label livesDisp;
    private Label startMessage;
    private Label powerUpDisp;
    private Stage stage;
    Timeline animation;
    Alien alien;
    boolean paddleRoidsActivated = false;
    boolean ballAcidActivated = false;
    private boolean alienPresent;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) throws FileNotFoundException {
        // attach scene to the stage and display it
        currentLevel = 1;
        mySplash = splashScreen(stage);
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setTitle(TITLE);
        stage.setScene(mySplash);
        stage.show();
        this.stage = stage;
    }

    //Creates the splash screen to display when the game is launched
    private Scene splashScreen(Stage stage){
        Label label = new Label("                                       WELCOME TO SPACE BREAKOUT!\n\n" +
                "Here are the rules:\n" +
                "- Use the left and right arrow keys to move the paddle and \n   prevent the ball from falling\n\n" +
                "- You gain 1 point for every brick you hit and lose a life for \n   every ball you miss\n\n" +
                "- The faded out bricks contain power ups (Paddle Roids, Ball Acid \n   and Extra Lives)\n\n" +
                "- Occasionally an alien enters your solar system and the only \n   way to stop them is by catching them with your paddle\n\n" +
                "- Catching an alien gives you additional score points and if you \n   fail to catch them, you lose a life\n\n" +
                "                                                   ARE YOU READY?!");

        label.setFont(new Font("Algerian", 17));
        label.setTextFill(Color.GREENYELLOW);
        label.setTranslateY(50);
        Button button = new Button("PLAY");
        button.setTranslateX(275); button.setTranslateY(520);
        button.setOnAction(e -> {stage.setScene(myScene);gameLoop();});
        Group group = new Group();
        group.getChildren().addAll(button, label);
        Scene retSplash = new Scene(group, SIZE, SIZE, BACKGROUND);
        return retSplash;
    }
    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) throws FileNotFoundException {
        // create one top level collection to organize the things in the scene
        root = new Group();
        // make some shapes and set their properties
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new ImageView(image);
        // x and y represent the top left corner, so center it in window
        myBall.setX(width / 2 - myBall.getBoundsInLocal().getWidth() / 2);
        myBall.setY(height / 2 - myBall.getBoundsInLocal().getHeight() / 2);
        myPaddle = new Rectangle(width / 2 - MOVER_SIZE / 2, height-MOVER_SIZE/2, MOVER_SIZE + 10, MOVER_SIZE/4);

        myPaddle.setFill(MOVER_COLOR);
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPaddle);

        bouncer_speed = 200 + currentLevel*25;
        mover_speed = 30 + currentLevel*5;

        scoreDisp = new Label("Score: " + score);
        livesDisp = new Label("Lives: " + lives);
        levelDisp = new Label("Level " + currentLevel);
        powerUpDisp = new Label();
        startMessage = new Label("Press G to start");

        scoreDisp.setTextFill(Color.GREENYELLOW);
        livesDisp.setTextFill(Color.GREENYELLOW);
        levelDisp.setTextFill(Color.AQUA);
        startMessage.setTextFill(Color.BLUEVIOLET);
        powerUpDisp.setTextFill(Color.AQUA);

        powerUpDisp.setTranslateX(400);
        powerUpDisp.setVisible(false);
        scoreDisp.setTranslateX(100);
        livesDisp.setTranslateX(200);
        startMessage.setTranslateX(233); startMessage.setTranslateY(350);
        startMessage.setFont(new Font("Algerian", 17));

        powerUpDisp.setFont(new Font(12));

        root.getChildren().addAll(scoreDisp, livesDisp, levelDisp, startMessage, powerUpDisp);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));

        //Create bricks and add to scene
        makeBricks(scene);
        return scene;
    }

    private void gameLoop (){
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        //animation.play();
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) {
        // update "actors" attributes
        myBall.setX(myBall.getX() + bouncer_speed * elapsedTime*directionX);
        myBall.setY(myBall.getY() + bouncer_speed * elapsedTime*directionY);
        if(alienPresent){
            AlienPaddleCollision(elapsedTime);
        }


        
        
        if (myBall.getX() + myBall.getBoundsInLocal().getWidth() >= myScene.getWidth()){
            directionX *= -1;
        }
        if (myBall.getX() <= 0){
            directionX *= -1;
        }
        if (myBall.getY() <= 0){
            directionY *= -1;
        }
        if (myBall.getY() > myScene.getHeight()){
            checkAndRemoveLives();
        }

        //Handle Paddle-Ball Interaction (add specificity to the paddle)
        // condition for collision: find shape interaction and check to see if above -1.
        //Shape shape = Shape.intersect(myBall, myPaddle);
        if (myBall.getY() + myBall.getBoundsInLocal().getHeight() >= myPaddle.getY() && myBall.getY() + myBall.getBoundsInLocal().getHeight() <= myPaddle.getY()+3
                && myBall.getX() >= myPaddle.getX() && myBall.getX() <= (myPaddle.getX() + myPaddle.getWidth())){
            directionY *= -1;
            if (myPaddle.getFill() == Color.ROYALBLUE){
                myPaddle.setFill(Color.DARKGREEN);
                return;
            }
            myPaddle.setFill(Color.ROYALBLUE);
            //directionX *= -1;
        }
        brickBallCollision();
    }

    private void AlienPaddleCollision(double elapsedTime) {
        alien.setyValue(alien.getYValue() + alien.getSpeed() * elapsedTime);
        if (myPaddle.intersects(alien.getNode().getBoundsInLocal())){
            updateScore(20);
            alienPresent = false;
            root.getChildren().remove(alien.getNode());
        }
        else if (alien.getYValue() > myScene.getHeight()){
            lives--;
            livesDisp.setText("Lives: " + lives);
            alienPresent = false;
            root.getChildren().remove(alien.getNode());
        }
    }

    private void initiateAlien() {
        if (!alienPresent) {
            alienPresent = true;
            double xValue = new Random().nextInt((int)(myScene.getWidth()) - 10) + 5;
            this.alien = new Alien(xValue, 0, bouncer_speed / 2);
            root.getChildren().add(alien.getNode());
        }
    }


    private void checkAndRemoveLives() {
        lives--;
        if(lives > -1){
            livesDisp.setText("lives: " + lives);
            resetBall();
            return;
        }
        else if (lives < 0){
            gameEnd("Lost");
        }
    }

    private void gameEnd(String end) {
        animation.stop();
        Group group = new Group();
        Label label = new Label("                     You "+ end + "!\n\n" +
                "         You got a score of " + score + "\n\n" +
                "   Would you like to replay?");
        Button button = new Button("Replay");
        button.setTranslateY(350);
        button.setTranslateX(270);
        button.setScaleX(2.8); button.setScaleY(2);
        button.setOnAction(e -> {score = 0; lives = 3; currentLevel = 0; callNewLevel();});
        label.setTranslateY(100); label.setTranslateX(70);
        label.setTextFill(Color.GREENYELLOW);
        label.setFont(new Font("Algerian", 30));
        group.getChildren().addAll(label, button);
        Scene scene = new Scene(group, SIZE, SIZE, BACKGROUND);
        stage.setScene(scene);
    }

    private void resetBall() {
        animation.pause();
        startMessage.setVisible(true);
        myBall.setX(myScene.getWidth()/2);
        myBall.setY(myScene.getHeight()/2);
        directionX = 1;
        directionY = 1;
    }

    /*This function checks for collisions between the ball and all the bricks
    * This function runs the appropriate logic to update the brick values and deflect the ball if a collision occurs
    * */
    private void brickBallCollision() {
        //System.out.println(gameBricks.size());    //this works
        int bricksLeft = gameBricks.size();
        //System.out.println(bricksLeft);
        for(int i = 0; i < bricksLeft; i++){
            boolean hasHit = false;
            ImageView brickNode = gameBricks.get(i).getNode();
            //If the ball is within the horizontal space of the brick, check whether a collision occurred at the top or bottom of the brick
            if(myBall.getX() >= brickNode.getX() + 3 && myBall.getX() <= brickNode.getX() + brickNode.getFitWidth() -3){
                //Check for collision on the top of the brick
                if (myBall.getY() + myBall.getBoundsInLocal().getHeight() >= brickNode.getY()
                        && myBall.getY() + myBall.getBoundsInLocal().getHeight() <= brickNode.getY() + brickNode.getFitHeight()) {
                    directionY *= -1;
                    hasHit = true;
                }
                //Check for collision on the bottom of the brick
                else if (myBall.getY() <= brickNode.getY() + brickNode.getFitHeight()
                        && myBall.getY() >= brickNode.getY()){
                    directionY *= -1;
                    hasHit = true;
                }

            }
            //Check for side collisions
            else if (myBall.getY() <= brickNode.getY() + brickNode.getFitHeight()-3 && myBall.getY() + myBall.getBoundsInLocal().getHeight() >= brickNode.getY()+3){
                // Collision on the left side of the brick
                if (myBall.getX() + myBall.getBoundsInLocal().getWidth() >= brickNode.getX()
                        && myBall.getX() + myBall.getBoundsInLocal().getWidth() <= brickNode.getX() + brickNode.getFitWidth()){
                    directionX *= -1;
                    hasHit = true;
                }
                //Collision on the right side of the brick
                else if (myBall.getX() <= brickNode.getX() + brickNode.getFitWidth() && myBall.getX() >= brickNode.getX()){
                    directionX *= -1;
                    hasHit = true;
                }
            }

            if (hasHit){
                brickCheckAndRemove(gameBricks.get(i));
                bricksLeft--;

            }

        }
        checkGameCondition(bricksLeft);
    }

    // if no bricks are left, the next level is called.
    private void checkGameCondition(int bricksLeft) {
        //System.out.println("debug checkGame");
        if (bricksLeft == 0){
            if (currentLevel == 3){
                gameEnd("Won");
                return;
            }
            //System.out.println("Calling callNewLevel");
            callNewLevel();
        }
    }

    private void callNewLevel() {
        //System.out.println("debug");
        if(currentLevel < 3){
            animation.stop();
            currentLevel++;
            try{
                myScene = setupGame(SIZE, SIZE, BACKGROUND);
            }
            catch(FileNotFoundException e){return;}

            stage.setScene(myScene);
            //bouncer_speed += 50;
            gameLoop();
            resetBall();
            //bouncer_speed/=2;
        }
    }

    //Removes respective bricks from the screen if it has not hits left and the gameBricks arrayList
    private void brickCheckAndRemove(Brick brick) {
        brick.brickHit();
        updateScore(1);
        if(brick.getHits() < 1){
            root.getChildren().remove(brick.getNode());
            gameBricks.remove(brick);
            System.out.println(brick.getPowerUpType());
            Random rand = new Random();
            boolean [] choiceArray = {true,false,false,false,false};
            boolean choice = choiceArray[rand.nextInt(choiceArray.length)];
            if(choice){
                initiateAlien();
            }
            if(brick.isHasPowerUp()){
                powerUpHandler(brick.getPowerUpType());
            }

        }
    }

    private void powerUpHandler(String powerUpType) {
        if(powerUpType.equals("Lives")){
            if(lives < 5){
                Thread thread = new Thread(() -> {
                    try {
                        Platform.runLater(() -> {lives++;
                            livesDisp.setText("lives: " + lives);
                            powerUpDisp.setText("Extra Life Obtained!"); powerUpDisp.setVisible(true);});
                        Thread.sleep(1500);
                        Platform.runLater(() -> {powerUpDisp.setVisible(false);});
                    } catch (InterruptedException exc) {
                        // should not be able to get here...
                        throw new Error("Unexpected interruption");
                    }
                });
                thread.start();
            }
        }
        else if (powerUpType.equals("PaddleRoids")){
            if(!paddleRoidsActivated){
                paddleRoidsActivated = true;
                myPaddle.setWidth(myPaddle.getWidth() + 100);
                myPaddle.setX(myPaddle.getX() - 50);
                powerUpDisp.setText("Paddle Roids Activated!"); powerUpDisp.setVisible(true);

                Thread thread1 = new Thread(() -> {
                    try {
                        Platform.runLater(() -> {myPaddle.setWidth(MOVER_SIZE + 100);});
                        Thread.sleep(5000);
                        Platform.runLater(() -> {myPaddle.setWidth(MOVER_SIZE);
                            myPaddle.setX(myPaddle.getX() + 50);
                            paddleRoidsActivated = false; powerUpDisp.setVisible(false);});
                    } catch (InterruptedException exc) {
                        // should not be able to get here...
                        throw new Error("Unexpected interruption");
                    }
                });
                thread1.start();

            }
        }
        else if (powerUpType.equals("BallAcid")){
            if (!ballAcidActivated){
                Thread thread2 = new Thread(() -> {
                    try {
                        Platform.runLater(() -> {bouncer_speed -= 85;
                            ballAcidActivated = true;
                            powerUpDisp.setText("Ball Acid Activated!"); powerUpDisp.setVisible(true);});
                        Thread.sleep(5000);
                        Platform.runLater(() -> {bouncer_speed += 85;
                            ballAcidActivated = false;
                            powerUpDisp.setVisible(false);});
                    } catch (InterruptedException exc) {
                        // should not be able to get here...
                        throw new Error("Unexpected interruption");
                    }
                });
                thread2.start();

            }
        }
    }

    private void updateScore(int val) {
        score += val;
        scoreDisp.setText("Score: " + score);
    }

    private void makeBricks(Scene scene) throws FileNotFoundException {
        //read text file
        File file = new File("resources/level_" + currentLevel + ".txt");
        Scanner sc = new Scanner(file);


        int initXValue = (int) (scene.getWidth()/8 + 15);
        int yValue = (int) (scene.getHeight()/8);
        Brick dummy = new Brick (1, 0, 0, "brick1.gif"); //dummy brick object to extract width and height values
        int brickWidth = (int) dummy.getNode().getFitWidth();
        int brickHeight = (int) dummy.getNode().getFitHeight();

        String [] imageNames = {"none", "brick7.gif", "brick4.gif", "brick8.gif"};
        gameBricks = new ArrayList<>();
        while(sc.hasNextLine()){
            String row = sc.nextLine();
            String [] brickHits = row.split(" ");
            int xValue = initXValue;
            for (int i = 0; i< brickHits.length; i++){
                int hits = Integer.parseInt(brickHits[i]);
                if(hits != 0){  //skip creating a new brick object if the hits is 0
                    Brick temp = new Brick (hits, xValue, yValue, imageNames[hits]);
                    root.getChildren().add(temp.getNode()); //add the nodes to the group
                    gameBricks.add(temp);
                }
                xValue += brickWidth;
            }
            yValue += brickHeight;
       }

    }






    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            if (myPaddle.getX() + myPaddle.getWidth() >= myScene.getWidth()-5){
                return;
            }
            myPaddle.setX(myPaddle.getX() + mover_speed);
        }
        else if (code == KeyCode.LEFT) {
            if (myPaddle.getX() <= 5){
                return;
            }
            myPaddle.setX(myPaddle.getX() - mover_speed);
        }
        else if (code == KeyCode.G){
            animation.play();
            startMessage.setVisible(false);
        }
        else{
            callCheatCode(code);
        }
        // NEW Java 12 syntax that some prefer (but watch out for the many special cases!)
        //   https://blog.jetbrains.com/idea/2019/02/java-12-and-intellij-idea/
        // Note, must set Project Language Level to "13 Preview" under File -> Project Structure
        // switch (code) {
        //     case RIGHT -> myPaddle.setX(myPaddle.getX() + MOVER_SPEED);
        //     case LEFT -> myPaddle.setX(myPaddle.getX() - MOVER_SPEED);
        //     case UP -> myPaddle.setY(myPaddle.getY() - MOVER_SPEED);
        //     case DOWN -> myPaddle.setY(myPaddle.getY() + MOVER_SPEED);
        // }
    }

    private void callCheatCode(KeyCode code) {
        if (code == KeyCode.S){
            bouncer_speed /= 2;
        }
        else if (code == KeyCode.F){
            bouncer_speed *= 2;
        }
        else if (code == KeyCode.M){
            double newWidth = myPaddle.getWidth()*2;
            myPaddle.setWidth(newWidth);
            myPaddle.setX(myPaddle.getX() - newWidth/4);
        }
        else if (code == KeyCode.N){
            double newWidth = myPaddle.getWidth()/2;
            myPaddle.setWidth(newWidth);
            myPaddle.setX(myPaddle.getX() + newWidth/4);
        }
        else if (code == KeyCode.R){
           resetBall();
           myPaddle.setX(myScene.getWidth()/2 - 10);
           myPaddle.setWidth(MOVER_SIZE);
        }
        else if (code == KeyCode.DIGIT1){
            animation.stop();
            currentLevel = 0;
            callNewLevel();
        }
        else if (code == KeyCode.DIGIT2){
            animation.stop();
            currentLevel = 1;
            callNewLevel();
        }
        else if (code == KeyCode.DIGIT3){
            animation.stop();
            currentLevel = 2;
            callNewLevel();
        }
        else if(code == KeyCode.L){
            lives++;
            livesDisp.setText("Lives: " + lives);
        }
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
        }

    public static void main (String[] args){launch(args);}

    }

