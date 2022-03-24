package modal.entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import modal.helper.CopyFile;
import modal.helper.Log;

public final class PerfilUsuario {

	// Lista de pastas encontradas no diretório escaneado
	private List<File> ListPerfilBackup = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	// Lista de pastas no diretório que não serão salvas
	private final String[] toNotBackupToFolder = { "3D Objects", "Application Data", "Contacts", "Cookies",
			"IntelGraphicsProfiles", "Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive",
			"PrintHood", "Recent", "Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents",
			"VirtualBox VMs", "MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] notFolder = { "$GetCurrent", "$WINDOWS.~BT", "$Windows.~WS", "$Recycle.Bin", "$SysReset",
			"AppData", "Arquivos de Programas", "Config.Msi", "Documents and Settings", "Intel", "Microsoft",
			"MSOCache", "OneDriveTemp", "PerfLogs", "Program Files", "Program Files (x86)", "ProgramData", "Recovery",
			"System Volume Information", "Temp", "tmp", "Users", "Windows" };

	public PerfilUsuario() {

	}

	public final void salvar(File src, File srcDi, Filtro filtro) {

		// Verificando qual filtro será aplicado para passar como parâmetro a lista
		// adequada
		if (srcDi.getPath().contains("DiscoC")) {
			// Escaneando diretório e recuperando lista
			PastasBackupDiscoC = filtro.search(notFolder, src);

			for (File folder : PastasBackupDiscoC) {

				extensionSave(notFolder, srcDi, folder, filtro);

			}

		} else {
			// Escaneando diretório e recuperando lista
			ListPerfilBackup = filtro.search(usersNegados, src);

			for (File user : ListPerfilBackup) {
				AppData(user, srcDi);
				extensionSave(toNotBackupToFolder, srcDi, user, filtro);

			}
		}

	}

	// Salvando dados
	private void extensionSave(String[] list, File srcDi, File user, Filtro filtro) {

		File directorySave = new File(srcDi, user.getName());
		// Criando pasta para salvar dados
		if (!directorySave.exists()) {
			directorySave.mkdir();

			Log.log("info", "Pasta criada em: \n" + directorySave.getPath());
			Log.log("info", "");
		}
		// Criando lista de diretórios a partir do caminho informado
		File[] folders = user.listFiles(File::isDirectory);

		Log.log("info", "Copiando pastas...");
		// Passando uma pasta de cada vez para salvar e validando se essas pastas serão
		// salvas no backup
		for (File folder : folders) {
			/*
			 * Método que valida se a pasta será salva, poderia usar os isEmpety da
			 * interface Filtro para verificar se a pasta está vazia automaticamente ele
			 * chama esse método, optei por não fazer isso por causa do tempo que leva para
			 * calcular o tamanho da pasta
			 */
			
			if (filtro.isValidFolder(list, folder)) {
				CopyFile.copy(folder, new File(directorySave, folder.getName()));
			}

		}

	}

	public void AppData(File caminhoPerfilUsuario, File destino) {

		File pastaParaSalvarPerfilUsuario = new File(destino, caminhoPerfilUsuario.getName());

		if (!pastaParaSalvarPerfilUsuario.exists()) {
			pastaParaSalvarPerfilUsuario.mkdir();
		}
		
		

		// Salvar dados do AppData
		File diretorio = new File(
				caminhoPerfilUsuario + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks");

		if (!diretorio.exists()) {
			Log.log("info", "Não tem BookMarks para salvar! \n" + caminhoPerfilUsuario
					+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks");

		} else {

			diretorio = new File(destino.getPath());
			diretorio.mkdir();

			Log.log("info", "Copiando bookmarks...");
			try {
				Path copiarArquivos = Files.copy(
						Paths.get(caminhoPerfilUsuario
								+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
						Paths.get(pastaParaSalvarPerfilUsuario + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);
				if (Files.exists(copiarArquivos)) {
					Log.log("info", "Bookmarks salvo.");

				}

			} catch (IOException e) {
				Log.log("info", e.getMessage());
				e.printStackTrace();
			}

		}

		// Salvar dados do AppData
		diretorio = new File(caminhoPerfilUsuario + "\\AppData\\Local\\Microsoft\\Outlook");

		if (!diretorio.exists()) {
			Log.log("info", "Não arquivo Outlook para salvar! \n" + caminhoPerfilUsuario
					+ "\\AppData\\Local\\Microsoft\\Outlook");

		} else {

			diretorio = new File(destino.getPath());
			diretorio.mkdir();

			Log.log("info", "Copiando \\Local\\Microsoft\\Outlook...");
			try {
				FileUtils.copyDirectory(new File(caminhoPerfilUsuario, "\\AppData\\Local\\Microsoft\\Outlook"),
						new File(pastaParaSalvarPerfilUsuario, "\\Local_Outlook"));

			} catch (IOException e) {
				Log.log("info", e.getMessage());
				e.printStackTrace();
			}
			Log.log("info", "\\Local\\Microsoft\\Outlook salvo.");
		}

		// Salvar dados do AppData
		diretorio = new File(caminhoPerfilUsuario + "\\AppData\\Roaming\\Microsoft\\Outlook");

		if (!diretorio.exists()) {
			Log.log("info", "Não arquivo Outlook para salvar! \n" + caminhoPerfilUsuario
					+ "\\AppData\\Roaming\\Microsoft\\Outlook");

		} else {

			diretorio = new File(destino.getPath());
			diretorio.mkdir();

			Log.log("info", "Copiando Roaming\\Microsoft\\Outlook...");
			try {
				FileUtils.copyDirectory(new File(caminhoPerfilUsuario, "\\AppData\\Roaming\\Microsoft\\Outlook"),
						new File(pastaParaSalvarPerfilUsuario, "\\Roaming_Outlook"));

			} catch (IOException e) {
				Log.log("info", e.getMessage());
				e.printStackTrace();
			}
			Log.log("info", "Roaming\\\\Microsoft\\\\Outlook salvo.");
		}

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
