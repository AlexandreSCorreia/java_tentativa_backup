package GUI.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

public class CopyFile {
	// https://www.guj.com.br/t/copiar-arquivo-para-outro-diretorio/131127/2
	public static void copy(File source, File destination) {
		try {

			if (source.isDirectory()) {

				if (!destination.exists()) {
					destination.mkdir();
				}
				String[] children = source.list();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {

						copy(new File(source, children[i]), new File(destination, children[i]));

					}
				}
			} else if (!source.isHidden()) {

				FileUtils.copyFile(source.getAbsoluteFile(), destination);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
