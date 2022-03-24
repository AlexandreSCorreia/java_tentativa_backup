package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import GUI.util.ContSize;
import GUI.util.Log;

public class FiltrarPerfilUsuario implements Filtro {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	// 2
	private final boolean fazBackupDesseUsuario(String[] list, File file) {

		if (Arrays.asList(usersNegados).indexOf(file.getName()) != -1) {

			Log.log("info", "Perfil de usuário está na lista de perfis que não seram feito backup "
					+ Arrays.asList(usersNegados).get(Arrays.asList(usersNegados).indexOf(file.getName())));

			return false;
		}
		Log.log("info", "Verificando tamanho do diretorio: " + file.getName());
		return estaVazio(list, file);

	}

	/// 1
	@Override
	public List<File> procurarUsuario(String[] list, File path) {

		Log.log("info", "Procurando usuários...");

		final File[] folders = path.listFiles(File::isDirectory);

		final List<File> listReturn = new ArrayList<File>();

		for (File folder : folders) {
			// Usuario permitido
			if (fazBackupDesseUsuario(list, folder)) {
				Log.log("info", "Usuário encontrado: " + folder.getName());
				listReturn.add(folder);

			}
		}

		return listReturn;
	}

	@Override
	public boolean isValidFolder(String[] list, File past) {

		final List<String> array = Arrays.asList(list);
		try {
			if(past.getAbsoluteFile().equals(past.getCanonicalFile())){
				if (array.indexOf(past.getName()) == -1 && !past.getName().equals("AppData")
						&& !past.getName().contains("OneDrive") && !past.getName().contains(".")) {
					Log.log("info", "Pasta valida: " + past.getName());
					return true;
				}
			}
		} catch (IOException e) {
			Log.log("error","Error: "+ e.getMessage());
		}
		

		return false;
	}

	// 3
	@Override
	public boolean estaVazio(String[] list, File file) {
		Long sizeTotal = 0L;

		File[] folders = file.listFiles(File::isDirectory);
		if (folders != null) {
			for (File past : folders) {
				// pastas permitidas
				if (isValidFolder(list, past)) {
					// tamanho da pasta
					sizeTotal += FileUtils.sizeOfDirectory(past);
				}
			}
		}

		Log.log("info", "Tamanho da pasta " + ContSize.humanReadableByteCountSI(sizeTotal));

		return sizeTotal > 0L;
	}

}
