package modal.entities;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import modal.helper.ContSize;
import modal.helper.CopyFile;
import modal.helper.Log;

public class Escaner {

	private final String[] notFolder = { "$GetCurrent", "$Recycle.Bin","$RECYCLE.BIN","$WINDOWS.~BT","$WinREAgent", "$SysReset", "AppData", "Arquivos de Programas",
			"Config.Msi", "Documents and Settings", "Intel", "Microsoft", "MSOCache", "OneDriveTemp", "PerfLogs",
			"Program Files", "Program Files (x86)", "ProgramData", "Recovery", "System Volume Information", "Temp",
			"tmp","DEV","ASDD", "Users", "Windows" };
	
	private final String[] toNotBackupToFolder = { "3D Objects", "Application Data", "Contacts", "Cookies",
			"IntelGraphicsProfiles", "Links", "Local Settings", "MicrosoftEdgeBackups", "NetHood", "OneDrive",
			"PrintHood", "Recent", "Saved Games", "Searches", "SendTo", "Start Menu", "Templates", "My Documents",
			"VirtualBox VMs", "MyFirstGame", "NI mate", "source", "Ambiente de Impressão", "Ambiente de Rede", "Apple",
			"Configurações Locais", "Dados de Aplicativos", "Creative Cloud Files", "drivers", "Jogos", "Menu Iniciar",
			"MyFirstGame", "Modelos", "source", "NI mate" };
	
	public void Escanear(File caminho) {
			
			final File[] folders = caminho.listFiles(File::isDirectory);
			
			for (File folder : folders) {
				System.out.println(folder.getName());
				System.out.println("===========================");
				
				
				if(folder.getName().equals("Users")) {
					
					final File[] foldersUsu = folder.listFiles(File::isDirectory);
					
					for (File folderUsu : foldersUsu) {
						
						System.out.println(folderUsu.getName());
						System.out.println("===========================");
						
						ValidaNomePerfilUsu(folderUsu);}
				
				}else {
					
					ValidaNome(folder);
				}
								
			}
				
	}
	
	
	void ValidaNome(File file) {		
			if (Arrays.asList(notFolder).indexOf(file.getName()) == -1) {
				
				System.out.println(file.getName());
				ValidaTamanho(file);
				
			}else {
				
				System.out.println("não será feito backup: " + file.getName());
			}
			
	}
	
	void ValidaNomePerfilUsu(File file) {		
		if (Arrays.asList(toNotBackupToFolder).indexOf(file.getName()) == -1) {
			
			System.out.println(file.getName());
			ValidaTamanho(file);
			
		}else {
			
			System.out.println("não será feito backup: " + file.getName());
		}
			
	}
	
	void ValidaTamanho(File file) {
		Long sizeTotal = 0L;

		File[] folders = file.listFiles(File::isDirectory);
		if (folders != null) {
			for (File past : folders) {				
					// tamanho da pasta
					sizeTotal += FileUtils.sizeOfDirectory(past);
				}		
		}

		Log.log("info", "Tamanho: " + ContSize.humanReadableByteCountSI(sizeTotal));

		if( sizeTotal > 0L) {
			System.out.println("Será feito backup: " + file.getName());
			Fazerbackup(file);
		}
	}

	void Fazerbackup(File file) {
	
		Log.log("info", "");
		Log.log("info", "Inicializando backup");
		
		CopyFile.copy(file, new File("C:\\VAMOSNEGADA\\" + file.getName()));

	}
}
