/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Hydron
 */
public class Rocket {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private double angle;
    private double fitness;
    private boolean active;
    private Trail trail;
    private final Random random = new Random();

    public static final double HEIGHT = 20; // Match the rocketHeight in drawSimulation
    public static final double WIDTH = 10;  // Match the rocketWidth in drawSimulation

    private static final double INITIAL_SPEED = 2.0;

    public Rocket(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.fitness = 0;
        this.active = true;
        this.trail = new Trail();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVX() {
        return vx;
    }

    public double getVY() {
        return vy;
    }

    public double getAngle() {
        return angle;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean isActive() {
        return active;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVX(double vx) {
        this.vx = vx;
    }

    public void setVY(double vy) {
        this.vy = vy;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void updatePosition() {
        if (active) {
            x += vx;
            y += vy;

            // Optional: Add some randomness or "jitter" to the motion
            vx += (Math.random() - 0.5) * 0.1;
            vy += (Math.random() - 0.5) * 0.1;

            // Update angle based on current velocity
            angle = Math.atan2(vy, vx);

            trail.addPoint(x, y);
        }
    }

    public boolean hasCollided(List<Rectangle> obstacles) {
        // Update collision detection for triangular shape
        for (Rectangle obstacle : obstacles) {
            if (x >= obstacle.x && x <= obstacle.x + obstacle.width
                    && y >= obstacle.y && y <= obstacle.y + obstacle.height) {
                // Simple bounding box collision detection
                // For more precise collision, you'd need to implement triangle-rectangle collision detection
                return true;
            }
        }
        return false;
    }

    public boolean isOutOfBounds() {
        return x < 0 || x > 800 || y < 0 || y > 600; // Adjust based on your screen size
    }

    public boolean hasReachedTarget(Target target) {
        double distance = Math.sqrt(Math.pow(x - target.getX(), 2) + Math.pow(y - target.getY(), 2));
        return distance < 10; // Adjust this value based on your target size
    }

    public void reset() {
        x = 400; // Starting x position
        y = 600; // Starting y position
        active = true;
        if (trail == null) {
            trail = new Trail();
        } else {
            trail.clear();
        }
    }

    public Trail getTrail() {
        return trail != null ? trail : new Trail();
    }
}
