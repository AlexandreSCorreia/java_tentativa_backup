package application;

import java.io.IOException;

import GUI.View_Controller;
import GUI.util.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public final class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View.fxml"));

			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Backup");
			primaryStage.setResizable(false);
			primaryStage.setAlwaysOnTop(true);
			// https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
			primaryStage.setOnCloseRequest(event -> System.exit(0));
			primaryStage.show();
			Log.setTextArea(((View_Controller) loader.getController()).getLogTextArea);
			ScrollPane scroll = ((View_Controller) loader.getController()).root;
			scroll.setFitToHeight(true);
			scroll.setFitToWidth(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		launch(args);
	}
}
