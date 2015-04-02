/**
 * <PuzzlerBoy>
 * Command Compressor Deluxe (redone) by PuzzlerBoy
 * initially created 3/5/15
 *
 * I redid this to suit my personal taste and make it work correctly w/ 1.8(.3)
 * This is NOT a virus, don't ask me if it is, whatever is telling you it is is lying (if you got this
 * from a shady site and that's why you're asking, stop using the shady site. Lern2intrnt, scrublord)
 *
 * I'm fairly certain this code is sloppy but I'm not particularly excellent at this (doing this to learn, frankly) so don't judge me ;P
 *
 * (3/20) DANG this thing has grown into a monster, and it's only gonna get better hopefully. Looking to add
 * image-buttons that autofill commands or something cool like that, and to let you reference
 * other commands in the inputs to simplify things like crazy. Might even add a save as feature so you
 * can work on different projects.
 *
 * </PuzzlerBoy>
 *
 * Original Creator: dankydrank
 * Original version created ~Jan 11, 2014
 *
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class CamsCommandCompressor implements KeyListener, ActionListener, Serializable {

	private static final long serialVersionUID = 6928060005306615400L;
	public static final int wHeight = 850;
	public static final int wWidth = 750;
	private boolean labelHistory = sr.getSetting("LabelCommandsDefault");
	private JFrame frame;
	private JLayeredPane pane;

	CamsCommandSheet cs = new CamsCommandSheet();
	private JScrollPane commandSheet;
	public static ArrayList < String > commandClickables = new ArrayList < String > ();

	private static JTextArea input;
	private static JTextArea output;
	private Integer totalOutputCommands = 0;
	private static CamsSettingsReader sr = new CamsSettingsReader();
	private static ArrayList < String > commands = deserializeHistory();
	private static int historyListSelection = 1;
	private static JScrollPane outPane;

	private static JToggleButton back;
	private ImageIcon forwardimg = new ImageIcon("./bin/forward.png");
	private static JToggleButton forward;
	//private static String preserveInput;

	public static void main(String[] args) {
		new CamsCommandCompressor();
	}
	
	protected static void doubleClickOutput() {
		if (sr.getSetting("AllowToggleEdit")) {
			if (sr.getSetting("InquireToggleEdit")) {
				int decision = JOptionPane.showConfirmDialog(new JLabel("!"), "(can edit output pane:" + output.isEditable() + ")\nAre you sure you want to edit this?! You could \nmess up the command, and some edits \nmay accidentally not get saved!", "", JOptionPane.YES_NO_OPTION);
				if (decision == JOptionPane.YES_OPTION) {
					if (!output.isEditable()) JOptionPane.showMessageDialog(null, "Alright, you can edit the output pane. Double click the output area again to rechoose.");
					output.setEditable(true);
				} else {
					if (output.isEditable()) JOptionPane.showMessageDialog(null, "You may no longer edit the output pane. Double click the output area again to rechoose.");
					output.setEditable(false);
				}
			} else {
				if (!output.isEditable()) output.setEditable(true);
				else if (output.isEditable()) output.setEditable(false);
			}
		}
	}
	
	private static void setHistoryOutput(int o) {
		if ((o < historyListSelection && o > -1) || (o > historyListSelection && o < commands.size() + 1)) {
			if (o < historyListSelection || output.getText() != "") historyListSelection = o;

			output.setText("");
			if (o == commands.size()) System.out.println("o = cmd size");
			else if (!output.getText().equals("") && !input.getText().equals("") && o > commands.size() /*o==size+1*/ ) {
				JOptionPane.showMessageDialog(null, "Make sure to clear the input menu out before\n going to work on a new command.");
			} //
			if (o < commands.size()) output.setText("Paste this into a COMMAND BLOCK and run it via direct redstone contact:\n\n" + (commands.get(historyListSelection) != null ? commands.get(historyListSelection) : "") + (sr.getSetting("IncludeInputInOutput") && input.getText() == "" ? "\n\n[INPUT]\n\n" + input.getText() : ""));
			if (output.getText().equals("") && !input.getText().equals("") && historyListSelection == o) {
				System.out.println("this is the part where normally it'd auto back up");
			}
		}
	}
	
	private String getLabel() //this is used multiple times so it's needed sadly
	{
		if (labelHistory) return " (name: " + JOptionPane.showInputDialog(new JFrame("Give it a name!"), "Pick a name for this command segment -\n make sure it doesn't show up anywhere\n in the command!") + ")";
		else return "";
	}
	
	private static ArrayList < String > deserializeHistory() //this will also assign the inputs initially I think
	{
		if (sr.getSetting("UseSavedHistory")) {
			ObjectInputStream historyIn = null;
			try {
				historyIn = new ObjectInputStream(new FileInputStream("./bin/serialize.txt"));
				return (ArrayList < String > ) historyIn.readObject(); //T_T
			} catch (Exception e) {
				System.out.println("No serialized history object found - moving on...");
				return new ArrayList < String > ();
			} finally {
				try {
					historyIn.close();
				} catch (Exception e) {
					System.err.println("Had issue closing arraylist deserializer, this is no different than leaving the setting to use history off");
					return new ArrayList < String > ();
				}
			}
		} else return new ArrayList < String > ();
	}
	private static void prepareToSerializeHistory() throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (sr.getSetting("UseSavedHistory")) {
						FileOutputStream historyFileStream = new FileOutputStream("./bin/serialize.txt");
						ObjectOutputStream historyStream = new ObjectOutputStream(historyFileStream);
						historyStream.writeObject(commands);
						historyStream.flush();
						historyFileStream.close();
						historyStream.close();
					}
				} catch (Exception e) {
					System.err.println("Error serializing history");
				}
			}
		});
	}
	private void generate() {
		boolean userCancel = false;
		int dialog;
		String s = "summon MinecartCommandBlock ~ ~1 ~ {";
		String inputstr = "First command doesn't run, so I'm just a filler\n" + input.getText();
		String[] lines = inputstr.split("\n");
		int numCommands = inputstr.length() - inputstr.replace("\n", "").length();
		if (numCommands > 35) {
			dialog = JOptionPane.showConfirmDialog(new JLabel("Warning!"), "This is a long sequence and may \nleave minecart(s) behind - do you \nreally want to use it?", "", JOptionPane.YES_NO_OPTION);
			if (dialog != 0) userCancel = true;
		}
		if (userCancel) return;
		for (String temp: lines)
		if (temp != "\n") s += "Riding:{id:MinecartCommandBlock,";
		s += "Riding:{id:FallingSand,TileID:157,Time:1}";
		for (String temp: lines)
		if (temp != "\n") s += ",Command:" + temp + "}";
		s += ",Command:setblock ~ ~ ~ lava 7}";
		totalOutputCommands++;
		if (output.getText().equals("")) commands.add(new StringBuilder("[Command " + (commands.size() + 1) + (labelHistory ? getLabel() : "") + " (" + numCommands + " subcommands)]\n\n" + s) + (sr.getSetting("IncludeInputInOutput") ? "\n\n[INPUT]\n\n" + input.getText() : "").toString());
		else {
			if (sr.getSetting("VerifyHistoryReplace")) {
				int verreplace = JOptionPane.showConfirmDialog(null, "Are you sure you want to replace this \ncommand with the one you've specified?", "", JOptionPane.YES_NO_OPTION);
				if (verreplace == JOptionPane.YES_OPTION) {
					commands.set(historyListSelection, new StringBuilder("[Command " + (historyListSelection + 1) + (labelHistory ? getLabel() : "") + " (" + numCommands + " subcommands) (overwritten)]\n\n" + s) + (sr.getSetting("IncludeInputInOutput") ? "\n\n[INPUT]\n\n" + input.getText() : "").toString());
				}
			} else {
				commands.set(historyListSelection, new StringBuilder("[Command " + (historyListSelection + 1) + (labelHistory ? getLabel() : "") + " (" + numCommands + " subcommands) (overwritten)]\n\n" + s) + (sr.getSetting("IncludeInputInOutput") ? "\n\n[INPUT]\n\n" + input.getText() : "").toString());
			}
			setHistoryOutput(historyListSelection - 1);
		}
		output.setText("Paste this into a COMMAND BLOCK and run it via direct redstone contact:\n\n" + commands.get(commands.size() - 1));
		setHistoryOutput(historyListSelection + 1);
	}
	public CamsCommandCompressor() {
		try {
			prepareToSerializeHistory();
		} catch (Exception serialize) {
			serialize.printStackTrace();
		}
		JLabel instructions = new JLabel("Press 'Convert...' to convert your text, and then manually select/copy it. Seperate commands with RETURN/ENTER.");
		instructions.setSize(instructions.getPreferredSize());
		instructions.setForeground(Color.white);
		instructions.setLocation((wWidth / 2) - (instructions.getWidth() / 2), 8);

		ImageIcon copyimg = new ImageIcon("./bin/convert.png");
		JButton conv = new JButton(copyimg);
		conv.setSize(conv.getPreferredSize());
		conv.setLocation(10, (int)(wHeight - (conv.getHeight() * 1.45)) - (wHeight - 350));
		conv.addActionListener(this);
		conv.setFocusable(false);
		conv.setBorderPainted(false);

		ImageIcon helpimg = new ImageIcon("./bin/help.png");
		JButton help = new JButton(helpimg);
		help.setSize(help.getPreferredSize());
		help.setLocation(wWidth - (10 + help.getWidth()), (int)(wHeight - (help.getHeight() * 1.45)) - (wHeight - 350));
		help.addActionListener(this);
		help.setFocusable(false);
		help.setBorderPainted(false);

		ImageIcon settingimg = new ImageIcon("./bin/settings.png");
		JButton settings = new JButton(settingimg);
		settings.setSize(settings.getPreferredSize());
		settings.setLocation(160, (int)(wHeight - (settings.getHeight() * 1.45)) - (wHeight - 350));
		settings.addActionListener(this);
		settings.setFocusable(false);
		settings.setBorderPainted(false);

		ImageIcon backimg = new ImageIcon("./bin/back.png");
		back = new JToggleButton(backimg);
		back.setSize(back.getPreferredSize());
		back.setLocation(230, (int)(wHeight - (back.getHeight() * 1.38)) - (wHeight - 350));
		back.addActionListener(this);
		back.setFocusable(false);
		back.setBorderPainted(false);

		forward = new JToggleButton(forwardimg);
		forward.setSize(forward.getPreferredSize());
		forward.setLocation(300, (int)(wHeight - (back.getHeight() * 1.38)) - (wHeight - 350));
		forward.addActionListener(this);
		forward.setFocusable(false);
		forward.setBorderPainted(false);

		frame = new JFrame("CommandCompressor Deluxe Edition (for MC 1.8.1(?)-3) by PuzzlerBoy");
		frame.setVisible(true);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(wWidth, wHeight));
		pane.setVisible(true);
		pane.setBounds(0, 0, wWidth, wHeight);

		ImageIcon bg = new ImageIcon("./bin/bg.jpg"); //this image was a PITA
		JLabel imglabel = new JLabel(null, bg, JLabel.LEFT);
		imglabel.setLocation(0, -20);
		imglabel.setSize(imglabel.getPreferredSize());

		input = new JTextArea(15, 30);
		input.addKeyListener(this);
		JScrollPane inPane = new JScrollPane(input);
		inPane.setLocation(5, 30);
		inPane.setSize(inPane.getPreferredSize());

		output = new JTextArea(15, 30);
		output.addKeyListener(this);
		outPane = new JScrollPane(output);
		output.setEditable(sr.getSetting("OutputEditableDefault"));
		output.setLineWrap(true);
		outPane.setSize(outPane.getPreferredSize());
		outPane.setLocation(745 - outPane.getWidth(), 30);

		pane.setLayout(null);
		pane.add(imglabel, new Integer(1), 0);
		pane.add(instructions, new Integer(2));
		pane.add(inPane, new Integer(2));
		pane.add(outPane, new Integer(2));
		pane.add(conv, new Integer(2));
		pane.add(help, new Integer(2));
		pane.add(settings, new Integer(2));
		pane.add(back, new Integer(2));
		pane.add(forward, new Integer(2));

		commandSheet = cs.getPane();
		commandSheet.setPreferredSize(new Dimension(650, 650));
		commandSheet.setLayout(null);
		commandSheet.setVisible(true);
		commandSheet.setBounds(50, 350, 650, 450);
		for (JButton button: cs.commandButtons) {
			button.addActionListener(this);
		}

		for (String say: commandClickables)
		System.out.println(say);

		frame.setLayout(null);
		frame.add(pane);
		frame.getContentPane().add(commandSheet);
		frame.setBounds(new Rectangle(wWidth, wHeight));

		CamsClickCount.addClickCount(output);
		if (commands.size() > 0) {
			output.setText(commands.get(commands.size() - 1));
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

		frame.repaint();
		frame.validate();
	}
	public void actionPerformed(ActionEvent ayy) {
		forward.setSelected(false);
		back.setSelected(false);

		//System.out.println(ayy);

		String whichButton = "";
		for (int i = ayy.toString().indexOf("on javax.swing.JButton[,") + 24; i <= ayy.toString().indexOf(",alignmentX=") - 10; i++)
		whichButton += ayy.toString().charAt(i);
		if (whichButton.contains("10,")) //convert
		{
			if (input.getText().replace(" ", "").replace("\n", "").length() != 0) {
				generate();
			} else {
				output.setText("You need to type up some stuff to get converted! Type your commands in the box on the left.");
			}
		} else if (whichButton.contains("604,")) //help
		{
			File help = new File("./bin/help.txt");
			try {
				Desktop.getDesktop().edit(help);
			} catch (IOException iof) {
				iof.printStackTrace();
			}
		} else if (whichButton.contains("160,")) //settings
		{
			File settings = new File("./bin/settings.txt");
			try {
				Desktop.getDesktop().edit(settings);
			} catch (IOException iof) {
				iof.printStackTrace();
			}
		} else if (whichButton.contains("230,")) //back
		{
			setHistoryOutput(historyListSelection - 1);
		} else if (whichButton.contains("300,")) //forward
		{
			setHistoryOutput(historyListSelection + 1);
		} else //assume it's a member of the command sheet
		{
			System.out.println("Button from commandsheet pressed: " + commandClickables.get(CamsCommandSheet.commandButtons.indexOf(ayy.getSource())));
		}
		forward.setIcon(forwardimg);
		outPane.getVerticalScrollBar().setValue(outPane.getVerticalScrollBar().getMinimum());
	}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}