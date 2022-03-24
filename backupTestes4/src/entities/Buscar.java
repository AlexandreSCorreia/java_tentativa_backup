package entities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

public final class Buscar {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents", "VirtualBox VMs",
			"MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };

	public final void procurarPerfisDeUsuarios(File path, String target) {
		if (path != null) {
			final File[] folders = path.listFiles(File::isDirectory);
			final Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (File folder : folders) {
						if (Arrays.asList(usersNegados).indexOf(folder.getName()) == -1) {
							try {
								System.out.println("NÃO SERÁ FEITO BACKUP DE PERFIS VAZIOS!");
								System.out.println("Procurando perfils de Usuarios...");
								System.out.println("Usuario encontrado: " + folder.getName());
								System.out.println("---------------------------------------------------");
								PerfilUsuario perfilUsuario = new PerfilUsuario(new File(target), folder);
								File[] folderUsu = folder.listFiles(File::isDirectory);
								Long tamanhoTotal = 0L;
								String[] ajust = null;
								System.out.println("Checando tamanho das pastas que seram salvas...");
								for (File past : folderUsu) {

									if (!past.getName().equals("AppData") && !past.getName().substring(0, 1).equals(".")
											&& !past.getName().contains("OneDrive")) {
										List<String> array = Arrays.asList(backUsu);
										if (array.indexOf(past.getName()) == -1) {
											perfilUsuario.addPastasBackupPerfil(past);
											Long tamanho = FileUtils.sizeOfDirectory(past);
											String size = humanReadableByteCountSI(tamanho);
											ajust = size.split(" ");
											tamanhoTotal += tamanho;
											System.out.println(past.getName() + " -  Tamanho do Diretorio: " + ajust[0]
													+ " " + ajust[1]
													+ "\n ---------------------------------------------------");

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
									perfilUsuario.salvar();
									// fazerBackupDoPerfil(folder,new File(target));
								}

							} catch (NullPointerException e) {
								System.out.println(e.getMessage());
							}
						}

					}

					System.out.println("BACKUP-FINISH");

				}

			});

			t.start();
		}
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
