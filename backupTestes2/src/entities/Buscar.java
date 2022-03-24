package entities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Buscar {

	private String[] usersNegados = null;
	private List<File> filtroUsu = new ArrayList<>();
	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates" };

	public Buscar(String[] usersNegados) {
		this.usersNegados = usersNegados;
	}

	public List<File> filtrarLista(File[] folders) {
		List<File> newFile = new ArrayList<>();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for (File a : folders) {
					if (Arrays.asList(usersNegados).indexOf(a.getName()) == -1) {
						try {
							final String size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(a));
							final String[] ajust = size.split(" ");
							if (!ajust[0].equals("0")) {
								newFile.add(a);

								System.out.println(a.getName() + "\nTamanho do Diretorio: " + ajust[0] + " " + ajust[1]
										+ "\n ----------------------------------");
							}

						} catch (NullPointerException e) {
							System.out.println(e.getMessage());
						}
					}

				}

			}

		});

		t.start();

		// aguarda a finaliza��o das tarefas
		try {
			t.join();

		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println(newFile.size() + " perfis encontrados.");
		return newFile;
	}

	// List<File>
	public final void listDiretorios(File diretorio, String pathSave) {
		List<File> newFile = new ArrayList<>();
		if (diretorio != null) {
			if (diretorio.exists()) {
				File[] folders = diretorio.listFiles(File::isDirectory);
				// System.out.println("Procurando Perfils de usuarios em " + diretorio + ":\n");
				if (!(filtroUsu.size() > 0)) {
					filtroUsu = filtrarLista(folders);
					copiaArquivos(filtroUsu, pathSave);
					return;
				} else {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							for (File a : folders) {

								if (a.getName().equals("AppData")) {
									File diretorio = new File(a + "\\Local\\Google\\Chrome\\User Data\\Default\\");
									if (!diretorio.exists()) {
										System.out.println("N�o tem BookMarks para salvar! \n" + a
												+ "\\Local\\Google\\Chrome\\User Data\\Default\\");
									} else {
										System.out.println("Copiar BookMarks para salvar!");
										try {
											Path copiarArquivos = Files.copy(Paths.get(a + "\\Local\\Google\\Chrome\\User Data\\Default\\"),Paths.get( "E:\\FIELDPC-BACKUP\\554033\\"), StandardCopyOption.REPLACE_EXISTING);
										
											System.out.println(Files.exists(copiarArquivos));
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}

								else if (Arrays.asList(backUsu).indexOf(a.getName()) == -1) {
									try {
										final String size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(a));
										final String[] ajust = size.split(" ");
										if (!ajust[0].equals("0")) {

											
											newFile.add(a);

											System.out.println(a.getName() + "\nTamanho do Diretorio: " + ajust[0] + " "
													+ ajust[1] + "\n ----------------------------------");
										}

									} catch (NullPointerException e) {
										System.out.println(e.getMessage());
									}
								}

							}

						}

					});

					t.start();

					// aguarda a finaliza��o das tarefas
					try {
						t.join();

					} catch (InterruptedException ex) {
						System.out.println(ex.getMessage());
					}

				}

			}

		}

		// return newFile;
		copiaArquivos(newFile, pathSave);
	}

	// --------------------------------------------------------------------------------------------------------------

	public void copiaArquivos(List<File> EntradanewFile, String pathSave) {

		if (EntradanewFile != null) {

			for (File a : EntradanewFile) {
				if (a.exists()) {
					boolean success = new File(pathSave + "\\" + a.getName()).mkdir();

					System.out.println(
							"Directory created successfully: " + success + "\n" + pathSave + "\\" + a.getName());
					listDiretorios(a, pathSave + "\\" + a.getName());
				}
			}

			/*
			 * File[] folders = diretorio.listFiles(File::isDirectory);
			 * System.out.println("Procurando Perfils de usuarios em " + diretorio + ":\n");
			 * 
			 * Thread t = new Thread(new Runnable() {
			 * 
			 * @Override public void run() { for (File a : folders) { if
			 * (Arrays.asList(usersNegados).indexOf(a.getName()) == -1) { try { final String
			 * size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(a)); final String[]
			 * ajust = size.split(" "); if (!ajust[0].equals("0")) { //newFile.add(a);
			 * 
			 * System.out.println("Usuario: " + a.getName() + "\nTamanho do Diretorio: " +
			 * ajust[0] + " " + ajust[1] + "\n ----------------------------------"); }
			 * 
			 * } catch (NullPointerException e) { System.out.println(e.getMessage()); } }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * });
			 * 
			 * t.start();
			 * 
			 * // aguarda a finaliza��o das tarefas try { t.join();
			 * 
			 * } catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
			 * //System.out.println(newFile.size() + " perfis encontrados.");
			 */
		}

		// return newFile;
		// copiaArquivos(newFile);*/
	}

	public static String humanReadableByteCountSI(long bytes) {
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
