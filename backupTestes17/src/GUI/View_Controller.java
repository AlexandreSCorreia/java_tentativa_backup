package GUI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import GUI.util.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import modal.entities.FiltrarDiscoC;
import modal.entities.FiltrarPerfilUsuario;
import modal.entities.PerfilUsuario;
import modal.helper.Log;

public final class View_Controller implements Initializable {

	private DirectoryChooser directoryChooser = new DirectoryChooser();

	private String guardarBackup;

	@FXML
	public TextArea textAreaLog;

	@FXML
	public ProgressBar progressBar;

	@FXML
	private TextField caminho;

	@FXML
	private Button botaoProcurar;

	@FXML
	private Button botaoComecar;

	@FXML
	public ScrollPane scrollPane;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy HH-mm-ss");
	private Date data = new Date(System.currentTimeMillis());

	@FXML
	public void Procurar() {
		// Capturando o caminho que o backup será salvo
		File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			criarDiretorioSave(selectedDirectory);
			Log.log("info", "====================");
			Log.log("info", "LOGGER inicializado.");
			Log.log("info", "====================");
			Log.log("info", "Criando diretório...");
			// Pegar usuário logado
			// System.out.println(System.getProperty("user.name"));

		}
	}

	@FXML
	public void OnComecar() {

		if (!caminho.getText().trim().isEmpty()) {

			File diretorio = new File(caminho.getText());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
				Log.log("info", "Diretório criado em " + diretorio.getPath());

			}

			guardarBackup = diretorio.getPath();

			salvarDadosRunnable salvarDadosRunnable = new salvarDadosRunnable();
			new Thread(salvarDadosRunnable).start();

			interfacOn();

		} else {

			Alerts.showAlert("Informação", null, "Escolha a onde você vai salvar o seu backup!", AlertType.INFORMATION);

		}

	}

	private void interfacOn() {
		botaoProcurar.setDisable(true);
		botaoComecar.setDisable(true);
		progressBar.setVisible(true);
	}

	/*
	 * private void interfacOff() { caminho.setText("");
	 * progressBar.setVisible(false); botaoProcurar.setDisable(false);
	 * botaoComecar.setDisable(false); textAreaLog.setText("");
	 * 
	 * }
	 */

	@FXML
	private void OnCancelar() {
		Alerts.showAlert("Informação", null, "Função ainda não foi implementada!", AlertType.INFORMATION);
		/*
		 * File diretorio = new File(caminho.getText());
		 * 
		 * if (diretorio.exists()) { diretorio.delete(); }
		 * 
		 * interfacOff();
		 */
	}

	// Criando o nome da pasta onde o backup ficará salvo
	public final void criarDiretorioSave(File selectedDirectory) {

		try {

			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String path = selectedDirectory + " BACKUP" + hostName + "_" + sdf.format(data);

			caminho.setText(path);

		} catch (UnknownHostException e) {

			Log.log("error", e.getMessage());

		}
	}

	class salvarDadosRunnable implements Runnable {

		@Override
		public void run() {

			try {
				Log.log("info", "");
				Log.log("info", "==============================");
				Log.log("info", "Verificando Disco Local (C:)..");
				Log.log("info", "==============================");
				// Fazer backup do disco C
				new PerfilUsuario().salvar(new File("C:\\"), new File(guardarBackup, "DiscoC"), new FiltrarDiscoC());
				
				// Fazer Backup dos Perfis de Usuários
				Log.log("info", "");
				Log.log("info", "");
				Log.log("info", "================================");
				Log.log("info", "Procurando Perfis de Usuários...");
				Log.log("info", "================================");
				new PerfilUsuario().salvar(new File("C:\\Users\\"), new File(guardarBackup),
						new FiltrarPerfilUsuario());
				
				// Abrir Directory
				Desktop.getDesktop().open(new File(guardarBackup));

			} catch (NullPointerException e) {
				Log.log("error", e.getMessage());
			} catch (IOException e) {
				Log.log("error", e.getMessage());
			}

		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Colocando um observador no textArea para fazer o scroll descer conforme vai
		// adicionando texto
		textAreaLog.textProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

				textAreaLog.setScrollTop(Double.MAX_VALUE);

			}
		});

	}

}
