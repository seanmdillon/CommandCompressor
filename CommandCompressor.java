import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

class Command {
	private String textIfNeeded, imgPath;
	boolean hasImage;
	JButton myButton;

	private Command(boolean bool, String imgPath, String returnable) {
		if (bool) {
			myButton = new JButton(new ImageIcon(imgPath));
			myButton.setBorderPainted(false);
		} else myButton = new JButton(returnable);
	}

	public static JButton getCommand(boolean bool, String imgPath, String textIfNeeded) {
		Command returner = new Command(bool, imgPath, textIfNeeded);
		return returner.myButton;
	}
}

public class CommandCompressor {

	// Graphical elements to make up the UI
	private JFrame parentFrame;
	private JLayeredPane controlPane;
	private JScrollPane commandPane;
	private JTextArea inputTextArea, outputTextArea;
	private JScrollPane inputScrollPane, outputScrollPane;
	private JButton convBtn, helpBtn, settingsBtn, cmdButton;  
	private JToggleButton backBtn, forwardBtn;
	private JLabel instructionsLabel;
	private ArrayList<JButton> commandButtons = new ArrayList<JButton>();
	
	private final int wHeight = 850;
	private final int wWidth = 750;

	private static final long serialVersionUID = 6928060005306615400L;

	// Settings class
	private static SettingsReader sr = new SettingsReader();
	private ArrayList <String> commands; // = deserializeHistory();

	private boolean labelHistory = sr.getSetting("LabelCommandsDefault");
	private int historyListSelection = 1;

	// necessary?
	// private Integer totalOutputCommands = 0;
	
	private void setupWindow() {
		parentFrame = new JFrame("CommandCompressor Deluxe Edition (for MC 1.8.1(?)-3) by PuzzlerBoy");
		parentFrame.setVisible(true);
		parentFrame.setResizable(true);
		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		parentFrame.setLayout(null);
		parentFrame.add(controlPane);
		parentFrame.getContentPane().add(commandPane);
		parentFrame.setBounds(new Rectangle(wWidth, wHeight));

		instructionsLabel = new JLabel("Press 'Convert...' to convert your text, and then manually select/copy it. Seperate commands with RETURN/ENTER.");
		instructionsLabel.setSize(instructionsLabel.getPreferredSize());
		instructionsLabel.setForeground(Color.white);
		instructionsLabel.setLocation((wWidth / 2) - (instructionsLabel.getWidth() / 2), 8);
	}

	private void setupControlPane() {
		controlPane = new JLayeredPane();
		controlPane.setPreferredSize(new Dimension(wWidth, wHeight));
		controlPane.setVisible(true);
		controlPane.setBounds(0, 0, wWidth, wHeight);

		JLabel imgLabel = new JLabel(null, new ImageIcon("./bin/bg.jpg"), JLabel.LEFT); //the second param image was a PITA
		imgLabel.setLocation(0, -20);
		imgLabel.setSize(imgLabel.getPreferredSize());

		controlPane.setLayout(null);
		controlPane.add(imgLabel, new Integer(1), 0);
		controlPane.add(instructionsLabel, new Integer(2));
		controlPane.add(inputScrollPane, new Integer(2));
		controlPane.add(outputScrollPane, new Integer(2));
		controlPane.add(convBtn, new Integer(2));
		controlPane.add(helpBtn, new Integer(2));
		controlPane.add(settingsBtn, new Integer(2));
		controlPane.add(backBtn, new Integer(2));
		controlPane.add(forwardBtn, new Integer(2));

		inputTextArea = new JTextArea(15, 30);
		inputTextArea.addKeyListener((KeyListener)this);
		inputScrollPane = new JScrollPane(inputTextArea);
		inputScrollPane.setLocation(5, 30);
		inputScrollPane.setSize(inputScrollPane.getPreferredSize());

		outputTextArea = new JTextArea(15, 30);
		outputTextArea.addKeyListener((KeyListener)this);
		outputScrollPane = new JScrollPane(outputTextArea);
		outputTextArea.setEditable(sr.getSetting("OutputEditableDefault"));
		outputTextArea.setLineWrap(true);
		outputScrollPane.setSize(outputScrollPane.getPreferredSize());
		outputScrollPane.setLocation(745 - outputScrollPane.getWidth(), 30);
	}
	
	private void setupControlButtons() {
		convBtn = new JButton(new ImageIcon("./bin/convert.png"));
		convBtn.setSize(convBtn.getPreferredSize());
		convBtn.setLocation(10, (int)(wHeight - (convBtn.getHeight() * 1.45)) - (wHeight - 350));
		convBtn.addActionListener((ActionListener)this);
		convBtn.setFocusable(false);
		convBtn.setBorderPainted(false);

		helpBtn = new JButton(new ImageIcon("./bin/help.png"));
		helpBtn.setSize(helpBtn.getPreferredSize());
		helpBtn.setLocation(wWidth - (10 + helpBtn.getWidth()), (int)(wHeight - (helpBtn.getHeight() * 1.45)) - (wHeight - 350));
		helpBtn.addActionListener((ActionListener) this);
		helpBtn.setFocusable(false);
		helpBtn.setBorderPainted(false);

		settingsBtn = new JButton(new ImageIcon("./bin/settings.png"));
		settingsBtn.setSize(settingsBtn.getPreferredSize());
		settingsBtn.setLocation(160, (int)(wHeight - (settingsBtn.getHeight() * 1.45)) - (wHeight - 350));
		settingsBtn.addActionListener((ActionListener)this);
		settingsBtn.setFocusable(false);
		settingsBtn.setBorderPainted(false);

		backBtn = new JToggleButton(new ImageIcon("./bin/back.png"));
		backBtn.setSize(backBtn.getPreferredSize());
		backBtn.setLocation(230, (int)(wHeight - (backBtn.getHeight() * 1.38)) - (wHeight - 350));
		backBtn.addActionListener((ActionListener)this);
		backBtn.setFocusable(false);
		backBtn.setBorderPainted(false);

		forwardBtn = new JToggleButton(new ImageIcon("./bin/forward.png"));
		forwardBtn.setSize(forwardBtn.getPreferredSize());
		forwardBtn.setLocation(300, (int)(wHeight - (backBtn.getHeight() * 1.38)) - (wHeight - 350));
		forwardBtn.addActionListener((ActionListener)this);
		forwardBtn.setFocusable(false);
		forwardBtn.setBorderPainted(false);
	}
	
	private void addCommand(boolean b, String cmdImage, String returnable) {
		if (b) {
			cmdButton = new JButton(new ImageIcon(cmdImage));
			cmdButton.setBorderPainted(false);
		} else {
			cmdButton = new JButton(returnable);
		}
		cmdButton.setToolTipText(returnable);
		
		// SOMETHING LIKE THIS?!
		commands.add(returnable);
	}
	
	private void addCommands() {
		addCommand(true, "./bin/cmd/achievement.png", "achievement");
		addCommand(true, "./bin/cmd/ban-ip.png", "ban-ip");
		addCommand(false, "", "banlist");
		addCommand(false, "", "blockdata");
		addCommand(false, "", "clear");
		addCommand(false, "", "clone");
		addCommand(false, "", "debug");
		addCommand(false, "", "defaultgamemode");
		addCommand(false, "", "deop");
		addCommand(false, "", "difficulty");
		addCommand(false, "", "effect");
		addCommand(false, "", "enchant");
		addCommand(false, "", "entitydata");
		addCommand(false, "", "execute");
		addCommand(false, "", "fill");
		addCommand(false, "", "gamemode");
		addCommand(false, "", "gamerule");
		addCommand(false, "", "give");
		addCommand(false, "", "help");
		addCommand(false, "", "kick");
		addCommand(false, "", "kill");
		addCommand(false, "", "list");
		addCommand(false, "", "me");
		addCommand(false, "", "op");
		addCommand(false, "", "pardon");
		addCommand(false, "", "pardon");
		addCommand(false, "", "particle");
		addCommand(false, "", "playsound");
		addCommand(false, "", "publish");
		addCommand(false, "", "replaceitem");
		addCommand(false, "", "save-all");
		addCommand(false, "", "save-off");
		addCommand(false, "", "save-on");
		addCommand(false, "", "say");
		addCommand(false, "", "scoreboard");
		addCommand(false, "", "seed");
		addCommand(false, "", "setblock");
		addCommand(false, "", "setidletimeout");
		addCommand(false, "", "setworldspawn");
		addCommand(false, "", "spawnpoint");
	}

	private void setupCommandPane() {
		JPanel commandButtonPanel = new JPanel(null);
		commandButtonPanel.setBounds(0, 0, 650, 650);
		commandButtonPanel.setBackground(Color.GREEN.darker());

		for (JButton button: commandButtons) {
			commandButtonPanel.add(button);
			button.setBounds((int)((commandButtons.indexOf(button) % 5 + 1) * 25) + ((commandButtons.indexOf(button) % 5) * 100), 
							 (int)((Math.floor(commandButtons.indexOf(button) / 5)) * (commandButtons.indexOf(button) + 115)) + 15, 100, 100);
			button.setFocusable(false);
			System.out.println("Index of button found:" + commandButtons.indexOf(button));
		}		

		JScrollPane commandScrollPane = new JScrollPane(commandButtonPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		commandScrollPane.setBounds(0, 0, 650, 650);
		commandScrollPane.setSize(commandScrollPane.getPreferredSize());
		commandScrollPane.add(commandButtonPanel, new Integer(2));
		
		commandScrollPane.setPreferredSize(new Dimension(650, 650));
		commandScrollPane.setLayout(null);
		commandScrollPane.setVisible(true);
		commandScrollPane.setBounds(50, 350, 650, 450);
	
		// setup the buttons here
		addCommands();
		
		// setup the pane, panel, etc.
		JPanel commandPanel = new JPanel(null);
		commandPanel.setBounds(0, 0, 650, 650);
		commandPanel.setBackground(Color.green.darker());

		for (JButton button: commandButtons) {
			commandPanel.add(button);
			button.setBounds((int)((commandButtons.indexOf(button) % 5 + 1) * 25) + ((commandButtons.indexOf(button) % 5) * 100), (int)((Math.floor(commandButtons.indexOf(button) / 5)) * (commandButtons.indexOf(button) + 115)) + 15, 100, 100);
			button.setFocusable(false);
			System.out.println("Adding button '" + commandButtons.indexOf(button) + "' to commandPanel.");
		}

		for (JButton button: commandButtons) {
			button.addActionListener((ActionListener)this);
		}

		for (String say: commands) {
			System.out.println(say);
		}
	}

	public CommandCompressor() {
		try {
			//prepareToSerializeHistory();
		} catch (Exception serialize) {
			serialize.printStackTrace();
		}

		setupWindow();
		setupControlPane();
		setupControlButtons();
		setupCommandPane();

		CamsClickCount.addClickCount(outputTextArea);

		if (commands.size() > 0) {
			outputTextArea.setText(commands.get(commands.size() - 1));
			historyListSelection = commands.size() - 1;
		}

		if (sr.getSetting("PromptLabelCommands")) {
			int promptname = JOptionPane.showConfirmDialog(new JLabel("Label operations?"), "Pick names for command compilations?", "", JOptionPane.YES_NO_OPTION);
			if (promptname == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Will ask for command labels. Please\nrestart the program to rechoose.");
				labelHistory = true;
			} else {
				JOptionPane.showMessageDialog(null, "Will not ask for command labels. Please\nrestart the program to rechoose.");
			}
		}
		//setHistoryOutput(1);

		parentFrame.repaint();
		parentFrame.validate();	
	}

	public static void main(String[] args) {
		new CommandCompressor();
	}
}
