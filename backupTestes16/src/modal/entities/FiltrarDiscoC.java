package modal.entities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import modal.helper.Log;

public class FiltrarDiscoC implements Filtro {

	@Override
	public boolean isValidFolder(String[] list, File past) {
		final List<String> array = Arrays.asList(list);
		try {
			if (past.getAbsoluteFile().equals(past.getCanonicalFile())) {
				if (array.indexOf(past.getName()) == -1) {

					return true;
				}
			}
		} catch (IOException e) {
			Log.log("error", "Error: " + e.getMessage());
		}

		return false;
	}

}
