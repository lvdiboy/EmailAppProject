package com.dilyu;

import com.dilyu.view.ViewFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ViewFactory viewFactory = ViewFactory.defaultFactory;
		
		Scene scene = viewFactory.getMainScene();
		primaryStage.setScene(scene);
		primaryStage.show();				
		
	}

}