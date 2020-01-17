package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */
    public static final String TITLE = "Example JavaFX";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final Paint MOVER_COLOR = Color.BLACK;
    public static final int MOVER_SIZE = 60;
    public static final int MOVER_SPEED = 15;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_SIZE = 50;

    // some things needed to remember during game
    private Scene myScene;
    private ImageView myBall;
    private int BOUNCER_SPEED = 200;
    private Rectangle myPaddle;
    private Rectangle myGrower;
    private Scene mySplash;
    double directionX = 1;
    double directionY = 1;


    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) throws FileNotFoundException {
        // attach scene to the stage and display it
        mySplash = splashScreen(stage);
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setTitle(TITLE);
        stage.setScene(mySplash);
        stage.show();
    }

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
        Group root = new Group();
        // make some shapes and set their properties
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall = new ImageView(image);
        // x and y represent the top left corner, so center it in window
        myBall.setX(width / 2 - myBall.getBoundsInLocal().getWidth() / 2);
        myBall.setY(height / 2 - myBall.getBoundsInLocal().getHeight() / 2);
        myPaddle = new Rectangle(width / 2 - MOVER_SIZE / 2, height-MOVER_SIZE/2, MOVER_SIZE + 10, MOVER_SIZE/3);
        myPaddle.setFill(MOVER_COLOR);
        myGrower = new Rectangle(width / 2 - GROWER_SIZE / 2, height / 2 + 50, GROWER_SIZE, GROWER_SIZE);
        myGrower.setFill(GROWER_COLOR);
        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall);
        root.getChildren().add(myPaddle);
        root.getChildren().add(myGrower);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));

        //Create bricks and add to scene
        int level = 1;
        makeBricks(root, level);
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
        myBall.setX(myBall.getX() + BOUNCER_SPEED * elapsedTime*directionX);
        myBall.setY(myBall.getY() + BOUNCER_SPEED * elapsedTime*directionY);
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
            //directionX *= -1;
        }
    }

    private void makeBricks(Group root, int level) throws FileNotFoundException {
        //read text file
        File file = new File("resources/level_1.txt");
        Scanner sc = new Scanner(file);

       while(sc.hasNextLine()){
           Bricks test = new Bricks (3, false, "brick1.gif");
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
            BOUNCER_SPEED /= 2;
        }
        else if (code == KeyCode.F){
            BOUNCER_SPEED *= 2;
        }
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
        if (myGrower.contains(x, y)) {
            myGrower.setScaleX(myGrower.getScaleX() * GROWER_RATE);
            myGrower.setScaleY(myGrower.getScaleY() * GROWER_RATE);
        }
    }
    public static void main (String[] args){launch(args);};
    }

