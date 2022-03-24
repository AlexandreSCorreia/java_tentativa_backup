package GUI;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import entities.Buscar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public final class View_Controller implements Initializable {

	private DirectoryChooser directoryChooser = new DirectoryChooser();
	public String guardarBackup;

	SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy HH-mm-ss");
	Date data = new Date(System.currentTimeMillis());

	@FXML
	public ProgressBar progressBar;

	@FXML
	private TextField caminho;
	
	@FXML
	public Label label;

	@FXML
	public void Procurar() {
		File selectedDirectory = directoryChooser.showDialog(null);
		criarDiretorioSave(selectedDirectory);
		
	}

	@FXML
	public void OnComecar() {
		Buscar b = new Buscar();
		b.procurarPerfisDeUsuarios(new File("C:\\Users\\"), guardarBackup);
		progressBar.setVisible(true);
	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public final void criarDiretorioSave(File selectedDirectory) {
		String hostName = null;
		InetAddress inetAddress;
		if (selectedDirectory != null) {
			try {
				inetAddress = InetAddress.getLocalHost();
				hostName = inetAddress.getHostName();
			} catch (UnknownHostException e) {

				e.printStackTrace();
			}
			if (hostName != null) {
				String path = selectedDirectory + " BACKUP_" + hostName + "_" + sdf.format(data);
				caminho.setText(path);
				File diretorio = new File(path);
				// System.out.println(path);
				if (!diretorio.exists()) {
					diretorio.mkdirs();
				}
				guardarBackup = diretorio.getPath();
				System.out.println("Pasta do backup foi criada em: " + guardarBackup);
				label.setText("Pasta do backup foi criada em: ");
				System.out.println("-----------------------------------------------");
			}
		}

	}

}
