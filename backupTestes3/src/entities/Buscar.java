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
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class Buscar {

	private final String[] usersNegados = { "Administrador", "All Users", "Default", "Default User", "Public",
			"Todos os Usuários", "Usuário Padrão" };

	private final String[] backUsu = { "3D Objects", "Application Data", "Contacts", "Cookies", "IntelGraphicsProfiles",
			"Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive", "PrintHood", "Recent",
			"Saved Games", "Searches", "SendTo", "Start Menu", "Templates","My Documents" };

	public void procurarPerfisDeUsuarios(File path, String target) {
		if (path != null) {
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
										File e = new File(target +"\\"+ folder.getName());
										if(!e.exists()) {
											e.mkdir();
										}
										System.out.println(folder.getName() + "\nTamanho do Diretorio: " + ajust[0]
												+ " " + ajust[1] + "\n ----------------------------------");

										fazerBackupDoPerfil(folder, e);

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
	
	
	//https://www.guj.com.br/t/copiar-arquivos-de-uma-pasta-para-outra/61631/4
	public boolean fazerBackupDoPerfil(File srcDir, File dstDir) {
		try {
			if (srcDir.isDirectory()) {
				if (srcDir.getName().equals("AppData")) {
					File diretorio = new File(srcDir + "\\Local\\Google\\Chrome\\User Data\\Default\\");
					if (!diretorio.exists()) {
						System.out.println("Não tem BookMarks para salvar! \n" + srcDir
								+ "\\Local\\Google\\Chrome\\User Data\\Default\\");
					} else {
						diretorio = new File(dstDir.getPath());
						diretorio.mkdir();
						System.out.println("Copiar BookMarks para salvar!");
						try {
							Path copiarArquivos = Files.copy(
									Paths.get(srcDir + "\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"),
									Paths.get(dstDir + "\\Bookmarks"), StandardCopyOption.REPLACE_EXISTING);

							System.out.println(Files.exists(copiarArquivos));
							return true;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					}
				} 
				
				if (!dstDir.exists()) {
					dstDir.mkdir();
				}
				
				String[] children = srcDir.list();
				if(children != null) {
					
					for (int i = 0; i < children.length; i++) {
						if (Arrays.asList(backUsu).indexOf(children[i]) == -1) {
							
							fazerBackupDoPerfil(new File(srcDir, children[i]), new File(dstDir, children[i]));
						}
						
					}
				}
				

			 } 
            else{

                InputStream in = new FileInputStream( srcDir );
                OutputStream out = new FileOutputStream( dstDir );

                byte[] buf = new byte[1024];
                int len;

                while( (len = in.read( buf ) ) > 0 ) {

                    out.write( buf, 0, len );
                }

                in.close();
                out.close();
            }
        }
        catch( IOException ioex ){

            ioex.printStackTrace();
            return false;
        }

        return true;

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
