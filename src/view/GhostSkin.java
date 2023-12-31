package view;

public class GhostSkin {

	/** Tableau des figure composants le fantome */
	private Figure[] figures;
	private boolean isVisible;

	public GhostSkin (int size, int x, int y, String color, boolean isVisible) {

		int diametrehead=(int)(size);
		int heightbody=(int)(size/2.6);
		int sizeleg=size/5;
		int eyesize=(int)(size/3.5);
		int insideeyesize=eyesize/2;
		this.isVisible = isVisible;

		figures = new Figure[9];

		figures[0] = new ArcCircle(size,x,y,color,0,180, isVisible); // head
		figures[1] = new Rect(diametrehead,heightbody,x,y+diametrehead/2,color, isVisible); // body

		figures[2] = new ArcCircle(sizeleg,x,y+diametrehead/2+heightbody-sizeleg/2,color,180,180, isVisible); // leg
		figures[3] = new ArcCircle(sizeleg,x+4*sizeleg,y+diametrehead/2+heightbody-sizeleg/2,color,180,180, isVisible); // leg
		figures[4] = new ArcCircle(sizeleg,x+2*sizeleg,y+diametrehead/2+heightbody-sizeleg/2,color,180,180, isVisible);//leg

		figures[5] = new Circle(eyesize,x+eyesize/2,y+diametrehead/2-eyesize,"white", isVisible);//eye
		figures[6] = new Circle(eyesize,(int)(x+size-1.5*eyesize),y+diametrehead/2-eyesize,"white", isVisible);//eye
		figures[7] = new Circle(insideeyesize,x+eyesize/2+insideeyesize/2,y+diametrehead/2-eyesize+insideeyesize/2,"black", isVisible);//eye
		figures[8] = new Circle(insideeyesize,(int)(x+size-1.5*eyesize+insideeyesize/2),y+diametrehead/2-eyesize+insideeyesize/2,"black", isVisible);//eye
	}


	public Figure[] getFigures () {
		return this.figures;
	}

	/**
	 * retourne la position x de l'entite
	 * @return la position x de l'entite
	 */
	public int getX () {
		return this.figures[0].getX();
	}

	/**
	 * retourne la position y de l'entite
	 * @return la position y de l'entite
	 */
	public int getY () {
		return this.figures[0].getY();
	}

	/**
	 * retourne la taille de l'Entite fantome
	 * @return la taille de Ghost
	 */
	public int getWidth () {
		return this.figures[0].getWidth();
	}

	/**
	 * Draw the figure with current specifications on screen.
	 */
	public void draw() {
		for (Figure figure : this.figures) {
			if(this.isVisible)
				figure.draw();
		}
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		for (Figure figure : this.figures) {
			figure.setVisible(isVisible);
		}
	}

}
