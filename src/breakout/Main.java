package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */
    public static final String TITLE = "Super Breakout (Platinum Edtion)";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 120;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.BLACK;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final Paint MOVER_COLOR = Color.ROYALBLUE;
    public static final int MOVER_SIZE = 70;
    public static final int MOVER_SPEED = 25;
    public static final Paint GROWER_COLOR = Color.BISQUE;

    // some things needed to remember during game
    private Scene myScene;
    private ImageView myBall;
    private int bouncer_speed;
    private Rectangle myPaddle;
    private Scene mySplash;
    double directionX = 1;
    double directionY = 1;
    ArrayList<Brick> gameBricks;
    private Group root;
    private int currentLevel;
    private int score;
    private int lives;
    private Label levelDisp;
    private Label scoreDisp;
    private Label livesDisp;
    private Stage stage;

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
        Button button = new Button("pundek");
        button.setOnAction(e -> {stage.setScene(myScene);gameLoop();});
        Group group = new Group();
        group.getChildren().add(button);
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
        myPaddle = new Rectangle(width / 2 - MOVER_SIZE / 2, height-MOVER_SIZE/2, MOVER_SIZE + 10, MOVER_SIZE/3);
        myPaddle.setFill(MOVER_COLOR);
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPaddle);

        bouncer_speed = 200;
        score = 0;
        lives = 3;

        scoreDisp = new Label("Score: " + score);
        livesDisp = new Label("Lives: " + lives);
        levelDisp = new Label("Level " + currentLevel);

        scoreDisp.setTextFill(Color.GREENYELLOW);
        livesDisp.setTextFill(Color.GREENYELLOW);
        levelDisp.setTextFill(Color.AQUA);

        scoreDisp.setTranslateX(100);
        livesDisp.setTranslateX(150);
        //levelDisp.setFont(new Font(15));

        root.getChildren().addAll(scoreDisp, livesDisp, levelDisp);
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
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) {
        // update "actors" attributes
        myBall.setX(myBall.getX() + bouncer_speed * elapsedTime*directionX);
        myBall.setY(myBall.getY() + bouncer_speed * elapsedTime*directionY);
        if (myBall.getX() + myBall.getBoundsInLocal().getWidth() >= myScene.getWidth()){
            directionX *= -1;
        }
        if (myBall.getX() <= 0){
            directionX *= -1;
        }
        if (myBall.getY() <= 0){
            directionY *= -1;
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
            else if (myBall.getY() <= brickNode.getY() + brickNode.getFitHeight()-3 && myBall.getY() + myBall.getBoundsInLocal().getHeight() >= brickNode.getY()+3){
                if (myBall.getX() + myBall.getBoundsInLocal().getWidth() >= brickNode.getX()
                        && myBall.getX() + myBall.getBoundsInLocal().getWidth() <= brickNode.getX() + brickNode.getFitWidth()){
                    directionX *= -1;
                    hasHit = true;
                }
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
            //System.out.println("Calling callNewLevel");
            callNewLevel();
        }
    }

    private void callNewLevel() {
        //System.out.println("debug");
        if(currentLevel < 3){
            currentLevel++;
            try{
                myScene = setupGame(SIZE, SIZE, BACKGROUND);
            }
            catch(FileNotFoundException e){return;}

            stage.setScene(myScene);
            //bouncer_speed += 50;
            gameLoop();
            bouncer_speed/=2;
        }
        else{
            //Call end game protocol
        }
    }

    //Removes respective bricks from the screen if it has not hits left and the gameBricks arrayList
    private void brickCheckAndRemove(Brick brick) {
        brick.brickHit();
        updateScore();
        if(brick.getHits() < 1){
            root.getChildren().remove(brick.getNode());
            gameBricks.remove(brick);
        }
    }

    private void updateScore() {
        score++;
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

        String [] imageNames = {"none", "brick7.gif", "brick4.gif", "brick10.gif"};
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
            myPaddle.setX(myPaddle.getX() + MOVER_SPEED);
        }
        else if (code == KeyCode.LEFT) {
            if (myPaddle.getX() <= 5){
                return;
            }
            myPaddle.setX(myPaddle.getX() - MOVER_SPEED);
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
            myBall.setX(myScene.getWidth()/2);
            myBall.setY(myScene.getHeight()/2);
        }
        else if (code == KeyCode.E){
            gameBricks.clear();
        }
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
        }

    public static void main (String[] args){launch(args);}

    }

