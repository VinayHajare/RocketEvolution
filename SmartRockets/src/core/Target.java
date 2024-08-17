/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

/**
 *
 * @author Hydron
 */
public class Target {
    
    // Position
    private double x;
    private double y;
    
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    
    public Target(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public double getX(){ return x; }
    public double getY(){ return y; }
    
    // Setters
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
    
}
