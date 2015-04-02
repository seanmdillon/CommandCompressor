import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelExcept­ion;

/**
 *
 * @author Vincent
 */
public class ButtonScrollBar {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		//UIManager.setLookAndFeel(UIManager.getSy­stemLookAndFeelClassName());
		JFrame window = new JFrame("Button Scroll Bar");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		panel_1.setBackground(Color.gray);
		window.add(BorderLayout.NORTH, panel_1);

		final JScrollPane scroll_1 = new JScrollPane(panel_1, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JScrollPane scroll_2 = new JScrollPane(panel_2, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//final JScrollPane scroll_1 = new JScrollPane(panel_1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//final JScrollPane scroll_2 = new JScrollPane(panel_2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panel_1.add(new JButton("Button 1"));
		panel_1.add(new JButton("Button 2"));
		panel_1.add(new JButton("Button 3"));
		panel_1.add(new JButton("Button 4"));
		panel_1.add(new JButton("Button 5"));
		panel_1.add(new JButton("Button 6"));
		panel_2.add(new JButton("Button 1"));
		panel_2.add(new JButton("Button 2"));
		panel_2.add(new JButton("Button 3"));
		panel_2.add(new JButton("Button 4"));
		panel_2.add(new JButton("Button 5"));
		panel_2.add(new JButton("Button 6"));
		GridLayout griddy = new GridLayout(2, 0);
		window.setLayout(griddy);
		window.add(BorderLayout.NORTH, scroll_1);
		window.add(BorderLayout.SOUTH, scroll_2);
		window.setSize(300, 190);
		window.setVisible(true);
		//window.setLocationRelativeTo(null);
	}
}