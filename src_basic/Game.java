
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.sun.javafx.geom.Point2D;

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
	private final static int NBNEUTRALPLANETS = 7;
	
	/** The minimum production time. */
	private final static int MinProd = 1;

	/** The maximum production time. */
	private final static int MaxProd = 15;

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
	 * error, true if an error appear*/
	boolean gameStarted = false, secondMenu, AI=false, firstMenu=true, press=false, loading = false, error = false;

	/**  The save's button. */
	Button save = new Button(10,10,130,30);

	/** The left button (used in menus). */
	Button leftButton = new Button(WIDTH/2-250,HEIGHT/2,200,80);

	/** The right button (used in menus). */
	Button rightButton = new Button(WIDTH/2 + 50,HEIGHT/2,200,80);

	/** The neutral player. */
	Player Neutre = new Player("Neutre",Color.GRAY);

	/** The A player. */
	Player A = new Player("A",Color.BLUE);

	/** The B player. */
	Player B = new Player("B",Color.RED);

	/** The AI. */
	AI ai = null;

	/** The timer (used in the AnimationTimer). 
	 * If a player win he has to wait that the timer be at 300 before he completely win.*/
	int timer = 0, timer_win = 0;

	/** The number of vessels that the AI send. */
	int nb_vessel_send_AI = 0;

	/** The percentage of the reserve of vessels of a planet that we send. */
	int Percentage_send;

	

	/** The number of the vessels that we send. */
	int numvess = 0, numvessAI = 0;

	/** The current game where we are playing. */
	Game game = this;

	/** The loading timer. */
	int loading_timer = 0;


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
	 */
	public void add_planet(ArrayList<Planet> list, Player player, int nbVes) {
		try {

			int MinR = 40;
			int MaxR = 75;

			int Minx = MaxR+50;
			int Maxx = WIDTH - MaxR-50;

			int Miny = MaxR+50;
			int Maxy = HEIGHT - MaxR-25;			

			Circle c = circle_alea(Minx,Maxx,Miny,Maxy,MaxR,MinR);

			Planet p1 = new Planet( c, ProdTime ,nbVes ,player, false, this);

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
							add_planet(list,player,nbVes);
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
		int cptA = 0;
		int cptB = 0;
		Iterator<Planet> it = l_planet.iterator();
		while (it.hasNext()) {
			Planet planet = it.next();
			if(planet.getProp().getName().equals("A")) {
				cptA ++;
			}
			if(planet.getProp().getName().equals("B")) {
				cptB ++;
			}
		}
		if(cptA==0) {
			return B;
		}

		if(cptB==0) {
			return A;
		}

		return null;
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
	 * Generates all planets of the game.
	 */
	public void generate_planet() {

		int MinnbVess = 12;
		int MaxnbVess = 150;

		for(int i=0; i<NBNEUTRALPLANETS ; i++) {
			int nbVesN = MinnbVess + (int)(Math.random() * ((MaxnbVess - MinnbVess) + 1));
			add_planet(l_planet, Neutre, nbVesN);
		}

		int nbVes = MinnbVess + (int)(Math.random() * ((MaxnbVess - MinnbVess) + 1));

		add_planet(l_planet,A,nbVes);
		add_planet(l_planet,B,nbVes);
	}

	/**
	 * Generate all vessels of the game.
	 */
	public void generate_vessel() {
		Iterator<Planet> it = l_planet.iterator();
		while (it.hasNext()) {
			Planet planet = it.next();
			add_vessel(planet,  planet.getNbVessels());
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

				else if(B.isOnSelect(e.getX(), e.getY())) {
					B.setYButton(e.getY());
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
							Planet p;
							Player player;
							int nbIter = 0;
							while(nbIter < NBNEUTRALPLANETS+2) {
								p = (Planet) in.readObject();
								l_planet.add(p);
								player = (Player) in.readObject();
								if(player.getName().equals("A")) {
									p.getProp().setColor(A.getColor());
									p.setProp(A);
								}
								else if(player.getName().equals("B")) {
									p.getProp().setColor(B.getColor());
									p.setProp(B);
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
								ai = new AI(game, "AI",B.getColor(),l_planet,B);
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
					if(leftButton.isOn(e.getX(),e.getY())&&secondMenu==true) {
						loading = true;
						gameStarted=true;
						AI = true;
						secondMenu=false;
						firstMenu = false;
						ai = new AI(game, "AI",B.getColor(),l_planet,B);
						printGameInfo();
					}

					if(rightButton.isOn(e.getX(),e.getY())&&secondMenu==true) {
						secondMenu = false;
						generate_planet();
						gameStarted = true;
						firstMenu=false;
						printGameInfo();
					}

					if(rightButton.isOn(e.getX(),e.getY())&&secondMenu==false) {
						secondMenu = true;
					}
				}

				if(e.isControlDown()) {
					redirection(A,e.getX(),e.getY());
				}

				if(e.isShiftDown()&&AI==false) {
					redirection(B,e.getX(),e.getY());
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
						point.x = 0;
						point.y = 0;
						if (source.getProp().getName() == "A"){
							Percentage_send = A.getPercentage();
						}else {
							Percentage_send = B.getPercentage();
						}
						int nbves = (source.getNbVessels() *  Percentage_send) / 100;
						source.setNbVessels(source.getNbVessels() - nbves);
						Iterator<Vessel> it = l_Vessel.iterator();
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
						rightButton.draw(gc, "1 VS 1");
						leftButton.draw(gc,"1 VS AI");
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
					gc.fillText("ERROR, TOO MANY PLANETES FOR THIS SPACE BETWEEN THEM, ", WIDTH/2 - 450, HEIGHT/2+100 );
					gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
					gc.fillText("REDUCE THE NUMBER OF PLANETS OR THE SPACE BETWEEN THEM AND RESTART THE GAME. ", WIDTH/2 - 400, HEIGHT/2+150 );
					gc.setFont(Font.font ("Helvetica", 100));
					gc.fillText("RESTART THE GAME", -5, HEIGHT/2 );
					gameStarted = false;
					firstMenu = false;
					gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
				}
				
				else {
					gc.setFill(Color.WHITESMOKE);
					gc.fillRect(0, 0, WIDTH, HEIGHT);


					save.draw(gc, "SAVE");


					A.drawButton(gc, scene, 25, HEIGHT - 120, 10,115);
					if(AI==false) {
						B.drawButton(gc, scene, WIDTH-60, HEIGHT - 120, 10, 115);
					}

					Iterator<Planet> it = l_planet.iterator();
					while (it.hasNext()) {
						Planet planet = it.next();
						if(timer%15==0) {
							planet.setNumvess(0);
						}
						if(planet.getProp().getName() != "Neutre" && timer==100) {
							planet.setNbVessels(planet.getNbVessels()+planet.getProdTime());
							add_vessel(planet,  planet.getProdTime());
						}
						planet.draw(gc);
					}


					Iterator<Vessel> it2 = l_Vessel.iterator();
					while (it2.hasNext()) {
						Vessel vessel = it2.next();
						if(vessel.getWaiting()==true && vessel.getCurrent().getNumvess() < 4){
							vessel.setMovement(true);
							vessel.setWaiting(false);
							vessel.getCurrent().setNumvess(vessel.getCurrent().getNumvess() + 1);
						}

						if(vessel.getMovement() == true) {
							vessel.update_position(); 
							vessel.draw(gc);
							if (vessel.getCurrent() == null) {
								it2.remove();
							}
						}

						if(AI == true && timer%10 ==0 && timer != 100 &&  nb_vessel_send_AI<1 && vessel.getCurrent().getNbVessels() != 0 && vessel.getMovement() == false && vessel.getCurrent().getProp().getName().equals(ai.getPlayer().getName())) {
							ai.send(vessel,nb_vessel_send_AI);
							nb_vessel_send_AI++;
						}
					}


					if(isGameOver(l_planet) == B || isGameOver(l_planet) == A) {
						l_Vessel = new ArrayList<Vessel>();
						gc.setFill(Color.rgb(0,0,0,0.75));
						gc.fillRect(0, 0, WIDTH, HEIGHT);
						gc.setFill(Color.WHITE);
						gc.setFont(Font.font ("Helvetica", 100));
						if(AI == true && isGameOver(l_planet) == B) {
							gc.fillText("YOU LOST",(WIDTH/2 - 200),(HEIGHT  / 2+50));
						}
						else
							gc.fillText("PLAYER " +isGameOver(l_planet).getName() + " WON",(WIDTH/2 - 400),(HEIGHT  / 2+50));
						
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