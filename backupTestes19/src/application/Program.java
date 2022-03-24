package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Program extends Application {

	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/MainView.fxml"));
			ScrollPane scrollPane = loader.load();

			// Deixar o scrollPane ajustar a janela
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/GUI/icon.png")));
			primaryStage.setTitle("BACKUP");
			primaryStage.setResizable(false);
			// https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
			primaryStage.setOnCloseRequest(event -> System.exit(0));
			primaryStage.show();

			// new MainViewController().loadViewBackup();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Scene getScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
