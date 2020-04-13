package Pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * This is Energizer class which extends Circle to represent circular Energizers in Pacman board
 */
public class Energizer extends Circle implements Collidable {
	
	private int _score;
	
	/**
	 * Constructor for Energizer object
	 * @param x row position of object
	 * @param y column position of object
	 */
	public Energizer( int x, int y ){
		super( x*Constants.SQUARE_SIZE, y*Constants.SQUARE_SIZE, 5 );
		this.setCenterX( x * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
		this.setCenterY( y * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
		this.setFill(Color.WHITE);
		
		this._score = 100;
	}
	
	/**
	 * Returns the score for Energizer
	 */
	public int getScore() {
		return _score;
	}
}
