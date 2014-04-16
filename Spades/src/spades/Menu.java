package spades;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

//import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Menu extends JMenu{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4934526137119595826L;

	public Menu(){
		super();
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
	//	JCheckBoxMenuItem cbMenuItem;
	
		//Create the menu bar.
		menuBar = new JMenuBar();
	
		//Build the file menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);	// set corresponding keystroke to 'F'
		menu.getAccessibleContext().setAccessibleDescription(
		        "File Menu");
		menuBar.add(menu);
	
		//a group of JMenuItems
		menuItem = new JMenuItem("New Game",
		                         KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_N, ActionEvent.CTRL_MASK));		// ctrl n for new game
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "This doesn't really do anything yet");
		menu.add(menuItem);	
	}
}
