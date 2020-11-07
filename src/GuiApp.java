import java.awt.BorderLayout;
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
			JPanel bottomPanel = new JPanel(new BorderLayout());

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
			GuiApp app = this;

			addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {}

				@Override
				public void windowClosing(WindowEvent e) {
					try {
						appCore.close();
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(app, new JLabel("Cannot write down the new content because the file cannot be opened!"), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (SecurityException exception) {
						JOptionPane.showMessageDialog(app, new JLabel("Cannot operate due to security violation!"), "Error", JOptionPane.ERROR_MESSAGE);

						System.exit(101);
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

	private void newSession(String path) {
		try {
			if (appCore != null) {
				appCore.close();

				appCore = null;
			}

			filePath.setText(null);
			content.setText(null);

			try {
				appCore = new AppCore(path);

				filePath.setText(path);
				content.setText(appCore.getContentString());
			} catch (FileNotFoundException exception) {
				JOptionPane.showMessageDialog(this, new JLabel("File cannot be found!"), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException exception) {
				JOptionPane.showMessageDialog(this, new JLabel("Cannot read content of the file!"), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(this, new JLabel("Cannot write down the new content because the file cannot be opened!"), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException exception) {
			JOptionPane.showMessageDialog(this, new JLabel("Cannot operate due to security violation!"), "Error", JOptionPane.ERROR_MESSAGE);

			System.exit(101);
		}
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

	private MouseListener swapLinesHandler() {
		GuiApp app = this;

		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (appCore == null) {
					JOptionPane.showMessageDialog(app, "Please open a file first!", "Error", JOptionPane.ERROR_MESSAGE);

					return;
				}

				try {
					int firstLine, secondLine;

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter first line's number:"));

						try {
							firstLine = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter second line's number:"));

						try {
							secondLine = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					appCore.swapLines(firstLine - 1, secondLine - 1);

					content.setText(appCore.getContentString());
				} catch (IndexOutOfBoundsException exception) {
					JOptionPane.showMessageDialog(app, "Please enter line numbers that are between 1 and the number of lines!", "Error", JOptionPane.ERROR_MESSAGE);
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
					JOptionPane.showMessageDialog(app, "Please open a file first!", "Error", JOptionPane.ERROR_MESSAGE);

					return;
				}

				try {
					int firstLine, firstWord, secondLine, secondWord;

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter first line's number:"));

						try {
							firstLine = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter first word's number:"));

						try {
							firstWord = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter second line's number:"));

						try {
							secondLine = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					do {
						String input = JOptionPane.showInputDialog(app, new JLabel("Please enter second word's number:"));

						try {
							secondWord = Integer.parseUnsignedInt(input);

							break;
						} catch (NumberFormatException exception) {
							if (input == null) {
								return;
							}

							JOptionPane.showMessageDialog(app, "Please enter valid whole number!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					} while (true);

					appCore.swapWords(firstLine - 1, firstWord - 1, secondLine - 1, secondWord - 1);

					content.setText(appCore.getContentString());
				} catch (IndexOutOfBoundsException exception) {
					JOptionPane.showMessageDialog(app, "Please enter line numbers that are between 1 and the number of lines and word numbers between 1 and the number of words in the line!", "Error", JOptionPane.ERROR_MESSAGE);
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
