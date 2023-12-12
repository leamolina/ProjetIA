package logic;
import java.util.*;
import data.*;
import view.*;

//Test
public class PacManLauncher {

	private data.Map maps;
	private Pacman pacman;
	private Ghost[] ghost;
	private ArrayList<Boolean> pastCollisionG;
	private boolean pastCollisionP;
	public static final String UP = "UP";
	public static final String DOWN = "DOWN";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	private static final int NBR_LVL = 3; // TODO : compter le nbr de fichier .map ??
	private double meanTimeResolution;
	private long nbrSamples;
	
	/**
	 * initialize au lancement le jeu pacman
	 * en creant la map de niveau 1
	 * le pacman de toute la partie
	 * les fantomes du niveau
	 */
	public PacManLauncher () {
		this.maps = new data.Map(1, this);
		this.fillGhost();
		this.pacman = new Pacman(this.maps.getTailleCase(), this.maps.getPMX(), this.maps.getPMY());
		this.pacman.setMap(this.maps);
		this.pastCollisionG = new ArrayList<Boolean>();
		for(int i = 0; i < this.ghost.length; i++)
			this.pastCollisionG.add(false);
		this.pastCollisionP = false;
	}

	public static void main (String[] args) {
		//Canvas c = Canvas.getCanvas();
		PacManLauncher pml = new PacManLauncher();
		pml.draw();
		pml.animate(); // Le lvl 1

		int i = 2;
		while ((pml.getPacman().getLife() > 0)) {
			pml.upLvl(i);
			pml.draw();
			pml.animate();
			i++;
			if (i > PacManLauncher.NBR_LVL) {
				i=1;
			}
		}

		if (Integer.valueOf(Score.getScore()) < pml.getPacman().getScore()) {
			Score.setScore(pml.getPacman().getScore()+"");
		}
		System.out.println("mean time resolution:" + pml.meanTimeResolution + "ms\nnbr of actions: " + pml.nbrSamples);
		System.out.println("~~~END~~~");
	}

	/**
	 * change la map en prenant le niveau passe en parametre
	 * @param lvl lvl le niveau souhaité
	 */
	public void upLvl (int lvl) {
		this.maps = new data.Map(lvl, this);
		this.fillGhost();
		this.pacman.setLocation(this.maps.getPMX(), this.maps.getPMY());
		this.pacman.setCount(0);
		this.pacman.setMap(this.maps);
	}

	/**
	 * creer tous les fantomes necessaires
	 * en fonction de l'objet this.map
	 */
	public void fillGhost () {
		ArrayList<Integer[]> gs = this.maps.getPGhost();//tab des positions fantome
		this.ghost = new Ghost[gs.size()];

		String[] color = {"redG", "blueG", "orangeG", "pinkG"};
		int cpt = 0;
		int cptGhost = 0;
		for (Integer[] t : gs) {
			this.ghost[cpt] = new Ghost(this.maps.getTailleCase(), t[0], t[1], color[cptGhost], this.maps, cpt);
			
			cpt++;
			cptGhost++;
			if (cptGhost >= color.length) {
				cptGhost = 0;
			}
		}
	}

	/**
	 * dessine la map
	 * et pacman
	 * et tous les fantomes
	 * d'un niveau
	 */
	public void draw () {
		this.maps.draw();
		this.pacman.draw();
		for (Ghost g : this.ghost) {
			if (g != null && (this.maps.isVisible(this.pacman.getY() / this.maps.getTailleCase(), this.pacman.getX() / this.maps.getTailleCase(), g.getY() / this.maps.getTailleCase(), g.getX() / this.maps.getTailleCase()))) {
				g.draw();
			}
		}
	}

	/**
	 * retourne le pacman de la partie
	 * @return le pacman de la partie
	 */
	public Pacman getPacman () {
		return this.pacman;
	}

	/**
	 * lance le deroulement du jeu
	 * en regardant la touche utiliser par l'utilisateur pour deplacer pacman
	 * puis deplace les fantomes
	 * et verifie les colisions eventuelles entre pacman et les fantomes
	 * (sans redessiner toute la map)
	 */
	public void animate () {
		Canvas c = Canvas.getCanvas();
		c.resetMove();
		while ((this.maps.getNbGom() > 0) && (this.pacman.getLife() > 0)) {
			/*if(this.getPacman().getX() % this.maps.getTailleCase() == 0 && this.getPacman().getY() % this.maps.getTailleCase() == 0 && this.getPacman().getCount() % (this.maps.getTailleCase() / Pacman.SPEED_PACMAN) == 0) {
				System.out.println(this.maps.getState().toString());
				System.out.println("Actual position: P(" + this.pacman.getY() / this.maps.getTailleCase()  + ", " + this.getPacman().getX() / this.maps.getTailleCase() + ") " + this.getPacman().getScore());
				if(this.pacman.getY() / this.maps.getTailleCase() != this.maps.getState().getPacmanPos().x || this.getPacman().getX() / this.maps.getTailleCase() != this.maps.getState().getPacmanPos().y)
					System.out.println("Problem");
				int j = 0;
				for(Ghost g: this.ghost) {
					System.out.println("G" + (j) + " (" + g.getY() / this.maps.getTailleCase() + ", " + g.getX() / this.maps.getTailleCase() + ") (" +  g.getY() + ", " + g.getX() + ") [" + g.getPeur() + "]");
					if(g.getY() % this.maps.getTailleCase() % this.maps.getTailleCase() != 0 || g.getX() % this.maps.getTailleCase() != 0)
						System.out.println("Problem");
					Position posG = this.maps.getState().getPGhost(j++);
					if(g.getY() / this.maps.getTailleCase() != posG.x || g.getX() / this.maps.getTailleCase() != posG.y)
						System.out.println("Problem");
				}
				ArrayList<State> listState = this.maps.getVisibleState();
				int k = 0;
				for(State state: listState) {
					System.out.println("State[" + k + "]\n" + state.toString());
					k++;
				}
				if(this.getPacman().getScore() != this.maps.getState().getScore())
					System.out.println("Problem");
				if(this.getPacman().getScore() != this.maps.getVisibleState().get(0).getScore())
					System.out.println("Problem");
				for(int row = 0; row < this.maps.getState().getMap().length; row++) {
					for(int column = 0; column < this.maps.getState().getMap()[row].length; column++) {
						if(this.maps.getState().getMap()[row][column] != this.maps.getVisibleState().iterator().next().getMap()[row][column])
							System.out.println("Problem");
					}
				}
				if(this.maps.getState().getLife() != this.pacman.getLife())
					System.out.println("Problem");
				if(this.maps.getState().getLife() != this.maps.getVisibleState().getFirst().getLife())
					System.out.println("Problem");
				if(this.maps.getState().getNbrOfGommes() != this.maps.getVisibleState().getFirst().getNbrOfGommes())
					System.out.println("Problem");
				if(this.maps.getState().getNbrOfGommes() != this.maps.getNbGom())
					System.out.println("Problem");
				if(this.maps.getState().getNbrOfSuperGommes() != this.maps.getVisibleState().getFirst().getNbrOfSuperGommes())
					System.out.println("Problem");
				
			}*/
			boolean isInit = false;
			//swich the key press, move the pacman
			if(Canvas.getCanvas().isAIdriven()) {
				long elapsedTime = System.currentTimeMillis();
				isInit = this.pacman.move(AI.findNextMove(this.maps.getBeliefState()));
				elapsedTime = System.currentTimeMillis() - elapsedTime;
				this.nbrSamples++;
				this.meanTimeResolution = ((double)elapsedTime) / this.nbrSamples + (((double)(this.nbrSamples - 1)) / this.nbrSamples) * this.meanTimeResolution;
			}
			else {
				System.out.println("On est kà");
				System.out.println("x : " + pacman.getX() + "y : " + pacman.getY());
				if (c.isUpPressed()) {
					isInit = this.pacman.move(PacManLauncher.UP);
				} else if (c.isDownPressed()) {
					isInit = this.pacman.move(PacManLauncher.DOWN);
				} else if (c.isLeftPressed()) {
					isInit = this.pacman.move(PacManLauncher.LEFT);
				} else if (c.isRightPressed()) {
					isInit = this.pacman.move(PacManLauncher.RIGHT);
				} else {
					isInit = this.pacman.move(this.pacman.getPreviousMove());
				}
			}

			if(!isInit) {
				boolean collision = false;
				if(this.pastCollisionP) {
					this.pastCollisionP = false;
					collision = this.collisionGhost();
				}
					
				if(!collision) {
					if (this.pacman.getPMSupra()) {
						for (Ghost g : this.ghost) {
							g.setEtatPeur();
						}
						this.pacman.resetSupra();
					}

					for (Ghost g : this.ghost) {
						if(!collision) {
							collision = g.move();
						}
						else {
							g.moveWithout();
						}
					}
					if(this.getPacman().getX() % this.maps.getTailleCase() == 0 && this.getPacman().getY() % this.maps.getTailleCase() == 0 && this.getPacman().getCount() % (this.maps.getTailleCase() / Pacman.SPEED_PACMAN) == 0)
						this.collisionGhost();
					else
						this.pastCollisionGhost();
				}
				
			}
			else {
				this.maps.setBeliefState(new BeliefState(this.maps.getBeliefState(), true));
				for (Ghost g : this.ghost) {
					g.moveWithout();
				}
				this.collisionGhost();
			}
			
			for(int i = 0; i < this.ghost.length; i++) {
				BeliefState.filter(this.maps.getVisibleBeliefState(), i, this.maps.getBeliefState().getPGhost(i));
			}
			
			Canvas.getCanvas().redraw(this.pacman.getScore(), this.pacman.getLife(), Score.getScore());
		}
	}

	/**
	 * verifie s'il existe une colision entre pacman et l'un des fantome
	 * si oui alors pacman perd une vie
	 * et toutes les Entites sont repositionner à leur point de départ pour le niveau en cours
	 * @return true si une colision existe
	 */
	private boolean collisionGhost () {
		boolean ret = false;
		if(ret) {
			this.pacman.carryOff();
			this.pacman.setLocation(this.maps.getPMX(), this.maps.getPMY());
			
			ret = true;
		}
		int i = 0;
		while (!ret && i<this.ghost.length) {
			//si pacman colision avec un fantome
			//perdre une vie a pacman && repositionner les entites
			if(this.pacman.colisionGhost(this.ghost[i]) && this.ghost[i].getPeur() == 0) {

				this.pacman.carryOff();
				this.pacman.setLocation(this.maps.getPMX(), this.maps.getPMY());
				
				ret = true;
			}
			else if(this.pastCollisionG.get(i) || this.pacman.colisionGhost(this.ghost[i]) && this.ghost[i].getPeur() > 0) {

				ArrayList<Integer[]> gs = this.maps.getPGhost();
				this.ghost[i].setLocation(gs.get(i)[0], gs.get(i)[1]);
				this.ghost[i].setPreviousMove(PacManLauncher.UP);
				this.ghost[i].setEtatNormal();
				this.pastCollisionG.set(i, false);
				this.pacman.upScoreFantomme();
				
			}
			i++;
		}

		if (ret) {//si une colision
			ArrayList<Integer[]> gs = this.maps.getPGhost();//tab des positions fantome

			int cpt = 0;
			for (Integer[] t : gs) {
				this.ghost[cpt].setLocation(t[0], t[1]);
				this.ghost[cpt].setEtatNormal();
				this.pastCollisionG.set(cpt, false);
				cpt++;
			}
		}
		return ret;
	}
	
	private void pastCollisionGhost () {
		int i = 0;
		while (!this.pastCollisionP && i<this.ghost.length) {
			if(this.pacman.colisionGhost(this.ghost[i]) && this.ghost[i].getPeur() == 0) {
				this.pastCollisionP = true;
			}
			else if(this.pacman.colisionGhost(this.ghost[i]) && this.ghost[i].getPeur() > 0) {
				this.pastCollisionG.set(i, true);				
			}
			i++;
		}

	}

}
