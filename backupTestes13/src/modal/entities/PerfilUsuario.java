package modal.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import modal.helper.CopyFile;
import modal.helper.Log;

public final class PerfilUsuario {

	private List<File> ListPerfilBackup = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	private final String[] toNotBackupToFolder = { "3D Objects", "Application Data", "Contacts", "Cookies",
			"IntelGraphicsProfiles", "Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive",
			"PrintHood", "Recent", "Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents",
			"VirtualBox VMs", "MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	public PerfilUsuario() {

	}

	public final void salvar(File src, File srcDi, Filtro filtro) {
		
		
		PastasBackupDiscoC = filtro.procurarUsuario(toNotBackupToFolder, src);
		
		Log.log("info", "");
		Log.log("info", "Inicializando backup");
		
		for (File user : PastasBackupDiscoC) {
		
			File diretorioUser = new File(srcDi, user.getName());
			
			if (!diretorioUser.exists()) {
				diretorioUser.mkdir();
				Log.log("info", "Pasta para salvar dados do disco C: \n"+ diretorioUser.getPath());
				Log.log("info", "");
			}
			
			File[] folders = user.listFiles(File::isDirectory);
			
			Log.log("info", "Copiando pastas...");
			
			for (File folder : folders) {
				if (filtro.isValidFolder(null, folder)) {

					CopyFile.copy(folder, new File(diretorioUser, folder.getName()));

				}

			}

		}

		/*ListPerfilBackup = filtro.procurarUsuario(toNotBackupToFolder, src);
		Log.log("info", "");
		Log.log("info", "Inicializando backup");
		
		for (File user : ListPerfilBackup) {
		
			File diretorioUser = new File(srcDi, user.getName());
			
			if (!diretorioUser.exists()) {
				diretorioUser.mkdir();
				Log.log("info", "Pasta para salvar perfil criada em: \n"+ diretorioUser.getPath());
				Log.log("info", "");
			}
			
			File[] folders = user.listFiles(File::isDirectory);
			
			Log.log("info", "Copiando pastas...");
			
			for (File folder : folders) {
				if (filtro.isValidFolder(toNotBackupToFolder, folder)) {

					CopyFile.copy(folder, new File(diretorioUser, folder.getName()));

				}

			}

		}*/

	}

	public void addPastasBackupPerfil(File file) {
		ListPerfilBackup.add(file);
	}

	public void removePastasBackupPerfil(File file) {
		ListPerfilBackup.remove(file);
	}

	public void addPastasBackupDiscoC(File file) {
		PastasBackupDiscoC.add(file);
	}

	public void removePastasBackupDiscoC(File file) {
		PastasBackupDiscoC.remove(file);
	}

}
