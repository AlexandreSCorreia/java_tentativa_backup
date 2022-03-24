package GUI;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import entities.Buscar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public final class View_Controller implements Initializable {
	
	private DirectoryChooser directoryChooser = new DirectoryChooser();
	public String guardarBackup;

	@FXML
	private TextField caminho;

	public View_Controller() {

	}

	private String getHostname() {
		String hostName = null;
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			hostName = inetAddress.getHostName();
		} catch (UnknownHostException ex) {

			ex.printStackTrace();
		}
		return hostName;
	}

	@FXML
	public void Procurar() {
		File selectedDirectory = directoryChooser.showDialog(null);
		
		if(selectedDirectory != null) {
			caminho.setText(selectedDirectory.getPath());
			String hostName = getHostname();
			if (hostName != null) {
				String path = selectedDirectory + hostName + "-BACKUP";
				File diretorio = new File(path);
				//System.out.println(path);
				if (!diretorio.exists()) {
					diretorio.mkdirs();
					guardarBackup = diretorio.getPath();
					System.out.println("Print: " + guardarBackup);
				}
			}
		}
		

	}

	@FXML
	public void OnComecar() {
		Buscar b = new Buscar();
		b.procurarPerfisDeUsuarios(new File("C:\\Users\\"),guardarBackup);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
