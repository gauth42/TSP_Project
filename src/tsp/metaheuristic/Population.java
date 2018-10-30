package tsp.metaheuristic;

import java.util.ArrayList;
import java.util.Collections;

import tsp.Instance;
import tsp.Solution;

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
	
	
// -----------------------------
// ----- METHODS ---------------
// -----------------------------
	
	
	//Génère des individus au hasard pour initialiser la population
	
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
	
	//Retourne le meilleur individu d'une population
	
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
	
	public Solution Tournoi() throws Exception {
		Population tournoi = new Population(this.getInstance(), true, this.getTauxmut(), this.isElitisme(), this.getTaillepop(), this.getNbindtournoi());
		for(int i=0; i<this.getNbindtournoi(); i++) {
			int rand = (int)(Math.random()*this.getTaillepop());
			tournoi.setIndividu(this.getIndividus().get(rand), i);
		}
		return tournoi.getMeilleurInd();
	}

	//Applique potentiellement une mutation à un individu
	
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

	//Renvoie l'enfant de deux parents par crossover
	
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

	//Faire évoluer la population
	
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
	
	
	public int getNbindtournoi() {
		return nbindtournoi;
	}

	public Instance getInstance() {
		return instance;
	}

	public boolean isVide() {
		return vide;
	}

	public double getTauxmut() {
		return tauxmut;
	}

	public int getTaillepop() {
		return taillepop;
	}

	public ArrayList<Solution> getIndividus() {
		return individus;
	}


	
	public boolean isElitisme() {
		return elitisme;
	}
	
	public void setIndividu(Solution sol, int index) {
		this.getIndividus().set(index, sol);
	}
}
