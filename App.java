package Pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
  * This is the  main class where your Pacman game will start.
  * The main method of this application calls the App constructor. You
  * will need to fill in the constructor to instantiate your game.
  *
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
    	// Create top-level object, set up the scene, and show the stage here.
	  PaneOrganizer organizer = new PaneOrganizer();
	  Scene scene = new Scene(organizer.getRoot(), Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, Color.BLACK );
	  
	  stage.setScene(scene);
	  
	  stage.setTitle("Pac Man");
	  stage.show();
    	
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}

