package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import view.Gomme;


/**
 * class used to represent plan. It will provide for a given set of results an action to perform in each result
 */
class Plans{
	ArrayList<Result> results;
	ArrayList<ArrayList<String>> actions;
	
	/**
	 * construct an empty plan
	 */
	public Plans() {
		this.results = new ArrayList<Result>();
		this.actions = new ArrayList<ArrayList<String>>();
	}
	
	/**
	 * add a new pair of belief-state and corresponding (equivalent) actions 
	 * @param beliefBeliefState the belief state to add
	 * @param action a list of alternative actions to perform. Only one of them is chosen but their results should be similar
	 */
	public void addPlan(Result beliefBeliefState, ArrayList<String> action) {
		this.results.add(beliefBeliefState);
		this.actions.add(action);
	}
	
	/**
	 * return the number of belief-states/actions pairs
	 * @return the number of belief-states/actions pairs
	 */
	public int size() {
		return this.results.size();
	}
	
	/**
	 * return one of the belief-state of the plan
	 * @param index index of the belief-state
	 * @return the belief-state corresponding to the index
	 */
	public Result getResult(int index) {
		return this.results.get(index);
	}
	
	/**
	 * return the list of actions performed for a given belief-state
	 * @param index index of the belief-state
	 * @return the set of actions to perform for the belief-state corresponding to the index
	 */
	public ArrayList<String> getAction(int index){
		return this.actions.get(index);
	}
}

/**
 * class used to represent a transition function i.e., a set of possible belief states the agent may be in after performing an action
 */
class Result{
	private ArrayList<BeliefState> beliefStates;

	/**
	 * construct a new result
	 * @param states the set of states corresponding to the new belief state
	 */
	public Result(ArrayList<BeliefState> states) {
		this.beliefStates = states;
	}

	/**
	 * returns the number of belief states
	 * @return the number of belief states
	 */
	public int size() {
		return this.beliefStates.size();
	}

	/**
	 * return one of the belief state
	 * @param index the index of the belief state to return
	 * @return the belief state to return
	 */
	public BeliefState getBeliefState(int index) {
		return this.beliefStates.get(index);
	}
	
	/**
	 * return the list of belief-states
	 * @return the list of belief-states
	 */
	public ArrayList<BeliefState> getBeliefStates(){
		return this.beliefStates;
	}
}


/**
 * class implement the AI to choose the next move of the Pacman
 */
public class AI{
	/**
	 * function that compute the next action to do (among UP, DOWN, LEFT, RIGHT)
	 * @param beliefState the current belief-state of the agen
	 * @param deepth the deepth of the search (size of the largest sequence of action checked)
	 * @return a string describing the next action (among PacManLauncher.UP/DOWN/LEFT/RIGHT)
	 */
	public static String findNextMove(BeliefState beliefState) {

		int deepth = 3; //La profondeur de notre recherche
		double bestScore = 0;
		String bestMove = PacManLauncher.LEFT;
		//String bestAction = PacManLauncher.LEFT;
		//Object[] decision  = getPotentialScore(deepth, beliefState);
		Plans plan = beliefState.extendsBeliefState();
		for(int i=0; i<plan.size(); i++) {
			Result result = plan.getResult(i);
			double sum = 0;
			int cpt = 0;
			for(BeliefState beliefState1:result.getBeliefStates()){
				cpt++;
				double potentialScore = getHeuristic(beliefState1);
				System.out.println("Potental score : " + potentialScore);
				sum+=potentialScore;
			}
			double average = sum/cpt;
			if(average>bestScore){
				bestScore = average;
				bestMove = plan.getAction(i).get(0);
			}

		}
		System.out.println("On a choisi ce move " + bestMove);
		return bestMove;





		//String bestAction = String.valueOf(decision[1]);

		/* int bestPotentialScore = 0;
		Plans plan = beliefState.extendsBeliefState();
		for(int i=0; i<plan.size(); i++){
			Result result = plan.getResult(i);
			//int potentialScore = 0;
			for(BeliefState beliefState1:result.getBeliefStates()){
				int potentialScore = getPotentialScore(deepth, beliefState1);
				//Si elle est meilleure, on récupère son action
				if(potentialScore>bestPotentialScore){
					bestPotentialScore = potentialScore;
					bestAction = plan.getAction(i).get(0);
				}
			}
		}*/

		//CASE:UP
		//CASE:DOWN
		//CASE:LEFT
		//CASE:RIGHT

	}

	// On applique l'algorithme And Or avec une limite de profondeur
	/*private static Object[] getPotentialScore(int deepth, BeliefState beliefState) {
		Object[] decision = new Object[2];
		//On descend jusqu'à notre limite deepth (en faisait attention à s'arrêter si y'a un état final)
		if(beliefState.getLife()==0 || beliefState.getNbrOfGommes()==0){
			return new Object[] {beliefState.getScore()};
		}
		else if(deepth == 0) {
			return getHeuristic(beliefState);
		}
		else {
			int bestScore = 0;
			String bestAction = PacManLauncher.LEFT;
			//On passe à la profondeur suivante
			Plans plan = beliefState.extendsBeliefState();
			int score = 0;
			//Je parcours chaque choix
			for (int i = 0; i < plan.size(); i++) {
				Result result = plan.getResult(i); //Je le récupère



				//Je vais parcourir les différents beliefState à l'intérieur de ce result
				for (BeliefState beliefState1 : result.getBeliefStates()) {

					score = (int) getPotentialScore(deepth - 1, beliefState1)[0];
					if (score > bestScore) {
						bestScore = score;
						bestAction = plan.getAction(i).get(0);
 					}
				}




			}
			return new Object[] {bestScore,bestAction};
		}


	}*/

	/*private static String orSearch(BeliefState beliefState){
		if(beliefState.getLife()==0 || beliefState.getNbrOfGommes()==0){
			return null;
		}
		//Si on l'a déjà rencontré
		else if{
			return
		}
		else{
			Plans plans = beliefState.extendsBeliefState();
			for(int i=0; i<plans.size(); i++){
				String action = andSearch(plans.getResult(0).)
			}
		}
	}*/

	private static double getHeuristic(BeliefState beliefState) {
		double bonus = 0;
		double malus = 0;

		int xPacman = beliefState.getPacmanPosition().getRow();
		int yPacman = beliefState.getPacmanPosition().getColumn();

		//On vérifie si à gauche / droite /haut / bas y'a une superGomme ou une gomme
		double cptSuperGomme = 0;
		double cptGomme = 0;
		//On regarde ce qu'il y a gauche de pacMan
		if(beliefState.getMap(xPacman-1, yPacman) =='*'){
			cptSuperGomme++;
		}
		else if(beliefState.getMap(xPacman-1, yPacman) =='.'){
			cptGomme++;
		}
		//On regarde ce qu'il y a droite de pacMan
		if(beliefState.getMap(xPacman+1, yPacman) =='*'){
			cptSuperGomme++;
		}
		else if(beliefState.getMap(xPacman+1, yPacman) =='.'){
			cptGomme++;
		}
		if(beliefState.getMap(xPacman, yPacman-1) =='*'){
			cptSuperGomme++;
		}
		else if(beliefState.getMap(xPacman, yPacman-1) =='.'){
			cptGomme++;
		}
		if(beliefState.getMap(xPacman, yPacman+1) =='*'){
			cptSuperGomme++;
		}
		else if(beliefState.getMap(xPacman, yPacman+1) =='.'){
			cptGomme++;
		}
		bonus+=(cptSuperGomme/4)*100;
		bonus+=(cptGomme/4)*10;


		//Nombre de gommes mangées
		//Nombre de fantomes en vue / Nombre de fantomes pas en vue
		//CompteurPeur (en fonction des cases qui te séparent d'un fantôme) --> pas obligatoire
		//Si le pacman est proche des gommes/supergommes
		//Score qu'on a déjà

		//Premier fantome
		int cptGhost = 0;
		TreeSet<Position> tree0 = beliefState.getGhostPositions(0);
		for(Position p:tree0){
			//Une position potentielle du fantome
			//On vérifie s'il a un fantome à gauche, à droite, en haut, en bas
			if(p.x == xPacman-1 && p.y == yPacman){
				cptGhost++;
			}
			if(p.x == xPacman+1 && p.y == yPacman){
				cptGhost++;
			}
			if(p.x == xPacman && p.y == yPacman-1){
				cptGhost++;
			}
			if(p.x == xPacman && p.y == yPacman+1){
				cptGhost++;
			}
		}
		malus +=(cptGhost)/4;


		//Fuir les murs
		/*double cptMur = 0;
		if(beliefState.getMap(xPacman-1, yPacman) =='#'){
			cptMur++;
		}
		//On regarde ce qu'il y a droite de pacMan
		if(beliefState.getMap(xPacman+1, yPacman) =='#'){
			cptMur++;
		}
		if(beliefState.getMap(xPacman, yPacman-1) =='#'){
			cptMur++;
		}
		if(beliefState.getMap(xPacman, yPacman+1) =='#'){
			cptMur++;
		}

		malus+=cptMur/4;*/



		//Il va préférer un état où il y a moins de gommes
		malus +=beliefState.getNbrOfGommes();


		/*Iterator<Position> it0  = tree0.iterator();
		//Iterator<Position> it0 = tree0.descendingIterator();
		while(it0.hasNext()){
			Position p = it0.next();
		}
		System.out.println("Fin du fantome 0 pour ce mouv");*/
		return bonus-malus;
	}


	//Un OU pour chaque mouvement
	//Un ET pour que element de result
}