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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

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
	 * ".tif", ".fla", ".swf", ".dwg", ".sql", ".lnk", ".url" };
	 */

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents", "VirtualBox VMs",
			"MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	
	public PerfilUsuario() {

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
							Paths.get(pastaParaSalvarPerfilUsuario + "\\Bookmarks"),
							StandardCopyOption.REPLACE_EXISTING);
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
				sourceChannel = new FileInputStream(source).getChannel();
				destinationChannel = new FileOutputStream(destination).getChannel();
				sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
			}
		} finally {
			if (sourceChannel != null && sourceChannel.isOpen())
				sourceChannel.close();
			if (destinationChannel != null && destinationChannel.isOpen())
				destinationChannel.close();
		}
	}
	
	

	public final void procurarPerfisDeUsuarios(File path, String target) {
		if (path != null) {
			final File[] folders = path.listFiles(File::isDirectory);
			
					System.out.println("NÃO SERÁ FEITO BACKUP DE PERFIS VAZIOS! \n");
					for (File folder : folders) {
						if (Arrays.asList(usersNegados).indexOf(folder.getName()) == -1) {
							try {
								System.out.println("Procurando perfils de Usuarios...");
								System.out.println("Usuario encontrado: " + folder.getName());
								System.out.println("---------------------------------------------------");
								destino = new File(target);
								caminhoPerfilUsuario = folder;
								File[] folderUsu = folder.listFiles(File::isDirectory);
								Long tamanhoTotal = 0L;
								String[] ajust = null;
								System.out.println("Checando tamanho das pastas a serem salvas...\n");
								if (folderUsu != null) {
									for (File past : folderUsu) {
										if (!past.getName().equals("AppData")
												&& !past.getName().substring(0, 1).equals(".")
												&& !past.getName().contains("OneDrive")) {
											List<String> array = Arrays.asList(backUsu);
											if (array.indexOf(past.getName()) == -1) {
												addPastasBackupPerfil(past);
												Long tamanho = FileUtils.sizeOfDirectory(past);
												String size = humanReadableByteCountSI(tamanho);
												ajust = size.split(" ");
												tamanhoTotal += tamanho;
												System.out.println(past.getName() + " -  Tamanho do Diretorio: "
														+ ajust[0] + " " + ajust[1]
														+ "\n ---------------------------------------------------");
											}
										}
									}
								}
								if (tamanhoTotal != 0) {
									String size = humanReadableByteCountSI(tamanhoTotal);
									ajust = size.split(" ");
									System.out.println("Tamanho do Diretorio: " + ajust[0] + " " + ajust[1]
											+ "\n ----------------------------------");
									System.out.println(
											"Fazendo backup do usuario..." + "\n ----------------------------------");
									salvar();
								} else {
									System.out.println("Tamanho do diretorio está vazio. "
											+ "\n ----------------------------------");
								}
							} catch (NullPointerException e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println("BACKUP-FINISH");
				
		}
	}

	public String humanReadableByteCountSI(long bytes) {
		String s = bytes < 0 ? "-" : "";
		long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		return b < 1000L ? bytes + " B"
				: b < 999_950L ? String.format("%s%.1f kB", s, b / 1e3)
						: (b /= 1000) < 999_950L ? String.format("%s%.1f MB", s, b / 1e3)
								: (b /= 1000) < 999_950L ? String.format("%s%.1f GB", s, b / 1e3)
										: (b /= 1000) < 999_950L ? String.format("%s%.1f TB", s, b / 1e3)
												: (b /= 1000) < 999_950L ? String.format("%s%.1f PB", s, b / 1e3)
														: String.format("%s%.1f EB", s, b / 1e6);
	}


}
