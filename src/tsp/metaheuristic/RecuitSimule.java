package tsp.metaheuristic;

import tsp.Instance;
import tsp.Solution;

public class RecuitSimule extends AMetaheuristic{
	
	static double T = 1000;  //température du système
	static double refroidissement = 0.99; //coefficient de refroidissement

	public RecuitSimule(Instance instance, String name) throws Exception {
		super(instance, name);
	}
	
	
	public double ProbaAccepter(double EnergieActuelle, double EnergieNouvelle, double temperature) {
		if(EnergieNouvelle<EnergieActuelle) {
			return 1.0;
		} else {
			return Math.exp(-(EnergieActuelle-EnergieNouvelle)/temperature);
		}
	}
	

	
	@Override
	public Solution solve(Solution sol) throws Exception {
		Solution couranteSol = sol;
		double temperature = T;
		double distance = sol.getObjectiveValue();
		
		Solution meilleureSol = sol;
		Solution nvSol = sol;
		
		while(temperature>1) {
			//nvSol.RSTwoCitiesRandom();
			double EnergieActuelle = meilleureSol.evaluate();
			double EnergieNouvelle = nvSol.evaluate();
			
			if(this.ProbaAccepter(EnergieActuelle, EnergieNouvelle, temperature)>Math.random()) {
				couranteSol = nvSol;
			}
			if(couranteSol.evaluate()<distance) {
				meilleureSol = couranteSol;
				distance = couranteSol.evaluate();
			}
			temperature = temperature*refroidissement;
		}
		
		meilleureSol.setCityPosition(meilleureSol.getCity(0), meilleureSol.getM_nbCities());
		return meilleureSol;
	}
	
	
}
