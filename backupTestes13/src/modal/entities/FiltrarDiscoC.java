package modal.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

import modal.helper.ContSize;
import modal.helper.Log;

public class FiltrarDiscoC implements Filtro {

	private final String[] notFolder = { "$GetCurrent", "$Recycle.Bin", "$SysReset", "AppData", "Arquivos de Programas",
			"Config.Msi", "Documents and Settings", "Intel", "Microsoft", "MSOCache", "OneDriveTemp", "PerfLogs",
			"Program Files", "Program Files (x86)", "ProgramData", "Recovery", "System Volume Information", "Temp",
			"tmp", "Users", "Windows" };

	
	/// 1
	@Override
	public List<File> procurarUsuario(String[] list, File path) {

		final List<File> listReturn = new ArrayList<File>();
		
		final File[] folders = path.listFiles(File::isDirectory);
		
		for (File folder : folders) {

			if (fazBackupDesseUsuario(list, folder)) {

				listReturn.add(folder);

			}
		}

		return listReturn;
	}
	
	// 2
		private final boolean fazBackupDesseUsuario(String[] list, File file) {

			if (Arrays.asList(notFolder).indexOf(file.getName()) != -1) {
				return false;
			}
			return isEmpety(list, file);

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

		
	//4
	@Override
	public boolean isValidFolder(String[] list, File past) {
		try {
			if (past.getAbsoluteFile().equals(past.getCanonicalFile())) {

				return true;
			}
		} catch (IOException e) {
			Log.log("error", "Error: " + e.getMessage());
		}

		return false;
	}

	

}
