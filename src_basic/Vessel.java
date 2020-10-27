
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The Class Vessel.
 */
public class Vessel {
	
	/** Coordinates x and y of the vessel on the map. Width and height of the vessel. */
	private double x, y, width, height;
	
	/** The strength of the vessel. */
	private int strength;
	
	/** The vesselPath that the vessel has to follow. */
	public int vesselPath[][] = new int[500][2];
	
	/** The index (useful to browse the vesselPath). */
	int index = 0;
	
	/** The current planet where is the vessel. */
	private Planet destination,current;
	
	/** Movement : true when the vessel has left his current planet.
	 * Waiting : true when the vessel is waiting to take off. */
	
	private boolean movement = false, waiting = false;
	
	/** The proprietor of the vessel. */
	private Player prop;

	/** The current game where we are playing. */
	Game game;
	
	/**
	 * Instantiates a new vessel.
	 *
	 * @param game the game.
	 * @param strength the strength of the vessel.
	 * @param current the current planet where the vessel is stored.
	 */
	Vessel(Game game, int strength, Planet current){
		this.game = game;
		this.x = current.getX();
		this.y = current.getY();
		this.current = current;
		this.strength = strength;
		this.width = 25;
		this.height = 10;
	}
	

	/**
	 * Sets the prop.
	 *
	 * @param prop the new prop of the vessel.
	 */
	public void setProp(Player prop) {
		this.prop = prop;
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width of the vessel.
	 */
	public double getWidth() {
		return this.width;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height of the vessel.
	 */
	public double getHeight() {
		return this.height;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x coordinate.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y coordinate.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Sets the x.
	 *
	 * @param x the new x.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y.
	 *
	 * @param y the new y.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination where the vessel has to land.
	 */
	public Planet getDestination() {
		return this.destination;
	}
	
	/**
	 * Sets the destination.
	 *
	 * @param p the new destination.
	 */
	public void setDestination(Planet p) {
		this.destination = p;
	}
	
	/**
	 * Gets the current.
	 *
	 * @return the current planet where the vessel is stored.
	 */
	public Planet getCurrent() {
		return this.current;
	}
	
	/**
	 * Sets the current.
	 *
	 * @param p the new current planet.
	 */
	public void setCurrent(Planet p) {
		this.current = p;
	}
	
	/**
	 * Gets the movement.
	 *
	 * @return true is the vessel is on movement and false otherwise.
	 */
	public boolean getMovement() {
		return this.movement;
	}
	
	/**
	 * Sets the movement.
	 *
	 * @param movement the new boolean for the movement.
	 */
	public void setMovement(boolean movement) {
		this.movement = movement;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param index the new index to read a new element in vesselPath.
	 */
	public void setIndex (int index) {
		this.index = index;
	}
	
	/**
	 * Sets the waiting.
	 *
	 * @param waiting the new waiting.
	 */
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	
	/**
	 * Gets the waiting.
	 *
	 * @return true if the vessel is waiting to take off and false otherwise.
	 */
	public boolean getWaiting() {
		return this.waiting;
	}
	
	/**
	 * Attack.
	 *
	 * @return false if we just transfer a vessel from a planet to another planet which has the same proprietor than the current planet.
	 * true otherwise.
	 */
	public boolean attack(){
		if(prop == destination.getProp()) {
			return false;
		}
		else {
			return true;
		}
	}
	

	/**
	 * Update the current position of the vessel thanks to the vesselPath table.
	 * Refresh the number of vessels on the current planet and on the destination planet if it has to be. 
	 */
	public void update_position() {
		if( (x <= destination.getX()+destination.getR()&&x+width>=destination.getX()-destination.getR()) && ((y <= destination.getY()+destination.getR())&&y+height>=destination.getY() - destination.getR())){
			if(attack()) {
				if(destination.getNbVessels()-strength <= 0) {
					destination.setProp(prop);
					destination.setNbVessels(destination.getNbVessels() + 1);
				}else {
					destination.setNbVessels(destination.getNbVessels() - strength );
					index = 0;
				}
			}else {
				destination.setNbVessels(destination.getNbVessels() + 1);
			}
			current = destination;
			destination = null;
			movement = false;
			index = 0;
			return ;
		}
		else if(vesselPath[index][0]!=0 && vesselPath[index][1]!=0 ) {
			x = (vesselPath[index][0]);
			y = (vesselPath[index][1]);
			index++;
		}
	}
	
	/**
	 * Copy a table t in vesselPath.
	 *
	 * @param t the table that we want to copy.
	 */
	public void vesselPath(int t[][]) {
		for(int i=0;i<t.length;i++) {
			for(int j=0;j<t[i].length;j++) {
				if(t[i][j]!=0) {
					vesselPath[i][j]=t[i][j];
				}
			}
		}
	}
	
	/**
	 * Delete vesselPath and set to 0 all the boxes in the table.
	 */
	public void deleteVesselPath() {
		for(int i=0;i<vesselPath.length;i++) {
			for(int j=0; j<vesselPath[i].length;j++) {
				vesselPath[i][j] = 0;
			}
		}
		index = 0;
	}
	
	/**
	 * Draw the vessel on the game.
	 *
	 * @param gc the graphicsContext.
	 */
	public void draw(GraphicsContext gc) {
		gc.setFill(prop.getColor());
		gc.fillRect(x, y, width, height);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(x, y, width, height);
	}
}
