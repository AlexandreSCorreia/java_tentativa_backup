package GUI.util;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Alerts {

	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}

	public static String showInputTextDialog() {

		Dialog<String> dialog = new Dialog<>();

		dialog.setTitle("Salvar");
		dialog.setHeaderText(
				"O apelido ajuda a identificar, qual site ou software \n" + "usará está criptografia como senha. \n"
						+ "Não digite apelidos que facilitem identificação do site ou software,\n"
						+ "onde usará está criptografia como senha!");

		ButtonType salvarButtonType = new ButtonType("Salvar", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(salvarButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 80));

		TextField txtApelido = new TextField();
		txtApelido.setPrefWidth(200);
		txtApelido.setPromptText("De um apelido para a sua senha");

		grid.add(new Label("Apelido:"), 0, 0);
		grid.add(txtApelido, 1, 0);

		Node salvarButton = dialog.getDialogPane().lookupButton(salvarButtonType);
		salvarButton.setDisable(true);

		txtApelido.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				salvarButton.setDisable(newValue.trim().isEmpty());
			}
		});

		dialog.getDialogPane().setContent(grid);

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == salvarButtonType) {

				return txtApelido.getText();
			}
			return null;
		});

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			System.out.println(result.get());
			return result.get().toString();
		}

		return null;
	}
}