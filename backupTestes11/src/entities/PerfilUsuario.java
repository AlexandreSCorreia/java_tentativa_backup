package entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import GUI.util.CopyFile;
import application.Main;

public final class PerfilUsuario {

	private List<File> PastasBackupPerfil = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	
	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents", "VirtualBox VMs",
			"MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };
	
	public PerfilUsuario() {
		
	}

	public final void salvar(File src, File srcDi,Filtro filtro) {
		TextAreaAppender a = new TextAreaAppender();
		Main.logger.info("Teste");

		PastasBackupPerfil = filtro.procurarUsuario(backUsu,src);
		for (File usu : PastasBackupPerfil) {
			File diretorioUser = new File(srcDi, usu.getName());
			if(!diretorioUser.exists()) {
				diretorioUser.mkdir();
			}
			File[] folders = usu.listFiles(File::isDirectory);
			
			for (File folder : folders) {
				if (filtro.validarPasta(backUsu, folder)) {					
					CopyFile.copy(folder, new File(diretorioUser, folder.getName()));
				}
			}
		}
	}


	public void addPastasBackupPerfil(File file) {
		PastasBackupPerfil.add(file);
	}

	public void removePastasBackupPerfil(File file) {
		PastasBackupPerfil.remove(file);
	}

	public void addPastasBackupDiscoC(File file) {
		PastasBackupDiscoC.add(file);
	}

	public void removePastasBackupDiscoC(File file) {
		PastasBackupDiscoC.remove(file);
	}

}
