package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public final class PerfilUsuario extends Buscar {

	private File destiny;
	private File source;

	private List<File> PastasBackupPerfil = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	public PerfilUsuario() {

	}

	public PerfilUsuario(File source, File destiny) {
		this.source = source;
		this.destiny = destiny;

	}

	public final void salvar() {
		List<PerfilUsuario> list = procurarPerfisDeUsuarios(source, destiny.getPath());
		for (PerfilUsuario usu : list) {
			File[] folders = usu.source.listFiles(File::isDirectory);
			for (File folder : folders) {
				if (fazBackupDessaPasta(folder)) {
					System.out.println(
							folder.getPath() + " vai para " + new File(usu.destiny, usu.source.getName()).getPath());
					copyFile(folder, new File(usu.destiny, usu.source.getName() + "\\" + folder.getName()));
				}
			}
		}
	}

	// https://www.guj.com.br/t/copiar-arquivo-para-outro-diretorio/131127/2
	public void copyFile(File source, File destination) {
		try {
			if (source.isDirectory()) {
				if (!destination.exists()) {
					destination.mkdir();

				}
				String[] children = source.list();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						copyFile(new File(source, children[i]), new File(destination, children[i]));

					}
				}
			} else if (source.isHidden() == false) {

				FileUtils.copyFile(source.getAbsoluteFile(), destination);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public File getDestiny() {
		return destiny;
	}

	public void setDestiny(File destiny) {
		this.destiny = destiny;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

	public void addPastasBackupPerfil(File file) {
		PastasBackupPerfil.add(file);
	}

	public void removePastasBackupPerfil(File file) {
		PastasBackupPerfil.remove(file);
	}

	public void addPastasBackupDiscoC(File file) {
		PastasBackupDiscoC.add(file);
	}

	public void removePastasBackupDiscoC(File file) {
		PastasBackupDiscoC.remove(file);
	}

}
