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

import org.apache.commons.io.FileUtils;

import modal.helper.CopyFile;
import modal.helper.Log;
import modal.service.DirectoryService;

public final class PerfilUsuario {

	private DirectoryService service;
	
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

	/*private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] notFolder = { "$GetCurrent", "$WINDOWS.~BT", "$Windows.~WS", "$Recycle.Bin", "$SysReset",
			"AppData", "Arquivos de Programas", "Config.Msi", "Documents and Settings", "Intel", "Microsoft",
			"MSOCache", "OneDriveTemp", "PerfLogs", "Program Files", "Program Files (x86)", "ProgramData", "Recovery",
			"System Volume Information", "Temp", "tmp", "Users", "Windows" };*/

	public PerfilUsuario() {

	}

	public final synchronized void salvar(File src, File srcDi, Filtro filtro) {

		service = new DirectoryService();
		
		// Verificando qual filtro será aplicado para passar como parâmetro a lista
		// adequada
		if (srcDi.getPath().contains("DiscoC")) {
			// Escaneando diretório e recuperando lista
			List<DirectoryList> listC = service.findAll();
			
			String[] listaC = new String[listC.size()];	
			
			for(int i = 0; i <= service.findAll().size()-1;i++) {
				listaC[i] = listC.get(i).getName();
				System.out.println(listC.get(i).getName());
			}
			
			PastasBackupDiscoC = filtro.search(listaC, src);

			for (File folder : PastasBackupDiscoC) {
				long start = System.currentTimeMillis();

				extensionSave(listaC, srcDi, folder, filtro);

				Log.log("info", "Concluido em: " + (System.currentTimeMillis() - start));

			}

			Log.log("info", "");
			Log.log("info", "BACKUP DISCO LOCAL (C:) FINALIZADO!");
			Log.log("info", "===================================");
		} else {

			// Escaneando diretório e recuperando lista
			List<DirectoryList> listP = service.findAll2();
			String[] listaP = new String[listP.size()];
			
			for(int i=0; i <= service.findAll2().size()-1;i++) {
				listaP[i] = listP.get(i).getName();
			}
			ListPerfilBackup = filtro.search(listaP, src);

			for (File user : ListPerfilBackup) {

				//AppData(user, srcDi, "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks", "\\Bookmarks");
				//AppData(user, srcDi, "\\AppData\\Local\\Microsoft\\Outlook", "\\Local_Outlook");
				//AppData(user, srcDi, "\\AppData\\Roaming\\Microsoft\\Outlook", "\\Roaming_Outlook");
				AppData(user, srcDi);
				long start = System.currentTimeMillis();
				extensionSave(toNotBackupToFolder, srcDi, user, filtro);
				Log.log("info", "Concluido em: " + (System.currentTimeMillis() - start) / 60000 + " min "
						+ (System.currentTimeMillis() - start) / 1000 + " seg");

			}
			Log.log("info", "");
			Log.log("info", "BACKUP DOS PERFIS DE USUÁRIOS FINALIZADO!");
			Log.log("info", "=========================================");

		}

	}

	// Salvando dados
	private synchronized void extensionSave(String[] list, File srcDi, File user, Filtro filtro) {
		Log.log("info", "=========================================");
		Log.log("info", "Salvando dados aprovados...");
		Log.log("info", "Copiando pastas e arquivos...");
		File directorySave = new File(srcDi, user.getName());
		// Criando pasta para salvar dados
		if (!directorySave.exists()) {
			directorySave.mkdir();

		}
		Log.log("info", "*" + directorySave.getName());
		// Criando lista de diretórios a partir do caminho informado
		File[] folders = user.listFiles(File::isDirectory);

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

				try {

					CopyFile.copy(folder, new File(directorySave, folder.getName()));

				} catch (FileNotFoundException e) {

					Log.log("error", "Caminho para o diretorio não existe " + e.getMessage());

				} catch (IOException e) {

					Log.log("error", e.getMessage());
				}
			}

		}

	}

	/*public void AppData(File caminhoPerfilUsuario, File destino, String search, String name) {

		File pastaParaSalvarPerfilUsuario = new File(destino, caminhoPerfilUsuario.getName());

		if (!pastaParaSalvarPerfilUsuario.exists()) {
			pastaParaSalvarPerfilUsuario.mkdir();
		}

		// Salvar dados do AppData
		File diretorio = new File(new File(caminhoPerfilUsuario, search).getPath());

		if (!diretorio.exists()) {
			Log.log("info", "Não tem " + name + " para salvar! \n" + new File(caminhoPerfilUsuario, search).getPath());

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
	}*/
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
