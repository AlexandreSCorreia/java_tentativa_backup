package entities;

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

public final class PerfilUsuario {

	private File caminhoPerfilUsuario;
	private File destino;

	private List<File> PastasBackupPerfil = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	public PerfilUsuario(File destino, File idPerfilUsuario) {
		this.destino = destino;
		this.caminhoPerfilUsuario = idPerfilUsuario;
	}

	public final void salvar() {
		try {
			File pastaParaSalvarPerfilUsuario = new File(destino, caminhoPerfilUsuario.getName());
			if (!pastaParaSalvarPerfilUsuario.exists()) {
				pastaParaSalvarPerfilUsuario.mkdir();
			}
			// Salvar dados do AppData
			File diretorio = new File(
					caminhoPerfilUsuario + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks");
			if (!diretorio.exists()) {
				System.out.println("Não tem BookMarks para salvar! \n" + caminhoPerfilUsuario
						+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\");
			} else {
				diretorio = new File(destino.getPath());
				diretorio.mkdir();
				System.out.println("Copiando bookmarks...");

				Path copiarArquivos = Files.copy(
						Paths.get(caminhoPerfilUsuario
								+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
						Paths.get(pastaParaSalvarPerfilUsuario + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);
				if (Files.exists(copiarArquivos)) {
					System.out.println("Bookmarks salvo \n");
				}
			}

			// Salvando pastas e arquivos do perfil
			System.out.println("Copiando pastas...");
			for (File test : PastasBackupPerfil) {
				System.out.println(test.getPath());
				copyFile(test, new File(pastaParaSalvarPerfilUsuario, test.getName()));
			}
			System.out.println("Backup do usuário " + caminhoPerfilUsuario.getName() + " finalizado! \n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	// https://www.guj.com.br/t/copiar-arquivo-para-outro-diretorio/131127/2
		public void copyFile(File source, File destination){
			try {
				if (source.isDirectory() && source.isHidden() == false) {
					if (!destination.exists()) {
						destination.mkdir();
					}
					String[] children = source.list();
					if (children != null) {
						for (int i = 0; i < children.length; i++) {
							copyFile(new File(source, children[i]), new File(destination, children[i]));
						}
					}
				} else if(source.isHidden() == false){					
					FileUtils.copyFile(source.getAbsoluteFile(), destination);	

				}
			} catch(IOException e) {
				e.printStackTrace();
			}
						
		}

	public File getIdPerfilUsuario() {
		return caminhoPerfilUsuario;
	}

	public void setIdPerfilUsuario(File idPerfilUsuario) {
		this.caminhoPerfilUsuario = idPerfilUsuario;
	}

	public File getDestino() {
		return destino;
	}

	public void setDestino(File destino) {
		this.destino = destino;
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
