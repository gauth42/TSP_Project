package tsp.metaheuristic;

import java.util.ArrayList;

public class ColoniesDeFourmis {

	private ArrayList<Ant> colonie;
	
	public ColoniesDeFourmis(ArrayList<Ant> colo) {
		this.colonie = colo;
	}
	
	public ColoniesDeFourmis() {
		this.colonie = new ArrayList<Ant>();
	}
	
	public void AjouterFourmis( Ant a) {
		this.colonie.add(a);
	}
	
	
	
}
