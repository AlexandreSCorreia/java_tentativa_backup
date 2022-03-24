package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class PerfilUsuario {

	private File caminhoPerfilUsuario;
	private File destino;

	private List<File> PastasBackupPerfil = new ArrayList<>();
	private List<File> PastasBackupDiscoC = new ArrayList<>();
	/*
	 * private String[] sd = { ".txt", ".exe", ".zip", ".pdf", ".doc", ".docx",
	 * ".ppt", ".pps", ".xls", ".xlsx", ".jpg", ".gif", ".png", ".mp3", ".wav",
	 * ".mid", ".mp4", ".avi", ".mpg", ".wmv", ".mov", ".psd", ".cdr", ".ai",
	 * ".xml", ".css", ".js", ".php", ".asp", ".aspx", ".jsp", ".cfm", ".svg",
	 * ".tif", ".fla", ".swf", ".dwg", ".sql" };
	 */

	public PerfilUsuario(File destino, File idPerfilUsuario) {
		this.destino = destino;
		this.caminhoPerfilUsuario = idPerfilUsuario;
	}

	public final void salvar() {
		try {
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
					System.out.println("Copiando bookmarks...");

					Path copiarArquivos = Files.copy(
							Paths.get(caminhoPerfilUsuario
									+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
							Paths.get(destino + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);
					if (Files.exists(copiarArquivos)) {
						System.out.println("Bookmarks salvo \n");
					}
				}
				// Salvando pastas e arquivos do perfil
				System.out.println("Copiando pastas...");
				for (File test : PastasBackupPerfil) {
					copyFile(test, new File(pastaParaSalvarPerfilUsuario, test.getName()));
				}
				System.out.println("Backup do usuário " + caminhoPerfilUsuario.getName() + " finalizado! \n");
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// https://www.guj.com.br/t/copiar-arquivo-para-outro-diretorio/131127/2
	public void copyFile(File source, File destination) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;

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

			} else {
				/*
				 * String extensao =
				 * source.getName().substring(source.getName().lastIndexOf("."),
				 * source.getName().length()); List<String> list = Arrays.asList(sd);
				 */

				// if (list.indexOf(extensao) != -1) {
				sourceChannel = new FileInputStream(source).getChannel();
				destinationChannel = new FileOutputStream(destination).getChannel();
				sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
				// }

			}

		} finally {

			if (sourceChannel != null && sourceChannel.isOpen())
				sourceChannel.close();
			if (destinationChannel != null && destinationChannel.isOpen())
				destinationChannel.close();
		}
	}

	// https://www.guj.com.br/t/copiar-arquivos-de-uma-pasta-para-outra/61631/4
	/*
	 * public boolean fazerBackupDoPerfil(File srcDir, File dstDir) { try { if
	 * (srcDir.isDirectory()) {
	 * 
	 * if (!dstDir.exists()) { dstDir.mkdir(); }
	 * 
	 * String[] children = srcDir.list(); if (children != null) {
	 * 
	 * for (int i = 0; i < children.length; i++) {
	 * 
	 * fazerBackupDoPerfil(new File(srcDir, children[i]), new File(dstDir,
	 * children[i]));
	 * 
	 * } }
	 * 
	 * } else {
	 * 
	 * InputStream in = new FileInputStream(srcDir); OutputStream out = new
	 * FileOutputStream(dstDir);
	 * 
	 * byte[] buf = new byte[1024]; int len;
	 * 
	 * while ((len = in.read(buf)) > 0) {
	 * 
	 * out.write(buf, 0, len); }
	 * 
	 * in.close(); out.close(); } } catch (IOException ioex) {
	 * 
	 * ioex.printStackTrace(); return false; }
	 * 
	 * return true;
	 * 
	 * }
	 */

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
