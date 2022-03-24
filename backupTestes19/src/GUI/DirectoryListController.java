package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import GUI.util.Alerts;
import GUI.util.Utils;
import application.Program;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modal.entities.DirectoryList;
import modal.service.DirectoryService;


public class DirectoryListController implements Initializable {

	private DirectoryService service;
	
	@FXML
	private TableView<DirectoryList> tableViewDirectoryList;

	@FXML
	private TableColumn<DirectoryList, String> tableColumnDirectory;

	@FXML
	private Button btNew;

	private ObservableList<DirectoryList> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("/gui/DirectoryForm.fxml", parentStage);
	}

	public void setDirectoryService(DirectoryService service) {
		this.service = service;
	}
	
	
	private void initiallizeNodes() {

		tableColumnDirectory.setCellValueFactory(new PropertyValueFactory<>("Name"));
		// Pegando referencia do stage para fazer o tableView acompanhar a altura da
		// janela
		Stage stage = (Stage) Program.getScene().getWindow();
		tableViewDirectoryList.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView(Integer i) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		if(i == 0) {
			List<DirectoryList> list = service.findAll();
			obsList = FXCollections.observableArrayList(list);
			tableViewDirectoryList.setItems(obsList);
		}else if(i == 1) {
			List<DirectoryList> list = service.findAll2();
			obsList = FXCollections.observableArrayList(list);
			tableViewDirectoryList.setItems(obsList);
		}else {
			System.out.println("ERROR");
		}
		

		

	}

	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initiallizeNodes();
	}

}
