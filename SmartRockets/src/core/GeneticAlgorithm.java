/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Hydron
 */
public class GeneticAlgorithm {

    private int populationSize;
    private int numGenerations;
    private double mutationRate;
    private Rocket[] population;
    private Target target;
    private int currentGeneration;
    private double bestFitnessSoFar;
    private List<Rectangle> obstacles;

    public GeneticAlgorithm(int populationSize, int numGenerations, double mutationRate) {
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.mutationRate = mutationRate;
        this.population = new Rocket[populationSize];
        this.target = new Target(400, 50);
        this.obstacles = new ArrayList<>();
        this.currentGeneration = 0;
        this.bestFitnessSoFar = 0;
        initializePopulation();
    }

    public void evolveOneGeneration() {
        if (currentGeneration < numGenerations) {
            // Elitism
            Rocket[] eliteRockets = getEliteRockets(population, 10);

            // Update positions
            for (int j = 0; j < populationSize; j++) {
                population[j].updatePosition();
            }

            // Evaluation
            for (int j = 0; j < populationSize; j++) {
                population[j].setFitness(evaluateFitness(population[j]));
            }

            // Tournament Selection
            Rocket[] selectedRockets = selectRocketsUsingTournament(population, populationSize / 2);

            // SBX Crossover
            Rocket[] offspring = sbxCrossover(selectedRockets, populationSize / 2);

            // Polynomial Mutation
            for (int j = 0; j < offspring.length; j++) {
                polynomialMutation(offspring[j]);
            }

            // Replacement
            replaceLeastFitRockets(population, offspring, eliteRockets);

            // Print the fitness of the fittest rocket at each generation
            System.out.println("Generation " + (currentGeneration + 1) + ": Fitness = " + getFittestRocket().getFitness());

            double currentBestFitness = getFittestRocket().getFitness();
            if (currentBestFitness > bestFitnessSoFar) {
                bestFitnessSoFar = currentBestFitness;
            }

            currentGeneration++;
        }
    }

    private Rocket[] getEliteRockets(Rocket[] population, int numEliteRockets) {
        // Sort the population by fitness in descending order
        Rocket[] sortedPopulation = population.clone();
        Arrays.sort(sortedPopulation, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));

        // Return the fittest rockets
        Rocket[] eliteRockets = new Rocket[numEliteRockets];
        System.arraycopy(sortedPopulation, 0, eliteRockets, 0, numEliteRockets);

        return eliteRockets;
    }

    private Rocket[] selectRocketsUsingTournament(Rocket[] population, int numRockets) {
        // Select the fittest rockets using tournament selection
        Rocket[] selectedRockets = new Rocket[numRockets];
        for (int i = 0; i < numRockets; i++) {
            Rocket[] tournament = new Rocket[5];
            for (int j = 0; j < 5; j++) {
                tournament[j] = population[(int) (Math.random() * population.length)];
            }
            selectedRockets[i] = getFittestRocket(tournament);
        }
        return selectedRockets;
    }

    private Rocket[] sbxCrossover(Rocket[] selectedRockets, int numOffspring) {
        // SBX crossover
        Rocket[] offspring = new Rocket[numOffspring];
        for (int i = 0; i < numOffspring; i++) {
            Rocket parent1 = selectedRockets[i % selectedRockets.length];
            Rocket parent2 = selectedRockets[(i + 1) % selectedRockets.length];
            double crossoverPoint = Math.random();
            Rocket child = new Rocket(crossoverPoint * parent1.getX() + (1 - crossoverPoint) * parent2.getX(), crossoverPoint * parent1.getY() + (1 - crossoverPoint) * parent2.getY());
            child.setVX(crossoverPoint * parent1.getVX() + (1 - crossoverPoint) * parent2.getVX());
            child.setVY(crossoverPoint * parent1.getVY() + (1 - crossoverPoint) * parent2.getVY());
            offspring[i] = child;
        }
        return offspring;
    }

    private void polynomialMutation(Rocket rocket) {
        // Polynomial mutation
        double mutationPoint = Math.random();
        rocket.setVX(rocket.getVX() + mutationPoint * 2 - 1);
        rocket.setVY(rocket.getVY() + mutationPoint * 2 - 1);
    }

    private void replaceLeastFitRockets(Rocket[] population, Rocket[] offspring, Rocket[] eliteRockets) {
        // Replace the least fit rockets with the new offspring
        int replacementSize = Math.min(offspring.length, population.length);
        for (int i = 0; i < replacementSize; i++) {
            population[population.length - i - 1] = offspring[i];
        }

        // Replace the weakest rockets with the elite rockets
        int eliteSize = Math.min(eliteRockets.length, population.length);
        for (int i = 0; i < eliteSize; i++) {
            population[i] = eliteRockets[i];
        }
    }

//    private double evaluateFitness(Rocket rocket) {
//        // Calculate the fitness of the rocket
//        double distance = Math.sqrt(Math.pow(rocket.getX() - target.getX(), 2) + Math.pow(rocket.getY() - target.getY(), 2));
//        return 1 / distance;
//    }
    private Rocket getFittestRocket(Rocket[] rockets) {
        // Sort the rockets by fitness in descending order
        Rocket[] sortedRockets = rockets.clone();
        Arrays.sort(sortedRockets, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));

        // Return the fittest rocket
        return sortedRockets[0];
    }

    private void initializePopulation() {
        // Initialize rockets with random positions and velocities
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            double x = 400;
            double y = 600;
            double vx = random.nextDouble() * 2 - 1;
            double vy = random.nextDouble() * 2 - 1;
            Rocket rocket = new Rocket(x, y);
            rocket.setVX(vx);
            rocket.setVY(vy);
            rocket.setAngle(Math.atan2(vy, vx));
            population[i] = rocket;
        }
    }

    public Target getTarget() {
        // Return the targets in the simulation
        return target;
    }

    public Rocket[] getRockets() {
        // Return the rockets in the simulation
        return population;
    }

    public int getPopulationSize() {
        // Return the population size
        return populationSize;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public boolean isEvolutionComplete() {
        return currentGeneration >= numGenerations;
    }

    public double getBestFitnessSoFar() {
        return bestFitnessSoFar;
    }

    public Rocket getFittestRocket() {
        return Arrays.stream(population).max(Comparator.comparingDouble(Rocket::getFitness)).orElse(null);
    }

    public void setObstacles(List<Rectangle> obstacles) {
        this.obstacles = obstacles;
    }

    public double getAverageFitness() {
        return Arrays.stream(population).mapToDouble(Rocket::getFitness).average().orElse(0);
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setPopulationSize(int size) {
        this.populationSize = size;
        // Reinitialize population with new size
        this.population = new Rocket[size];
        initializePopulation();
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    private double evaluateFitness(Rocket rocket) {
        if (obstacles != null) {
            for (Rectangle obstacle : obstacles) {
                if (rocket.getX() >= obstacle.x && rocket.getX() <= obstacle.x + obstacle.width
                        && rocket.getY() >= obstacle.y && rocket.getY() <= obstacle.y + obstacle.height) {
                    return 0; // Rocket collided with obstacle
                }
            }
        }

        // Calculate fitness based on distance to target
        double distance = Math.sqrt(Math.pow(rocket.getX() - target.getX(), 2) + Math.pow(rocket.getY() - target.getY(), 2));
        return 1 / distance;
    }

    public void reset() {
        this.currentGeneration = 0;
        this.bestFitnessSoFar = 0;
        initializePopulation();
    }

}
