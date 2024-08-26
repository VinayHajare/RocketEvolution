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
    private int successfulRocketsCount;
    private boolean generationFinished;

    public GeneticAlgorithm(int populationSize, int numGenerations, double mutationRate) {
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.mutationRate = mutationRate;
        this.population = new Rocket[populationSize];
        this.target = new Target(400, 50);
        this.obstacles = new ArrayList<>();
        this.currentGeneration = 0;
        this.bestFitnessSoFar = 0;
        this.successfulRocketsCount = 0;
        this.generationFinished = false;
        initializePopulation();
    }

    public void evolveOneGeneration() {
        if (currentGeneration < numGenerations) {
            if (generationFinished) {
                // Start new generation
                for (Rocket rocket : population) {
                    rocket.reset();
                }
                generationFinished = false;
                successfulRocketsCount = 0;
                currentGeneration++;
            }

            boolean allRocketsInactive = true;
            for (Rocket rocket : population) {
                if (rocket.isActive()) {
                    allRocketsInactive = false;
                    rocket.updatePosition();
                    if (rocket.hasReachedTarget(target)) {
                        successfulRocketsCount++;
                        rocket.setActive(false);
                    } else if (rocket.hasCollided(obstacles) || rocket.isOutOfBounds()) {
                        rocket.setActive(false);
                    }
                }
            }

            if (allRocketsInactive && !generationFinished) {
                // Perform evolution steps
                evaluatePopulation();
                Rocket[] eliteRockets = getEliteRockets(population, 10);
                Rocket[] selectedRockets = selectRocketsUsingTournament(population, populationSize / 2);
                Rocket[] offspring = sbxCrossover(selectedRockets, populationSize / 2);
                for (Rocket rocket : offspring) {
                    polynomialMutation(rocket);
                }
                replaceLeastFitRockets(population, offspring, eliteRockets);
                updateBestFitness();
                generationFinished = true;
            }
        }
    }

//    public void evolveOneGeneration() {
//        if (currentGeneration < numGenerations) {
//            boolean allRocketsInactive = Arrays.stream(population).noneMatch(Rocket::isActive);
//
//            if (allRocketsInactive) {
//                // Evaluation
//                for (int j = 0; j < populationSize; j++) {
//                    population[j].setFitness(evaluateFitness(population[j]));
//                }
//
//                // Elitism
//                Rocket[] eliteRockets = getEliteRockets(population, 10);
//
//                // Tournament Selection
//                Rocket[] selectedRockets = selectRocketsUsingTournament(population, populationSize / 2);
//
//                // SBX Crossover
//                Rocket[] offspring = sbxCrossover(selectedRockets, populationSize / 2);
//
//                // Polynomial Mutation
//                for (int j = 0; j < offspring.length; j++) {
//                    polynomialMutation(offspring[j]);
//                }
//
//                // Replacement
//                replaceLeastFitRockets(population, offspring, eliteRockets);
//
//                // Update best fitness
//                double currentBestFitness = getFittestRocket().getFitness();
//                if (currentBestFitness > bestFitnessSoFar) {
//                    bestFitnessSoFar = currentBestFitness;
//                }
//
//                // Reset all rockets for the next generation
//                for (Rocket rocket : population) {
//                    rocket.reset();
//                }
//
//                successfulRocketsCount = 0;
//                currentGeneration++;
//            } else {
//                // Update positions of active rockets
//                for (Rocket rocket : population) {
//                    if (rocket.isActive()) {
//                        rocket.updatePosition();
//                        if (rocket.hasReachedTarget(target)) {
//                            successfulRocketsCount++;
//                            rocket.setActive(false);
//                        } else if (rocket.hasCollided(obstacles) || rocket.isOutOfBounds()) {
//                            rocket.setActive(false);
//                        }
//                    }
//                }
//            }
//        }
//    }
    private Rocket[] getEliteRockets(Rocket[] population, int numEliteRockets) {
        Rocket[] sortedPopulation = population.clone();
        Arrays.sort(sortedPopulation, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        Rocket[] eliteRockets = new Rocket[numEliteRockets];
        System.arraycopy(sortedPopulation, 0, eliteRockets, 0, numEliteRockets);
        return eliteRockets;
    }

    private Rocket[] selectRocketsUsingTournament(Rocket[] population, int numRockets) {
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
        Rocket[] offspring = new Rocket[numOffspring];
        for (int i = 0; i < numOffspring; i++) {
            Rocket parent1 = selectedRockets[i % selectedRockets.length];
            Rocket parent2 = selectedRockets[(i + 1) % selectedRockets.length];
            double crossoverPoint = Math.random();
            Rocket child = new Rocket(crossoverPoint * parent1.getX() + (1 - crossoverPoint) * parent2.getX(),
                    crossoverPoint * parent1.getY() + (1 - crossoverPoint) * parent2.getY());
            child.setVX(crossoverPoint * parent1.getVX() + (1 - crossoverPoint) * parent2.getVX());
            child.setVY(crossoverPoint * parent1.getVY() + (1 - crossoverPoint) * parent2.getVY());
            offspring[i] = child;
        }
        return offspring;
    }

    private void polynomialMutation(Rocket rocket) {
        double mutationStrength = 0.5; // Adjust this value to control mutation intensity
        rocket.setVX(rocket.getVX() + (Math.random() * 2 - 1) * mutationStrength);
        rocket.setVY(rocket.getVY() + (Math.random() * 2 - 1) * mutationStrength);
        rocket.setAngle(Math.atan2(rocket.getVY(), rocket.getVX()));
    }

    private void replaceLeastFitRockets(Rocket[] population, Rocket[] offspring, Rocket[] eliteRockets) {
        int replacementSize = Math.min(offspring.length, population.length);
        for (int i = 0; i < replacementSize; i++) {
            population[population.length - i - 1] = offspring[i];
        }
        int eliteSize = Math.min(eliteRockets.length, population.length);
        for (int i = 0; i < eliteSize; i++) {
            population[i] = eliteRockets[i];
        }
    }

    private double evaluateFitness(Rocket rocket) {
        double distance = Math.sqrt(Math.pow(rocket.getX() - target.getX(), 2) + Math.pow(rocket.getY() - target.getY(), 2));
        return 1 / distance;
    }

    private Rocket getFittestRocket(Rocket[] rockets) {
        return Arrays.stream(rockets).max(Comparator.comparingDouble(Rocket::getFitness)).orElse(null);
    }

    public Target getTarget() {
        return target;
    }

    public Rocket[] getRockets() {
        return population;
    }

    public int getPopulationSize() {
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
        this.population = new Rocket[size];
        initializePopulation();
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    public int getSuccessfulRocketsCount() {
        return successfulRocketsCount;
    }

    private void evaluatePopulation() {
        for (Rocket rocket : population) {
            rocket.setFitness(evaluateFitness(rocket));
        }
    }

    private void updateBestFitness() {
        double currentBestFitness = getFittestRocket().getFitness();
        if (currentBestFitness > bestFitnessSoFar) {
            bestFitnessSoFar = currentBestFitness;
        }
    }

    public void reset() {
        currentGeneration = 0;
        bestFitnessSoFar = 0;
        successfulRocketsCount = 0;
        generationFinished = false;
        initializePopulation();
    }

    private void initializePopulation() {
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            double x = 400; // Starting x position
            double y = 600; // Starting y position
            double vx = random.nextDouble() * 2 - 1;
            double vy = random.nextDouble() * 2 - 1;
            Rocket rocket = new Rocket(x, y);
            rocket.setVX(vx);
            rocket.setVY(vy);
            rocket.setAngle(Math.atan2(vy, vx));
            population[i] = rocket;
        }
    }
}
