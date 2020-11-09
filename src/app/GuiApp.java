package app;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import app.core.AppCore;

public final class GuiApp extends JFrame {
	private static final long serialVersionUID;

	private static final int minWidth;
	private static final int minHeight;

	private final JTextField filePath;
	private final JTextArea content;

	private AppCore appCore;

	static {
		serialVersionUID = 0;
		minWidth = 640;
		minHeight = 480;
	}

	private GuiApp() {
		super();

		setSize(minWidth, minHeight);

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

				button.addMouseListener(GuiHandlers.addEmptyLineHandler(this, appCore));

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Remove Last Line");

				button.addMouseListener(GuiHandlers.removeLastLineHandler(this, appCore));

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Swap Lines");

				button.addMouseListener(GuiHandlers.swapLinesHandler(this, appCore));

				bottomPanel.add(button, BorderLayout.WEST);
			}

			{
				JButton button = new JButton("Swap Words");

				button.addMouseListener(GuiHandlers.swapWordsHandler(this, appCore));

				bottomPanel.add(button, BorderLayout.EAST);
			}

			add(bottomPanel, BorderLayout.SOUTH);
		}

		addWindowListener(GuiHandlers.defaultWindowListener(this, appCore));

		setVisible(true);
	}

	public static void run() {
		try {
			new GuiApp();
		} catch (HeadlessException exception) {
			System.err.println("Cannot operate in graphical mode when there is no graphics environment available!");
		}
	}

	// Accessible by the package
	void refreshContent() {
		if (appCore == null) {
			content.setText(null);
		} else {
			content.setText(appCore.getContentString());
		}
	}

	// Accessible by the package
	void newSession(String path) {
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
				GuiErrors.errorFileNotFound(this);
			} catch (IOException exception) {
				GuiErrors.errorCannotRead(this);
			}
		} catch (IOException exception) {
			GuiErrors.errorCannotWriteDown(this);
		} catch (SecurityException exception) {
			GuiErrors.errorSecurityViolation(this);

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

	private void addChooseFile(Container container) {
		JButton button = new JButton("Choose File");

		button.addMouseListener(GuiHandlers.chooseFileHandler(this, appCore));

		container.add(button, BorderLayout.EAST);
	}
}
