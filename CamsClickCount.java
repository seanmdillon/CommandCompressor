import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
//this is code from some intelligent person, edited to supplement this program. See http://stackoverflow.com/questions/4577424/distinguish-between-a-single-click-and-a-double-click-in-java
//class file added 3/9/15
public class CamsClickCount extends MouseAdapter implements ActionListener {
	private final static int clickInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
	MouseEvent lastEvent;
	Timer timer;
	private CamsClickCount() {
		this(clickInterval);
	}
	private CamsClickCount(int delay) {
		timer = new Timer(delay, this);
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 2) return;

		lastEvent = e;

		if (timer.isRunning()) {
			timer.stop();
			doubleClick(lastEvent);
		} else {
			timer.restart();
		}
	}
	public void singleClick(MouseEvent e) {}
	public void doubleClick(MouseEvent e) {}
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		singleClick(lastEvent);
	}
	public static void addClickCount(JTextArea area) {
		//JFrame frame = new JFrame( "Double Click Test" );
		//frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		area.addMouseListener(new CamsClickCount() {
			public void doubleClick(MouseEvent e) {
				CamsCommandCompressor.doubleClickOutput();
			}
		});
	}
}