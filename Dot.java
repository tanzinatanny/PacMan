package Pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This is the DOT class which extends Circle to represent small circular dots in Pacman board
 */
public class Dot extends Circle implements Collidable{
	
	private int _score;
	
	/**
	 * Construction for DOT object
	 * @param x row position of Dot
	 * @param y column position of Dot
	 */
	public Dot( int x, int y ){
		super( x*Constants.SQUARE_SIZE, y*Constants.SQUARE_SIZE, 2 );
		this.setCenterX( x * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
		this.setCenterY( y * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
		this.setFill(Color.WHITE);
		
		this._score = 10;
	}
	
	/**
	 * Returns the score for DOT 
	 */
	public int getScore() {
		
		return _score;
	}
	
	
}