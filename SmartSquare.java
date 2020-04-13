package Pacman;

import java.util.ArrayList;

import cs015.fnl.PacmanSupport.BoardLocation;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;

/**
 * This is the SmartSquare class that represents each square of our pacman Board. 
 * It contains information and objects of each square.
 */
class SmartSquare{
	private int _positionX;
	private int _positionY;
	private Shape _node;
	private BoardLocation _type;
	
	private ArrayList<Shape> _collidables = new ArrayList<Shape>();
	
	/**
	 * This constructor takes 2 positions as int x and int y and places them as objects corresponding Row and Column
	 * and then generates the corresponding shape with given type Enum of BoardLocation
	 * 
	 * @param x Row value for object
	 * @param y Column value for object
	 * @param type The type of the object e.g : DOT, Energizer etc.
	 */
	public SmartSquare( int x, int y, BoardLocation type ){
		this._positionX = x;
		this._positionY = y;
		this._type = type;
		this._node = setNode();
	}
	
	/**
	 * Returns the shape that is contained within the square
	 */
	public Shape getNode(){
		return _node;
	}
	
	/**
	 * Create the shape that is contained within the square
	 * 
	 */
	public Shape setNode(){
		
		Shape piece;
		switch ( _type ){
		
			case WALL :
				piece = new Rectangle( Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Color.YELLOW );
				piece.setLayoutX(_positionX * Constants.SQUARE_SIZE );
				piece.setLayoutY(_positionY * Constants.SQUARE_SIZE);
				break;
				
			case DOT : 
				// create an instance of DOT class
				piece = new Dot(_positionX, _positionY );
				_collidables.add(piece);
				break;
				
			case FREE :
				piece = new Rectangle( Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Color.BLACK );
				piece.setLayoutX(_positionX * Constants.SQUARE_SIZE );
				piece.setLayoutY(_positionY * Constants.SQUARE_SIZE);
				break;
				
			case ENERGIZER :
				// create an instance of Energizer class
				piece = new Energizer(_positionX, _positionY);
				_collidables.add(piece);
				break;
				
			case GHOST_START_LOCATION :
				// set the starting point for the _pinky Ghost object from Game class
				Game.getPinky().setPositionX( _positionX );
				Game.getPinky().setPositionY( _positionY - 2 );
				
				piece = Game.getPinky();
				
				break;
				
			case PACMAN_START_LOCATION :
				// set the starting point for the _pacman Pacman object from Game class
				Game.getPacman().setPositionX(_positionX);
				Game.getPacman().setPositionY(_positionY);
				piece = Game.getPacman();
				break;
				
			default :
				// By default everything is Black and Free
				piece = new Rectangle( Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, Color.BLACK );
				piece.setLayoutX(_positionX * Constants.SQUARE_SIZE );
				piece.setLayoutY(_positionY * Constants.SQUARE_SIZE);
		}
		
		
		return piece;
		
	}
	
	/**
	 * Tells if the square is a Wall type of not 
	 */
	public Boolean isWall(){
		if( _type == BoardLocation.WALL ){
			return true;
		}
		return false;
	}
	
	/**
	 * Tells if the square is a given type of object
	 */
	public Boolean isSquareType( BoardLocation type ){
		
		if( _type == type ){
			return true;
		}
		
		return false;
	}
	
}