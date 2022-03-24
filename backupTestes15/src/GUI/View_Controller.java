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
		// Capturando o caminho que o backup ser� salvo
		File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			criarDiretorioSave(selectedDirectory);
			Log.log("info", "=================");
			Log.log("info", "Log inicializado.");
			Log.log("info", "=================");
			Log.log("info", "Criando diret�rio...");
			
			//Pegar usu�rio logado			
			//System.out.println(System.getProperty("user.name"));
		
		}
	}

	@FXML
	public void OnComecar() {

		if (!caminho.getText().trim().isEmpty()) {

			File diretorio = new File(caminho.getText());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
				Log.log("info", "Diret�rio criado em " + diretorio.getPath());
			
			}

			guardarBackup = diretorio.getPath();
			// Inicializando Thread para inicializar o backup
			new MyThread().start();

			interfacOn();

		} else {

			Alerts.showAlert("Informa��o", null, "Escolha a onde voc� vai salvar o seu backup!", AlertType.INFORMATION);

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
		textAreaLog.setText("");
	}

	@FXML
	private void OnCancelar() {
		File diretorio = new File(caminho.getText());

		if (diretorio.exists()) {
			diretorio.delete();
		}

		interfacOff();
	}

	// Criando o nome da pasta onde o backup ficar� salvo
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

	class MyThread extends Thread {
		@Override
		public synchronized void run() {
			try {
				Log.log("info", "==============================");
				Log.log("info", "Verificando Disco Local (C:)..");
				Log.log("info", "==============================");
				// Fazer backup do disco C
				new PerfilUsuario().salvar(new File("C:\\"), new File(guardarBackup, "DiscoC"), new FiltrarDiscoC());
				// Fazer Backup dos Perfis de Usu�rios
				Log.log("info", "================================");
				Log.log("info", "Procurando Perfis de Usu�rios...");
				Log.log("info", "================================");
				new PerfilUsuario().salvar(new File("C:\\Users\\"), new File(guardarBackup),
						new FiltrarPerfilUsuario());

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			Log.log("info", "");
			Log.log("info", "BACKUP FINALIZADO");
			//interfacOff();

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
