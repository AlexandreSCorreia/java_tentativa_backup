package application;

import java.io.IOException;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Main extends Application {

	public static final Logger logger = Logger.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) {
		try {

			final Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/GUI/View.fxml"));
			final Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Teste");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setAlwaysOnTop(true);
			//https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
			primaryStage.setOnCloseRequest(event -> System.exit(0));			
			primaryStage.show();
			// TextAreaAppender.setTextArea(((View_Controller)loader.getController()).getLogTextArea);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		launch(args);
	}
}
