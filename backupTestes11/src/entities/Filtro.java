package entities;

import java.io.File;
import java.util.List;

public interface Filtro {
	
	List<File> procurarUsuario(String[] list,File path);
	boolean validarPasta(String[] list, File past);
	boolean estaVazio(String[] list,File file);
}
