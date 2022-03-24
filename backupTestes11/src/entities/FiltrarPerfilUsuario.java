package entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;

import GUI.View_Controller;
import GUI.util.ContSize;

public class FiltrarPerfilUsuario implements Filtro{

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	
	
	
	private final boolean fazBackupDesseUsuario(String[] list,File file) {
		if (Arrays.asList(usersNegados).indexOf(file.getName()) != -1) {
		//	View_Controller.logger.log(Level.INFO, "User Negado"+" "+
			//		Arrays.asList(usersNegados).get(Arrays.asList(usersNegados).indexOf(file.getName()))
		//);
			return false;
		}
		return estaVazio(list,file);
	}

	
	@Override
	public List<File> procurarUsuario(String[] list,File path) {
		
		final File[] folders = path.listFiles(File::isDirectory);
		
		final List<File> listReturn = new ArrayList<File>();
		try {
			for (File folder : folders) {
				// Usuario permitido
				if (fazBackupDesseUsuario(list,folder)) {
				//	View_Controller.logger.log(Level.INFO, "User Add "+folder.getName());
					listReturn.add(folder);
					
				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return listReturn;
	}


	@Override
	public boolean validarPasta(String[] list, File past) {
		final List<String> array = Arrays.asList(list);
		if (array.indexOf(past.getName()) == -1 && !past.getName().equals("AppData")
				 && !past.getName().contains("OneDrive") ) {
	
			//View_Controller.logger.log(Level.INFO, "Pasta valida: "+past.getName());
			return true;
		}
		return false;
	}


	@Override
	public boolean estaVazio(String[] list,File file) {
		Long sizeTotal = 0L;
		File[] folders = file.listFiles(File::isDirectory);
		if (folders != null) {
			for (File past : folders) {
				// pastas permitidas
				if (validarPasta(list, past) ) {
					// tamanho da pasta
					sizeTotal += FileUtils.sizeOfDirectory(past);
				}
			}
		}
		
		//View_Controller.logger.log(Level.INFO, "Tamanho da pasta "+ContSize.humanReadableByteCountSI(sizeTotal));
		return sizeTotal > 0L;
	}
	

}
