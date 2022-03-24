package GUI;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import GUI.util.Alerts;
import entities.PerfilUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
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
	private ScrollPane root;

	SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy HH-mm-ss");
	Date data = new Date(System.currentTimeMillis());

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

			File diretorio = new File(caminho.getText());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			guardarBackup = diretorio.getPath();
			System.out.println("Pasta do backup foi criada em: " + guardarBackup);
			System.out.println("-----------------------------------------------");
			botaoProcurar.setDisable(true);
			botaoComecar.setDisable(true);
			new MyThread().start();

		} else {
			Alerts.showAlert("Informação", null, "Escolha a onde você vai salvar o seu backup!", AlertType.INFORMATION);
		}

	}

	@FXML
	private void OnCancelar() {
		new MyThread().interrupt();
		File diretorio = new File(caminho.getText());
		if (diretorio.exists()) {
			diretorio.delete();
		}
		caminho.setText("");
		progressBar.setVisible(false);
		botaoProcurar.setDisable(false);
		botaoComecar.setDisable(false);
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

	public class MyThread extends Thread {

		public void run() {
			PerfilUsuario usu = new PerfilUsuario(new File("C:\\Users\\"), new File(guardarBackup));
			usu.salvar();
			progressBar.setVisible(true);

		}
	}

}
