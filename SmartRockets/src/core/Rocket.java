/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

/**
 *
 * @author Hydron
 */
public class Rocket {
    
    // Position
    private double x; 
    private double y;
    
    // Velocity
    private double vx; 
    private double vy;
    
    // Direction
    private double angle;
    
    // Fitness Score
    private double fitness;
    
    // Trail
    private Trail trail;
    
    private static final double MAX_VELOCITY = 5.0;
    public static final int WIDTH = 4;
    public static final int HEIGHT = 10;
    
    public Rocket(double x, double y){
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.fitness = 0;
        this.trail = new Trail();
    }
    
    // Getters
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getVX(){ return vx; }
    public double getVY(){ return vy; }
    public double getAngle(){ return angle; }
    public double getFitness(){ return fitness; }
    
    // Setters
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
    public void setVX(double vx){ this.vx = vx; }
    public void setVY(double vy){ this.vy = vy; }
    public void setAngle(double angle){ this.angle = angle; }
    public void setFitness(double fitness){ this.fitness = fitness; }
    
    public void updatePosition() {
        // Apply velocity limits
        vx = Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, vx));
        vy = Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, vy));

        x += vx;
        y += vy;

        // Keep within bounds (assuming 800x600 window)
        x = Math.max(0, Math.min(800 - WIDTH, x));
        y = Math.max(0, Math.min(600 - HEIGHT, y));
        
        // Update angle based on velocity
        angle = Math.atan2(vy, vx);
        
        trail.addPoint(x, y);
    }
    
    public Trail getTrail(){
        return trail;
    }
}
