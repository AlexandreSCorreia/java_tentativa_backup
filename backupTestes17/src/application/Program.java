package application;

import java.io.IOException;

import GUI.View_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modal.helper.Log;

public class Program extends Application {

	@Override
	public void start(Stage primaryStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View.fxml"));
			Parent parent = loader.load();
			Scene scene = new Scene(parent);			
			primaryStage.setScene(scene);		
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/GUI/icon.png")));
			primaryStage.setTitle("BACKUP");
			primaryStage.setResizable(false);
			// https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
			primaryStage.setOnCloseRequest(event -> System.exit(0));
			primaryStage.show();
			// Capturando referencia do objeto textArea da UI para setar os logs
			Log.setTextArea(((View_Controller) loader.getController()).textAreaLog);
			// Capturando referencia do objeto ScrollPane da UI para ajustar o tamanho do
			// textArea
			final ScrollPane scroll = ((View_Controller) loader.getController()).scrollPane;
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
