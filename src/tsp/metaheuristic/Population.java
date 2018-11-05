package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.Collections;

import tsp.Instance;
import tsp.Solution;
/**
 * 
 * @author gauthier.gris
 *
 * This class represents a population with all its features.
 */
public class Population {
	private Instance instance;
	private boolean vide;
	private double tauxmut;
	private int taillepop;
	private ArrayList<Solution> individus;
	private int nbindtournoi;
	public boolean elitisme;

	
// -----------------------------
// ----- CONSTRUCTOR -----------
// -----------------------------
	/**
	 * Initialize a population with random solutions.
	 * 
	 * @param instance
	 * @param vide If true create a population with no solutions
	 * @param tauxmut Mutation rate
	 * @param elitisme If true elitism is applied
	 * @param taillepop Number of solutions in a population
	 * @param nbindtournoi Number of cities in the tournament selection
	 * @throws Exception
	 */
	public Population(Instance instance, boolean vide, double tauxmut, boolean elitisme, int taillepop, int nbindtournoi) throws Exception {
		this.instance=instance;
		this.vide=vide;
		this.tauxmut=tauxmut;
		this.taillepop=taillepop;
		this.nbindtournoi=nbindtournoi;
		this.elitisme=elitisme;
		
		this.individus = new ArrayList<Solution>();
		
		if(this.isVide()) {
			for(int i=0; i<this.getNbindtournoi(); i++) {
				this.individus.add(null);
			}
		} else {
			for(int i=0; i<this.getTaillepop(); i++) {
				individus.add(this.genIndividu());
			}
		}
	}
	
	/**
	 * Initialize a population with solutions derived with swaps of solinit.
	 * 
	 * @param instance
	 * @param vide If true create a population with no solutions
	 * @param tauxmut Mutation rate
	 * @param elitisme If true elitism is applied
	 * @param taillepop Number of solutions in a population
	 * @param nbindtournoi Number of cities in the tournament selection
	 * @param solinit The solution that generates all the population
	 * @throws Exception
	 */
	public Population(Instance instance, boolean vide, double tauxmut, boolean elitisme, int taillepop, int nbindtournoi, Solution solinit) throws Exception {
		this.instance=instance;
		this.vide=vide;
		this.tauxmut=tauxmut;
		this.taillepop=taillepop;
		this.nbindtournoi=nbindtournoi;
		this.elitisme=elitisme;
		this.individus = new ArrayList<Solution>();
		
		int nbswap = 1;
		int nbcity = this.instance.getNbCities();
		individus.add(solinit);
		for(int i=1; i<this.getTaillepop(); i++) {
			Solution sol = solinit.copy();
			for(int j=0; j<nbswap; j++) {
				int index1 = (int)(Math.random()*nbcity);
				int index2 = (int)(Math.random()*nbcity);
				
				while(index1==index2 || index1==0 || index1==this.getInstance().getNbCities() || index2==0 || index2==this.getInstance().getNbCities()) {
					index1 = (int)(Math.random()*nbcity);
					index2 = (int)(Math.random()*nbcity);
				}
				sol.swap(index1, index2);
			}
			individus.add(sol);
		}
	}
	
// -----------------------------
// ----- METHODS ---------------
// -----------------------------
	
	
	
	/**
	 * Create a random solution.
	 * 
	 * @return A new solution to fill the population
	 * @throws Exception
	 */
	public Solution genIndividu() throws Exception {
		ArrayList<Integer> liste = new ArrayList<Integer>();
		for(int i=1; i<this.getInstance().getNbCities(); i++) {
			liste.add(i);
		}
		Collections.shuffle(liste);
		
		Solution individu = new Solution(this.getInstance());
		
		for(int i=0; i<this.getInstance().getNbCities()-1; i++) {
			individu.setCityPosition(liste.get(i), i+1);
		}
		return individu;
	}
	
	
	
	/**
	 * This method calculates all solutions' objectiveValue and return the solution with the best one.
	 * 
	 * @return The best solution of a population
	 * @throws Exception
	 */
	public Solution getMeilleurInd() throws Exception {
		
		double min = this.getIndividus().get(0).evaluate();
		int indice = 0;
		
		for(int i=1; i<this.getIndividus().size(); i++) {
			if(this.getIndividus().get(i).evaluate()<min) {
				indice = i;
				min = this.getIndividus().get(i).getObjectiveValue();
			}
		} 
		return this.getIndividus().get(indice);
	}
	
	//Retourne le meilleur individu du tournoi
	
	/**
	 * Create a tournament:
	 * nbindtournoi are selected randomly in the population and the one with the best objectiveValue is the "winner" of the tournament.
	 * 
	 * @return the winner of the tournament
	 * @throws Exception
	 */
	public Solution Tournoi() throws Exception {
		Population tournoi = new Population(this.getInstance(), true, this.getTauxmut(), this.isElitisme(), this.getNbindtournoi(), this.getNbindtournoi());
		for(int i=0; i<this.getNbindtournoi(); i++) {
			int rand = (int)(Math.random()*this.getTaillepop());
			tournoi.setIndividu(this.getIndividus().get(rand), i);
		}
		return tournoi.getMeilleurInd();
	}

	
	/**
	 * This methods generation a random number between 0 and 1.
	 * If this number is inferior to the mutation rate, a mutation is applied to the solution.
	 * The mutation is a swap of two cities in the solution.
	 * 
	 * @param individu the solution which possibly receives a mutation
	 * @return
	 * @throws Exception
	 */
	public Solution Muter(Solution individu) throws Exception {
		for(int pos1=1; pos1<individu.getM_nbCities(); pos1++) {
			if(Math.random()<this.getTauxmut()) {
				int pos2 = (int)Math.random()*individu.getM_nbCities();
				if(pos2==0) {
					pos2++;
				}
				individu.swap(pos1, pos2);
			}
		}
		return individu;
	}

	
	
	/**
	 * Two integers "debut" and "fin" are randomly chosen between 1 and taillepop-1.
	 * A child solution is created through the order of cities in the parent's solutions.
	 * 
	 * 
	 * 
	 * @param parent1 The first solution chosen
	 * @param parent2 The second solution chosen
	 * @return the child solution created with the two parents
	 * @throws Exception
	 */
	public Solution Crossover(Solution parent1, Solution parent2) throws Exception {
		
		Solution child = new Solution(this.getInstance());
		int debut = (int)(Math.random()*parent1.getM_nbCities());
		int fin = (int)(Math.random()*parent1.getM_nbCities());
		
		while(debut==fin || debut==0 || debut==this.getInstance().getNbCities() || fin==0 || fin==this.getInstance().getNbCities()) {
			debut = (int)(Math.random()*parent1.getM_nbCities());
			fin = (int)(Math.random()*parent1.getM_nbCities());
		}
		
		for(int i=1; i<parent1.getM_nbCities(); i++) {
			if(debut<fin && (i>=debut && i<=fin)) {
				child.setCityPosition(parent1.getCity(i), i);
			} else if (fin < debut) {
				if(i<=fin || i>=debut) {
					child.setCityPosition(parent1.getCity(i), i);
				}
			}	
		}
	
	
		for(int i=1; i<parent2.getM_nbCities(); i++) {
			if(!child.contient(parent2.getCity(i))) {
				int j=1;
				boolean place = child.getCity(j)==0;
				if(place) {
					child.setCityPosition(parent2.getCity(i), 1);
				}
				while(!place && j<child.getM_nbCities()-1) {
					j++;
					if (child.getCity(j)==0) {
						place = true;
						child.setCityPosition(parent2.getCity(i), j);
					}
				}
			}
		}
		return child;
	}

	
	/**
	 * This method makes the generation evolve to the next generation
	 * 
	 * For each member of the new population :
	 * 		After two tournaments, two solution are selected to be the parents.
	 * 		A solution child is created from the crossover of these two parents.
	 * 		The child is subject to a mutation
	 * 		This solution is finally added in the new population
	 * 
	 * If elitism is applied, the best solution is kept from a generation to another
	 * @return
	 * @throws Exception
	 */
	public Population EvolvePopulation() throws Exception {
		Population newPop = new Population(this.getInstance(), this.isVide(), this.getTauxmut(), this.isElitisme(), this.getTaillepop(), this.getNbindtournoi());
		int eli=0;
		if(elitisme) {
			newPop.setIndividu(this.getMeilleurInd(), 0);
			eli =1;
		}
		for(int i=eli; i<newPop.getTaillepop(); i++) {
			Solution parent1 = this.Tournoi();
			Solution parent2 = this.Tournoi();
			Solution child = this.Crossover(parent1, parent2);
			if(Math.random()<0.01) {
			}
			newPop.setIndividu(child, i);
		}
		for(int i=eli; i<newPop.getTaillepop(); i++) {
			newPop.setIndividu(this.Muter(newPop.getIndividus().get(i)), i);
		}
		return newPop;
	}


// -----------------------------
// ----- GETTERS / SETTERS -----
// -----------------------------
	
	/**
	 * 
	 * @return The number of members in a tournament
	 */
	public int getNbindtournoi() {
		return nbindtournoi;
	}

	/**
	 * 
	 * @return The instance used
	 */
	public Instance getInstance() {
		return instance;
	}

	/**
	 * 
	 * @return yes if the population is empty, otherwise false
	 */
	public boolean isVide() {
		return vide;
	}
	
	/**
	 * 
	 * @return The mutation rate
	 */
	public double getTauxmut() {
		return tauxmut;
	}

	/**
	 * 
	 * @return The number of solutions in the population
	 */
	public int getTaillepop() {
		return taillepop;
	}

	/**
	 * 
	 * @return An ArrayList containing all the solutions of the population
	 */
	public ArrayList<Solution> getIndividus() {
		return individus;
	}

	/**
	 * 
	 * @return yes if elitism is applied, otherwise false
	 */
	public boolean isElitisme() {
		return elitisme;
	}
	
	/**
	 * Set the solution sol at the index index in the population
	 * 
	 * @param sol The solution to be placed in the population
	 * @param index The index to place the solution
	 */
	public void setIndividu(Solution sol, int index) {
		this.getIndividus().set(index, sol);
	}
}
