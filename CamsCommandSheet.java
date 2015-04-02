import java.awt.*;
import java.util.*;
import javax.swing.*;

class CamsCommand {
	String textIfNeeded;
	String imgPath;
	boolean hasImage;
	JButton myButton;
	private CamsCommand(boolean bool, String imgPath, String returnable) {
		if (bool) {
			myButton = new JButton(new ImageIcon(imgPath));
			myButton.setBorderPainted(false);
		} else myButton = new JButton(returnable);
	}
	public static JButton getComm(boolean bool, String imgPath, String textIfNeeded) {
		CamsCommand returner = new CamsCommand(bool, imgPath, textIfNeeded);
		return returner.myButton;
	}
}

public class CamsCommandSheet {
	public static ArrayList < JButton > commandButtons = new ArrayList < JButton > ();
	//private static Comm com;
	private static JScrollPane pane;
	private static void addCommand(boolean bool, String imgPath, String returnable) {
		//commandButtons.add(Command.getComm(bool, imgPath, returnable));
		CamsCommandCompressor.commandClickables.add(returnable);
	}
	private static JScrollPane addCommands() {
		//add the commands...
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

		JPanel returnerInside = new JPanel(null);
		returnerInside.setBounds(0, 0, 650, 650);
		returnerInside.setBackground(Color.GREEN.darker());
		for (JButton button: commandButtons) {
			returnerInside.add(button);
			button.setBounds((int)((commandButtons.indexOf(button) % 5 + 1) * 25) + ((commandButtons.indexOf(button) % 5) * 100), (int)((Math.floor(commandButtons.indexOf(button) / 5)) * (commandButtons.indexOf(button) + 115)) + 15, 100, 100);
			button.setFocusable(false);
			//button.setLocation(); //15 15
			//button.setToolTipText("Help text for the button");
			System.out.println("Index of button found:" + commandButtons.indexOf(button));
		}
		JScrollPane returner = new JScrollPane(returnerInside, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		returner.setBounds(0, 0, 650, 650);
		returner.setSize(returner.getPreferredSize());
		returner.add(returnerInside, new Integer(2));
		for (JButton button: commandButtons) {
			button.setToolTipText(CamsCommandCompressor.commandClickables.get(commandButtons.indexOf(button)));
			System.out.println("set tool tip text of " + button.getLabel() + " to " + button.getToolTipText());
		}
		return returner;
	}

	public CamsCommandSheet() {
		pane = addCommands();
	}

	public JScrollPane getPane() {
		return pane;
	}
}