package tsp.metaheuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;

public class ColoniesDeFourmis extends AMetaheuristic{

	private ArrayList<Ant> colonie;
	private int[][] tableau_pheromone;
	
	public ColoniesDeFourmis(Instance instance, String name, ArrayList<Ant> colo,int[][] tableau_pheromone) throws Exception {
		super(instance, name);
		this.colonie = colo;
		this.tableau_pheromone=tableau_pheromone;
	}
	
	public ArrayList<Ant> getColonie() {
		return colonie;
	}

	public int[][] getTableau_pheromone() {
		return tableau_pheromone;
	}

	public void AjouterFourmis( Ant a) {
		this.colonie.add(a);
	}
	
	public void RetirerFourmis(Ant a) {
		this.colonie.remove(a);
	}

	@Override
	public Solution solve(Solution sol) throws Exception {
		Solution res = new Solution(sol.getInstance());
		
		
		return null;
	}
	
	
	
	
}
