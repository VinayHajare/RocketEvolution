/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

/**
 *
 * @author Hydron
 */

public class SmartRocketVisualization extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private GeneticAlgorithm ga;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 16_666_667; // 60 FPS

    @Override
    public void start(Stage primaryStage) {
        ga = new GeneticAlgorithm(20, 1000, 0.02);
        
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Rockets");
        primaryStage.show();
        
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateTime >= UPDATE_INTERVAL) {
                    if (!ga.isEvolutionComplete()) {
                        ga.evolveOneGeneration();
                        drawSimulation(gc);
                    }
                    lastUpdateTime = now;
                }
            }
        }.start();
    }

    private void drawSimulation(GraphicsContext gc) {
        // Clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Draw the target
        Target target = ga.getTarget();
        gc.setFill(Color.RED);
        gc.fillOval(target.getX() - Target.WIDTH/2, target.getY() - Target.HEIGHT/2, Target.WIDTH, Target.HEIGHT);
        
        // Draw the rockets and their trails
        for (Rocket rocket : ga.getRockets()) {
            // Draw trail
            rocket.getTrail().draw(gc);
            
            // Draw rocket
            gc.setFill(Color.BLUE);
            gc.save();
            gc.translate(rocket.getX(), rocket.getY());
            gc.rotate(Math.toDegrees(rocket.getAngle()));
            double[] xPoints = {-Rocket.WIDTH/2, 0, Rocket.WIDTH/2};
            double[] yPoints = {Rocket.HEIGHT/2, -Rocket.HEIGHT/2, Rocket.HEIGHT/2};
            gc.fillPolygon(xPoints, yPoints, 3);
            //gc.fillRect(-Rocket.WIDTH/2, -Rocket.HEIGHT/2, Rocket.WIDTH, Rocket.HEIGHT);
            gc.restore();
        }
        
        // Display information
        gc.setFill(Color.BLACK);
        gc.fillText("Generation: " + ga.getCurrentGeneration(), 10, 20);
        gc.fillText("Best Fitness: " + String.format("%.4f", ga.getBestFitnessSoFar()), 10, 40);
    }

    public static void main(String[] args) {
        launch(args);
    }
}