
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * The Class Algo.
 * @author inspired from https://www.youtube.com/watch?v=oeT8B8sqbxQ&t=1571s
 *
 */
public class Algo {
	
	/** The constant diagonal cost. */
	public static final int DIAGONAL_COST = 14;
	
	/** The constant vertical and horizontal cost. */
	public static final int V_H_COST = 10;
	
	/** The grid. */
	private static Cell[][] grid;
	
	/** The open cells. */
	private PriorityQueue<Cell> openCells;
	
	/** The closed cells. */
	private static boolean[][] closedCells;
	
	/** The x coordinate of the starting point
	 *  The y coordinate of the starting point. */
	private int startI, startJ;
	
	/** The x coordinate of the ending point
	 *  The y coordinate of the ending point. */
	private static int endI, endJ;
	

	/** The table where we are going to store our result. */
	static int [][] tab = new int[1000][2];
	
	/**
	 * Instantiates a new algo.
	 *
	 * @param width the width of the map.
	 * @param height the height of the map.
	 * @param si the x coordinate of the starting point
	 * @param sj the y coordinate of the starting point
	 * @param ei the y coordinate of the ending point.
	 * @param ej the y coordinate of the ending point.
	 * @param blocks the blocked point on the map.
	 */
	public Algo(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
		grid = new Cell[width][height];
		closedCells = new boolean[width][height];
		openCells = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;}
		);
		
		
		startCell(si,sj);
		endCell(ei,ej);
		
		for(int i=0;i<grid.length;i++) {
			for(int j=0; j<grid[i].length; j++) {
				grid[i][j] = new Cell(i,j);
				grid[i][j].heuristicCost = Math.abs(i-endI) + Math.abs(j-endJ);
				grid[i][j].solution = false;
			}
		}
		
		grid[startI][startJ].finalCost = 0;
		
		for(int i=0; i<blocks.length; i++) {
			addBlockOnCell(blocks[i][0], blocks[i][1]);
		}
	}
		
	/**
	 * Adds the block on cell.
	 *
	 * @param i the x coordinate of the point that we add to the grid.
	 * @param j the y coordinate of the point that we add to the grid.
	 */
	public void addBlockOnCell(int i, int j) {
		grid[i][j] = null;
	}
	
	/**
	 * Start cell.
	 *
	 * @param i the x coordinate of the starting point.
	 * @param j the y coordinate of the starting point.
	 */
	public void startCell(int i, int j) {
		startI = i;
		startJ = j;
	}
	
	/**
	 * End cell.
	 *
	 * @param i the x coordinate of the ending point.
	 * @param j the y coordinate of the ending point.
	 */
	public void endCell(int i, int j) {
		endI = i;
		endJ = j;
	}
	
	/**
	 * Update cost if needed.
	 *
	 * @param current the current cell.
	 * @param t the table of all the cell.
	 * @param cost the current cost.
	 */
	public void updateCostIfNeeded(Cell current, Cell t, int cost) {
		if(t==null || closedCells[t.i][t.j])
			return;
		
		int tFinalCost = t.heuristicCost + cost;
		boolean isOpen = openCells.contains(t);
		
		if(!isOpen || tFinalCost < t.finalCost) {
			t.finalCost = tFinalCost;
			t.parent = current;
			
			if(!isOpen) {
				openCells.add(t);
			}
		}
	}
	
	/**
	 * Calculates the path.
	 */
	public void process() {
		openCells.add(grid[startI][startJ]);
		Cell current;
		
		while(true) {
			current = openCells.poll();
			
			if(current == null)
				break;
			
			closedCells[current.i][current.j] = true;
			
			if(current.equals(grid[endI][endJ]))
				return;
			
			Cell t;
			
			if(current.i - 1>= 0) {
				t=grid[current.i-1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
				
				if(current.j - 1>= 0) {
					t=grid[current.i-1][current.j-1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
				
				if(current.j + 1 < grid[0].length) {
					t=grid[current.i-1][current.j+1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
			}
				
			if(current.j - 1>= 0) {
				t=grid[current.i][current.j-1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			
			if(current.j + 1 < grid[0].length) {
				t=grid[current.i][current.j+1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}
			
			if(current.i + 1 < grid.length) {
				t=grid[current.i+1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				
				if(current.j - 1 >= 0) {
					t=grid[current.i+1][current.j-1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
				
				if(current.j + 1 < grid[0].length) {
					t=grid[current.i+1][current.j+1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
			}
			
		}
	}
	
	
	/**
	 * Applies the algorithm to have the shortest path between 2 points. 
	 * Before that, we store all the points that we don't have access into the blocked table while using the isOn method.
	 *
	 * @param game the current game.
	 * @param list the list of all planets.
	 * @param x the x
	 * @param y the y
	 * @param desti the destination planet. (end point)
	 * @return res the table with all the point of the path from source to destination.
	 */
	public static int[][] searchPath(Game game, ArrayList<Planet> list,int x, int y, Planet desti){
		int [][] blocked = new int[50000][2];
		int width = 500;
		int height = 650/2;
		int vwidth = (int)game.l_Vessel.get(0).getWidth();
		int vheight = (int)game.l_Vessel.get(0).getHeight();
		
		int k =0;
		
		for(int i=0;i<width ; i++) {
			for(int j=0 ; j<height ; j++) {
				if(game.isOn(list,(i*2)+vwidth,(j*2)+vheight)) {
					Planet ban = game.isOnPlanet(list, i*2+vwidth, j*2+vheight);
					if(!ban.equals(desti)&& !ban.equals(game.isOnPlanet(list, x, y)) ) {
						blocked[k][0]=(int)(i);
						blocked[k][1]=(int)(j);
						k++;
					}
			
				}
				
				else if(game.isOn(list,(i*2)+vwidth,(j*2))) {
					Planet ban = game.isOnPlanet(list, i*2+vwidth, j*2);
					if(!ban.equals(desti)) {
						blocked[k][0]=(int)(i);
						blocked[k][1]=(int)(j);
						k++;
					}
				}
				
				else if(game.isOn(list,(i*2),(j*2)+vheight)) {
					Planet ban = game.isOnPlanet(list, i*2, j*2+vheight);
					if(!ban.equals(desti)) {
						blocked[k][0]=(int)(i);
						blocked[k][1]=(int)(j);
						k++;
					}
				}
			
				else if(game.isOn(list,(i*2),(j*2))) {
					Planet ban = game.isOnPlanet(list, i*2, j*2);
					if(!ban.equals(desti)) {
						blocked[k][0]=(int)(i);
						blocked[k][1]=(int)(j);
						k++;
					}
				}
				
				
			}
		}
		
		
		Algo astar = new Algo(width,height,(int) desti.getX()/2,(int) desti.getY()/2, x/2, y/2,
				blocked);
		astar.process();
		
		
		int i=0;
		
		if(closedCells[endI][endJ]) {
			Cell current = grid[endI][endJ];
			grid[current.i][current.j].solution = true;
			while(current.parent != null) {
				grid[current.parent.i][current.parent.j].solution = true;
				tab[i][0]=current.i*2;
				tab[i][1]=current.j*2;
				current = current.parent;
				i++;
			}
			
		}else
			System.out.println("No possible Path");
	
		
		i = 0;
		int res[][] = new int[500][2];
		int a=0,b=0;
		for(int v = 0;v<tab.length;v++) {
			for(int j=0;j<tab[v].length;j++) {
				if(tab[v][j]!=0) {
					res[a][b]=tab[v][j];
				}
				b++;
			}
			b=0;
			a++;
		}
		return res;
		
	}
	
	
	/**
	 * Deletes the current values which are in tab.
	 */
	public static void deleteTab() {
		for(int z=0;z<tab.length;z++) {
			for(int j=0;j<tab[z].length;j++) {
				tab[z][j]=0;
			}
		}
	}
}
