
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * The Class Game.
 */

public class Game extends Application implements Serializable{

	/** The constant serialVersionUID. */
	private static final long serialVersionUID = -1040237118571490979L;

	/** The constant width of the game's grid. */
	private final static int WIDTH = 1000;

	/** The constant height of the game's grid. */
	private final static int HEIGHT = 650;

	/** The minimum space between planets. */
	private final static int MIN_GAP_PLANET = 50;

	/** The number of the planets that are neutral. */
	private final static int NBNEUTRALPLANETS = 5;
	
	/** The minimum production time. */
	private final static int MinProd = 1;

	/** The maximum production time. */
	private final static int MaxProd = 10;

	/** The production time. */
	private static int ProdTime = MinProd + (int)(Math.random() * ((MaxProd - MinProd) + 1));

	/** The minimum strength of vessels. */
	private final static int minVessStrength = 1;

	/** The maximum strength of vessels. */
	private final static int maxVessStrength = 5;

	/** The strength of vessels. */
	private static int VessStrength = minVessStrength + (int)(Math.random() * ((maxVessStrength - minVessStrength) + 1));

	/** The list of all the planets in the game. */
	ArrayList<Planet> l_planet = new ArrayList<Planet>();

	/** The list of all the vessel in the game. */
	ArrayList<Vessel> l_Vessel = new ArrayList<Vessel>();
	
	/** The list of all the strong vessel in the game. */
	ArrayList<Vessel> l_StrongVessel = new ArrayList<Vessel>();

	/** The file output stream that we will use to write the game into a file. */
	public FileOutputStream fos;

	/** The object output stream that we will use to write the game into a file. */
	public ObjectOutputStream oos;

	/** The object input stream that we will use to read the game into a file. */
	public ObjectInputStream in;

	/** The file input stream that we will use to read the game into a file. */
	public  FileInputStream  fis;

	/**gameStarted, false if we are in a menu and true if the game has started. 
	 * secondMenu, false if we are in the firstMenu and true if we have chose "new game".
	 * firstMenu, true if we are in the first Menu and false otherwise.
	 * AI, true if the user chose to play versus the AI, false otherwise.
	 * press, true if the mouse's button is pressed.
	 * loading, true if the game is loading and false otherwise. 
	 * error, true if an error appears.
	 * */
	boolean gameStarted = false, secondMenu, AI=false, firstMenu=true, press=false, loading = false, error = false;

	/** The save's button */
	Button save = new Button(10,10,130,30);

	/** The left button (used in menus). */
	Button leftButton = new Button(WIDTH/2-250,HEIGHT/2,200,80);

	/** The right button (used in menus). */
	Button rightButton = new Button(WIDTH/2 + 50,HEIGHT/2,200,80);
	
	/** The button to play versus the AI (used in menus). */
	Button one_player = new Button(WIDTH/2 - 475,HEIGHT/2+100,200,80);
	
	/** The button to select 2 players (used in menus). */
	Button two_player = new Button(WIDTH/2 - 225,HEIGHT/2+100,200,80);
	
	/** The button to select 3 players (used in menus). */
	Button three_player = new Button(WIDTH/2 + 25,HEIGHT/2+100,200,80);
	
	/** The button to select 4 players (used in menus). */
	Button four_player = new Button(WIDTH/2 + 275,HEIGHT/2+100,200,80);
	
	/** The button to select 1 player and 1 AI(used in menus). */
	Button one_AI = new Button(WIDTH/2 - 400,HEIGHT/2-200,200,80);
	
	/** The button to select 1 player and 2 AIs(used in menus). */
	Button two_AI = new Button(WIDTH/2 -100,HEIGHT/2-200,200,80);
	
	/** The button to select 1 player and 3 AIs(used in menus). */
	Button three_AI = new Button(WIDTH/2 + 200,HEIGHT/2-200,200,80);

	/** The neutral player. */
	Player Neutre = new Player("Neutre",Color.GRAY);

	/** The first player. */
	Player A = new Player("A",Color.BLUE);

	/** The second player. */
	Player B = new Player("B",Color.RED);
	
	/** The third player. */
	Player C = new Player("C",Color.GREEN);
	
	/** The fourth player. */
	Player D = new Player("D",Color.ORANGE);

	/** The first AI. */
	AI aiA = null;
	
	/** The second AI. */
	AI aiB = null;
	
	/** The third AI. */
	AI aiC = null;
	
	/** The timer (used in the AnimationTimer). 
	 * If a player win he has to wait that the timer be at 300 before he completely win.*/
	int timer = 0, timer_win = 0;

	/** The number of vessels that the AI send. */
	int nb_vessel_send_AI = 0;

	/** The percentage of the reserve of vessels of a planet that we send. */
	int Percentage_send;

	/** The number of vessels that we send. 
	 *  The number of strong vessels that we send.
	 *  The number of vessel that the AI send.*/
	int numvess = 0, numvessstrong = 0, numvessAI = 0;

	/** The current game where we are playing. */
	Game game = this;

	/** The loading timer. */
	int loading_timer = 0;
	
	/** number of player */
	int nb_player = 0, nb_AI=0;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Builds a circle with random attributes.
	 *
	 * @param Minx the minimum for the coordinate x of the circle that we will represent planets.
	 * @param Maxx the maximum for the coordinate x of the circle that we will represent planets.
	 * @param Miny the minimum for the coordinate y of the circle that we will represent planets.
	 * @param Maxy the maximum for the coordinate y of the circle that we will represent planets.
	 * @param MaxR the maximum length of the radius.
	 * @param MinR the minimum length of the radius.
	 * @return the circle.
	 */
	public Circle circle_alea(int Minx,int Maxx,int Miny,int Maxy,int MaxR,int MinR) {
		int x = Minx + (int)(Math.random() * ((Maxx - Minx) + 1));
		int y = Miny + (int)(Math.random() * ((Maxy - Miny) + 1));
		int r = MinR + (int)(Math.random() * ((MaxR - MinR) + 1));
		Circle planet = new Circle();
		planet.setCenterX(x);
		planet.setCenterY(y);
		planet.setRadius(r);
		return planet;
	}

	/**
	 * Adds the planet to the principal planet list of the game.
	 *
	 * @param list the list where we add the new planet.
	 * @param player the proprietor of the new planet.
	 * @param nbVes the number of vessel on the planet at the start.
	 * @param nbStrongVes the number of strong vessel on the planet at the start.
	 */
	public void add_planet(ArrayList<Planet> list, Player player, int nbVes, int nbStrongVes) {
		try {

			int MinR = 40;
			int MaxR = 75;

			int Minx = MaxR+50;
			int Maxx = WIDTH - MaxR-50;

			int Miny = MaxR+50;
			int Maxy = HEIGHT - MaxR-150;			

			Circle c = circle_alea(Minx,Maxx,Miny,Maxy,MaxR,MinR);

			Planet p1 = new Planet( c, ProdTime ,nbVes, nbStrongVes ,player, false, this);
			
			if(list.isEmpty()) {
				list.add(p1);
			}

			else {
				for(Iterator<Planet> It = list.iterator(); It.hasNext();){
					Planet p2 = It.next();
					if(FarEnough(p1,p2)&&(!It.hasNext())){
						add_vessel(p1,nbVes);
						list.add(p1);
					}
					else if(FarEnough(p1,p2)&&(It.hasNext())) {}				
					else {
						try {
							add_planet(list,player,nbVes,0);
						}catch (java.lang.StackOverflowError exception) {
							error = true;
							return;
						}
					}
				}
			}

		} catch (java.util.ConcurrentModificationException exception) {

		}

	}

	/**
	 * Checks if a planet is far enough of another planet.
	 *
	 * @param p1 the first planet that we want to check.
	 * @param p2 the second planet that we want to check.
	 * @return true, if both planet are far enough, and false otherwise.
	 */
	public boolean FarEnough(Planet p1, Planet p2) {
		double gap = Distance(p1, p2.getX(), p2.getY())- p1.getR() - p2.getR() ;
		if(gap >= MIN_GAP_PLANET){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Calculates the distance between a planet and a point.
	 *
	 * @param p the planet.
	 * @param x the x coordinate of the point.
	 * @param y the y coordinate of the point.
	 * @return the distance.
	 */
	public double Distance(Planet p, double x, double y) {
		double d1 = p.getX() - x;
		double d2 = p.getY() - y;
		return Math.sqrt(d1*d1 + d2*d2);
	}

	/**
	 * Checks if is game over.
	 *
	 * @param list the list of all the planet.
	 * @return the player who has won.
	 */
	public Player isGameOver(ArrayList<Planet> list) {
		Player check;
		Iterator<Planet> it = l_planet.iterator();
		while (it.hasNext()) {
			Planet planet = it.next();
			if(!planet.getProp().getName().equals("Neutre")) {
				check = planet.getProp();
				Iterator<Planet> it2 = l_planet.iterator();
				while (it2.hasNext()) {
					Planet planet2 = it2.next();
					if(!planet2.getProp().equals(Neutre)&& !planet2.getProp().equals(planet.getProp())) {
						return null;
					}
				}
				return check;
			}
		}
		return null;
	}
	
	/**
	 * Checks if is game over.
	 *
	 * @param list the list of all the planet.
	 * @return the true if the player lost.
	 */
	public boolean lose(ArrayList<Planet> list) {
		if(AI==true) {
			int cpt = 0;
			Iterator<Planet> it = l_planet.iterator();
			while (it.hasNext()) {
				Planet planet = it.next();
				if(planet.getProp().equals(A)) {
					cpt++;
				}
			}
			if(cpt==0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a point is on a planet or not.
	 *
	 * @param list the list of all planets.
	 * @param x the x coordinate of the point.
	 * @param y the y coordinate of the point.
	 * @return true, if is the point is on a planet.
	 */
	public boolean isOn(ArrayList<Planet> list, double x, double y) {
		for(Iterator<Planet> It = list.iterator(); It.hasNext();){
			Planet p = It.next();
			if(Distance(p,x,y)<p.getR()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks on which planet is the point.
	 *
	 * @param list the list of all planets.
	 * @param x the x coordinate of the point.
	 * @param y the y coordinate of the point.
	 * @return the planet where is the point.
	 */
	public Planet isOnPlanet(ArrayList<Planet> list, double x, double y) {
		for(Iterator<Planet> It = list.iterator(); It.hasNext();){
			Planet p = It.next();
			if(Distance(p,x,y)<p.getR()) {
				return p;
			}
		}
		return null; 
	}

	/**
	 * Adds the vessel.
	 *
	 * @param p the planet where we add the vessel.
	 * @param nb_vessel the number of vessels that we have to add.
	 */
	public void add_vessel(Planet p, int nb_vessel) {
		for(int i = 0;i<nb_vessel;i++) { 
			Vessel v = new Vessel(game,VessStrength, p);
			l_Vessel.add(v);
		}
	}
	
	/**
	 * Adds the strong vessel.
	 *
	 * @param p the planet where we add the vessel.
	 * @param nb_vessel the number of vessels that we have to add.
	 */
	public void add_strong_vessel(Planet p, int nb_vessel) {
		for(int i = 0;i<nb_vessel;i++) { 
			Vessel v = new Vessel(game,VessStrength*2, p);
			v.setStrong(true);
			l_StrongVessel.add(v);
		}
	}

	/**
	 * Generates all planets of the game.
	 */
	public void generate_planet() {

		int MinnbVess = 12;
		int MaxnbVess = 150;

		for(int i=0; i<NBNEUTRALPLANETS ; i++) {
			int nbVesN = MinnbVess + (int)(Math.random() * ((MaxnbVess - MinnbVess) + 1));
			add_planet(l_planet, Neutre, nbVesN,0);
		}

		int nbVes = MinnbVess + (int)(Math.random() * ((MaxnbVess - MinnbVess) + 1));

		add_planet(l_planet,A,nbVes,0);
		add_planet(l_planet,B,nbVes,0);
		if(nb_player>=3 || nb_AI>=2) {
			add_planet(l_planet,C,nbVes,0);
			if(nb_player>=4 || nb_AI==3) {
				add_planet(l_planet,D,nbVes,0);
			}
		}
		
		
	}

	/**
	 * Calculates all the distance between a planet and others and store it.
	 * 
	 * 
	 * @param list the list of planets in the game.
	 */
	public void distance_planet_for_all(ArrayList<Planet> list) {
		Iterator<Planet> it = list.iterator();
		while (it.hasNext()) {
			Planet planet = it.next();
			planet.distance_all_planet(list);
		}
	}

	/**
	 * Prints the game info.
	 */
	public void printGameInfo() {
		System.out.println("Game started :");
		System.out.println("ProdTime of planets : "+ProdTime);
		System.out.println("Strength of vessels : "+VessStrength);
		System.out.println("Strength of strong vessels : "+VessStrength*2);
	}
	
	/**
	 * Deletes the path of a all the vessel which are in movement, to a new path to the new planet.
	 * 
	 * @param player the player who wants to redirecting his vessels.
	 * @param x the x coordinate that we'll use to check which is the new planet.
	 * @param y the y coordinate that we'll use to check which is the new planet.
	 */
	public void redirection(Player player, double x, double y) {
		if(isOn(l_planet,x,y)) {
			Planet nw = isOnPlanet(l_planet, x, y);
			Iterator<Vessel> it2 = l_Vessel.iterator();
			Iterator<Vessel> it3 = l_StrongVessel.iterator();
			while (it2.hasNext()) {
				Vessel vessel = it2.next();
				if(vessel.getCurrent().getProp().getName().equals(player.getName())&&vessel.getMovement()==true) {
					Algo.deleteTab();
					Algo.searchPath(game, l_planet,(int)vessel.getX(),(int)vessel.getY(),nw);
					vessel.setIndex(0);
					vessel.deleteVesselPath();
					vessel.setDestination(nw);
					vessel.vesselPath(Algo.tab);
				}
			}
			while (it3.hasNext()) {
				Vessel vessel = it3.next();
				if(vessel.getCurrent().getProp().getName().equals(player.getName())&&vessel.getMovement()==true) {
					Algo.deleteTab();
					Algo.searchPath(game, l_planet,(int)vessel.getX(),(int)vessel.getY(),nw);
					vessel.setIndex(0);
					vessel.deleteVesselPath();
					vessel.setDestination(nw);
					vessel.vesselPath(Algo.tab);
				}
			}
		}
	}
	

	public void start(Stage stage)  {
																																							
		stage.setTitle("Projet JAVA");
		stage.setResizable(false);

		Group root = new Group();
		Scene scene = new Scene(root);
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
		gc.setFill(Color.BISQUE);
		gc.setStroke(Color.RED);
		gc.setLineWidth(1);

		stage.setScene(scene);

		stage.show();

		Point2D point = new Point2D();

		EventHandler<MouseEvent> mouseDragged = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(press==true) {
					point.x = (float) e.getX();
					point.y = (float) e.getY();
				}
				if(A.isOnSelect(e.getX(), e.getY())) {
					A.setYButton(e.getY());
				}
				if(B.isOnSelect(e.getX(), e.getY())) {
					B.setYButton(e.getY());
				}
				if(nb_player >= 3) {
					if(C.isOnSelect(e.getX(), e.getY())) {
						C.setYButton(e.getY());
					}
					if(nb_player == 4) {
						if(D.isOnSelect(e.getX(), e.getY())) {
							D.setYButton(e.getY());
						}
					}
				}

			}
		};

		Point2D pos_start = new Point2D();

		EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				pos_start.x = (float) e.getX();
				pos_start.y = (float) e.getY();
				if(isOn(l_planet, pos_start.x,pos_start.y)) {
					Planet source = isOnPlanet(l_planet, pos_start.x, pos_start.y);
					if(!(source.getProp().getName() == "Neutre")){
						press = true;
					}
				}
				if(save.isOn(e.getX(),e.getY())) {	
					try {
						fos = new FileOutputStream("t.txt");
						oos = new ObjectOutputStream(fos);
						oos.writeObject(nb_player);
						oos.writeObject(nb_AI);
						Iterator<Planet> it = l_planet.iterator();
						while (it.hasNext()) {
							Planet planet = it.next();
							oos.writeObject(planet);
							oos.writeObject(planet.getProp());
						}

						oos.writeObject(ProdTime);
						oos.writeObject(VessStrength);
						oos.writeObject(AI);
						System.out.println("Game saved");
						oos.close();

					}catch (IOException err) {
						err.printStackTrace();
					}
				}
				if(firstMenu==true) {	
					if(leftButton.isOn(e.getX(),e.getY())&&secondMenu==false){
						try {
							fis = new FileInputStream("t.txt");
							in = new ObjectInputStream(fis);
							nb_player = (int) in.readObject();
							nb_AI = (int) in.readObject();
							Planet p;
							Player player;
							int nbIter = 0;
							while(nbIter < NBNEUTRALPLANETS+nb_player+nb_AI) {
								p = (Planet) in.readObject();
								l_planet.add(p);
								player = (Player) in.readObject();
								if(player.getName().equals("A")) {
									A = new Player("A", A.getColor());
									p.setProp(A);
								}
								else if(player.getName().equals("B")) {
									B = new Player("B", B.getColor());
									p.setProp(B);
								}
								
								else if(player.getName().equals("C")) {
									C = new Player("C", C.getColor());
									p.setProp(C);
								}
								else if(player.getName().equals("D")) {
									D = new Player("D", D.getColor());
									p.setProp(D);
								}
								else{
									p.getProp().setColor(Neutre.getColor());
									p.setProp(Neutre);
								}
								nbIter++;
							}
							ProdTime = (int)in.readObject();
							VessStrength = (int)in.readObject();
							AI = (boolean)in.readObject();
							if(AI) {
								nb_player =1;
								if(nb_AI >= 1) {
									aiA = new AI(game, "AIA",B.getColor(),l_planet,B);
									if(nb_AI >=2) {
										aiB = new AI(game, "AIB",C.getColor(),l_planet,C);
										if(nb_AI ==3) {
											aiC = new AI(game, "AIC",D.getColor(),l_planet,D);
										}
									}
								}
							}
							in.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						
						gameStarted = true;
						firstMenu = false;
						secondMenu= false;
						printGameInfo();

					}
					if(one_player.isOn(e.getX(),e.getY())&&secondMenu==true) {
						loading = true;
						gameStarted=true;
						AI = true;
						secondMenu=false;
						firstMenu = false;
						nb_AI = 1;
						aiA = new AI(game, "AIA",B.getColor(),l_planet,B);
						printGameInfo();
					}

					if(two_player.isOn(e.getX(),e.getY())&&secondMenu==true) {
						secondMenu = false;
						gameStarted = true;
						firstMenu=false;
						nb_player = 2;
						generate_planet();
						printGameInfo();
					}
					
					if(three_player.isOn(e.getX(),e.getY())&&secondMenu==true) {
						secondMenu = false;
						gameStarted = true;
						firstMenu=false;
						nb_player = 3;
						generate_planet();
						printGameInfo();
					}
					
					if(four_player.isOn(e.getX(),e.getY())&&secondMenu==true) {
						secondMenu = false;
						gameStarted = true;
						firstMenu=false;
						nb_player = 4;
						generate_planet();
						printGameInfo();
					}
					
					if(one_AI.isOn(e.getX(), e.getY())) {
						loading = true;
						gameStarted=true;
						AI = true;
						secondMenu=false;
						firstMenu = false;
						nb_player = 1;
						nb_AI = 1;
						aiA = new AI(game, "AIA",B.getColor(),l_planet,B);
						printGameInfo();
					}
					
					if(two_AI.isOn(e.getX(), e.getY())) {
						loading = true;
						gameStarted=true;
						AI = true;
						secondMenu=false;
						firstMenu = false;
						nb_AI = 2;
						nb_player = 1;
						aiA = new AI(game, "AIA",B.getColor(),l_planet,B);
						aiB = new AI(game, "AIB",C.getColor(),l_planet,C);
						printGameInfo();
					}
					
					if(three_AI.isOn(e.getX(), e.getY())) {
						loading = true;
						gameStarted=true;
						AI = true;
						secondMenu=false;
						firstMenu = false;
						nb_AI = 3;
						nb_player = 1;
						aiA = new AI(game, "AIA",B.getColor(),l_planet,B);
						aiB = new AI(game, "AIB",C.getColor(),l_planet,C);
						aiC = new AI(game, "AIC",D.getColor(),l_planet,D);
						printGameInfo();
					}

					if(rightButton.isOn(e.getX(),e.getY())&&secondMenu==false) {
						secondMenu = true;
					}
				}

				if(e.isControlDown()) {
					redirection(A,e.getX(),e.getY());
				}
				if(e.isShiftDown() && AI==false) {
					redirection(B,e.getX(),e.getY());
				}
				if(nb_player>=3 && AI==false) {
					if(e.isMiddleButtonDown()) {
						redirection(C,e.getX(),e.getY());
					}
					if(nb_player==4 && AI==false) {
						if(e.isAltDown()) {
							redirection(D,e.getX(),e.getY());
						}
					}
				}
				
				if(e.isSecondaryButtonDown()) {
					if(isOn(l_planet, e.getX(),e.getY())) {
						Planet planet = isOnPlanet(l_planet,e.getX(),e.getY());
						if(!planet.getProp().equals(Neutre) && planet.isSelectStrong()==false) {
							if(AI==false) {
								planet.setSelectStrong(true);
							}else {
								if(planet.getProp().equals(A)) {
									planet.setSelectStrong(true);
								}
							}
						}else if(!planet.getProp().equals(Neutre) && planet.isSelectStrong()==true) {
							planet.setSelectStrong(false);
						}
					}
				}
			}
		};

		EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(press==true) {
					press = false;
					if(isOn(l_planet,point.x,point.y)) {
						Planet desti = isOnPlanet(l_planet,point.x, point.y);
						Planet source = isOnPlanet(l_planet, pos_start.x, pos_start.y);
						if (source.getProp().getName() == "A"){
							Percentage_send = A.getPercentage();
						}else if(source.getProp().getName() == "B"){
							Percentage_send = B.getPercentage();
						}else if(source.getProp().getName() == "C"){
							Percentage_send = C.getPercentage();
						}else if(source.getProp().getName() == "D"){
							Percentage_send = D.getPercentage();
						}
						int nbves=0;
						Iterator<Vessel> it = null;
						if(!source.isSelectStrong()) {
							nbves = (source.getNbVessels() *  Percentage_send) / 100;
							source.setNbVessels(source.getNbVessels() - nbves);
							it = l_Vessel.iterator();
						}
						if(source.isSelectStrong()) {
							nbves = (source.getNbStrongVessels() *  Percentage_send) / 100;
							source.setNbStrongVessels(source.getNbStrongVessels() - nbves);
							it=l_StrongVessel.iterator();
						}
						
						
						int iterator = 0;
						
						double angle_rad = (int) Math.toRadians(0);
						int tab1[][] = Algo.searchPath(game, l_planet,(int)(source.getX()+(Math.cos(angle_rad)*source.getR()+15)),(int)(source.getY()+(Math.sin(angle_rad)*source.getR())),desti);
						Algo.deleteTab();
						angle_rad = (int) Math.toRadians(90);
						int tab2[][] = Algo.searchPath(game, l_planet,(int)(source.getX()+(Math.cos(angle_rad)*source.getR()-25)),(int)(source.getY()+(Math.sin(angle_rad)*source.getR()+15)),desti);
						Algo.deleteTab();
						angle_rad = Math.toRadians(180);
						int tab3[][] = Algo.searchPath(game, l_planet,(int)(source.getX()+(Math.cos(angle_rad)*source.getR()-30)),(int)(source.getY()+(Math.sin(angle_rad)*source.getR())),desti);
						Algo.deleteTab();
						angle_rad = (int) Math.toRadians(-90);
						int tab4[][] = Algo.searchPath(game, l_planet,(int)(source.getX()+(Math.cos(angle_rad)*source.getR()-25)),(int)(source.getY()+(Math.sin(angle_rad)*source.getR()-20)),desti);
						Algo.deleteTab();
						
						
						while((it.hasNext()) && (iterator<nbves)) {
							for(int j = 0;j<4;j++) {
								
								if(!it.hasNext()&& (iterator>=nbves))
									return;
								Vessel vessel = it.next();
								
								if(vessel.getCurrent().equals(source) && vessel.getMovement() == false) {
									vessel.setDestination(desti);
									vessel.setProp(source.getProp());
									vessel.setWaiting(true);
									
									if(j == 0)
										vessel.vesselPath(tab1);
									
									if(j == 1)
										vessel.vesselPath(tab2);
									
									if(j == 2)
										vessel.vesselPath(tab3);
									
									if(j == 3)
										vessel.vesselPath(tab4);

									iterator++;
								}
							}
						}
						point.x = 0;
						point.y = 0;
					}
				}

			}
		};

		scene.setOnMouseReleased(mouseReleased);
		scene.setOnMouseDragged(mouseDragged);
		scene.setOnMousePressed(mousePressed);

		new AnimationTimer() {
			public void handle(long arg0) {
				if(firstMenu==true) {
					if(gameStarted == false && secondMenu==false) {
						gc.setFill(Color.WHITESMOKE);
						gc.fillRect(0, 0, WIDTH, HEIGHT);
						rightButton.draw(gc, "NEW GAME");
						leftButton.draw(gc,"LOAD GAME");
					}

					else if(gameStarted == false && secondMenu==true) {
						gc.setFill(Color.WHITESMOKE);
						gc.fillRect(0, 0, WIDTH, HEIGHT);
						one_player.draw(gc, "1 PLAYER");
						two_player.draw(gc, "2 PLAYERS");
						three_player.draw(gc, "3 PLAYERS");
						four_player.draw(gc, "4 PLAYERS");
						one_AI.draw(gc," VS 1 AI");
						two_AI.draw(gc,"VS 2 AIs");
						three_AI.draw(gc,"VS 3 AIs");
					}


				}

				else if(loading == true) {
					gc.setFill(Color.BLACK);
					gc.fillRect(0, 0, WIDTH, HEIGHT);
					gc.setFill(Color.WHITE);
					gc.fillText("Loading . . .", 10, HEIGHT-10);
					if(loading_timer>10) {
						generate_planet();
						distance_planet_for_all(l_planet);
						loading = false;
					}
					loading_timer++;
				}
				
				else if(error == true) {
					gc.setFill(Color.BLACK);
					gc.fillRect(0, 0, WIDTH, HEIGHT);
					gc.setFill(Color.WHITE);
					gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
					gc.fillText("ERROR : TOO MANY PLANETES FOR THIS SPACE BETWEEN THEM, ", WIDTH/2 - 450, HEIGHT/2+50 );
					gc.fillText("RESTART THE GAME AND IF THE PROBLEM PERSIST THEN : ", WIDTH/2 - 400, HEIGHT/2+100 );
					gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
					gc.fillText("REDUCE THE NUMBER OF PLANETS OR THE SPACE BETWEEN THEM AND RESTART THE GAME. ", WIDTH/2 - 375, HEIGHT/2+150 );
					gc.setFont(Font.font ("Helvetica", 100));
					gc.fillText("RESTART THE GAME", -5, HEIGHT/2);
					gameStarted = false;
					firstMenu = false;
					gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
				}

				else {
					gc.setFill(Color.WHITESMOKE);
					gc.fillRect(0, 0, WIDTH, HEIGHT);


					save.draw(gc, "SAVE");


					A.drawButton(gc, scene, 35, HEIGHT - 120, 10,115);
					if(AI==false) {
						B.drawButton(gc, scene, 335, HEIGHT - 120, 10, 115);
					}
					if(nb_player >= 3 && AI==false) {
						C.drawButton(gc, scene, 635, HEIGHT - 120, 10, 115);
						if(nb_player == 4) {
							D.drawButton(gc, scene, 935, HEIGHT - 120, 10, 115);
						}
					}
					
					
					
					
					Iterator<Planet> it = l_planet.iterator();
					while (it.hasNext()) {
						Planet planet = it.next();
						if(timer%15==0) {
							planet.setNumvess(0);
						}
						if(planet.getProp().getName() != "Neutre" && timer==100) {
							planet.setNbVessels(planet.getNbVessels()+planet.getProdTime());
							planet.setNbStrongVessels(planet.getNbStrongVessels()+1);
							add_vessel(planet,  planet.getProdTime());
							add_strong_vessel(planet, 1);
						}
						if(planet.isSelectStrong()==false)
							planet.draw(gc);
						if(planet.isSelectStrong()==true)
							planet.stroke(gc);
					}
					
					if(AI==true) {
						if(lose(l_planet)) {
							l_Vessel = new ArrayList<Vessel>();
							gc.setFill(Color.rgb(0,0,0,0.75));
							gc.fillRect(0, 0, WIDTH, HEIGHT);
							gc.setFill(Color.WHITE);
							gc.setFont(Font.font ("Helvetica", 100));
							gc.fillText("YOU LOST",(WIDTH/2 - 200),(HEIGHT  / 2+50));
							
						}
					}
					if(isGameOver(l_planet)!=null) {
						l_Vessel = new ArrayList<Vessel>();
						gc.setFill(Color.rgb(0,0,0,0.75));
						gc.fillRect(0, 0, WIDTH, HEIGHT);
						gc.setFill(Color.WHITE);
						gc.setFont(Font.font ("Helvetica", 100));
						gc.fillText("PLAYER " +isGameOver(l_planet).getName() + " WON",(WIDTH/2 - 400),(HEIGHT  / 2+50));
						
					}
					
					Iterator<Vessel> nvessel = l_Vessel.iterator();
					while (nvessel.hasNext()) {
						Vessel vessel = nvessel.next();
						if(vessel.getWaiting()==true && vessel.getCurrent().getNumvess() < 4){
							vessel.setMovement(true);
							vessel.setWaiting(false);
							vessel.getCurrent().setNumvess(vessel.getCurrent().getNumvess() + 1);
						}

						if(vessel.getMovement() == true) {
							vessel.update_position(); 
							vessel.draw(gc);
							if (vessel.getCurrent() == null) {
								nvessel.remove();
							}
						}

						if(AI == true && timer%11 ==0 && timer != 100 &&  nb_vessel_send_AI<1 && vessel.getCurrent().getNbVessels() > 0 && vessel.getMovement() == false && vessel.getCurrent().getProp().getName().equals(aiA.getPlayer().getName())) {						
							aiA.send(vessel, numvess);
							nb_vessel_send_AI++;
						}
					}
					
					Iterator<Vessel> svessel = l_StrongVessel.iterator();
					while (svessel.hasNext()) {
						Vessel vessel = svessel.next();
						if(vessel.getWaiting()==true && numvessstrong < 4) {
							vessel.setMovement(true);
							vessel.setWaiting(false);
							numvessstrong ++;
						}

						if(vessel.getMovement() == true) {
							vessel.update_position(); 
							vessel.draw(gc);
							if (vessel.getCurrent() == null) {
								svessel.remove();
							}
						}
					}
					
					
					
					Iterator<Vessel> ai2 = l_Vessel.iterator();
					while (ai2.hasNext()) {
						Vessel vessel = ai2.next();
						if(AI == true && timer%12 ==0 && timer != 100 &&  nb_vessel_send_AI<1 && vessel.getCurrent().getNbVessels() > 0 && vessel.getMovement() == false ) {
							if(nb_AI >= 2) {
								if(vessel.getCurrent().getProp().getName().equals(aiB.getPlayer().getName())) {
									nb_vessel_send_AI ++;
									aiB.send(vessel,1);
								}
							}
						}
					}
					
					Iterator<Vessel> ai3 = l_Vessel.iterator();
					while (ai3.hasNext()) {
						Vessel vessel = ai3.next();
						if(AI == true && timer%13 ==0 && timer != 100 &&  nb_vessel_send_AI<1 && vessel.getCurrent().getNbVessels() > 0 && vessel.getMovement() == false ) {
							if(nb_AI == 3) {
								if(vessel.getCurrent().getProp().getName().equals(aiC.getPlayer().getName())) {
									nb_vessel_send_AI ++;
									aiC.send(vessel,1);
								}
							}
						}	
					}
					

					if(timer%20==0 && timer!=100) {
						numvess = 0;
						numvessstrong = 0;
					}

					if(timer == 100) {
						timer = 0;
					}

					else {
						timer++;
					}

					nb_vessel_send_AI = 0;
				}
			}
		}.start();
	}
}