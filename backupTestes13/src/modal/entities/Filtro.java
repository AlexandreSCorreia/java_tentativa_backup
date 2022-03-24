package modal.entities;

import java.io.File;
import java.util.List;

public interface Filtro {
	
	List<File> procurarUsuario(String[] list,File path);
	boolean isValidFolder(String[] list, File past);
	boolean isEmpety(String[] list,File file);
}
