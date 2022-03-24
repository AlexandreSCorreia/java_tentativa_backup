package modal.entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import modal.helper.CopyFile;
import modal.helper.Log;

public final class PerfilUsuario {

	// Lista de pastas encontradas no diret�rio escaneado
	private List<File> ListPerfilBackup = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	// Lista de pastas no diret�rio que n�o ser�o salvas
	private final String[] toNotBackupToFolder = { "3D Objects", "Application Data", "Contacts", "Cookies",
			"IntelGraphicsProfiles", "Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive",
			"PrintHood", "Recent", "Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents",
			"VirtualBox VMs", "MyFirstGame", "NI mate", "source", "Ambiente de Impress�o", "Ambiente de Rede", "Apple",
			"Configura��es Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usu�rios", "Usu�rio Padr�o" };

	private final String[] notFolder = { "$GetCurrent", "$WINDOWS.~BT", "$Windows.~WS", "$Recycle.Bin", "$SysReset",
			"AppData", "Arquivos de Programas", "Config.Msi", "Documents and Settings", "Intel", "Microsoft",
			"MSOCache", "OneDriveTemp", "PerfLogs", "Program Files", "Program Files (x86)", "ProgramData", "Recovery",
			"System Volume Information", "Temp", "tmp", "Users", "Windows" };

	public PerfilUsuario() {

	}

	public final synchronized void salvar(File src, File srcDi, Filtro filtro) {

		// Verificando qual filtro ser� aplicado para passar como par�metro a lista
		// adequada
		if (srcDi.getPath().contains("DiscoC")) {
			// Escaneando diret�rio e recuperando lista
			PastasBackupDiscoC = filtro.search(notFolder, src);

			for (File folder : PastasBackupDiscoC) {
				long start = System.currentTimeMillis();

				extensionSave(notFolder, srcDi, folder, filtro);

				Log.log("info", "Concluido em: " + (System.currentTimeMillis() - start));

			}

			Log.log("info", "");
			Log.log("info", "BACKUP DISCO LOCAL (C:) FINALIZADO!");
			Log.log("info", "====================================");
		} else {

			// Escaneando diret�rio e recuperando lista
			ListPerfilBackup = filtro.search(usersNegados, src);

			for (File user : ListPerfilBackup) {

				AppData(user, srcDi, "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks", "\\Bookmarks");
				AppData(user, srcDi, "\\AppData\\Local\\Microsoft\\Outlook", "\\Local_Outlook");
				AppData(user, srcDi, "\\AppData\\Roaming\\Microsoft\\Outlook", "\\Roaming_Outlook");
				long start = System.currentTimeMillis();
				extensionSave(toNotBackupToFolder, srcDi, user, filtro);
				Log.log("info", "Concluido em: " + (System.currentTimeMillis() - start) / 60000 + " min "
						+ (System.currentTimeMillis() - start) / 1000 + " seg");

			}
			Log.log("info", "");
			Log.log("info", "BACKUP DOS PERFIS DE USU�RIOS FINALIZADO!");
			Log.log("info", "====================================");

		}

	}

	// Salvando dados
	private synchronized void extensionSave(String[] list, File srcDi, File user, Filtro filtro) {
		Log.log("info", "====================================");
		Log.log("info", "Salvando dados aprovados...");
		Log.log("info", "Copiando pastas e arquivos...");
		File directorySave = new File(srcDi, user.getName());
		// Criando pasta para salvar dados
		if (!directorySave.exists()) {
			directorySave.mkdir();

		}
		Log.log("info", "*" + directorySave.getName());
		// Criando lista de diret�rios a partir do caminho informado
		File[] folders = user.listFiles(File::isDirectory);

		// Passando uma pasta de cada vez para salvar e validando se essas pastas ser�o
		// salvas no backup
		for (File folder : folders) {
			/*
			 * M�todo que valida se a pasta ser� salva, poderia usar os isEmpety da
			 * interface Filtro para verificar se a pasta est� vazia automaticamente ele
			 * chama esse m�todo, optei por n�o fazer isso por causa do tempo que leva para
			 * calcular o tamanho da pasta
			 */

			if (filtro.isValidFolder(list, folder)) {

				try {

					CopyFile.copy(folder, new File(directorySave, folder.getName()));

				} catch (FileNotFoundException e) {

					Log.log("error", "Caminho para o diretorio n�o existe " + e.getMessage());

				} catch (IOException e) {

					Log.log("error", e.getMessage());
				}
			}

		}

	}

	public void AppData(File caminhoPerfilUsuario, File destino, String search, String name) {

		File pastaParaSalvarPerfilUsuario = new File(destino, caminhoPerfilUsuario.getName());

		if (!pastaParaSalvarPerfilUsuario.exists()) {
			pastaParaSalvarPerfilUsuario.mkdir();
		}

		// Salvar dados do AppData
		File diretorio = new File(new File(caminhoPerfilUsuario, search).getPath());

		if (!diretorio.exists()) {
			Log.log("info", "N�o tem " + name + " para salvar! \n" + new File(caminhoPerfilUsuario, search).getPath());

		} else {

			diretorio = new File(destino.getPath());
			diretorio.mkdir();

			Log.log("info", "Copiando " + name + "...");
			try {
				Path copiarArquivos = Files.copy(Paths.get(new File(caminhoPerfilUsuario, search).getPath()),
						Paths.get(pastaParaSalvarPerfilUsuario + name), StandardCopyOption.REPLACE_EXISTING);
				if (Files.exists(copiarArquivos)) {
					Log.log("info", name + " salvo.");

				}

			} catch (IOException e) {
				Log.log("info", e.getMessage());
				e.printStackTrace();
			}

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
