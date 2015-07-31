package spades;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/*
 * you have a bunch of slowly closing in panels. biggest panel contains hands for each player. 
 * next layer is tricks that each player individually has. 
 * next layer is action card. 
 * And that's it for now! Worry about menus/keeping score later. 
 * 
 * TODO:
 * 
 * - add menus
 * 		- options to include:
 * 			save/load game?
 * 			add variations?
 * 		- help menu
 * 			rules
 * 			strategy
 * 			team info
 * 
 * - improve AI (nil bids, partnership, third and fourth play strategies)
 * 		- if partner bid nil, try to take tricks if partner is going to take it
 * 		- keep track of cards already played (especially aces)
 *				if the ace has been played already, lead with the king next if possible
 * 		- advanced strategies?
 * - 
 */

class Spades extends JFrame implements ActionListener {

	static private Player nPlayer, sPlayer, ePlayer, wPlayer;
	static private Player dealer = null;
	static private Scoreboard scoreboard;
	static private Bidder bidder;
	static private Spades gameWindow;
	static private Suit clubs, diamonds, hearts, spades;
	static private boolean spadesBroken = false;

	static private boolean showAllCards = false; // set true to see faces of all cards
	static private boolean blindEnabled = false;
	static private String startSuite;
	static private int turnCount, bidCount;
	static private ArrayList<Card> deck;
	static private ArrayList<Card> actionCards;

	// OUTER RIM
	private JPanel nCard = new JPanel();
	private JPanel sCard = new JPanel();
	private JPanel eCard = new JPanel();
	private JPanel wCard = new JPanel();

	// GOING IN
	private JLabel nTricks = new JLabel("0");
	private JLabel sTricks = new JLabel("0");
	private JLabel eTricks = new JLabel("0");
	private JLabel wTricks = new JLabel("0");

	private JPanel nTricksP = new JPanel();
	private JPanel sTricksP = new JPanel();
	private JPanel wTricksP = new JPanel();
	private JPanel eTricksP = new JPanel();

	private JPanel center1 = new JPanel();
	private JPanel center2 = new JPanel();

	private static JButton showButton = new JButton("show cards");
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu helpMenu;
	private static JMenuItem newGame;
	private JMenuItem toggleScore;
	private JMenuItem exit;

	private JMenuItem rules;
	private JMenuItem strategy;
	private JMenuItem about;

	private static final long serialVersionUID = -6232587218493030138L;
	
	public Spades() {
		getShowButton().addActionListener(this);
		setSize(1024, 700);
		setTitle("spades");
		setLayout(new BorderLayout());
		makeMenu();
		initialize();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		gameWindow = new Spades();
		newGameScreen();
	}
	
	public static int getTurnCount(){
		return turnCount;
	}
	
	
	public static boolean debugMode(){
		return isShowAllCards();
	}
	
	public static ArrayList<Card> getActionCards(){
		return actionCards;
	}
	
	public static boolean getSpadesBroken(){
		return spadesBroken;
	}
	
	public static void setSpadesBroken(boolean value){
		spadesBroken = value;
	}
	
	public static String getStartSuite() {
		return startSuite;
	}
	
	public static void setStartSuite(String suit){
		startSuite = suit;
	}

	private static void dealCards() {
		Card.reset();
		deck = new ArrayList<Card>(52);
		actionCards = new ArrayList<Card>();
		for (int i = 0; i < 52; i++)
			deck.add(new Card());

		Collections.shuffle(deck);

		for (int i = 0; i < 13; i++)
			nPlayer.add(deck.remove(0));
		nPlayer.sortHand();
		nPlayer.showHand();
		for (int i = 0; i < 13; i++)
			sPlayer.add(deck.remove(0));
		sPlayer.sortHand();
		sPlayer.showHand();
		for (int i = 0; i < 13; i++)
			ePlayer.add(deck.remove(0));
		ePlayer.sortHand();
		ePlayer.showHand();
		for (int i = 0; i < 13; i++)
			wPlayer.add(deck.remove(0));
		wPlayer.sortHand();
		wPlayer.showHand();

		nPlayer.revalidate();
		nPlayer.repaint();
	}

	private void initialize() {
		// // OUTER RIM
		// // GOING IN
		setResizable(false);
		center1.setLayout(new BorderLayout());
		wTricksP.setLayout(new GridLayout(1, 1));
		eTricksP.setLayout(new GridLayout(1, 1));
		nTricksP.add(nTricks);
		sTricksP.add(sTricks);
		eTricksP.add(eTricks);
		wTricksP.add(wTricks);
		center1.add(nTricksP, BorderLayout.NORTH);
		center1.add(sTricksP, BorderLayout.SOUTH);
		center1.add(eTricksP, BorderLayout.EAST);
		center1.add(wTricksP, BorderLayout.WEST);
		add(center1, BorderLayout.CENTER);

		// EVEN FURTHER IN
		center2.setLayout(new BorderLayout());
		center2.add(nCard, BorderLayout.NORTH);
		center2.add(sCard, BorderLayout.SOUTH);
		center2.add(eCard, BorderLayout.EAST);
		center2.add(wCard, BorderLayout.WEST);

		// center2.add(p, BorderLayout.CENTER);
		center1.add(center2, BorderLayout.CENTER);

		nPlayer = new Player("North", nCard, nTricks);
		sPlayer = new Player("South", sCard, sTricks);
		ePlayer = new Player("East", eCard, eTricks);
		wPlayer = new Player("West", wCard, wTricks);

		add(nPlayer, BorderLayout.NORTH);
		add(ePlayer, BorderLayout.EAST);
		add(wPlayer, BorderLayout.WEST);
		add(sPlayer, BorderLayout.SOUTH);

		ePlayer.setLayout(new GridLayout(1, 1));
		wPlayer.setLayout(new GridLayout(1, 1));

	}

	private void makeMenu() {
		// TODO Auto-generated method stub
		// create menu bar
		menuBar = new JMenuBar(); // Create the menu bar.
		menu = new JMenu("File"); // Build the file menu.
		menu.setMnemonic(KeyEvent.VK_F); // set corresponding keystroke to 'F'
		menu.getAccessibleContext().setAccessibleDescription("File Menu");

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.getAccessibleContext().setAccessibleDescription("Help Menu");

		menuBar.add(menu);
		menuBar.add(helpMenu);

		newGame = new JMenuItem("New Game", KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		newGame.getAccessibleContext().setAccessibleDescription(
				"Begin a new game");
		newGame.addActionListener(this);

		toggleScore = new JMenuItem("Show Scoreboard", KeyEvent.VK_H);
		toggleScore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		toggleScore.getAccessibleContext().setAccessibleDescription(
				"toggle scoreboard");
		toggleScore.addActionListener(this);

		exit = new JMenuItem("Exit", KeyEvent.VK_Q);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		exit.getAccessibleContext().setAccessibleDescription("Exit game");
		exit.addActionListener(this);

		menu.add(newGame);
		// menu.add(enableBlind);
		// menu.add(setScore);
		menu.add(toggleScore);
		// menu.add(showAll);
		menu.add(exit);

		rules = new JMenuItem("Rules", KeyEvent.VK_R);
		rules.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		rules.getAccessibleContext().setAccessibleDescription(
				"Rules of the game");
		rules.addActionListener(this);

		strategy = new JMenuItem("Strategy", KeyEvent.VK_T);
		strategy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		strategy.getAccessibleContext().setAccessibleDescription("");
		strategy.addActionListener(this);

		about = new JMenuItem("About", KeyEvent.VK_T);
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK)); // ctrl-n for new game
		about.getAccessibleContext().setAccessibleDescription(
				"About Spades.java");
		about.addActionListener(this);
		helpMenu.add(rules);
		helpMenu.add(strategy);
		helpMenu.add(about);
		this.setJMenuBar(menuBar);

	}


	

	public static Player winner() {
		Player NS, EW;
		Card[] cards = { nPlayer.getActionCard(), sPlayer.getActionCard(),
				ePlayer.getActionCard(), wPlayer.getActionCard() };
		evaluateCards(cards);
		if (nPlayer.getActionCard().compareTo(sPlayer.getActionCard()) > 0)
			NS = nPlayer;
		else
			NS = sPlayer;
		if (ePlayer.getActionCard().compareTo(wPlayer.getActionCard()) > 0)
			EW = ePlayer;
		else
			EW = wPlayer;
		if (EW.getActionCard().compareTo(NS.getActionCard()) > 0) {
			EW.addTrick();
			JOptionPane.showMessageDialog(new JFrame(), EW.getType() + " wins!");
			clearActionCards();
			EW.setCurrentPlayer(true);
			if (EW.getContract() + partner(EW).getContract() == EW.getNumTricks()
					+ partner(EW).getNumTricks()) {
				EW.setTrying(false);
				partner(EW).setTrying(false);
			}
			return EW;
		} else {
			NS.addTrick();
			JOptionPane.showMessageDialog(new JFrame(), NS.getType() + " wins!");
			clearActionCards();
			NS.setCurrentPlayer(true);
			if (NS.getContract() + partner(NS).getContract() == NS.getNumTricks()
					+ partner(NS).getNumTricks()) {
				NS.setTrying(false);
			}

			return NS;
		}
	}

	private static void evaluateCards(Card[] cards) {
		Suit suit = getSuit(Spades.startSuite);
		suit.roundsPlayed += 1;
		if (!Spades.startSuite.equals("spades"))
			for (int i = 0; i < 4; i++) { // check for empty suits in players
				if (!cards[i].getSuit().equals(Spades.startSuite)) {
					Suit s = getSuit(cards[i].getSuit());
					s.setPlayerEmpty(numberedPlayer(i));
				}
			}

		// then, lower the highest card count.
		for (int i = 0; i < 4; i++) {
			Suit s = getSuit(cards[i].getSuit());
			s.currValue += s.rankInComparison(cards[i].getValue());
		}

		Suit[] suits = { getClubs(), getHearts(), getDiamonds(), getSpades() };
		for (int i = 0; i < suits.length; i++) {
			switch (suits[i].currValue) {
			case 8:
				suits[i].reduceHighCard(1);
				break;
			case 12:
				suits[i].reduceHighCard(2);
				break;
			case 14:
				suits[i].reduceHighCard(3);
				break;
			case 15:
				suits[i].reduceHighCard(4);
				break;
			default:
				break;
			}
		}
	}

	public static Player numberedPlayer(int i) {
		switch (i) {
		case 0:
			return nPlayer;
		case 1:
			return sPlayer;
		case 2:
			return ePlayer;
		default:
			return wPlayer;
		}

	}

	public static Suit getSuit(String suit) {
		if (suit.equals("spades"))
			return getSpades();
		else if (suit.equals("clubs"))
			return getClubs();
		else if (suit.equals("hearts"))
			return getHearts();
		return getDiamonds();
	}

	public static void clearActionCards() {
		nPlayer.clearCard();
		sPlayer.clearCard();
		ePlayer.clearCard();
		wPlayer.clearCard();
	}

	public static Player partner(Player currPlayer) {
		if (currPlayer.getType().equals("East"))
			return wPlayer;
		else if (currPlayer.getType().equals("West"))
			return ePlayer;
		else
			return sPlayer;
	}

	public static Player nextPlayer(Player currPlayer) {
		if (currPlayer == sPlayer) {
			wPlayer.setCurrentPlayer(true);
			return wPlayer;
		}
		if (currPlayer == wPlayer) {
			nPlayer.setCurrentPlayer(true);
			return nPlayer;
		}
		if (currPlayer == nPlayer) {
			ePlayer.setCurrentPlayer(true);
			return ePlayer;
		} else {
			sPlayer.setCurrentPlayer(true);
			return sPlayer;
		}
	}

	private static void startPlay() {
		sPlayer.showCards();
		sPlayer.enableClicks();
		setClubs(new Suit("clubs"));
		setDiamonds(new Suit("diamonds"));
		setHearts(new Suit("hearts"));
		setSpades(new Suit("spades"));
		setTurnCount(0);
		Spades.spadesBroken = false;
		Player startingPlayer = nextPlayer(dealer);
		startingPlayer.setCurrentPlayer(true);
		if (!startingPlayer.isSouth()) {
			startingPlayer.computerPlay();
		}
	}

	private void reset() {
		// TODO Auto-generated method stub

		nPlayer.getActionCardLocation().removeAll();
		nPlayer.getActionCardLocation().revalidate();
		nPlayer.getActionCardLocation().repaint();

		this.remove(nPlayer);
		this.remove(sPlayer);
		this.remove(ePlayer);
		this.remove(wPlayer);

		clearActionCards();
		bidder.setVisible(false);
		scoreboard.setVisible(false);

		nPlayer = new Player("North", nCard, nTricks);
		sPlayer = new Player("South", sCard, sTricks);
		ePlayer = new Player("East", eCard, eTricks);
		wPlayer = new Player("West", wCard, wTricks);

		add(nPlayer, BorderLayout.NORTH);
		add(ePlayer, BorderLayout.EAST);
		add(wPlayer, BorderLayout.WEST);
		add(sPlayer, BorderLayout.SOUTH);

		ePlayer.setLayout(new GridLayout(1, 1));
		wPlayer.setLayout(new GridLayout(1, 1));
	}

	private static void newGame() {
		// scoreboard.dispose();

		// get random dealer
		Random gen = new Random();
		int plr = gen.nextInt(4);
		Player player = null;
		switch (plr) {
		case 0:
			player = nPlayer;
			break;
		case 1:
			player = sPlayer;
			break;
		case 2:
			player = ePlayer;
			break;
		case 3:
			player = wPlayer;
			break;
		}
		dealer = player;
		startRound();
	}

	static void startRound() {
		dealCards();
		if (isBlindEnabled()) {
			bidder.blindButton.setVisible(true);
			getShowButton().setVisible(true);
			sPlayer.hideCards();
			for (int i = 0; i < 14; i++)
				bidder.bids.getComponent(i).setEnabled(false);
		} else {
			sPlayer.showCards();
			bidder.blindButton.setVisible(false);
			getShowButton().setVisible(false);
			for (int i = 0; i < 14; i++)
				bidder.bids.getComponent(i).setEnabled(true);
		}
		startBidding(nextPlayer(dealer));
		// startBidding(sPlayer);
	}

	public static void showScore() {
		scoreboard.updateScore(bidder.evaluateContracts(nPlayer.getNumTricks(),
				sPlayer.getNumTricks(), ePlayer.getNumTricks(), wPlayer.getNumTricks()));
	}

	private static void startBidding(Player currPlayer) {
		sPlayer.disableClicks();
		bidCount = 0;
		bidder.reset();
		nPlayer.reset();
		sPlayer.reset();
		ePlayer.reset();
		wPlayer.reset();
		bidder.turnOn();

		simulateBidding(currPlayer);
	}

	// run bidding simulation before human bid.
	private static void simulateBidding(Player currPlayer) {

		if (bidCount >= 4) {
			bidder.turnOff();
			JOptionPane.showMessageDialog(new JFrame(),
					"Bidding round has finished.");
			nPlayer.revalidate();
			startPlay();
			return;
		} else {
			if (!currPlayer.isSouth()) {
				int bid = currPlayer.computerBid();
				bidder.setBid(currPlayer, bid, false); // computer will never
														// bid blind
				bidCount++;
				bidder.turnOn();
				bidder.update();
				currPlayer.setContract(bid);

				simulateBidding(nextPlayer(currPlayer));
				return;
			} else {
				bidder.turnOn();
				return;
			}
		}
	}

	// specifically for human player
	static void simulateBidding(int bid, boolean blind) {
		bidder.setBid(sPlayer, bid, blind);
		bidCount++;
		sPlayer.setContract(bid);
		bidder.update();
		simulateBidding(nextPlayer(sPlayer));
	}
	
	static void newGameScreen() {
		// final JFrame j = new JFrame();
		final JDialog j = new JDialog();
		j.setModal(true);
		j.setResizable(false);
		j.setAlwaysOnTop(true);
		j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		j.setSize(400, 300);
		j.setTitle("New Game");
		JLabel welcome = new JLabel(
				"Select settings for your new spades match and then click start.");
		JPanel scoring = new JPanel(new GridLayout(2, 1));
		final JTextField scoreRange = new JTextField("500");
		JLabel desc = new JLabel(
				"Enter score you want to play to (multiples of 10).");

		scoring.add(scoreRange);
		scoring.add(desc);

		final JCheckBox blindNil = new JCheckBox("Allow Blind Bids");
		final JCheckBox debugMode = new JCheckBox(
				"Debug Mode (Computer's cards can be seen)");

		JButton start = new JButton("Start");

		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int ss = 50;
				// bidder.setAlwaysOnTop(true);
				try {
					ss = Integer.parseInt(scoreRange.getText());
					if (ss % 10 == 0) {
						Scoreboard.setFinalScore(ss / 10);
					} else {
						JOptionPane.showMessageDialog(null,
								"Invalid scoring range");
						return;
					}
				} catch (Exception ex) {
					JOptionPane
							.showMessageDialog(null, "Invalid scoring range");
					return;
				}

				setBlindEnabled(blindNil.isSelected());
				// blindEnabled = true;
				setShowAllCards(debugMode.isSelected());
				scoreboard = new Scoreboard();
				bidder = new Bidder();
				j.dispose();
				newGame();

			}
		});

		j.setLayout(new GridLayout(1, 1));
		JPanel p = new JPanel(new GridLayout(5, 1));
		p.add(welcome);
		p.add(scoring);
		p.add(blindNil);
		p.add(debugMode);
		p.add(start);
		j.add(p);
		j.setVisible(true);

	}


	public static void nextDealer() {
		// TODO Auto-generated method stub
		dealer = nextPlayer(dealer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO make this correctly reset window and start a new game
		Object source = e.getSource();
		if (source == getShowButton()) {
			// show cards
			sPlayer.showCards();
			getShowButton().setVisible(false);
			bidder.blindButton.setVisible(false);
			for (int i = 0; i < 14; i++) {
				bidder.bids.getComponent(i).setEnabled(true);
			}
			Spades.bidder.turnOn();
		}
		if (source == newGame) {
			JDialog.setDefaultLookAndFeelDecorated(true);
			int response = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to quit current game?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				gameWindow.reset();
				newGameScreen();
			}

		}
		if (source == rules) {
			setInfoBox("Rules","textfiles/rules.txt");
		}
		if (source == strategy) {
			setInfoBox("Strategy","textfiles/strategy.txt");
		}	
		if(source == about){
			setInfoBox("About","textfiles/about.txt");
		}
		if (source == toggleScore) {
			toggleScore();
		}
		if (source == exit)
			exit();
	}
	
	private void setInfoBox(String title,String filename){
		try {
			JDialog ruleBox = new JDialog();
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			 
			  String str = "<html>";
				while ((strLine = br.readLine()) != null)   {
					  str += strLine + "<br>";
				  }
				str += "<br></html>";
				 JLabel l = new JLabel(str);
				JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				pane.getViewport().add(l);
				pane.setBounds(50,10,230,300);
				
				ruleBox.add(pane);
				ruleBox.setVisible(true);
				ruleBox.setSize(600,300);
				ruleBox.setAlwaysOnTop(true);
				ruleBox.setResizable(false);
				ruleBox.setTitle(title);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, filename + " not found.");
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(null, filename + " not found.");
		}
	}

	private void toggleScore() {
		scoreboard.setVisible(!scoreboard.isVisible());
		scoreboard.getDone().setVisible(false);
		scoreboard.getNewGame().setVisible(false);
		scoreboard.getClose().setVisible(true);
	}

	private void exit() {
		System.exit(0);
	}

	public static void setShowAllCards(boolean showAllCards) {
		Spades.showAllCards = showAllCards;
	}

	public static boolean isShowAllCards() {
		return showAllCards;
	}

	public static void setTurnCount(int turnCount) {
		Spades.turnCount = turnCount;
	}

	public static void setDiamonds(Suit diamonds) {
		Spades.diamonds = diamonds;
	}

	public static Suit getDiamonds() {
		return diamonds;
	}

	public static void setClubs(Suit clubs) {
		Spades.clubs = clubs;
	}

	public static Suit getClubs() {
		return clubs;
	}

	public static void setHearts(Suit hearts) {
		Spades.hearts = hearts;
	}

	public static Suit getHearts() {
		return hearts;
	}

	public static void setSpades(Suit spades) {
		Spades.spades = spades;
	}

	public static Suit getSpades() {
		return spades;
	}

	public static void setBlindEnabled(boolean blindEnabled) {
		Spades.blindEnabled = blindEnabled;
	}

	public static boolean isBlindEnabled() {
		return blindEnabled;
	}

	public static void setShowButton(JButton showButton) {
		Spades.showButton = showButton;
	}

	public static JButton getShowButton() {
		return showButton;
	}

	

}