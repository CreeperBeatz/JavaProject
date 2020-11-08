import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class GuiApp extends JFrame {
	private static final long serialVersionUID = 0;

	private static final int width = 640;
	private static final int height = 480;

	private final JTextField filePath;
	private final JTextArea content;

	private AppCore appCore;

	public GuiApp() {
		super();

		setSize(width, height);

		setMinimumSize(getSize());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		// Top panel
		{
			Panel topPanel = new Panel(new BorderLayout());

			add(topPanel, BorderLayout.NORTH);

			addFilePathLabel(topPanel);
			filePath = addFilePath(topPanel);
			addChooseFile(topPanel);
		}

		// Center panel
		{
			JTextArea content = new JTextArea();

			this.content = content;

			content.setEditable(false);

			add(new JScrollPane(content), BorderLayout.CENTER);
		}

		// Bottom panel
		{
			JPanel bottomPanel = new JPanel(new GridLayout());

			{
				JButton button = new JButton("Add Empty Line");

				button.addMouseListener(addEmptyLineHandler());

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Remove Last Line");

				button.addMouseListener(removeLastLineHandler());

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Swap Lines");

				button.addMouseListener(swapLinesHandler());

				bottomPanel.add(button, BorderLayout.WEST);
			}

			{
				JButton button = new JButton("Swap Words");

				button.addMouseListener(swapWordsHandler());

				bottomPanel.add(button, BorderLayout.EAST);
			}

			add(bottomPanel, BorderLayout.SOUTH);
		}

		{
			addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {}

				@Override
				public void windowClosing(WindowEvent e) {
					if (appCore != null) {
						try {
							appCore.close();
						} catch (IOException exception) {
							errorCannotWriteDown();
						} catch (SecurityException exception) {
							errorSecurityViolation();

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
			});
		}

		setVisible(true);
	}

	private void refreshContent() {
		if (appCore == null) {
			content.setText(null);
		} else {
			content.setText(appCore.getContentString());
		}
	}

	private void newSession(String path) {
		try {
			if (appCore != null) {
				appCore.close();

				appCore = null;
			}

			filePath.setText(null);

			refreshContent();

			try {
				appCore = new AppCore(path);

				filePath.setText(path);

				refreshContent();
			} catch (FileNotFoundException exception) {
				showErrorMessage("File cannot be found!");
			} catch (IOException exception) {
				showErrorMessage("Cannot read content of the file!");
			}
		} catch (IOException exception) {
			showErrorMessage("Cannot write down the new content because the file cannot be opened!");
		} catch (SecurityException exception) {
			showErrorMessage("Cannot operate due to security violation!");

			System.exit(101);
		}
	}

	private void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorOpenFile() {
		showErrorMessage("Please open a file first!");
	}

	private void errorCannotWriteDown() {
		showErrorMessage("Cannot write down the new content because the file cannot be opened!");
	}

	private void errorSecurityViolation() {
		showErrorMessage("Cannot operate due to security violation!");
	}

	private void errorNoLines() {
		JOptionPane.showMessageDialog(this, "There are no lines!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorLastLineNotEmpty() {
		JOptionPane.showMessageDialog(this, "Last line must be empty to be removed!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorEnterValidNumber() {
		JOptionPane.showMessageDialog(this, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorLineOutOfBounds() {
		JOptionPane.showMessageDialog(this, "Please enter a line number between 1 and the number of lines!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorWordOutOfBounds() {
		JOptionPane.showMessageDialog(this, "Please enter a word number between 1 and the number of words on the specified lines!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorNoWordOnTheLine() {
		JOptionPane.showMessageDialog(this, "Please select a line with words!", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private static void addFilePathLabel(Container container) {
		container.add(new JLabel("File: "), BorderLayout.WEST);
	}

	private static JTextField addFilePath(Container container) {
		JTextField textField = new JTextField();

		textField.setEditable(false);

		container.add(textField, BorderLayout.CENTER);

		return textField;
	}

	private MouseListener addEmptyLineHandler() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (appCore == null) {
					errorOpenFile();

					return;
				}

				appCore.addEmptyLine();

				refreshContent();
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	private MouseListener removeLastLineHandler() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (appCore == null) {
					errorOpenFile();

					return;
				}

				if (appCore.isEmpty()) {
					errorNoLines();

					return;
				}

				if (!appCore.isLastLineEmpty()) {
					errorLastLineNotEmpty();

					return;
				}

				appCore.removeLastLine();

				refreshContent();
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}
	
	private MouseListener swapLinesHandler() {
		GuiApp app = this;

		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (appCore == null) {
					errorOpenFile();

					return;
				}

				try {
					int firstLine, secondLine;

					do {
						String input = JOptionPane.showInputDialog(app, "Please enter first line's number:");

						try {
							firstLine = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isLineInBounds(firstLine)) {
								break;
							} else {
								errorLineOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, "Please enter second line's number:");

						try {
							secondLine = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isLineInBounds(secondLine)) {
								break;
							} else {
								errorLineOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					appCore.swapLines(firstLine, secondLine);

					refreshContent();
				} catch (IndexOutOfBoundsException exception) {
					errorLineOutOfBounds();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	private MouseListener swapWordsHandler() {
		GuiApp app = this;

		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (appCore == null) {
					errorOpenFile();

					return;
				}

				try {
					int firstLine, firstWord, secondLine, secondWord;

					do {
						String input = JOptionPane.showInputDialog(app, "Please enter first line's number:");

						try {
							firstLine = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isLineInBounds(firstLine)) {
								if (appCore.lineHasWords(firstLine)) {
									break;
								} else {
									errorNoWordOnTheLine();
								}
							} else {
								errorLineOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter first word's number:"));

						try {
							firstWord = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isWordInBounds(firstLine, firstWord)) {
								break;
							} else {
								errorWordOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, "Please enter second line's number:");

						try {
							secondLine = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isLineInBounds(secondLine)) {
								if (appCore.lineHasWords(secondLine)) {
									break;
								} else {
									errorNoWordOnTheLine();
								}
							} else {
								errorLineOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter second word's number:"));

						try {
							secondWord = Integer.parseUnsignedInt(input) - 1;

							if (appCore.isWordInBounds(secondLine, secondWord)) {
								break;
							} else {
								errorWordOutOfBounds();
							}
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							errorEnterValidNumber();
						}
					} while (true);

					appCore.swapWords(firstLine, firstWord, secondLine, secondWord);

					refreshContent();
				} catch (IndexOutOfBoundsException exception) {
					// All indexes are bound checked.
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	private MouseListener chooseFileHandler() {
		GuiApp app = this;

		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getButton() & MouseEvent.BUTTON1) != 0) {
					JFileChooser fileChooser = new JFileChooser();

					fileChooser.setFileFilter(new FileNameExtensionFilter("Plain text", "txt"));

					if (fileChooser.showOpenDialog(app) == JFileChooser.APPROVE_OPTION) {
						newSession(fileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}
		};
	}

	private void addChooseFile(Container container) {
		JButton button = new JButton("Choose File");

		button.addMouseListener(chooseFileHandler());

		container.add(button, BorderLayout.EAST);
	}
}
