package modal.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import modal.helper.ContSize;
import modal.helper.Log;

public interface Filtro {

	default List<File> search(String[] list, File path) {

		final File[] folders = path.listFiles(File::isDirectory);

		final List<File> listReturn = new ArrayList<File>();

		for (File folder : folders) {
			// Usuário permitido
			Log.log("info", "Verificando: " + folder.getName());

			if (Arrays.asList(list).indexOf(folder.getName()) == -1 && isEmpety(list, folder)) {

				listReturn.add(folder);
				Log.log("info", "Aprovado para fazer backup.");
				Log.log("info", "");

			} else {
				Log.log("info", "Não está aprovado para fazer backup.");
				Log.log("info", "");
			}

		}

		return listReturn;
	}

	default boolean isEmpety(String[] list, File file) {
		Long sizeTotal = 0L;

		File[] folders = file.listFiles(File::isDirectory);
		if (folders != null) {
			for (File past : folders) {
				// pastas permitidas
				//if (isValidFolder(list, past)) {
					// tamanho da pasta
					sizeTotal += FileUtils.sizeOfDirectory(past);
				//}
			}
		}

		Log.log("info", "Tamanho: " + ContSize.humanReadableByteCountSI(sizeTotal));

		return sizeTotal > 0L;
	}

	boolean isValidFolder(String[] list, File past);
}
