import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The Class Button.
 */
public class Button {
	
	/** The x coordinate of the button.
	 *  The y coordinate of the button.
	 */
	private double x,y;
	
	/** The width of the button
	 * the height of the button. */
	private int width, height;
	
	/**
	 * Instantiates a new button.
	 *
	 * @param x the x coordinate of the button.
	 * @param y the y coordinate of the button.
	 * @param width the width of the button.
	 * @param height the height of the button.
	 */
	Button(double x, double y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Draw the button.
	 *
	 * @param gc the graphicsContext.
	 * @param text the text that we want to write on the button.
	 */
	public void draw(GraphicsContext gc, String text){
		int size = 17;
		gc.setFill(Color.BLACK);
		gc.fillRect(x, y, width, height);
		gc.setStroke(Color.RED);
		gc.strokeRect(x, y, width, height);
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font ("Verdana", size));
		gc.fillText(text,((x+(x+width))/2)-50,((y+(y+height))/2));
		gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
	}
	
	/**
	 * Checks if a point is on the button.
	 *
	 * @param d the x coordinate of the point.
	 * @param e the y coordinate of the point.
	 * @return true, if is on and false otherwise.
	 */
	public boolean isOn(double d, double e) {
		if((d<x+width&&d>x)&&(e<y+height&&e>y)) {
			return true;
		}else{
			return false;
		}
	}
}
