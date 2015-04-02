//Cameron D. - 3/16
//this is my first settings/configuration file reading program. I built it to test with this.
//properly implemented March 3/19/15
import java.io.*;
import java.util.ArrayList;

public class CamsSettingsReader {
	private static ArrayList < Boolean > settingsBools = new ArrayList < Boolean > (); //these arraylists need to be replaced with a hashmap...
	private static ArrayList < String > settingsStrs = new ArrayList < String > ();
	private static int totalSettings = 0;
	private static int currentLineLocation = 0;
	private static void processSettings(String nextLine) {
		currentLineLocation++;
		int nextLinePreferredLength = 0;
		if (nextLine.indexOf('=') != -1 && nextLine.indexOf('=', 0) != 0 && ((nextLine.substring(nextLine.indexOf('=', 0) + 1, nextLine.length()).contains("true")) || (nextLine.substring(nextLine.indexOf('=', 0) + 1, nextLine.length()).contains("false")))) {
			nextLinePreferredLength = (nextLine.contains("true") == true ? nextLine.indexOf("true") + 4 : (nextLine.contains("false") == true ? nextLine.indexOf("false") + 5 : 0));
			if (nextLinePreferredLength != 0) {
				settingsStrs.add(nextLine.substring(0, nextLine.indexOf('=', 0)));
				settingsBools.add(Boolean.valueOf(nextLine.substring(nextLine.indexOf('=', 0) + 1, nextLinePreferredLength)));
				totalSettings++;
			} else {
				System.out.println("Invalid setting found at line " + currentLineLocation + " due to boolean checking error, skipping it...");
			}
		} else {
			if (nextLine.equals("") || nextLine.indexOf('#') == 0) {
				if (nextLine.indexOf('=') == -1) System.out.println("Found " /*comment/blank*/ + (nextLine.equals("") ? "blank" : nextLine.indexOf('#') == 0 ? "comment" : "error...") + " line at line " + currentLineLocation + ", skipping - natural behavior");
			} else System.out.println("Warning: invalid setting found at line " + currentLineLocation + ", skipping it...");
		}
	}
	public Boolean getSetting(String str) {
		try {
			int index = settingsStrs.indexOf(str);
			return settingsBools.get(index);
		} catch (Exception e) {
			return null;
		}
	}
	public CamsSettingsReader() {
		BufferedReader filereader;
		FileInputStream filegetter = null;
		try {
			filegetter = new FileInputStream(new File("./bin/settings.txt"));
			filereader = new BufferedReader(new InputStreamReader(filegetter));
			String getLine = filereader.readLine();
			while (getLine != null) {
				processSettings(getLine);
				getLine = filereader.readLine();
			}
			System.out.println("Read " + totalSettings + " settings normally");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (filegetter != null) filegetter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}