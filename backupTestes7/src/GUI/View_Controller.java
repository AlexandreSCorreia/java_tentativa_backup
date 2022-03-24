package GUI;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import entities.PerfilUsuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;

public final class View_Controller implements Initializable {

	private DirectoryChooser directoryChooser = new DirectoryChooser();
	private String guardarBackup;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private TextField caminho;

	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private WebView browser;

	@FXML
	private void Procurar() {
		File selectedDirectory = directoryChooser.showDialog(null);
		criarDiretorioSave(selectedDirectory);

	}

	@FXML
	private void OnComecar() {
		new MyThread().start();
	}

	@FXML
	private void OnCancelar() {
		new MyThread().interrupt();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	public View_Controller(){

		//WebEngine we = browser.getEngine();
		//we.loadContent("Teste");
		//scrollPane.setContent(browser);
	
	}

	public class MyThread extends Thread {

		public void run() {
			PerfilUsuario perfilUsuario = new PerfilUsuario();
			perfilUsuario.procurarPerfisDeUsuarios(new File("C:\\Users\\"), guardarBackup);
			progressBar.setVisible(true);
		}
	}

	private final void criarDiretorioSave(File selectedDirectory) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String path = selectedDirectory + " BACKUP_" + hostName + "_"
					+ new SimpleDateFormat("dd_MM_yyyy HH-mm-ss").format(new Date(System.currentTimeMillis()));
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

}
