/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.List;

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

    public static final int WIDTH = 4;
    public static final int HEIGHT = 10;

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
        for (Rectangle obstacle : obstacles) {
            if (x >= obstacle.x && x <= obstacle.x + obstacle.width
                    && y >= obstacle.y && y <= obstacle.y + obstacle.height) {
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

        // Generate a random angle between 0 and 180 degrees (converted to radians)
        double randomAngle = Math.toRadians(Math.random() * 180);

        // Set initial velocity components based on the random angle and INITIAL_SPEED
        vx = INITIAL_SPEED * Math.cos(randomAngle);
        vy = -INITIAL_SPEED * Math.sin(randomAngle);

        angle = randomAngle;
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
