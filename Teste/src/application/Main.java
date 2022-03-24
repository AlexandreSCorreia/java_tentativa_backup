package application;

import java.io.File;
import java.util.Arrays;
import java.util.List;
//import org.apache.commons.io.FileUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	private final String[] usersNegados = {"Administrador","All Users","Default",
			"Default User","Public","Todos os Usuários","Usuário Padrão"};
	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("JavaFX App");

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File("src"));
		Button button = new Button("Select Directory");
		button.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(null);
			File diretorio = new File(selectedDirectory.getPath()+"\\Users\\554033\\");
			if (diretorio.exists()) {
				File[] folders = diretorio.listFiles(File :: isDirectory);
				List<String> negados = Arrays.asList(usersNegados);
				
				for(File a : folders) {
					
					if(negados.indexOf(a.getName()) == -1) {
						//long size= FileUtils.sizeOfDirectory(new File("C:/Windows"));;
						System.out.println(a.getName());
					}
					
				}

			}
			
		
			/*String hostName = null;
			InetAddress inetAddress;
			try {
				inetAddress = InetAddress.getLocalHost();
				hostName = inetAddress.getHostName();
			} catch (UnknownHostException ex) {

				ex.printStackTrace();
			}			
			String path = selectedDirectory + hostName + "-BACKUP"; 
			File diretorio = new File(path);
			System.out.println(path);
			if (!diretorio.exists()) {
				diretorio.mkdirs();

			}

			System.out.println(selectedDirectory.getAbsolutePath());*/
		});

		VBox vBox = new VBox(button);
		// HBox hBox = new HBox(button1, button2);
		Scene scene = new Scene(vBox, 400, 400);

		primaryStage.setScene(scene);
		primaryStage.show();

		/*
		 * try { Parent parent =
		 * FXMLLoader.load(getClass().getResource("/GUI/View.fxml")); Scene scene = new
		 * Scene(parent); stage.setScene(scene); stage.setTitle("HELPER");
		 * stage.setResizable(false); stage.show();
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	public static void main(String[] args) {
		launch(args);

	}

}
