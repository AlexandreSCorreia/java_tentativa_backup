package modal.helper;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public final class Log {

	// Niveis de log DEBUG, INFO, WARN, ERROR, FATAL

	private static volatile TextArea textArea = null;
	private static Logger logger = Logger.getLogger(Log.class);

	public final static void setTextArea(final TextArea textArea) {

		Log.textArea = textArea;

	}

	public static synchronized void log(String tipo, String msg) {

		try {

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {

						if (textArea != null) {
							
							if (textArea.getText().length() == 0) {
								
								textArea.setText(msg);
								
							} else {

								switch (tipo.toUpperCase()) {

								case "DEBUG":
									logger.debug(msg);
									break;

								case "INFO":
									logger.info(msg);
									break;

								case "WARN":
									logger.warn(msg);
									break;

								case "ERROR":
									logger.error(msg);
									break;

								case "FATAL":
									logger.fatal(msg);
									break;

								}
								
								textArea.setText(textArea.getText() + "\n" + msg);
								textArea.appendText("");
							}
						}

					} catch (final Throwable t) {

						t.printStackTrace();

					}
				}

			});

		} catch (final IllegalStateException e) {
			e.printStackTrace();
		}

	}

}
