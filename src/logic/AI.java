package logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
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
		ArrayList<String> listBestMove = new ArrayList<>();
		int deepth = 3; //La profondeur de notre recherche
		double bestScore = Double.NEGATIVE_INFINITY;
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
			System.out.println("L'average est : " + average + " et le move associé est " + plan.getAction(i).get(0));
			if(average>bestScore){
				listBestMove.clear();
				System.out.println("On met à jour notre meilleur score");
				bestScore = average;
				for(int j=0; j<plan.getAction(i).size(); j++){
					listBestMove.add(plan.getAction(i).get(j));
				}

				/*Random random = new Random();
				int randomNumber = random.nextInt(plan.getAction(i).size());
				bestMove = plan.getAction(i).get(randomNumber);*/
			}
			else if(average == bestScore){
				for(int j=0; j<plan.getAction(i).size(); j++){
					listBestMove.add(plan.getAction(i).get(j));
				}
			}

		}
		/*System.out.println("On a choisi ce move " + bestMove + " car son heuristique est " + bestScore);*/
		//On return un move au hasard parmis tous les best move
		if(listBestMove.size()>0){
			Random random = new Random();
			int randomNumber = random.nextInt(listBestMove.size());
			return listBestMove.get(randomNumber);
		}
		else{
			//Pour la dernière itération (avant qu'il se fasse manger, y'a aucun bon score donc listBestMove sera vide)
			return PacManLauncher.LEFT;
		}

		/*return bestMove;*/





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
		if(beliefState.getLife()==0){return -100000;}

		double bonus = 0;
		double malus = 0;

		int xPacman = beliefState.getPacmanPosition().getRow();
		int yPacman = beliefState.getPacmanPosition().getColumn();
		char dirPacman = beliefState.getPacmanPosition().getDirection();

		//On vérifie si à gauche / droite /haut / bas y'a une superGomme ou une gomme
		/*double cptSuperGomme = 0;
		double cptGomme = 0;
		if(beliefState.getMap(xPacman-1, yPacman) =='*'){
			cptSuperGomme++;
		}
		else if(beliefState.getMap(xPacman-1, yPacman) =='.'){
			cptGomme++;
		}
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
		bonus+=(cptSuperGomme)*100;
		bonus+=(cptGomme)*10;*/


		//Nombre de gommes mangées
		//Nombre de fantomes en vue / Nombre de fantomes pas en vue
		//CompteurPeur (en fonction des cases qui te séparent d'un fantôme) --> pas obligatoire
		//Si le pacman est proche des gommes/supergommes
		//Score qu'on a déjà

		//Premier fantome
		int cptGhostSameDirection = 0;
		int cptGhostPeur = 0;
		for(int i = 0 ; i<3; i++) {


			TreeSet<Position> tree0 = beliefState.getGhostPositions(i);
			for (Position p : tree0) {

				//Une position potentielle du fantome

				//On vérifie si y'a un fantôme qui nous fonce dessus (qui n'est pas effrayé)
				if (dirPacman == 'L' && p.dir == 'R' && p.x == xPacman && p.y < yPacman && !isWallRowBetween(p, beliefState) && beliefState.getCompteurPeur(i)<35) {
					cptGhostSameDirection++;
				}
				if (dirPacman == 'R' && p.dir == 'L' && p.x == xPacman && p.y > yPacman && !isWallRowBetween(p, beliefState) && beliefState.getCompteurPeur(i)<35) {
					cptGhostSameDirection++;
				}
				if (dirPacman == 'U' && p.dir == 'D' && p.y == yPacman && p.x < xPacman && !isWallColBetween(p, beliefState) && beliefState.getCompteurPeur(i)<40) {
					cptGhostSameDirection++;
				}
				if (dirPacman == 'D' && p.dir == 'U' && p.y == yPacman && p.x > xPacman && !isWallColBetween(p, beliefState) && beliefState.getCompteurPeur(i)<40) {
					cptGhostSameDirection++;
				}

				//On va rajouter du bonus s'il y a un fantôme effrayé très proche
				if (dirPacman == 'L' && p.x == xPacman && p.y < yPacman && !isWallRowBetween(p, beliefState) && beliefState.getCompteurPeur(i)>=35) {
					cptGhostPeur++;
				}
				if (dirPacman == 'R' && p.x == xPacman && p.y > yPacman && !isWallRowBetween(p, beliefState) && beliefState.getCompteurPeur(i)>=35) {
					cptGhostPeur++;
				}
				if (dirPacman == 'U' && p.y == yPacman && p.x < xPacman && !isWallColBetween(p, beliefState) && beliefState.getCompteurPeur(i)>=40) {
					cptGhostPeur++;
				}
				if (dirPacman == 'D' && p.y == yPacman && p.x > xPacman && !isWallColBetween(p, beliefState) && beliefState.getCompteurPeur(i)>=40) {
					cptGhostPeur++;
				}
				System.out.println("Cpt ghost peur = " + cptGhostPeur);
			}
		}
		malus += cptGhostSameDirection*100;
		bonus += cptGhostPeur*1000;

		//On vérifie la direction des fantômes





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
		malus +=beliefState.getNbrOfGommes()*10;
		malus +=beliefState.getNbrOfSuperGommes()*100;


		/*Iterator<Position> it0  = tree0.iterator();
		//Iterator<Position> it0 = tree0.descendingIterator();
		while(it0.hasNext()){
			Position p = it0.next();
		}
		System.out.println("Fin du fantome 0 pour ce mouv");*/
		return beliefState.getScore() + bonus-malus;
	}

	private static boolean isWallRowBetween(Position pGhost, BeliefState beliefState) {
		int xPacman = beliefState.getPacmanPosition().getRow();
		int yPacman = beliefState.getPacmanPosition().getColumn();
		int xGhost = pGhost.x;
		int yGhost = pGhost.y;

		//On va parcourir les colonnes y (pour la ligne x) et vérifier qu'il n'y a pas de mur qui sépare le pacman du ghost
		int yStart = Math.min(yPacman, yGhost);
		int yEnd = Math.max(yPacman, yGhost);
		for (int y = yStart; y<yEnd; y++){
			if(beliefState.getMap(xPacman, y) == '#'){
				return true;
			}
		}
		return false;

	}

	private static boolean isWallColBetween(Position pGhost, BeliefState beliefState) {
		int xPacman = beliefState.getPacmanPosition().getRow();
		int yPacman = beliefState.getPacmanPosition().getColumn();
		int xGhost = pGhost.x;
		int yGhost = pGhost.y;

		//On va parcourir les lignes x (pour la colonne y) et vérifier qu'il n'y a pas de mur qui sépare le pacman du ghost
		int xStart = Math.min(xPacman, xGhost);
		int xEnd = Math.max(xPacman, xGhost);
		for (int x = xStart; x<xEnd; x++){
			if(beliefState.getMap(x, yPacman) == '#'){
				return true;
			}
		}
		return false;

	}


	//Un OU pour chaque mouvement
	//Un ET pour que element de result
}