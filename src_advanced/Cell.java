/**
 * The Class Cell.
 * @author Sylvain Saurel
 * https://www.youtube.com/watch?v=oeT8B8sqbxQ&t=1571s
 */
public class Cell {
	
	/** The j. */
	public int i,j;
	
	/** The parent. */
	public Cell parent;
	
	/** The heuristic cost. */
	public int heuristicCost;
	
	/** The final cost. */
	public int finalCost;
	
	/** The solution. */
	public boolean solution;
	
	/**
	 * Instantiates a new cell.
	 *
	 * @param i the i
	 * @param j the j
	 */
	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}
}
