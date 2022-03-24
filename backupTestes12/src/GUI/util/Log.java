package GUI.util;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Log {

	// Niveis de log DEBUG, INFO, WARN, ERROR, FATAL

	private static volatile TextArea textArea = null;
	private static Logger logger = Logger.getLogger(Log.class);

	public static void setTextArea(final TextArea textArea) {

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
								// textArea.selectEnd();
								// textArea.insertText(textArea.getText().length(), message);
								switch (tipo.toUpperCase()) {

								case "DEBUG":
									logger.debug(msg);
									textArea.setText(textArea.getText() + "\n" + msg);
									textArea.appendText("");
									break;

								case "INFO":
									logger.info(msg);
									textArea.setText(textArea.getText() + "\n" + msg);
									textArea.appendText("");
									break;

								case "WARN":
									logger.warn(msg);
									textArea.setText(textArea.getText() + "\n" + msg);
									textArea.appendText("");
									break;

								case "ERROR":
									logger.error(msg);
									textArea.setText(textArea.getText() + "\n" + msg);
									textArea.appendText("");
									break;

								case "FATAL":
									logger.fatal(msg);
									textArea.setText(textArea.getText() + "\n" + msg);
									textArea.appendText("");
									break;

								}
							}
						}
					} catch (final Throwable t) {
						System.out.println("Unable to append log to textarea:" + t.getMessage());
					}
				}
			});
		} catch (final IllegalStateException e) {
		}

	}

}
