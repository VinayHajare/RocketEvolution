
# Rocket Evolution 🚀🔥

## About 🗣️

This project simulates the evolution of rockets using genetic algorithms. The goal is to demonstrate the concept of survival of the fittest, where the fittest rockets are selected to reproduce and create new offspring. The genetic algorithm is used to evolve the rockets over time, resulting in a population of rockets that are better adapted to their environment.

## Concept 📚

The genetic algorithm is a type of optimization technique inspired by the process of natural selection. It works by creating a population of candidate solutions, evaluating their fitness, and selecting the fittest individuals to reproduce and create new offspring. The process is repeated over multiple generations, resulting in a population of solutions that are better adapted to the problem.  

A genetic algorithm (GA) is a metaheuristic inspired by the process of natural selection that belongs to the larger class of evolutionary algorithms (EA). Genetic algorithms are commonly used to generate high-quality solutions to optimization and search problems by relying on biologically inspired operators such as mutation, crossover and selection.

It is inspired by Charles Darwin's theory of Natural Selection.

<p align="center"><b><i>Survival Of The Fittest.</i></b></p> 

## How it works 🤔

1. **Initialization**: A population of rockets is created with random parameters.
2. **Evaluation**: The fitness of each rocket is evaluated based on its performance.
3. **Selection**: The fittest rockets are selected to reproduce and create new offspring.
4. **Crossover**: The selected rockets are combined to create new offspring.
5. **Mutation**: The new offspring are mutated to introduce random changes.
6. **Replacement**: The least fit rockets are replaced with the new offspring.
7. **Repeat**: The process is repeated over multiple generations.
  
<p align="center"><img src="rockets.gif" width=500></p>

## Features 🎉

* Simulation of rocket evolution using genetic algorithms
* Survival of the fittest concept
* Genetic algorithm optimization technique
* Multiple generations of rockets
* Random mutations and crossover

## Getting started 🚀

1. Clone the repository: `git clone https://github.com/VinayHajare/RocketEvolution.git`
2. Run the simulation: `java -jar RocketEvolution.jar`
3. Observe the evolution of the rockets over time.

## Contributing 🤝

Contributions are welcome! If you have any ideas or suggestions, please open an issue or submit a pull request.

## License 📝

This project is licensed under the MIT License. See the LICENSE file for details.

### Genetic Algorithm
 

### How does it work ?
Here a population of rockets attempt to find a way to the target without crashing. We initially start with a population of rockets that have a DNA with random genes. After the life span of this population we use a fitness function to decide the fitness of each rocket. We then select two rockets as parents based on their fitness score for reproduction.

<p align="center"><img src="rockets.gif" width=500></p>

A crossover point is chosen in DNA of the child and it contains the DNA of first parent before the crossover point and DNA of second parent after crossover point.

After a child is produced by crossover we mutate some of the genes in the DNA. There is a very slight probability for a gene to mutate. Mutations are important to maintain diversity in a populationand to prevent the premature convergence.

This cycle of selection, crossover and mutation goes on and after some time a convergence in the maximum fitness of the population is achieved beyond which the fitness cannot increase. Then the offsprings produced are not significantly different from the previous generation. We then say that a set of solution to our problem is achieved by the genetic algorithm.
