package tsp.heuristic;

import tsp.Instance;

public class PPV extends AHeuristic{

	public PPV(Instance instance, String name) throws Exception {
		super(instance, name);
	}

	public void solve() throws Exception {
		
		super.m_solution.setCityPosition(0, 0); // Initialisation premier sommet à la ville 0
		super.m_solution.setCityPosition(0, super.m_solution.getM_nbCities()); // dernier sommet à 0
		for (int i = 1; i<super.m_solution.getM_nbCities();i++) {
			super.m_solution.setCityPosition(super.m_solution.getPPVSuivant(), i); // On prend le plus proche voisin non visité et on l'ajoute à al suite
		}
	
		
	}
	
	
		
}
