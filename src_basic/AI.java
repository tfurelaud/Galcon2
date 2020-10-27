
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.paint.Color;

/**
 * The Class AI.
 */
public class AI extends Player{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1838023564531026431L;
	
	/** The player whose AI will take the properties. */
	private Player player;
	
	/** The list of the planets which are in the game. */
	private ArrayList<Planet> l_planet;
	
	/** The current game where we are playing. */
	Game game;
	
	/**
	 * Instantiates a new AI.
	 *
	 * @param game the game
	 * @param name the name
	 * @param color the color which the AI will have.
	 * @param l_planet the list of the planets which are in the game.
	 * @param b the player whose AI will take the properties.
	 */
	public AI(Game game,String name, Color color, ArrayList<Planet> l_planet, Player b) {
		super("AI", b.getColor());
		this.game = game;
		this.l_planet = l_planet;
		this.player =b;
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player whose AI has taken the properties.
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Send a vessel to a random planet that the AI has to a random plant that it doesn't have. 
	 * Thanks to his number, the algorithm will choose a starting point around the planet.
	 *
	 * @param vessel the vessel that we send.
	 * @param numvess the number of the vessel that we send.
	 */
	public void send(Vessel vessel, int numvess) {
		Iterator<Planet> it3 = l_planet.iterator();
		while (it3.hasNext()) {
			Planet planet = it3.next();
			if(planet.getProp().getName() != player.getName()) {
				Iterator<D_Planet> it2 = vessel.getCurrent().d_all.iterator();
				while (it2.hasNext()) {
					D_Planet d = it2.next();
					if(d.getPlanet() == planet) {
						vessel.setDestination(planet);
						vessel.setProp(vessel.getCurrent().getProp());
						vessel.vesselPath(d.path);
						vessel.setMovement(true);
						vessel.getCurrent().setNbVessels(vessel.getCurrent().getNbVessels() - 1);
						return;
					}
				}
			}
		}
	}
}