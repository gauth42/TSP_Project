package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;

/**
 * 
 * @author gauthier.gris
 *
 *This is a genetic algorithm. The principle is to create a population of solutions with different features.
 *For each generation, some solutions are selected through a process to create new solutions (called child).
 *On top of that each solution can receive a mutation (depending on the mutation rate).
 */
public class Genetic extends AMetaheuristic{
	private Population population;
	private int nbgene;
	
	
// -----------------------------
// ----- CONSTRUCTOR -----------
// -----------------------------
	/**
	 * Initialize the genetic algorithm with a population.
	 * 
	 * @param instance the instance used
	 * @param name 
	 * @param tauxmut The mutation rate
	 * @param elitisme If true elitism is applied
	 * @param nbindtournoi Number of cities in the tournament selection
	 * @param nbgene Number of generations
	 * @param taillepop Number of solutions in a population
	 * @throws Exception
	 */
	public Genetic(Instance instance, String name, double tauxmut, boolean elitisme, int nbindtournoi, int nbgene, int taillepop) throws Exception {
		super(instance, name);
		this.nbgene=nbgene;
		
		this.population = new Population(instance, false, tauxmut, elitisme, taillepop, nbindtournoi);
	}

	/**
	 * Initialize the genetic algorithm with a population created with an existing solution.
	 * 
	 * @param instance the instance used
	 * @param name 
	 * @param tauxmut The mutation rate
	 * @param elitisme If true elitism is applied
	 * @param nbindtournoi Number of cities in the tournament selection
	 * @param nbgene Number of generations
	 * @param taillepop Number of solutions in a population
	 * @param solinit
	 * @throws Exception
	 */
	public Genetic(Instance instance, String name, double tauxmut, boolean elitisme, int nbindtournoi, int nbgene, int taillepop, Solution solinit) throws Exception {
		super(instance, name);
		this.nbgene=nbgene;
		
		this.population = new Population(instance, false, tauxmut, elitisme, taillepop, nbindtournoi, solinit);
	}

// -----------------------------
// ----- METHODS ---------------
// -----------------------------
	
	/**
	 * This method realizes the transition to the next generation until the number of generations due
	 */
	@Override
	public Solution solve(Solution sol) throws Exception {
		for(int i=0; i<this.getNbgene(); i++) {
			this.setPopulation(this.getPopulation().EvolvePopulation());
		}
		return this.getPopulation().getMeilleurInd();
	}


// -----------------------------
// ----- GETTERS / SETTERS -----
// -----------------------------

	/**
	 * 
	 * @return the population
	 */
	public Population getPopulation() {
		return population;
	}

	/**
	 * 
	 * @return the number of generation
	 */
	public int getNbgene() {
		return nbgene;
	}

	/**
	 * Set a population for the instance this.population
	 * @param population
	 */
	public void setPopulation(Population population) {
		this.population = population;
	}


	
	


}
