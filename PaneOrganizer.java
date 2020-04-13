package Pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * This is the paneorganizer class where we organizer our view panes and their methods
 */
public class PaneOrganizer {
	private BorderPane _border;
	private Pane _centerPane;
	
	public PaneOrganizer(){
		_border = new BorderPane();
		_border.setFocusTraversable(true);
		
		//setup the centerpane
		this.setupCenterPane();
		
		//start the game
		new Game(_centerPane);
	}
	
	//setup the center pane to hold the board
	public void setupCenterPane (){
		_centerPane = new Pane();
		_centerPane.requestFocus();
		_centerPane.setFocusTraversable(true);
		_centerPane.setStyle("-fx-background-color: rgb(0,0,0) ");
		_border.setCenter(_centerPane);
	}
	// return Root Pane
	public BorderPane getRoot(){
		return _border;
	}
	
}
		


		
	

