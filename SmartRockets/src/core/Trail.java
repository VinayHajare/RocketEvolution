/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import javafx.scene.paint.Color;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Hydron
 */

public class Trail {
    private LinkedList<double[]> points;
    private static final int MAX_LENGTH = 1000;
    
    public Trail(){
        points = new LinkedList<>();
    }
    
    public void addPoint(double x, double y){
        points.addFirst(new double[]{x, y});
        if(points.size() > MAX_LENGTH){
            points.removeLast();
        }
    }
    
    public void draw(GraphicsContext gc){
        if(points.size() < 2) {
            return;
        }
        
        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineWidth(2);
        gc.beginPath();
        double[] first = points.getFirst();
        gc.moveTo(first[0], first[1]);
        for(int i = 1; i < points.size(); i++){
            double[] point = points.get(i);
            gc.lineTo(point[0], point[1]);
            gc.setGlobalAlpha(1.0 - (double)i / MAX_LENGTH);
        }
        gc.stroke();
        gc.setGlobalAlpha(1.0);
    }
}
