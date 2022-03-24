package entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Buscar {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents", "VirtualBox VMs",
			"MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	private final boolean fazBackupDesseUsuario(File file) {
		if (Arrays.asList(usersNegados).indexOf(file.getName()) != -1) {
			return false;
		}
		return tamanhoFolder(file);
	}

	private final boolean tamanhoFolder(File file) {

		Long sizeTotal = 0L;
		File[] folders = file.listFiles(File::isDirectory);
		if (folders != null) {
			for (File past : folders) {
				// pastas permitidas
				if (fazBackupDessaPasta(past)) {
					// tamanho da pasta
					// String[] size = humanReadableByteCountSI().split(" ");
					sizeTotal += FileUtils.sizeOfDirectory(past);
				}
			}
		}
		return sizeTotal > 0L;
	}

	protected final boolean fazBackupDessaPasta(File past) {
		final List<String> array = Arrays.asList(backUsu);
		if (array.indexOf(past.getName()) == -1 && !past.getName().equals("AppData")
				&& !past.getName().substring(0, 1).equals(".") && !past.getName().contains("OneDrive")) {
			return true;
		}
		return false;
	}

	public final List<PerfilUsuario> procurarPerfisDeUsuarios(File path, String target) {
		final File[] folders = path.listFiles(File::isDirectory);
		final List<PerfilUsuario> list = new ArrayList<PerfilUsuario>();
		try {
			for (File folder : folders) {
				// Usuario permitido
				if (fazBackupDesseUsuario(folder)) {

					list.add(new PerfilUsuario(folder, new File(target)));
					System.out.println(folder.getPath());
				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return list;
	}

	/*private final String humanReadableByteCountSI(long bytes) {
		String s = bytes < 0 ? "-" : "";
		long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		return b < 1000L ? bytes + " B"
				: b < 999_950L ? String.format("%s%.1f kB", s, b / 1e3)
						: (b /= 1000) < 999_950L ? String.format("%s%.1f MB", s, b / 1e3)
								: (b /= 1000) < 999_950L ? String.format("%s%.1f GB", s, b / 1e3)
										: (b /= 1000) < 999_950L ? String.format("%s%.1f TB", s, b / 1e3)
												: (b /= 1000) < 999_950L ? String.format("%s%.1f PB", s, b / 1e3)
														: String.format("%s%.1f EB", s, b / 1e6);
	}*/
}
