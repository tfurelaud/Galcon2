import java.io.Serializable;

/**
 * The Class D_Planet.
 */
public class D_Planet implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5850971227258538857L;

	/** The planet. */
	private Planet planet;
	
	/** The path for vessels which are starting at 0° */
	public int path[][] = new int[1000][2];
	
	
	/**
	 * Instantiates a new d planet.
	 *
	 * @param p the planet.
	 */
	public D_Planet (Planet p){
		this.planet = p;
	}
	
	/**
	 * Gets the planet.
	 *
	 * @return the planet
	 */
	public Planet getPlanet() {
		return this.planet;
	}
	
	/**
	 * Stores the table t into path, the path where we store it is chose thanks to the number of the vessel.
	 *
	 * @param t the table that we want to copy.
	 */
	public void path(int t[][]) {
		for(int i=0;i<t.length;i++) {
			for(int j=0;j<t[i].length;j++) {
				path[i][j]=t[i][j];
				
			}
		}
	}
	
	/**
	 * Replaces all that contains t to 0.
	 *
	 * @param t the table that we want to delete.
	 */
	public void deletePath(int t[][]) {
		for(int i=0;i<t.length;i++) {
			for(int j=0; j<t[i].length;j++) {
				t[i][j] = 0;
			}
		}
	}
}
