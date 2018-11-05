package tsp.heuristic;

import tsp.Instance;

/**
 * This class is a PPV algorithm.
 * The principle is to always add the nearest city in the solution. 
 * @author gauthier.gris
 *
 */
public class PPV extends AHeuristic{

	public PPV(Instance instance, String name) throws Exception {
		super(instance, name);
	}

	/**
	 * We initialize the first and the last element of the solution with the city number 0;
	 * Then we complete the solution by adding the nearest city until the solution is filled.
	 */
	public void solve() throws Exception {
		
		super.m_solution.setCityPosition(0, 0);
		super.m_solution.setCityPosition(0, super.m_solution.getM_nbCities());
		for (int i = 1; i<super.m_solution.getM_nbCities();i++) {
			super.m_solution.setCityPosition(super.m_solution.getPPVSuivant(), i);
		}
	
		
	}
	
	
		
}
