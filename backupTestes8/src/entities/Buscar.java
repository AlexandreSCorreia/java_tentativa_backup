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

		try {
			final File[] folders = path.listFiles(File::isDirectory);
			final List<String> array = Arrays.asList(backUsu);
			System.out.println("NÃO SERÁ FEITO BACKUP DE PERFIS VAZIOS! \n");
			System.out.println("Procurando perfis de usuários...");
			for (File folder : folders) {
				if (Arrays.asList(usersNegados).indexOf(folder.getName()) == -1) {
					System.out.println("Usuário encontrado: " + folder.getName());
					System.out.println();
					PerfilUsuario perfilUsu = new PerfilUsuario(new File(target), folder);
					File[] folderUsu = folder.listFiles(File::isDirectory);
					Long tamanhoTotal = 0L;
					String[] ajust = null;
					System.out.println("Checando tamanho das pastas a serem salvas...\n");
					System.out.println("-----------------------------------------------");
					if (folderUsu != null) {
						for (File past : folderUsu) {
								if (!past.getName().equals("AppData") && !past.getName().substring(0, 1).equals(".")
										&& !past.getName().contains("OneDrive")) {
									
									if (array.indexOf(past.getName()) == -1) {
										
										Long tamanho = FileUtils.sizeOfDirectory(past);
										
										if (tamanho != 0) {
											perfilUsu.addPastasBackupPerfil(past);
											String size = humanReadableByteCountSI(tamanho);
											ajust = size.split(" ");
											tamanhoTotal += tamanho;
											System.out.println(past.getName() + " -  Tamanho do Diretorio: " + ajust[0]
													+ " " + ajust[1]);
										}
									}

								}
							}
						}
					

					if (tamanhoTotal != 0) {
						String size = humanReadableByteCountSI(tamanhoTotal);
						ajust = size.split(" ");
						System.out.println("----------------------------------------------------");
						System.out.println("Tamanho do total do diretorio: " + ajust[0] + " " + ajust[1]
								+ "\n ----------------------------------");
						System.out.println("Iniciando backup do usuário... \n");
						perfilUsu.salvar();
					} else {
						System.out.println("O diretorio está vazio. " + "\n ----------------------------------");
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println("BACKUP-FINISH");

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
