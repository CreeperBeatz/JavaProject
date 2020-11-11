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

/**
 * Represents the Graphical User Interface (GUI) variant of the application.
 * This class is responsible for all the interfaces provided by the application's GUI variant.
 * This is a utility class, therefore uninstantiable (from outside) nor inheritable.
 * To use this class see the {@code run} method.
 */
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

	/**
	 * Constructs a new instance of the class which is also the head of the application's main window.
	 */
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

				button.addMouseListener(GuiHandlers.addEmptyLineHandler(this));

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Remove Last Line");

				button.addMouseListener(GuiHandlers.removeLastLineHandler(this));

				bottomPanel.add(button);
			}

			{
				JButton button = new JButton("Swap Lines");

				button.addMouseListener(GuiHandlers.swapLinesHandler(this));

				bottomPanel.add(button, BorderLayout.WEST);
			}

			{
				JButton button = new JButton("Swap Words");

				button.addMouseListener(GuiHandlers.swapWordsHandler(this));

				bottomPanel.add(button, BorderLayout.EAST);
			}

			add(bottomPanel, BorderLayout.SOUTH);
		}

		addWindowListener(GuiHandlers.defaultWindowListener(this));

		setVisible(true);
	}

    /**
     * This method is used to start the application in GUI mode.
     */
	public static void run() {
		try {
			new GuiApp();
		} catch (HeadlessException exception) {
			System.err.println("Cannot operate in graphical mode when there is no graphics environment available!");
		}
	}

	/**
	 * This method is used for getting the new content's formatted string and placing replacing the old one on the window's control {@code content}.
	 */
	void refreshContent() {
		if (appCore == null) {
			content.setText(null);
		} else {
			content.setText(appCore.getContentString());
		}
	}

	/**
	 * This method is used to close the previous session, if any, and open a new one by opening the file with the given path.
	 * @param path Absolute or relative path to the file which will be used for the new session.
	 */
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

	/**
	 * This method is used to get a reference to the current session.
	 * @return A reference to the currently open session or a null-reference, if there is no such.
	 */
	AppCore getSession() {
		return appCore;
	}

	/**
	 * Adds a label control which has a cosmetic functionality.
	 * @param container An assumed to be non-null reference to the parent container.
	 */
	private static void addFilePathLabel(Container container) {
		container.add(new JLabel("File: "), BorderLayout.WEST);
	}

	/**
	 * Adds a text field that will hold the path to the file used to open the current session.
	 * @param container A assumed to be non-null reference to the parent container.
	 * @return A non-null reference to the newly created control.
	 */
	private static JTextField addFilePath(Container container) {
		JTextField textField = new JTextField();

		textField.setEditable(false);

		container.add(textField, BorderLayout.CENTER);

		return textField;
	}

	/**
	 * Adds a button which handles the choice of a new file.
	 * @param container A assumed to be non-null reference to the parent container.
	 */
	private void addChooseFile(Container container) {
		JButton button = new JButton("Choose File");

		button.addMouseListener(GuiHandlers.chooseFileHandler(this));

		container.add(button, BorderLayout.EAST);
	}
}
