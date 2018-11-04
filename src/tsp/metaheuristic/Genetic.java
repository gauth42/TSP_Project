package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;

public class Genetic extends AMetaheuristic{
	private Population population;
	private int nbgene;
	
	
// -----------------------------
// ----- CONSTRUCTOR -----------
// -----------------------------
	
	public Genetic(Instance instance, String name, double tauxmut, boolean elitisme, int nbindtournoi, int nbgene, int taillepop) throws Exception {
		super(instance, name);
		this.nbgene=nbgene;
		
		this.population = new Population(instance, false, tauxmut, elitisme, taillepop, nbindtournoi);
	}

	public Genetic(Instance instance, String name, double tauxmut, boolean elitisme, int nbindtournoi, int nbgene, int taillepop, Solution solinit) throws Exception {
		super(instance, name);
		this.nbgene=nbgene;
		
		this.population = new Population(instance, false, tauxmut, elitisme, taillepop, nbindtournoi, solinit);
	}

// -----------------------------
// ----- METHODS ---------------
// -----------------------------
	
	@Override
	public Solution solve(Solution sol) throws Exception {
		for(int i=0; i<this.getNbgene(); i++) {
			this.setPopulation(this.getPopulation().EvolvePopulation());
			if(i%100==0) {
				System.out.println("géné num = "+(i+1)+"   et objective value = "+this.getPopulation().getMeilleurInd().getObjectiveValue());
			}	
		}
		return this.getPopulation().getMeilleurInd();
	}


// -----------------------------
// ----- GETTERS / SETTERS -----
// -----------------------------

	public Population getPopulation() {
		return population;
	}

	public int getNbgene() {
		return nbgene;
	}


	
	public void setPopulation(Population population) {
		this.population = population;
	}


	
	


}
