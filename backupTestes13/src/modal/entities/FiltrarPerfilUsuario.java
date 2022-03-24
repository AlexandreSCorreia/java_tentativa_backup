package modal.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import modal.helper.ContSize;
import modal.helper.Log;

public class FiltrarPerfilUsuario implements Filtro {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	// 2
	private final boolean fazBackupDesseUsuario(String[] list, File file) {

		if (Arrays.asList(usersNegados).indexOf(file.getName()) != -1) {
			
			Log.log("info",Arrays.asList(usersNegados).get(Arrays.asList(usersNegados).indexOf(file.getName()))+
					" - está na lista de perfis que o backup não será feito.");
	
			return false;
		}
		Log.log("info", "Verificando usuário: " + file.getName());
		return isEmpety(list, file);

	}

	/// 1
	@Override
	public List<File> procurarUsuario(String[] list, File path) {
		Log.log("info", "");
		Log.log("info", "Procurando usuários...");
		Log.log("info", "=================");
		final File[] folders = path.listFiles(File::isDirectory);

		final List<File> listReturn = new ArrayList<File>();

		for (File folder : folders) {
			// Usuario permitido
			if (fazBackupDesseUsuario(list, folder)) {
				Log.log("info", "Usuário aprovado para fazer backup.");
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
	public boolean isEmpety(String[] list, File file) {
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

		Log.log("info", "Tamanho: " + ContSize.humanReadableByteCountSI(sizeTotal));
		

		return sizeTotal > 0L;
	}

}
