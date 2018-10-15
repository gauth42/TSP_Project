package tsp.metaheuristic;

import tsp.Instance;
import tsp.TSPSolver;

public class Ant extends TSPSolver {
	
	private int matricule;
	private int[] villes_visitees;
	private int[] chemin_parcouru;
	private int distance_parcourue;
	private int position;
	private int nbVillesParcoures;
	
	

	public Ant(Instance instance, long timeLimit,int m) throws Exception {
		super(instance, timeLimit);
		this.matricule = m;
		this.villes_visitees = new int[instance.getNbCities()];
		this.chemin_parcouru = new int[instance.getNbCities()+1];
		this.distance_parcourue = 0;
		this.position = 0;
		this.nbVillesParcoures=0;
	}
	
	public void initAnt() {
		this.villes_visitees[0]=1;
		this.chemin_parcouru[0]=0;
		this.chemin_parcouru[super.getInstance().getNbCities()]=0;
		this.nbVillesParcoures=1;
	}
	
	public void AVisite(int num_ville) {
		this.villes_visitees[num_ville]=1;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public int getDistanceParcourue() {
		return this.distance_parcourue;
	}
	
	public int getMatricule() {
		return this.matricule;
	}
	
	public int getNbVillesParcourues() {
		return this.nbVillesParcoures;
	}
	
	public void MAJCheminParcouru(int num_ville) {
		this.chemin_parcouru[this.nbVillesParcoures]=num_ville;
		this.nbVillesParcoures++;
	}
	
	public void avancéeFourmi() {
		
	}
	
	public void MAJPheromones() {
		if(this.getNbVillesParcourues()==super.getInstance().getNbCities()) {
			
		}
	}
	

	

}
