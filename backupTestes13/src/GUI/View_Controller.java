package GUI;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import GUI.util.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import modal.entities.Escaner;
import modal.entities.FiltrarDiscoC;
import modal.entities.PerfilUsuario;
import modal.helper.Log;

public final class View_Controller implements Initializable {

	private DirectoryChooser directoryChooser = new DirectoryChooser();

	private String guardarBackup;

	@FXML
	public TextArea getLogTextArea;

	@FXML
	public ProgressBar progressBar;

	@FXML
	private TextField caminho;

	@FXML
	private Button botaoProcurar;

	@FXML
	private Button botaoComecar;

	@FXML
	public ScrollPane root;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy HH-mm-ss");
	private Date data = new Date(System.currentTimeMillis());

	@FXML
	public void Procurar() {
		File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			criarDiretorioSave(selectedDirectory);
			Log.log("info", "=================");
			Log.log("info", "Log inicializado.");
			Log.log("info", "=================");
			Log.log("info", "Criando diretorio...");
		}
	}

	@FXML
	public void OnComecar() {

		if (!caminho.getText().trim().isEmpty()) {

			File diretorio = new File(caminho.getText());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
				Log.log("info", "Diretorio criado em " + diretorio.getPath());
			}
			guardarBackup = diretorio.getPath();
			interfacOn();

			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() {

					try {

						/*PerfilUsuario usu = new PerfilUsuario();
						 usu.salvar(new File("C:\\Users\\"), new File(guardarBackup), new
						// FiltrarPerfilUsuario());
						usu.salvar(new File("C:\\"), new File(guardarBackup,"DiscoC"), new FiltrarDiscoC());*/
						
						Escaner sc = new Escaner();
						sc.Escanear(new File("C:\\"));
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					Log.log("info", "");
					Log.log("info", "BACKUP FINALIZADO");
					interfacOff();

					return null;

				}
			};

			new Thread(task).start();

		} else {
			Alerts.showAlert("Informação", null, "Escolha a onde você vai salvar o seu backup!", AlertType.INFORMATION);
		}

	}

	private void interfacOn() {
		botaoProcurar.setDisable(true);
		botaoComecar.setDisable(true);
		progressBar.setVisible(true);
	}

	private void interfacOff() {
		caminho.setText("");
		progressBar.setVisible(false);
		botaoProcurar.setDisable(false);
		botaoComecar.setDisable(false);
		getLogTextArea.setText("");
	}

	@FXML
	private void OnCancelar() {

		File diretorio = new File(caminho.getText());
		if (diretorio.exists()) {
			diretorio.delete();
		}
		interfacOff();
	}

	public final void criarDiretorioSave(File selectedDirectory) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String path = selectedDirectory + " BACKUP_" + hostName + "_" + sdf.format(data);
			caminho.setText(path);
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		getLogTextArea.textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

				getLogTextArea.setScrollTop(Double.MAX_VALUE);

			}
		});

	}

}
