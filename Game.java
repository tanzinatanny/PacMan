package Pacman;

import java.util.ArrayList;
import java.util.LinkedList;

import cs015.fnl.PacmanSupport.BoardLocation;
import cs015.fnl.PacmanSupport.SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
  * This is the  Game class where our Pacman game logics are placed.
  */
class Game{
	
	private static Pacman _pacman;
	
	private static Ghost _pinky;
	private static Ghost _blinky;
	private static Ghost _clide;
	private static Ghost _inky;
	
	private Pane _boardPane;
	private Timeline _timeline;
	private ArrayList<SmartSquare> _boardSquares = new ArrayList<SmartSquare>();
	private SmartSquare[] [] _squares;
	
	private int _gameScore;
	private int _livesRemaining;
	private Label _scoreLabel;
	private Label _livesLabel;
	private Label _notice;
	
	private LinkedList<Ghost> _inactiveGhosts;
	private LinkedList<Ghost> _activeGhosts;
	
	/**
	 * Constructor for game class that takes in a Pane object and add Board elements in it and initiates all necessary variables and objects
	 * @param _centerPane
	 */
	public Game(Pane _centerPane) {
		
		this._boardPane = _centerPane;
		this._boardPane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
		
		//initiate instance variables
		_gameScore = 0;
		_livesRemaining = 3;
		_squares = new SmartSquare[23][23];
		_inactiveGhosts = new LinkedList<Ghost>();
		_activeGhosts = new LinkedList<Ghost>();
		
		//setup labels
		setScoreLabel();
		setLiveLabel();
		setNoticeLabel();
		//instantiate our pacman
		_pacman = new Pacman( 0, 0 );
		
		//create the ghosts
		createGhosts();
		
		//Create the board
		this.createBoard();
		
		//Keep the board updated with pacman and ghost moves
		this.updateBoard();
	}
	
	/**
	 * Instantiate the ghost objects
	 */
	private void createGhosts(){

		//create Ghosts
		_pinky  = new Ghost( 11, 10, Color.PINK);
		
		_blinky = new Ghost( 10, 10, Color.RED );
		_clide  = new Ghost( 12, 10, Color.CYAN );
		_inky   = new Ghost( 11, 11, Color.GREENYELLOW );
		
		_activeGhosts.add(_pinky);
		
		//make other ghosts inactive
		_inactiveGhosts.add(_blinky);
		_inactiveGhosts.add(_clide);
		_inactiveGhosts.add(_inky);
		
		//add ghosts to Board
		_boardPane.getChildren().add((Rectangle)(_blinky));
		_boardPane.getChildren().add((Rectangle)(_clide));
		_boardPane.getChildren().add((Rectangle)(_inky));
	}
	
	/**
	 * Getter method for _pacman
	 * @return
	 */
	public static Pacman getPacman(){
		return _pacman;
	}
	
	/**
	 * Getter method for _blinky
	 * @return
	 */
	public static Ghost getBlinky(){
		return _blinky;
	}
	
	/**
	 * Getter method for _pinky
	 * @return
	 */
	public static Ghost getPinky(){
		return _pinky;
	}
	
	/**
	 * Getter method for _gameScore
	 * @return
	 */
	public int getScore(){
		return _gameScore;
	}
	
	/**
	 * Getter method for _livesRemaining
	 * @return
	 */
	public int getLives(){
		return _livesRemaining;
	}
	
	/**
	 * This is used to fill our _squares array and Generate objects ( WALL, DOT, ENERGIZER etc. )
	 * using given locations of supportMap points array
	 * and visually add them to our _boardPane
	 */
	private void createBoard(){
		BoardLocation[][] points = SupportMap.getMap();
		
		SmartSquare square;
		
		for( int row = 0; row <= Constants.NUM_ROWS; row++ ){
			
			for( int col = 0; col <= Constants.NUM_COLS; col++ ){
				
				square = new SmartSquare(row, col, points[col][row]);
				
				 if( !square.isSquareType(BoardLocation.PACMAN_START_LOCATION) || !square.isSquareType(BoardLocation.GHOST_START_LOCATION) ){
					 _boardSquares.add(square);
				 }
				 _squares[row][col] = square;
				_boardPane.getChildren().add(square.getNode());
			}
		
		}
	}
	
	/**
	 * This function handles the _timeline of the game and updates positions of objects
	 */
	void updateBoard(){
		KeyFrame kf = new KeyFrame(Duration.seconds(0.2),new EventHandler<ActionEvent>(){
			
			int counter = 0;
			int frightenModeCounter = 0;
            @Override
            public void handle(ActionEvent event) {
             	//cycle counter
            	counter++;
             	
            	if( counter == 10 ){
            		_notice.setText("Go ->>>");
            	} else if( counter > 30 ){
            		_notice.setText("");
            	}
             	//bring ghosts to front
             	_pinky.toFront();
             	_blinky.toFront();
        		_clide.toFront();
        		_inky.toFront();
            	
        		//move pacman
            	_pacman.toFront();
            	_pacman.move(_squares);
            	
            	checkCollision();
            	
            	//each 20 cycle release a Ghost from inactive list
            	if( counter % 20 == 0 ){
             		
            		if( !_inactiveGhosts.isEmpty() ){
            			Ghost ghost = _inactiveGhosts.remove();
                		ghost.setPositionX( ghost.getStartPositionX() );
                		ghost.setPositionY( ghost.getStartPositionY() - 2 );
                		_activeGhosts.add(ghost);
            		}
            		
             	}
            	
            	//switch between Scatter and Chase mode each 30 cycle if the mode is not Frightened mode
            	if( counter % 30 == 0 ){
            		if( Ghost.getMode() != GhostMode.FRIGHTENED ){
            			if( Ghost.getMode() == GhostMode.CHASE ){
            				Ghost.setMode(GhostMode.SCATTER);
            			} else {
            				Ghost.setMode(GhostMode.CHASE);
            			}
            		}
            	}
            	
            	// wait 10 cycles and then start moving ghosts
            	if( counter > 10 ){
            		
            		switch (Ghost.getMode()) {
            		
						case SCATTER:
								if( _activeGhosts.contains(_pinky) ){
									_pinky.Chase( _squares, 0, 0 ); // target top left
								}
								
								if( _activeGhosts.contains(_blinky) ){
									_blinky.Chase( _squares, 0, 22 );  // target top right
								}
								
								if( _activeGhosts.contains(_clide) ){
									_clide.Chase( _squares, 22,0 );  // target bottom left
								}
								
								if( _activeGhosts.contains(_inky) ){
									_inky.Chase( _squares, 22,22 );  // target bottom right
								}
							break;
							
						case CHASE:
							
							if( _activeGhosts.contains(_pinky) ){
								_pinky.Chase( _squares,  _pacman.getRow(), _pacman.getCol() ); // target Pacman location
							}
							
							if( _activeGhosts.contains(_blinky) ){
								_blinky.Chase( _squares, _pacman.getRow() + 2, _pacman.getCol() );  // target 2 spaces to the right of Pacman’s location
							}
							
							if( _activeGhosts.contains(_clide) ){
								_clide.Chase( _squares, _pacman.getRow(), _pacman.getCol() - 4 );  // target 4 spaces above of Pacman’s location
							}
							
							if( _activeGhosts.contains(_inky) ){
								_inky.Chase( _squares, _pacman.getRow() - 3, _pacman.getCol() + 1 );  // target 3 spaces left and 1 space down of Pacman’s location
							}
							
							break;
							
						case FRIGHTENED:
							
							int randRow = (int) (Math.random() * 22);
							int randCol = (int) (Math.random() * 22);
							
							//set active Ghost's color to blue and chase random location
							if( _activeGhosts.contains(_pinky) ){
								_pinky.setFill(Color.BLUE);
								_pinky.Chase( _squares, randRow, randCol );
							}
							
							if( _activeGhosts.contains(_blinky) ){
								_blinky.setFill(Color.BLUE);
								_blinky.Chase(_squares, randRow - 5, randCol + 5 );
							}
							
							if( _activeGhosts.contains(_clide) ){
								_clide.setFill(Color.BLUE);
								_clide.Chase(_squares, randRow + 4, randCol - 4 );
							}
							
							if( _activeGhosts.contains(_inky) ){
								_inky.setFill(Color.BLUE);
								_inky.Chase(_squares, randRow + 3,  randCol - 2 );
							}
								
	                    	frightenModeCounter++; //increase frightened mode counter
	                    	
							break;
	
						default:
							break;
					}
            		
            		//If Frightened mode lasted for 20 counters reset mode
                	if( frightenModeCounter > 20 ){
                		frightenModeCounter = 0;
                		Ghost.setMode(GhostMode.CHASE);
                		_pinky.resetColor();
                		_blinky.resetColor();
                		_clide.resetColor();
                		_inky.resetColor();
                	}
            	}
            	
            	checkCollision();
            }
        });
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}
	
	/**
	 * This is where collision is checked between pacman and Dots or Energizers or Ghosts
	 */
	private void checkCollision(){
		
		int pacRow = _pacman.getRow();
		int pacCol = _pacman.getCol();
		
		SmartSquare currentSquare = _squares[pacRow][pacCol];
		
		if( currentSquare.isSquareType(BoardLocation.DOT) || currentSquare.isSquareType(BoardLocation.ENERGIZER)){
			Shape s = currentSquare.getNode();
			_boardPane.getChildren().remove(s);
			updateScore( ((Collidable) s ).getScore() );
			_squares[pacRow][pacCol] = new SmartSquare(pacRow, pacCol, BoardLocation.FREE);
			
			if( currentSquare.isSquareType(BoardLocation.ENERGIZER) ){
				Ghost.setMode(GhostMode.FRIGHTENED);
			}
		}
		
		checkGhostCollision(_pinky);
		checkGhostCollision(_blinky);
		checkGhostCollision(_clide);
		checkGhostCollision(_inky);
	}
	
	private void checkGhostCollision( Ghost ghost ){
		
		if( ghost.getRow() == _pacman.getRow() && ghost.getCol() == _pacman.getCol() ){
			if( Ghost.getMode() == GhostMode.FRIGHTENED ){
				ghostDead( ghost );
			} else {
				pacmanDead();
			}
		}
	}
	
	/**
	 * Reset pacman position and update remaining lives
	 */
	private void pacmanDead(){
		updateLives();
		_pacman.setPositionX(11);
		_pacman.setPositionY(13);
	}
	
	/**
	 * update the score and resend the Ghost to GhostPen
	 */
	private void ghostDead( Ghost ghost ){
		
		updateScore(ghost.getScore());
		ghost.resetPosition();
		ghost.resetColor();
		ghost.toFront();
		_inactiveGhosts.add(ghost);
		_activeGhosts.remove(ghost);
		
	}
	
	/**
	 * Sets the Label text for score
	 */
	private void setScoreLabel(){
		_scoreLabel = new Label("Score : 0");
		_scoreLabel.setLayoutX(50);
		_scoreLabel.setLayoutY(470);
		_scoreLabel.setStyle(" -fx-font: 15px Tahoma;");
		_scoreLabel.setTextFill(Color.WHITE);
		_boardPane.getChildren().add(_scoreLabel);
	}
	
	/**
	 * Sets the Label text for lives remaining
	 */
	private void setLiveLabel(){
		_livesLabel = new Label( "Lives : 3" );
		_livesLabel.setLayoutX(300);
		_livesLabel.setLayoutY(470);
		_livesLabel.setStyle(" -fx-font: 15px Tahoma;");
		_livesLabel.setTextFill(Color.WHITE);
		_boardPane.getChildren().add(_livesLabel);
	}
	
	/**
	 * Sets the Label text for showing notice at first
	 */
	private void setNoticeLabel(){
		_notice = new Label( "Ready !!!" );
		_notice.setLayoutX(190);
		_notice.setLayoutY(470);
		_notice.setStyle(" -fx-font: 15px Tahoma;");
		_notice.setTextFill(Color.WHITE);
		_boardPane.getChildren().add(_notice);
	}
	
	/**
	 * Updates the score label text with the given score added to original score
	 * @param score
	 */
	public void updateScore( int score){
		_gameScore = _gameScore + score;
		_scoreLabel.setText( "Score : "+ _gameScore );
	}
	
	/**
	 * Reduces available life count by 1 and updates the label 
	 */
	public void updateLives(){
		
		_livesRemaining--;
		
		_livesLabel.setText( "Lives : "+ _livesRemaining );
		if( _livesRemaining == 0 ){
			gameOver();
		}
	}
	
	/**
	 * Stops the Game timeline and shows Game Over Label
	 */
	public void gameOver(){
		_timeline.stop();
		showGameOverText();
		_boardPane.getChildren().remove(_pacman);
	}
	
	private void showGameOverText(){
		
		Label label = new Label( "Game Over" );
		label.setLayoutX( 140 );
		label.setLayoutY(200);
		label.setStyle(" -fx-font: 30px Arial; -fx-font-weight: 600; -fx-background-color : #fff;  -fx-padding : 10px");
		label.setTextFill(Color.RED);
		
		_boardPane.getChildren().add(label);
	}
	
	//Key handler class to handle KeyPress events
	private class KeyHandler implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent e) {
			KeyCode keyPressed = e.getCode();
			
		        switch (keyPressed) {
		           
		            case LEFT:
		            	_pacman.setDirection(Direction.LEFT);
		            	e.consume();
			            break;
			            
		            case RIGHT:
		            	_pacman.setDirection(Direction.RIGHT);
		            	e.consume();
			            break;
			            
		            case UP:
		            	_pacman.setDirection(Direction.UP);
		            	e.consume();
			            break;
			            
		            case DOWN:
		            	_pacman.setDirection(Direction.DOWN);
		            	e.consume();
			            break;
			     
		            case Q:
		            	Platform.exit();
		            	break;
		            	
					default:
						break;
		        }
		        
			}
	}
	
}