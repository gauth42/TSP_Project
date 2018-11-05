package tsp.metaheuristic;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
/**
 * 
 * @author gauthier.gris
 *
 */
public class ColoniesDeFourmis extends AMetaheuristic{

	private ArrayList<Ant> colonie;
	private int[][] tableau_pheromone;
	/**
	 * 
	 * @param instance
	 * @param name
	 * @param colo
	 * @param tableau_pheromone
	 * @throws Exception
	 */
	public ColoniesDeFourmis(Instance instance, String name, ArrayList<Ant> colo,int[][] tableau_pheromone) throws Exception {
		super(instance, name);
		this.colonie = colo;
		this.tableau_pheromone=tableau_pheromone;
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Ant> getColonie() {
		return colonie;
	}
	/**
	 * 
	 * @return
	 */
	public int[][] getTableau_pheromone() {
		return tableau_pheromone;
	}
	/**
	 * 
	 * @param a
	 */
	public void AjouterFourmis( Ant a) {
		this.colonie.add(a);
	}
	/**
	 * 
	 * @param a
	 */
	public void RetirerFourmis(Ant a) {
		this.colonie.remove(a);
	}
	/**
	 * 
	 */
	@Override
	public Solution solve(Solution sol) throws Exception {
		Solution res = new Solution(sol.getInstance());
		
		
		return null;
	}
	
	
	
	
}
