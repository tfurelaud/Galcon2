
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The Class Planet.
 */
public class Planet implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1302754692214885701L;
	
	/** The number of vessel produced in 1second. */
	private int ProdTime;
	
	/** The number of vessel ready to take off*/
	private int numvess = 0;
	
	/** The number of vessels on the planet. */
	private int nbVessels;
	
	/**  The proprietor of the planet. */
	private Player prop;
	
	/** The x coordinate of the planet.
	 *  The y coordinate of the planet.
	 *  The radius of the planet. */
	private double x,y,r;
	
	/** The current game where we are playing. */
	transient Game game;
	
	/** All of distances to all the other planets. */
	public ArrayList<D_Planet> d_all = new ArrayList<D_Planet>(); 
	
	/**
	 * Instantiates a new planet.
	 *
	 * @param planet the planet.
	 * @param ProdTime the production time.
	 * @param nbVessels the number of vessels on the planet at the beginning.
	 * @param prop the first proprietor.
	 * @param pressed the pressed
	 * @param g the current game.
	 */
	Planet( Circle planet, int ProdTime, int nbVessels, Player prop,boolean pressed, Game g){
		this.x = planet.getCenterX();
		this.y = planet.getCenterY();
		this.r = planet.getRadius();
		this.ProdTime = ProdTime;
		this.nbVessels = nbVessels;
		this.prop = prop;
		this.game = g;
	}
	
	
	/**
	 * Gets the x coordinate of the planet.
	 *
	 * @return the x coordinate of the planet.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Gets the y coordinate of the planet.
	 *
	 * @return the y coordinate of the planet.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Gets the radius.
	 *
	 * @return the radius of the planet.
	 */
	public double getR() {
		return this.r;
	}
	
	/**
	 * Gets the proprietor.
	 *
	 * @return the proprietor of the planet
	 */
	public Player getProp() {
		return this.prop;
	}
	
	/**
	 * Gets the number of vessels on the planet.
	 *
	 * @return the number of vessels on the planet.
	 */
	public int getNbVessels() {
		return this.nbVessels;
	}
	
	/**
	 * Sets the number of vessels on the planet.
	 *
	 * @param nb the new number of vessels on the planet.
	 */
	public void setNbVessels(int nb) {
		this.nbVessels = nb;
	}
	
	/**
	 * Sets the number of vessels ready to take off.
	 *
	 * @param nb the new number of vessels ready to take off.
	 */
	public void setNumvess(int nb) {
		this.numvess = nb;
	}
	
	/**
	 * Gets the number of vessels ready to take off.
	 *
	 * @return the number of vessels ready to take off.
	 */
	public int getNumvess() {
		return this.numvess;
	}
	
	/**
	 * Gets the production time.
	 *
	 * @return the production time.
	 */
	public int getProdTime() {
		return this.ProdTime;
	}
	
	/**
	 * Sets the proprietor of this planet.
	 *
	 * @param p the new proprietor of the planet.
	 */
	public void setProp(Player p) {
		this.prop = p;
	}
	
	/**
	 * Calculate and store in different table all path from the planet to all other planets in the game.
	 * For the current planet we calculate the path for 4 start point which are at 4 different points around the planet.
	 * @param l_planet the list where are stored all the planet of the game.
	 */
	public void distance_all_planet( ArrayList<Planet> l_planet) {
		Iterator<Planet> it = l_planet.iterator();
		while (it.hasNext()) {
			Planet planet = it.next();
			if(planet != this) {
				D_Planet d = new D_Planet(planet);
				
				double angle_rad = (int) Math.toRadians(0);
				Algo.searchPath(game, l_planet,(int)(this.getX()+(Math.cos(angle_rad)*this.getR()+15)),(int)(this.getY()+(Math.sin(angle_rad)*this.getR())),planet);
				d.path(Algo.tab);
				Algo.deleteTab();
				
				d_all.add(d);
			}
		}
	}
	
	/**
	 * Draw the planet.
	 *
	 * @param gc the graphicsContext.
	 */
	public void draw(GraphicsContext gc) {
		gc.setFill(prop.getColor());
		gc.fillOval(x-r,y-r, r*2, r*2);
		gc.setFill(Color.BLACK);
		gc.fillText(Integer.toString(nbVessels), x-24, y+6);
	}

}
