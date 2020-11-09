package app;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.core.AppCore;

final class GuiHandlers {
	private GuiHandlers() {
		super();
	}

	static WindowListener defaultWindowListener(GuiApp app, AppCore appCore) {
		return new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				if (appCore != null) {
					try {
						appCore.close();
					} catch (IOException exception) {
						GuiErrors.errorCannotWriteDown(app);
					} catch (SecurityException exception) {
						GuiErrors.errorSecurityViolation(app);

						System.exit(101);
					}
				}
			}

			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
		};
	}

	static MouseListener addEmptyLineHandler(GuiApp app, AppCore appCore) {
		return new MouseListener() {
			private boolean withinControl = false;

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				withinControl = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				withinControl = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != MouseEvent.BUTTON1 || !withinControl) {
					return;
				}

				if (appCore == null) {
					GuiErrors.errorOpenFile(app);

					return;
				}

				appCore.addEmptyLine();

				app.refreshContent();
			}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	static MouseListener removeLastLineHandler(GuiApp app, AppCore appCore) {
		return new MouseListener() {
			private boolean withinControl = false;

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				withinControl = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				withinControl = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != MouseEvent.BUTTON1 || !withinControl) {
					return;
				}

				if (appCore == null) {
					GuiErrors.errorOpenFile(app);

					return;
				}

				if (appCore.isEmpty()) {
					GuiErrors.errorNoLines(app);

					return;
				}

				if (!appCore.isLastLineEmpty()) {
					GuiErrors.errorLastLineNotEmpty(app);

					return;
				}

				appCore.removeLastLine();

				app.refreshContent();
			}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	static MouseListener swapLinesHandler(GuiApp app, AppCore appCore) {
		return new MouseListener() {
			private boolean withinControl = false;

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				withinControl = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				withinControl = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != MouseEvent.BUTTON1 || !withinControl) {
					return;
				}

				if (appCore == null) {
					GuiErrors.errorOpenFile(app);

					return;
				}

				if (appCore.lineCount() < 2) {
					GuiErrors.errorNotEnoughLines(app);

					return;
				}

				try {
					int firstLine = GuiLineInput.inputFirstLine(app, appCore).intValue();

					int secondLine = GuiLineInput.inputSecondLineExcluding(app, appCore, firstLine).intValue();

					appCore.swapLines(firstLine, secondLine);

					app.refreshContent();
				} catch (NullPointerException exception) {
					// User canceled procedure.
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	static MouseListener swapWordsHandler(GuiApp app, AppCore appCore) {
		return new MouseListener() {
			private boolean withinControl = false;

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				withinControl = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				withinControl = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != MouseEvent.BUTTON1 || !withinControl) {
					return;
				}

				if (appCore == null) {
					GuiErrors.errorOpenFile(app);

					return;
				}

				{
					int totalWordCount = 0;

					for (int z = 0; z < appCore.lineCount(); ++z) {
						totalWordCount += appCore.wordCountOnLine(z);

						if (totalWordCount > 1) {
							break;
						}
					}

					if (totalWordCount < 2) {
						GuiErrors.errorNotEnoughWords(app);

						return;
					}
				}

				try {
					int firstLine = GuiLineInput.inputFirstLineForWords(app, appCore).intValue();

					int firstWord = GuiWordInput.inputFirstWord(app, appCore, firstLine).intValue();

					int secondLine = GuiLineInput.inputSecondLineForWordsExcluding(app, appCore, firstLine).intValue();

					int secondWord = (
						firstLine == secondLine ?
						GuiWordInput.inputSecondWordExcluding(app, appCore, secondLine, firstWord) :
						GuiWordInput.inputSecondWord(app, appCore, secondLine)
					).intValue();

					appCore.swapWords(firstLine, firstWord, secondLine, secondWord);

					app.refreshContent();
				} catch (NullPointerException exception) {
					// User cancelled procedure.
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	static MouseListener chooseFileHandler(GuiApp app, AppCore appCore) {
		return new MouseListener() {
			private boolean withinControl = false;

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				withinControl = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				withinControl = false;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != MouseEvent.BUTTON1 || !withinControl) {
					return;
				}

				if ((e.getButton() & MouseEvent.BUTTON1) != 0) {
					JFileChooser fileChooser = new JFileChooser();

					fileChooser.setFileFilter(new FileNameExtensionFilter("Plain text", "txt"));

					if (fileChooser.showOpenDialog(app) == JFileChooser.APPROVE_OPTION) {
						app.newSession(fileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}
}
