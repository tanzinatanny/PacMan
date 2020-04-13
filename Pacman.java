package Pacman;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * This is the class for our pacman object
 * 
 * Pacman class extends Circle class
 * 
 */
public class Pacman extends Circle{
	
	private Direction _direction; //current direction the pacman is moving to
	private Direction _previousDirection; // previous direction the pacman was moving to
	private int _posX; // current row position
	private int _posY; // current column position
	private SmartSquare[][] _squares;
	
	/**
	 * Constructor for creating the pacman instance
	 * 
	 * @param x row position
	 * @param y column position
	 */
	public Pacman( int x, int y ){
		super(8, Color.ORANGE);
		this._posX = x;
		this._posY = y;
		this.setCenterX(x * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
		this.setCenterY(y * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
	}
	
	/*
	 * Get row position
	 */
	public int getRow(){
		return _posX;
	}
	
	/*
	 * Get column position
	 */
	public int getCol(){
		return _posY;
	}
	/**
	 * Keep the pacman moving on current Direction
	 */
	public void move( SmartSquare[][] squares ){
		_squares = squares;
		
		if( _direction == null ){
			return;
		}
		
		switch (_direction) {
			case UP:
				moveUp();
				break;
			case DOWN:
				moveDown();
				break;
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
			default:
				break;
		}
	}
	/*
	 * Set direction of pacman
	 */
	public void setDirection( Direction dir ){
		_direction = dir;
	}
	
	/*
	 * Get direction
	 */
	public Direction getDirection(){
		return _direction;
	}
	
	/*
	 * Set Row position
	 */
	public void setPositionX( int x ){
		if( !validateX(x) ){
			return;
		}
		_posX = x;
		setCenterX(x * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
	}
	
	/*
	 * Set column position
	 */
	public void setPositionY( int y ){
		if( !validateY(y) ){
			return;
		}
		_posY = y;
		setCenterY(y * Constants.SQUARE_SIZE + Constants.SQUARE_SIZE/2 );
	}
	
	/*
	 * Move pacman up by 1 row
	 */
	public void moveUp(){
		if ( !validateY(_posY-1) || _squares[_posX][_posY-1].isWall() ){
			_direction = _previousDirection;
			return;
		}
		_previousDirection = _direction;
		setPositionY(_posY-1);
	}
	
	/*
	 * Move pacman down by 1 row
	 */
	public void moveDown(){
		if (!validateY(_posY+1) || _squares[_posX][_posY+1].isWall() ){
			_direction = _previousDirection;
			return;
		}
		_previousDirection = _direction;
		setPositionY(_posY+1);
	}
	
	/*
	 * move pacman left by 1 column
	 */
	public void moveLeft(){
		// if on the left edge position to right most square
		if( _posX == 0 ){
			_posX = 23;
		}
		
		if ( !validateX(_posX-1) || _squares[_posX-1][_posY].isWall() ){
			_direction = _previousDirection;
			return;
		}
		_previousDirection = _direction;
		setPositionX(_posX-1);
	}
	
	/*
	 * move pacman right by 1 column
	 */
	public void moveRight(){
		// if on the right edge position to left most square
		if( _posX == 22 ){
			_posX = -1;
		}
		
		if ( !validateX(_posX+1) || _squares[_posX+1][_posY].isWall() ){
			_direction = _previousDirection;
			return;
		}
		_previousDirection = _direction;
		setPositionX(_posX+1);
	}
	
	//validate X position 
	private Boolean validateX( int x ){
		
		if( x < 0 || x > Constants.NUM_ROWS ){
			return false;
		}
		
		return true;
	}
	
	//validate Y position 
	private Boolean validateY( int y ){
		
		if( y < 0 || y > Constants.NUM_COLS){
			return false;
		}
		
		return true;
	}
}
