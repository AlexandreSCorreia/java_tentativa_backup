package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import GUI.util.Alerts;
import application.Program;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modal.helper.Log;
import modal.service.DirectoryService;

public final class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemLocalDiskC;

	@FXML
	private MenuItem menuItemUserProfile;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private MenuItem menuItemInitializeBackup;

	@FXML
	public void onMenuItemLocalDiskCAtion() {

		loadView("/GUI/DirectoryList.fxml", (DirectoryListController controller) -> {
			controller.setDirectoryService(new DirectoryService());
			controller.updateTableView(0);
		});
	}

	@FXML
	public void onMenuItemUserProfileAction() {
		loadView("/GUI/DirectoryList.fxml", (DirectoryListController controller) -> {
			controller.setDirectoryService(new DirectoryService());
			controller.updateTableView(1);
		});
	}

	@FXML
	public void onMenuItemAboutAction(ActionEvent event) {
		loadView("/GUI/AboutView.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemInitializeBackupAction() {
		loadView("/GUI/BackupView.fxml", (BackupViewController controller) -> {

			// Capturando referencia do objeto textArea da UI para setar os logs
			Log.setTextArea(controller.textAreaLog);
			// Capturando referencia do objeto ScrollPane da UI para ajustar o tamanho do
			// textArea
			final ScrollPane scroll = controller.scrollPane;
			scroll.setFitToHeight(true);
			scroll.setFitToWidth(true);

		});
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Program.getScene();
			// Pegando uma referencipara o VBOX dentro do SCROLLPANE
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			// Capturando o menu para manter ele
			Node mainMenu = mainVBox.getChildren().get(0);
			// Limpando o vbox
			mainVBox.getChildren().clear();
			// adicionando o menu
			mainVBox.getChildren().add(mainMenu);
			// adicionando uma coleção que será os filhos do newVBox
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view ", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}
