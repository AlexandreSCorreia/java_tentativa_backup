package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PerfilUsuario {

	private File caminhoPerfilUsuario;
	private File destino;
	private List<File> PastasBackupPerfil = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();

	public PerfilUsuario(File destino, File idPerfilUsuario) {
		this.destino = destino;
		this.caminhoPerfilUsuario = idPerfilUsuario;
	}

	public void salvar() {
		if (destino != null && caminhoPerfilUsuario != null) {
			if (destino.exists()) {
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
					System.out.println("Copiar BookMarks para salvar!");
					try {
						Path copiarArquivos = Files.copy(
								Paths.get(caminhoPerfilUsuario
										+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
								Paths.get(destino + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);

						System.out.println(Files.exists(copiarArquivos));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Salvando pastas e arquivos do perfil
				for (File test : PastasBackupPerfil) {
					fazerBackupDoPerfil(test, new File(destino, test.getName()));
				}
				System.out.println("Usuario salvo!");

			}
		}

	}

	// https://www.guj.com.br/t/copiar-arquivos-de-uma-pasta-para-outra/61631/4
	public boolean fazerBackupDoPerfil(File srcDir, File dstDir) {
		try {
			if (srcDir.isDirectory()) {

				if (!dstDir.exists()) {
					dstDir.mkdir();
				}

				String[] children = srcDir.list();
				if (children != null) {

					for (int i = 0; i < children.length; i++) {

						fazerBackupDoPerfil(new File(srcDir, children[i]), new File(dstDir, children[i]));

					}
				}

			} else {

				InputStream in = new FileInputStream(srcDir);
				OutputStream out = new FileOutputStream(dstDir);

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {

					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}
		} catch (IOException ioex) {

			ioex.printStackTrace();
			return false;
		}

		return true;

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
