package Pacman;

import java.util.LinkedList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This is the Ghost Class it extends Rectangle shape and Implements Collidable Interface
 *
 */
public class Ghost extends Rectangle implements Collidable  {

	private int _score; //score that player get when Pacman eats Ghost
	private int _posX;  // Row position of Ghost
	private int _posY;  // Column position of Ghost
	private Color _color; //color of the Ghost Square
	private static GhostMode _mode;
	private Direction[][] _directions;
	private LinkedList<BoardCoordinate> _squaresQueue;
	private Direction _direction;
	private int _startPositionX;
	private int _startPositionY;
	/**
	 * Constructor for Ghost object that initiates all the Instance variables and calls super constructor
	 * @param x row position
	 * @param y column position
	 * @param color Color of Ghost
	 */
	public Ghost( int x, int y, Color color ){
		
		super( Constants.SQUARE_SIZE, Constants.SQUARE_SIZE );
		
		this.setFill(color);
		this._color = color;
		
		setPositionX(x);
		setPositionY(y);
		this._startPositionX = x;
		this._startPositionY = y;
		
		this._score = 200;
		this._direction = Direction.UP;
		_mode = GhostMode.CHASE;
	}
	
	/**
	 * Return the current Ghost mode
	 */
	public static GhostMode getMode(){
		return _mode;
	}
	
	/**
	 * Return the mode for Ghosts
	 */
	public static void setMode( GhostMode mode ){
		_mode = mode;
	}
	
	/**
	 * Returns current row 
	 */
	public int getRow(){
		return _posX;
	}
	
	/**
	 * Returns current column
	 */
	public int getCol(){
		return _posY;
	}
	
	public int getStartPositionX(){
		return _startPositionX;
	}
	
	public int getStartPositionY(){
		return _startPositionY;
	}
	
	/**
	 * Sets the position of object in the given Row as x
	 */
	public void setPositionX( int x){
		_posX = x;
		setX(x * Constants.SQUARE_SIZE);
	}
	
	/**
	 * Sets the position of object in the given Column as y
	 */
	public void setPositionY( int y ){
		_posY = y;
		setY(y * Constants.SQUARE_SIZE);
	}
	
	/**
	 * Move the ghost in the provided Direction Enum
	 * and update current _direction instance variable
	 */
	public void move( Direction move ){
		
		switch ( moveValidate(move) ) {
		
			case UP:
				setPositionY( _posY - 1  );
				_direction = move;
				break;
				
			case DOWN:
				setPositionY( _posY + 1  );
				_direction = move;
				
				break;
			case LEFT:
				// if on the left edge move to right most square
				if( _posX == 0  ){
					_posX = 23;
				}
				setPositionX( _posX - 1  );
				_direction = move;
				
				break;
			case RIGHT:
				// if on the right edge move to left most square
				if( _posX == 22  ){
					_posX = -1;
				}
				setPositionX( _posX + 1  );
				_direction = move;
				
				break;
				
			default:
				break;
		}
		
	}
	
	public Direction moveValidate( Direction move ){
		if( move == null ){
			if( _posX == 22 ){
				_posX = -1;
				return Direction.RIGHT;
			}
			
			if( _posX == 0 ){
				_posX = 23;
				return Direction.LEFT;
			}
		}
		
		return move;
	}
	
	public void Chase( SmartSquare[][] sqaures, int targetX, int targetY ){
		Direction next = moveToTarget( sqaures, targetX, targetY );
		move(next);
    	toFront();
	}
	
	/**
	 * Find the shortest next direction for the given Target Row and Column
	 * @param squares Squares locating 
	 * @param targetX Target Row position
	 * @param targetY Target Column position
	 * @return
	 */
	public Direction moveToTarget( SmartSquare[][] squares, int targetX, int targetY ){
		
		_directions = new Direction[23][23];
		_squaresQueue = new LinkedList<BoardCoordinate>();
		
		double distance = 999;
		Direction _nextDirection = null;
		
		int ghostX = _posX;
		int ghostY = _posY;
		
		BoardCoordinate target = new BoardCoordinate( targetX, targetY, true );
		BoardCoordinate start  = new BoardCoordinate( ghostX, ghostY, false );

		getNeighbours( squares, start );
		
		_directions[start.getRow()][start.getColumn()] = Direction.VISITED;
		while( ! _squaresQueue.isEmpty() ){
			
			BoardCoordinate current = _squaresQueue.remove();
			double currentDistance = getDistance( current, target );
			
			if ( currentDistance < distance ){
				distance = currentDistance;
				_nextDirection = _directions[current.getRow()][current.getColumn()];
				
			}
			
			_directions[current.getRow()][current.getColumn()] = Direction.VISITED;
			
			getNextNeighbours(squares, current, _nextDirection);
			
		}
		
		return _nextDirection;
		
	}
	
	/**
	 * Get the initial neighbor squares from start location for adding in queue
	 * @param squares
	 * @param start
	 */
	public void getNeighbours( SmartSquare[][] squares, BoardCoordinate start ){
		
		for ( Direction move : Direction.values()) {
			int x = start.getRow();
			int y = start.getColumn();
			
			switch( move ){
				case UP :
					y = y-1;
					addNeighbour(squares, x, y, move);
				break;
				case DOWN :
					y = y+1;
					addNeighbour(squares, x, y, move);
				break;
				case LEFT :
					x = x-1;
					addNeighbour(squares, x, y, move);
				break;
				case RIGHT :
					x = x+1;
					addNeighbour(squares, x, y, move);
				break;
				
				default:
					break;
			}
		}
		
	}
	
	/**
	 * get the neighbors for Dequeued squares
	 */
	public void getNextNeighbours( SmartSquare[][] squares, BoardCoordinate start , Direction dir){
		
		int x = start.getRow();
		int y = start.getColumn();
		
		addNeighbour(squares, x, y-1, dir);

		addNeighbour(squares, x, y+1, dir);

		addNeighbour(squares, x+1, y, dir);

		addNeighbour(squares, x-1, y, dir);
		
	}
	
	/**
	 * Check if a given position for neighbor is valid and add them to the queue
	 * @param squares
	 * @param x
	 * @param y
	 * @param direction
	 */
	public void addNeighbour( SmartSquare[][] squares, int x , int y , Direction direction ){
		BoardCoordinate neighbour = null;
		if( !validateX(x) || !validateY(y) ){
			return;
		}
		try {
			neighbour = new BoardCoordinate(x, y, false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( neighbour != null ){
				
				if( !squares[x][y].isWall() 
					&& _direction != direction.getOpposite() 
					&& _directions[x][y] != Direction.VISITED ) {
					_squaresQueue.add(neighbour);
					_directions[x][y] = direction;
				}
			}
		}
		
		
	}
	
	/**
	 * Calculate the distance from one BoardCoordinate to other
	 * @return distance
	 */
	public double getDistance( BoardCoordinate current, BoardCoordinate target ) {
	    
		double x1,x2,y1,y2;
		
		x1 = current.getRow();
		y1 = current.getColumn();
		
		x2 = target.getRow();
		y2 = target.getColumn();
		
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	
	/**
	 * Reset Ghost to original color
	 */
	public void resetColor(){
		setFill(_color);
	}
	
	/**
	 * Reset Ghost to start position
	 */
	public void resetPosition(){
		setPositionX(_startPositionX);
		setPositionY(_startPositionY);
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
	// return the Score for GHOST eating
	public int getScore() {
		return _score;
	}
}
