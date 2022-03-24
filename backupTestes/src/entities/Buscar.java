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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Buscar {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão", "t05807" };

	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates" };

	public void procurarPerfisDeUsuarios(File path, String target) {
		List<File> listPerfisUsuarios = new ArrayList<>();
		if (path != null) {
			if (path.exists()) {
				File[] folders = path.listFiles(File::isDirectory);
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						for (File folder : folders) {
							if (Arrays.asList(usersNegados).indexOf(folder.getName()) == -1) {
								try {
									final String size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(folder));
									final String[] ajust = size.split(" ");
									if (!ajust[0].equals("0")) {
										//listPerfisUsuarios.add(folder);
										
										fazerBackupDoPerfil(folder, new File(target));
										System.out.println(folder.getName() + "\nTamanho do Diretorio: " + ajust[0]
												+ " " + ajust[1] + "\n ----------------------------------");
									}

								} catch (NullPointerException e) {
									System.out.println(e.getMessage());
								}
							}

						}

						System.out.println(listPerfisUsuarios.size() + " perfis encontrados.");
						
					}

				});

				t.start();

			}
		}
	}

	public boolean fazerBackupDoPerfil(File srcDir, File dstDir) {
		try {
			if (srcDir.isDirectory()) {
				if (srcDir.getName().equals("AppData")) {
					File diretorio = new File(srcDir + "\\Local\\Google\\Chrome\\User Data\\Default\\");
					if (!diretorio.exists()) {
						System.out.println("Não tem BookMarks para salvar! \n" + srcDir
								+ "\\Local\\Google\\Chrome\\User Data\\Default\\");
					} else {
						System.out.println("Copiar BookMarks para salvar!");
						try {
							Path copiarArquivos = Files.copy(
									Paths.get(srcDir + "\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
									Paths.get(dstDir + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);

							System.out.println(Files.exists(copiarArquivos));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else if (!srcDir.exists()) {
					srcDir.mkdir();
				}

				else if (Arrays.asList(backUsu).indexOf(srcDir.getName()) == -1) {
					try {
						final String size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(srcDir));
						final String[] ajust = size.split(" ");
						if (!ajust[0].equals("0")) {
							
							String[] children = srcDir.list();

							for (int i = 0; i < children.length; i++) {

								fazerBackupDoPerfil(new File(srcDir, children[i]), new File(dstDir, children[i]));
							}

							System.out.println(srcDir.getName() + "\nTamanho do Diretorio: " + ajust[0] + " "
									+ ajust[1] + "\n ----------------------------------");
						}

					} catch (NullPointerException e) {
						System.out.println(e.getMessage());
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
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}

	public void copiarDados(List<File> perfisDeUsuarios, String target) {
		if (perfisDeUsuarios != null) {
			if (target != null && !target.equals("")) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println();
						System.out.println("---------------------------------------------");
						System.out.println(" Testando BackUp");
						// procura arquivos
						/*
						 * File[] files = perfil.listFiles(File::isFile); for (File file : files) {
						 * 
						 * System.out.println("Vendo arquivos" + file.getName()); }
						 */

						for (File a : perfisDeUsuarios) {
							File[] newListfiles = a.listFiles(File::isDirectory);

							

							

						}

						System.out.println("Finish Backup");

					}

				});

				t.start();
			}
		}
	}

	public void asd(File a) {
		if (a != null) {
			if (a.isDirectory()) {

			}
		}
	}

	public String criarPasta(File a, String caminho) {
		File arq = new File(caminho + "\\" + a.getName());

		if (!arq.exists()) {
			boolean success = new File(caminho + "\\" + a.getName()).mkdir();
			System.out.println("Directory created successfully: " + success + "\n" + caminho + "\\" + a.getName());
		}

		return arq.getPath();
	}

	// List<File>
	/*
	 * public final void listDiretorios(File diretorio, String pathSave) {
	 * List<File> newFile = new ArrayList<>(); if (diretorio != null) { if
	 * (diretorio.exists()) { File[] folders =
	 * diretorio.listFiles(File::isDirectory); //
	 * System.out.println("Procurando Perfils de usuarios em " + diretorio + ":\n");
	 * if (!(filtroUsu.size() > 0)) { filtroUsu = filtrarLista(folders);
	 * copiaArquivos(filtroUsu, pathSave); return; } else { Thread t = new
	 * Thread(new Runnable() {
	 * 
	 * @Override public void run() { for (File a : folders) {
	 * 
	 * if (a.getName().equals("AppData")) { File diretorio = new File(a +
	 * "\\Local\\Google\\Chrome\\User Data\\Default\\"); if (!diretorio.exists()) {
	 * System.out.println("Não tem BookMarks para salvar! \n" + a +
	 * "\\Local\\Google\\Chrome\\User Data\\Default\\"); } else {
	 * System.out.println("Copiar BookMarks para salvar!"); try { Path
	 * copiarArquivos = Files.copy(Paths.get(a +
	 * "\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),Paths.get(
	 * "E:\\FIELDPC-BACKUP\\554033\\Bookmarks"),
	 * StandardCopyOption.REPLACE_EXISTING);
	 * 
	 * System.out.println(Files.exists(copiarArquivos)); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } } }
	 * 
	 * else if (Arrays.asList(backUsu).indexOf(a.getName()) == -1) { try { final
	 * String size = humanReadableByteCountSI(FileUtils.sizeOfDirectory(a)); final
	 * String[] ajust = size.split(" "); if (!ajust[0].equals("0")) {
	 * 
	 * 
	 * newFile.add(a);
	 * 
	 * System.out.println(a.getName() + "\nTamanho do Diretorio: " + ajust[0] + " "
	 * + ajust[1] + "\n ----------------------------------"); }
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
	 * // aguarda a finalização das tarefas try { t.join();
	 * 
	 * } catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * // return newFile; copiaArquivos(newFile, pathSave); }
	 * 
	 * //
	 * -----------------------------------------------------------------------------
	 * ---------------------------------
	 * 
	 * public void copiaArquivos(List<File> EntradanewFile, String pathSave) {
	 * 
	 * if (EntradanewFile != null) {
	 * 
	 * for (File a : EntradanewFile) { if (a.exists()) { boolean success = new
	 * File(pathSave + "\\" + a.getName()).mkdir();
	 * 
	 * System.out.println( "Directory created successfully: " + success + "\n" +
	 * pathSave + "\\" + a.getName()); listDiretorios(a, pathSave + "\\" +
	 * a.getName()); } }
	 * 
	 * /* File[] folders = diretorio.listFiles(File::isDirectory);
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
	 * // aguarda a finalização das tarefas try { t.join();
	 * 
	 * } catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
	 * //System.out.println(newFile.size() + " perfis encontrados.");
	 */
	/*
	 * }
	 * 
	 * // return newFile; // copiaArquivos(newFile);
	 */
	/* } */

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
