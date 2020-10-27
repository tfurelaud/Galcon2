
import java.io.Serializable;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The Class Player.
 */
public class Player implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5485602759240501074L;

	/** The name of the player. */
	private String name;
	
	/** The color given to the player. */
	private transient Color color;
	
	/** The y coordinate of the player's button. */
	private double yButton = 650 - 50;
	
	/** The x coordinate of the player's button. */
	private double xButton;
	
	/** The height button. */
	private double heightButton;
	
	/** The width button. */
	private double widthButton;
	
	/** The percentage which is selected thanks to the button. */
	private int percentage;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param name the name.
	 * @param color the color.
	 */
	Player(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	/**
	 * Gets the x button.
	 *
	 * @return the x coordinate of the player's button.
	 */
	public double getXButton() {
		return this.xButton;
	}
	
	/**
	 * Gets the y button.
	 *
	 * @return the y coordinate of the player's button.
	 */
	public double getYButton() {
		return this.yButton;
	}
	
	/**
	 * Sets the y button.
	 *
	 * @param y the new y coordinate of the player's button.
	 */
	public void setYButton(double y) {
		this.yButton = y;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color of the player.
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Sets the color.
	 *
	 * @param c the new color that we going to give to the player.
	 */
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * Sets the percentage.
	 *
	 * @param i the new percentage
	 */
	public void setPercentage(int i) {
		this.percentage = i;
	}
	
	/**
	 * Gets the percentage.
	 *
	 * @return the percentage
	 */
	public int getPercentage() {
		return this.percentage;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name of the player.
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * Draw the button for both players..
	 *
	 * @param gc the graphicsContext.
	 * @param scene the scene.
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public void drawButton(GraphicsContext gc, Scene scene, int x, int y, int width, int height) {
		widthButton = width;
		heightButton = height;
		xButton = x;
		int heightBlackButton = 10;
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 18));
		if(yButton>scene.getHeight()-(scene.getHeight()-y-height)-heightBlackButton) {
			gc.setFill(color);
			gc.setStroke(Color.BLACK);
			gc.fillRect(x, y , width, height);
			gc.fillText(name + " - 0%",x-20 , y -10);
			gc.strokeText(name + " - 0%",x-20 , y-10);
			percentage = 0;
			gc.setFill(Color.BLACK);
			gc.fillRect(x-5, scene.getHeight()-(scene.getHeight()-y-height)-heightBlackButton , 20, heightBlackButton);
			yButton = scene.getHeight()-15;
		}
		if(yButton<scene.getHeight()-(scene.getHeight()-y)-(heightBlackButton/2)) {
			gc.setFill(color);
			gc.setStroke(Color.BLACK);
			gc.fillRect(x, y , width, height);
			gc.fillText(name + " - 100%",x-20 , y -10);
			gc.strokeText(name + " - 100%",x-20 , y-10);
			gc.setFill(Color.BLACK);
			percentage = 100;
			gc.fillRect(x-5, scene.getHeight()-height , 20, heightBlackButton);
			yButton = scene.getHeight()-height;
		}
		else{
			gc.setFill(color);
			gc.setStroke(Color.BLACK);
			gc.fillRect(x, y , width, height);
			percentage = (int) Math.abs((yButton-scene.getHeight()+15)/1.1);
			String text = Integer.toString(percentage);
			gc.fillText(name + " - "+text + "%",x-20 , y - heightBlackButton);
			gc.strokeText(name +" - "+ text + "%",x-20 , y - heightBlackButton);
			gc.setFill(Color.BLACK);
			gc.fillRect(x-5, yButton , 20, heightBlackButton);
		}
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
	
	}
	
	/**
	 * Checks if a point is on the player button or not.
	 * 
	 * @param d the coordinate x that we want to check.
	 * @param e the coordinate y that we want to check.
	 * @return true if the point(d,e) is on the button.
	 */
	public boolean isOnSelect(double d, double e) {
		if((d>xButton-5 && d<xButton+widthButton+5)&&(e<yButton+heightButton && e>yButton-heightButton)) {
			return true;
		}
		return false;
	}

	
}