package modal.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public final class CopyFile {
	// https://www.guj.com.br/t/copiar-arquivo-para-outro-diretorio/131127/2
	public static void copy(File source, File destination) throws IOException {

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
			//Files.copy(source.getAbsoluteFile().toPath(), destination.toPath());
		}

	}

}
