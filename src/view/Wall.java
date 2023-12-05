package view;

/**
 * Class pour la creation d'un mur pour le jeu pacman
 * devant empecher toutes Entite de la traverser
 */
public class Wall extends Square {

  /**
   * constructeur de mur
   * @param  size          la taille du mur
   * @param    x             position absolue x
   * @param    y             position absolue y
   * @param  color         couleur du mur
   */
  public Wall (int size, int x, int y, String color) {
    super(size, x, y, color);
  }

}
