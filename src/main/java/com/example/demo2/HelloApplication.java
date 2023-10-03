package com.example.demo2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private Stage primaryStage;
    //this is how the size of the maze is determined
    private final int MAZE_SIZE = 40;
    //Coordinates for the first maze (done via google sheets)
    private int[][] maze = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 3},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
            {2, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public GraphicsContext gc;
    public Image robotImage;
    private ImageView imageView;
    private double carRotation = 0;
    private double carX; // Initial X position
    private double carY; // Initial Y position
    final double moveAmount = 10; // movement speed

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Canvas canvas = new Canvas(MAZE_SIZE * maze[0].length, MAZE_SIZE * maze.length);
        gc = canvas.getGraphicsContext2D();
        MazeColors(gc);

        robotImage = new Image(getClass().getResourceAsStream("/com/example/demo2/robot.png"));
        imageView = new ImageView(robotImage);
        carX = -11 * MAZE_SIZE;
        carY = 3.5 * MAZE_SIZE;
        imageView.setTranslateX(carX);
        imageView.setTranslateY(carY);

        StackPane root = new StackPane(canvas, imageView);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(this::Traverse);

        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.getRoot().requestFocus();
    }
    // Method to set colors for each cell in the maze and draw them on the canvas.
    private void MazeColors(GraphicsContext gc) {
        // Loop through the maze array and draw colored cells
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                // Check the value of the current cell in the maze array
                if (maze[row][col] == 0) {
                    // If it's 0, it represents an empty space, so fill it with white color.
                    gc.setFill(javafx.scene.paint.Color.WHITE);
                } else if (maze[row][col] == 1) {
                    // If it's 1, it represents a wall, so fill it with blue color.
                    gc.setFill(javafx.scene.paint.Color.BLUE);
                } else if (maze[row][col] == 2) {
                    // If it's 2, it represents the start point, so fill it with green color.
                    gc.setFill(javafx.scene.paint.Color.GREEN); // Start point
                } else if (maze[row][col] == 3) {
                    // If it's 3, it represents the end point, so fill it with red color.
                    gc.setFill(javafx.scene.paint.Color.RED); // End point
                }

                // Draw a colored rectangle (cell) on the canvas based on the cell's position.
                // The size of each cell is determined by the MAZE_SIZE variable.
                gc.fillRect(col * MAZE_SIZE, row * MAZE_SIZE, MAZE_SIZE, MAZE_SIZE);
            }
        }
    }

    //this is the movement function
    private void rotateCarImage() {
        imageView.setRotate(carRotation);
    }

    // Inside your Traverse function in HelloApplication.java

    private void Traverse(KeyEvent event) {
        int redBlockCol = 11;
        double redBlockRow = 1.5;
        KeyCode keyCode = event.getCode();

        double newCarX = carX;
        double newCarY = carY;

        switch (keyCode) {
            case UP:
                newCarY -= moveAmount;
                carRotation = 0;
                break;
            case DOWN:
                newCarY += moveAmount;
                carRotation = 180;
                break;
            case LEFT:
                newCarX -= moveAmount;
                carRotation = -90;
                break;
            case RIGHT:
                newCarX += moveAmount;
                carRotation = 90;
                break;
            default:
                return;
        }

        int colIndex = (int) (newCarX / MAZE_SIZE);
        double rowIndex = (newCarY / MAZE_SIZE);

        if (colIndex == redBlockCol && rowIndex == redBlockRow) {


            // Create a new scene for Maze2 with the specified width and height

            Maze2 maze2 = new Maze2();
            Scene maze2Scene = new Scene(maze2.createContent());

            // Set the new scene for the primaryStage
            primaryStage.setScene(maze2Scene);
        } else {
            carX = newCarX;
            carY = newCarY;
            imageView.setTranslateX(carX);
            imageView.setTranslateY(carY);
            rotateCarImage();
        }
    }



}


