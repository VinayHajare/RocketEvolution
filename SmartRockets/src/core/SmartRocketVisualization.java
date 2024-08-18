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
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.layout.Priority;

/**
 *
 * @author Hydron
 */
public class SmartRocketVisualization extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private GeneticAlgorithm ga;
    private long lastUpdateTime = 0;
    private long updateInterval = 16_666_667; // 60 FPS
    private XYChart.Series<Number, Number> bestFitnessSeries;
    private XYChart.Series<Number, Number> avgFitnessSeries;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private List<Rectangle> obstacles = new ArrayList<>();
    private Canvas canvas;
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        ga = new GeneticAlgorithm(100, 1000, 0.01);
        initializeObstacles();

        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        VBox controls = createControls();
        LineChart<Number, Number> fitnessChart = createFitnessChart();

        VBox rightPanel = new VBox(10, controls, fitnessChart);
        rightPanel.setPrefWidth(300);

        HBox root = new HBox(10, canvas, rightPanel);
        HBox.setHgrow(canvas, Priority.ALWAYS);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Rocket Visualization");
        primaryStage.show();

        drawSimulation();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning.get() && now - lastUpdateTime >= updateInterval) {
                    if (!ga.isEvolutionComplete()) {
                        ga.evolveOneGeneration();
                        drawSimulation();
                        updateFitnessChart();
                    } else {
                        isRunning.set(false);
                    }
                    lastUpdateTime = now;
                }
            }
        }.start();
    }

    private void initializeObstacles() {
        obstacles.add(new Rectangle(300, 300, 200, 20));
        obstacles.add(new Rectangle(500, 100, 20, 400));
        ga.setObstacles(obstacles);
    }

    private VBox createControls() {
        Button startStopButton = new Button("Start");
        startStopButton.setOnAction(e -> {
            isRunning.set(!isRunning.get());
            startStopButton.setText(isRunning.get() ? "Pause" : "Start");
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            ga.reset();
            bestFitnessSeries.getData().clear();
            avgFitnessSeries.getData().clear();
            isRunning.set(false);
            startStopButton.setText("Start");
            drawSimulation();
        });

        Slider speedSlider = new Slider(0.1, 2, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);

        Label speedLabel = new Label("Speed: 1x");
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            speedLabel.setText(String.format("Speed: %.1fx", newVal.doubleValue()));
            updateInterval = (long) (16_666_667 / newVal.doubleValue());
        });

        TextField popSizeField = new TextField(String.valueOf(ga.getPopulationSize()));
        TextField mutationRateField = new TextField(String.valueOf(ga.getMutationRate()));

        Button applyButton = new Button("Apply");
        applyButton.setOnAction(e -> {
            try {
                int popSize = Integer.parseInt(popSizeField.getText());
                double mutationRate = Double.parseDouble(mutationRateField.getText());
                ga.setPopulationSize(popSize);
                ga.setMutationRate(mutationRate);
                ga.reset();
                drawSimulation();
            } catch (NumberFormatException ex) {
                // Handle invalid input
            }
        });

        VBox controls = new VBox(10,
                new HBox(10, startStopButton, resetButton),
                new HBox(10, speedSlider, speedLabel),
                new Label("Population Size:"),
                popSizeField,
                new Label("Mutation Rate:"),
                mutationRateField,
                applyButton
        );
        controls.setStyle("-fx-padding: 10;");

        return controls;
    }

    private LineChart<Number, Number> createFitnessChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        yAxis.setLabel("Fitness");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Fitness over Generations");
        lineChart.setPrefHeight(200);

        bestFitnessSeries = new XYChart.Series<>();
        bestFitnessSeries.setName("Best Fitness");
        avgFitnessSeries = new XYChart.Series<>();
        avgFitnessSeries.setName("Average Fitness");
        lineChart.getData().addAll(bestFitnessSeries, avgFitnessSeries);

        return lineChart;
    }

    private void updateFitnessChart() {
        bestFitnessSeries.getData().add(new XYChart.Data<>(ga.getCurrentGeneration(), ga.getBestFitnessSoFar()));
        avgFitnessSeries.getData().add(new XYChart.Data<>(ga.getCurrentGeneration(), ga.getAverageFitness()));
    }

    private void drawSimulation() {
        // Clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw obstacles
        gc.setFill(Color.GRAY);
        for (Rectangle obstacle : obstacles) {
            gc.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw the target
        Target target = ga.getTarget();
        gc.setFill(Color.RED);
        gc.fillOval(target.getX() - Target.WIDTH / 2, target.getY() - Target.HEIGHT / 2, Target.WIDTH, Target.HEIGHT);

        // Draw the rockets and their trails
        Rocket bestRocket = ga.getFittestRocket();
        for (Rocket rocket : ga.getRockets()) {
            // Draw trail
            rocket.getTrail().draw(gc);

            // Draw rocket
            gc.setFill(rocket == bestRocket ? Color.GREEN : Color.BLUE);
            gc.save();
            gc.translate(rocket.getX(), rocket.getY());
            gc.rotate(Math.toDegrees(rocket.getAngle()));
            gc.fillRect(-Rocket.WIDTH / 2, -Rocket.HEIGHT / 2, Rocket.WIDTH, Rocket.HEIGHT);
            gc.restore();
        }

        // Display information
        gc.setFill(Color.BLACK);
        gc.fillText("Generation: " + ga.getCurrentGeneration(), 10, 20);
        gc.fillText("Best Fitness: " + String.format("%.4f", ga.getBestFitnessSoFar()), 10, 40);
        gc.fillText("Average Fitness: " + String.format("%.4f", ga.getAverageFitness()), 10, 60);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
