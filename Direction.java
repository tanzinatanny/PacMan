package Pacman;

/**
 * This is an enum for types of Directions that can be taken
 */
public enum Direction {
	UP, DOWN, LEFT, RIGHT, VISITED;
	
	/**
	 * Returns the opposite direction for the current direction
	 * @return Direction
	 */
	public Direction getOpposite(){
		switch (this) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return VISITED;
			
		}
	}
}
