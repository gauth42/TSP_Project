package tsp;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * This class models a TSP Solution.
 * 
 * **Internal structure of the class:**
 * 
 * This solution itself is stored in the array #m_cities.
 *
 * #m_cities[i] should be the index of the element at index i in the solution.
 * 
 * The attribute #m_nbCities is the number of cities in the problem.
 * The array #m_cities has size m_nbCities+1 and it should start with the first city for the route and end with the same city.
 * 
 * For example, suppose a problem with 5 customers (hence, the corresponding cities index range from 0 to 4).
 * The following array represents a TSP solution (tour) that visit all customers by index order:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * m_cities = [0, 1, 2, 3, 4, 0];
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * A recommendation is to always have **m_cities[0]=m_cities[m_nbCities]= 0**.
 * 
 * So a complete feasible solution should respect the following rules:
 * 	- m_cities[0]=m_cities[m_nbCities]
 * 	- Apart from the city at indices 0 and #m_nbCities, each customer index should be represented once and only one in #m_cities.
 * 
 * ** Creating, accessing or modifying a solution:**
 * 
 * A Solution object is created for a given TSP problem, represented by an Instance object.
 * Let instance be an object of class Instance, the following code creates a solution for this problem:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Solution mySolution = new Solution(instance);
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * During its creation, this object is initialized with city 0 at all position. It should be modified to represent a feasible solution.
 * This modification is done by the method {@link #setCityPosition(int city, int position)}, which sets city s at position in the solution.
 * For example, the following code creates a TSP solution that visits all customers in increasing city order:
 * 
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * for(int i=0; i < instance.getNbCities(); i++)
 * {
 * 	mySolution.setVertexPosition(i, i);
 * }
 * mySolution.setVertexPosition(0, instance.getNbCities());
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 * To explore a {@link Solution} object, the city at position can be obtained calling {@link #getCity(int position)}.
 * 
 * The {@link #isFeasible} method allows to check the feasibility of a solution.
 * 
 * To calculate the cost of a solution, you can call {@link #evaluate}.
 * 
 * Note that {@link #evaluate()} recomputes every distance from zero. If you do a slight modification of the solution it is less time consuming to update
 * yourself the cost of the modified solution using the function {@link #setObjectiveValue(long newValue)}.
 * 
 * 
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
public class Solution{

	// -----------------------------
	// ----- ATTRIBUTS -------------
	// -----------------------------

	/**
	 * 
	 * This array stores the cities that constitute the solution.<br>
	 * #m_cities[i] is the index of the city at index i in the solution.
	 */
	private int[] m_cities;

	/**
	 * Route cost or length (objective function).<br>
	 * This value should be updated when the solution is modified with:
	 * 	- calling {@link #evaluate()} which recomputes the cost
	 * 	- {@link #setObjectiveValue(long)}
	 */
	private long m_objectiveValue;

	/** Data of the problem associated with the solution */
	private Instance m_instance;

	/** Number of cities in the problem. */
	private int m_nbCities;

	/** Error code returned by {@link #isFeasible()} */
	private String m_error;


	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance
	 * @param instance The instance of the problem
	 */
	public Solution(Instance instance) {
		m_instance = instance;
		m_nbCities = instance.getNbCities();
		m_cities = new int[m_nbCities + 1];
	}

	/**
	 * Creates a copy of this object solution
	 */
	public Solution copy() {
		Solution copySolution = new Solution(m_instance);
		copySolution.m_cities = Arrays.copyOf(m_cities, m_nbCities + 1);
		copySolution.m_objectiveValue = m_objectiveValue;
		return copySolution;

	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * Set city indexCity at position position in the solution.<br>
	 * Note that if there is already a city index at position then this city is replaced by indexCity.
	 * 
	 * @param indexCity index of the city to insert.
	 * @param position insertion position.
	 * @throws Exception returns an error if position is not a valid insertion position or indexCity is not a valid city number.
	 */
	public void setCityPosition(int indexCity, int position) throws Exception {
		if ((position < 0) || (position > m_nbCities))
			throw new Exception("Error Instance.setVertexPosition(i,s): index i=" + position
					+ ", must range between 0 and " + m_nbCities);
		if ((indexCity < 0) || (indexCity >= m_nbCities))
			throw new Exception("Error Instance.setVertexPosition(i,s) : city value s="
					+ indexCity + ", must range between 0 and "	+ (m_nbCities - 1));
		m_cities[position] = indexCity;
	}

	/**
	 * Recomputes the objective value of the solution and return its value.
	 * 
	 * @throws Exception
	 */
	public double evaluate() throws Exception {
		m_objectiveValue = 0;
		for (int i = 0; i < m_nbCities; i++) {
			m_objectiveValue += m_instance.getDistances(m_cities[i],m_cities[i + 1]);
		}
		return m_objectiveValue;
	}

	/**
	 * Check that the solution is feasible.
	 * 
	 * The performed tests are the following :
	 * 	- #m_cities[0]=#m_cities[#m_nbCities] 
	 * 	- apart from the city at index 0 and #m_nbCities, each customer index should be represented once and only one in #m_cities.
	 * 
	 * The cost is recomputed.
	 * 
	 * When an error is met, the error code can be obtained calling the method {@link #getError()}.
	 * 
	 * @return `true` if the solution is feasible `false` otherwise.
	 * @throws Exception
	 */
	public boolean isFeasible() throws Exception {
		boolean result = true;
		m_error = "";
		// Check first that the first and last cities are the same
		if (m_cities[0] != m_cities[m_nbCities]) {
			m_error += "Error : The route should start and end with the same city.\n";
			m_error += "The first city is " + m_cities[0]
					+ " and the last city is " + m_cities[m_nbCities]
							+ ".\n";
			result = false;
		}

		// Check that all cities are visited
		int[] occurences = new int[m_nbCities];
		Arrays.fill(occurences, 0);
		for (int i = 0; i < m_nbCities; i++) { 
			// the last city is not included
			occurences[m_cities[i]]++;
		}
		for (int i = 1; i < m_nbCities; i++) {
			if (occurences[i] != 1) {
				m_error += "Error : the city " + (i) + " is visited "
						+ occurences[i] + " times.\n";
				result = false;
			}
		}

		evaluate();
		if (result)
			m_error += "The solution is feasible.";
		else
			m_error += "The solution is not feasible.";
		return result;
	}

	/**
	 * Print the solution on output `out`. The solution is printed
	 * using cities numbers and not labels.
	 * 
	 * For example if you want to print a solution `mysol` on the
	 * screen, use :
	 * 
	 * `mysol.print(System.err);`
	 * 
	 * Reminder: do not print on System.out.
	 * 
	 * @param out output
	 */
	public void print(PrintStream out) {
		out.println("#### SOLUTION ####");
		out.println("objectiveValue \t"+m_objectiveValue);
		out.print(m_cities[0]);
		for (int i = 1; i <= m_nbCities; i++) {
			out.print("-" + m_cities[i]);
		}
		out.println();
		out.println("#### END SOLUTION ####");
	}
	
	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------

	/**
	 * @return The objective value of the function. This value should be updated when the
	 *         solution is modified, either calling {@link evaluate} which recomputes the cost or
	 *         {@link setObjectiveValue}
	 */
	public long getObjectiveValue() {
		return m_objectiveValue;
	}

	/**
	 * Sets the cost of the solution.
	 * @param newValue he new solution cost
	 */
	public void setObjectiveValue(long newValue) {
		this.m_objectiveValue = newValue;
	}

	/**
	 * @return return a link to the data of the problem associated with the solution.
	 */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * Returns the index of the city at position in the tour.
	 * @param position the position of the city
	 * @throws exception returns an error if i is not a valid position.
	 */
	public int getCity(int position) throws Exception {
		if ((position < 0) || (position > m_nbCities))
			throw new Exception("Error Instance.getSolution : index " + position
					+ " is not valid, it should range between 0 and "
					+ m_nbCities);
		return m_cities[position];
	}

	/**
	 * @return error code returned by `validate`
	 */
	public String getError() {
		return m_error;
	}

	/**
	 * @return the m_cities
	 */
	public int[] getM_cities() {
		return m_cities;
	}

	/**
	 * @return the m_nbCities
	 */
	public int getM_nbCities() {
		return m_nbCities;
	}
	
	
// ---------------------------------------------------
// ------------- METHODS ADDED -----------------------
// ---------------------------------------------------
	
	/** Check if the city number ville has been visited
	 * The city number 0 is always the first to be visited
	 * As the city are added in ascending order the algorithm stops when it finds 0 in m_cities[]
	 * 
	 * @param ville  number of the city to be checked
	 * @return true if the city is visited, otherwise false
	 */
	public boolean estVisite(int ville) {
		boolean res = false;
		int curseur = 1;
		while(m_cities[curseur]!=0 && !res) { 
			res = res || (ville == m_cities[curseur]); 
			curseur++; 
		}
		return res;
	}
	
	
	/** Find the nearest city of the last one added in m_cities[]
	 * The first step is to get the last city visited
	 * The second is to find its nearest city
	 * 
	 * @return the number of the next city to be added in the solution
	 * @throws Exception
	 */
	public int getPPVSuivant() throws Exception {
		int res = 1;
		int dernier_voisin =1;
		long distance_mini = 1000000000; 
		
		while(m_cities[dernier_voisin]!=0) {
			dernier_voisin++;//
		}
		
		for(int i=1;i<this.m_nbCities;i++) {
			if(!estVisite(i)) { // si on n'a pas déjà visité la ville
				if(this.m_instance.getDistances(m_cities[dernier_voisin-1], i)<distance_mini) { 
					distance_mini = this.m_instance.getDistances(m_cities[dernier_voisin-1], i);
					res = i;
					
				}
			}
		}
		return res;
	}
	
	
	/** Change the position of the city at the index index 1 and index2.
	 * 
	 * @param index1 the index of the first element to swap
	 * @param index2 the index of the second element to swap
	 * @throws Exception
	 */
	public void swap(int index1, int index2) throws Exception {
		int ville1 = this.getCity(index1);
		this.setCityPosition(this.getCity(index2), index1);
		this.setCityPosition(ville1, index2);
	}
	
	//---------------------------------
	//------OPTIMISATION SOLUTION------
	//---------------------------------
	
	/**
	 * 
	 * @param sol the solution which is 
	 * @param i
	 * @param k
	 * @return
	 * @throws Exception
	 */
	public Solution optSwap(Solution sol, int i, int k) throws Exception{
		Solution copy = sol.copy();
		Solution solSwap = sol.copy();
		for (int j=i; j<=k; j++) {
			solSwap.setCityPosition(copy.getCity(j), i+k-j);
		}
		return solSwap;
	}
	
	/**
	 * 
	 * @param sol
	 * @return
	 * @throws Exception
	 */
	public Solution opt(Solution sol) throws Exception {
		Solution newSolution = sol.copy();
		
		int amelioration = 0;
		while (amelioration<5) {
			double meilleureDistance = sol.evaluate();
			for (int i=1; i<m_nbCities-1; i++) {
				for (int k=i+1; k<m_nbCities; k++) {
					newSolution = optSwap(sol, i, k);
					double newDistance = newSolution.evaluate();
					if (newDistance<meilleureDistance) {
						amelioration = 0;
						sol = newSolution.copy();
						meilleureDistance = newDistance;
					}
				}
			}
			amelioration++;
		}
		return sol;
	}
	
	
	/**
	 * 
	 * @param sol
	 * @param i
	 * @param k
	 * @param j
	 * @return
	 * @throws Exception
	 */
	public Solution troisOptSwap(Solution sol, int i, int k, int j) throws Exception {
		Solution copy = sol.copy();
		Solution solSwap = sol.copy();
		int lon = k-i;
		for (int l=i; l<=k; l++) {
			solSwap.setCityPosition(copy.getCity(l), j-lon);
			lon--;
		}
		int pos = 1;
		for (int m=k+1; m<j; m++) {
			solSwap.setCityPosition(copy.getCity(m), i+pos);
			pos++;
		}
		solSwap.setCityPosition(copy.getCity(j), i);
		return solSwap;
	}
	
	
	/**
	 * 
	 * @param sol
	 * @return
	 * @throws Exception
	 */
	public Solution troisOpt(Solution sol) throws Exception {
		Solution newSolution = sol.copy();
		int amelioration = 0;
		while (amelioration<5) {
			double meilleureDistance = sol.evaluate();
			for (int i=1; i<m_nbCities-2; i++) {
				for (int k=i+1; k<m_nbCities-1; k++) {
					for (int j=k+1; j<m_nbCities; j++) {
						newSolution = troisOptSwap(sol, i, k, j);
						double newDistance = newSolution.evaluate();
						if (newDistance<meilleureDistance) {
							amelioration = 0;
							sol = newSolution.copy();
							meilleureDistance = newDistance;
						}
					}
				}
			}
			amelioration++;
		}
		return sol;
	}
	
	
	/** Allow us to see a solution through a string with the order of visited cities
	 * 
	 * @return A String describing the solution
	 */
	public String toString() {
		String res = "{";
		for(int i=0;i<this.getM_nbCities();i++) {
			res = res + this.m_cities[i]+", ";
		}
		res = res + " 0}";
		return res;
	}
	
	
	/** Check if a solution contains a city
	 * 
	 * @param val 
	 * @return true if the solution contains the city val, otherwise false
	 * @throws Exception
	 */
	public boolean contient(int val) throws Exception {
		boolean res = this.getCity(0)==val;
		int compteur=0;
		while(!res && compteur<this.getM_nbCities()) {
			res = this.getCity(compteur)==val;
			compteur++;
		} return res;
	}
}
