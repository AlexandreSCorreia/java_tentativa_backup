package entities;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

public class TextAreaAppender extends WriterAppender {

	private static volatile TextArea textArea = null;

	public static void setTextArea(final TextArea textArea) {
		TextAreaAppender.textArea = textArea;
		
		//textArea.textProperty().bind((ObservableValue) task.titleProperty());
	}

	@Override
	public synchronized void doAppend(LoggingEvent event) {
		final String message = this.layout.format(event);
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						if (textArea != null) {
							if (textArea.getText().length() == 0) {
								textArea.setText(message);
							} else {
								textArea.selectEnd();
								textArea.insertText(textArea.getText().length(), message);
							}
						}
					} catch (final Throwable t) {
						System.out.println("Unable to append log to textarea:" + t.getMessage());
					}
				}
			});
		} catch (final IllegalStateException e) {
		}
		super.doAppend(event);
	}

	@Override
	public void append(final LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						if (textArea != null) {
							if (textArea.getText().length() == 0) {
								textArea.setText(message);
							} else {
								textArea.selectEnd();
								textArea.insertText(textArea.getText().length(), message);
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