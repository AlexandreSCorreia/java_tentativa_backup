package GUI;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import GUI.util.Alerts;
import entities.Buscar;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public final class View_Controller {

	private DirectoryChooser directoryChooser = new DirectoryChooser();
	private String guardarBackup;

	@FXML
	public ProgressBar progressBar;

	@FXML
	private TextField caminho;

	@FXML
	private Button botaoProcurar;

	@FXML
	private Button botaoComecar;

	@FXML
	public void Procurar() {
		File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			criarDiretorioSave(selectedDirectory);
		}
	}

	@FXML
	public void OnComecar() {
		if (!caminho.getText().trim().isEmpty()) {
			new MyThread().start();
			botaoProcurar.setDisable(true);
			botaoComecar.setDisable(true);
		} else {
			Alerts.showAlert("Informação", null, "Escolha a onde você vai salvar o seu backup!", AlertType.INFORMATION);
		}

	}

	@FXML
	private void OnCancelar() {
		new MyThread().interrupt();
		progressBar.setVisible(false);
		File diretorio = new File(caminho.getText());
		if (diretorio.exists()) {
			diretorio.delete();
		}
		caminho.setText("");
	}

	public final void criarDiretorioSave(File selectedDirectory) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy HH-mm-ss");
			Date data = new Date(System.currentTimeMillis());
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String path = selectedDirectory + " BACKUP_" + hostName + "_" + sdf.format(data);
			caminho.setText(path);
			File diretorio = new File(path);
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			guardarBackup = diretorio.getPath();
			System.out.println("Pasta do backup foi criada em: " + guardarBackup);
			System.out.println("-----------------------------------------------");

		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
	}

	public class MyThread extends Thread {

		public void run() {
			Buscar b = new Buscar();
			b.procurarPerfisDeUsuarios(new File("C:\\Users\\"), guardarBackup);
			progressBar.setVisible(true);
		}
	}

}
