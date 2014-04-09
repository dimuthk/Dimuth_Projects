import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.Border;


public class PokemonGame extends JFrame{
	private static final long serialVersionUID = 1L;
	Player player1, player2, currPlayer;
	static PokemonGame game;
	boolean toxicGas = false;
	boolean applyWAndR = false;
	
	public PokemonGame(){
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300,300);
		initializeGame();
		setLayout(new GridLayout(2,1));
	}
	
	void initializeGame(){
		player1 = new Player("North", makeDeck(), false);
		player2 = new Player("South", makeDeck2(), true);
		player1.opponent = player2;
		player2.opponent = player1;
		pickActivePlayer1();
	}
	
	void pickActivePlayer1(){
		player1Orient();
		player1.benchAndActiveCardPanel.playerTurn = true;
		JOptionPane.showMessageDialog(null, "Pick your active pokemon " + player1.name);
	}
	
	void pickActivePlayer2(){
		player1.benchAndActiveCardPanel.playerTurn = false;
		player2.benchAndActiveCardPanel.playerTurn = true;
		player2Orient();
		JOptionPane.showMessageDialog(null, "Pick your active pokemon " + player2.name);
	}
	
	void player1Orient(){
		currPlayer = player1;
		player1.setOrientation(false); //false means you are current player...
		player2.setOrientation(true);
		remove(player1);
		remove(player2);
		add(player2);
		add(player1);
		revalidate();
		repaint();
	}
	
	void player2Orient(){
		currPlayer = player2;
		player2.setOrientation(false);
		player1.setOrientation(true);
		remove(player1);
		remove(player2);
		add(player1);
		add(player2);
		revalidate();
		repaint();
	}
	
	void player1StartTurn(){
		JOptionPane.showMessageDialog(null, player1.name + " turn");
		try{
			player1.benchAndActiveCardPanel.addCardToHand(player1.deckAndDiscardPanel.deck.remove(0));
			player1.benchAndActiveCardPanel.addCardToHand(player1.deckAndDiscardPanel.deck.remove(0));
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, player1.name + " has run out of cards. " + player2.name + " wins!");
			return;
		}
		
		player1Orient();		
		activePokemonSettings(player1);
		player1.benchAndActiveCardPanel.playerTurn = true;
		player2.benchAndActiveCardPanel.playerTurn = false;
	}
	
	void player2StartTurn(){
		JOptionPane.showMessageDialog(null, player2.name + " turn");
		try{
			player2.benchAndActiveCardPanel.addCardToHand(player2.deckAndDiscardPanel.deck.remove(0));
			player2.benchAndActiveCardPanel.addCardToHand(player2.deckAndDiscardPanel.deck.remove(0));
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, player2.name + " has run out of cards. " + player1.name + " wins!");
			return;
		}
		player2Orient();
		activePokemonSettings(player2);
		player2.benchAndActiveCardPanel.playerTurn = true;
		player1.benchAndActiveCardPanel.playerTurn = false;
	}
	/*
	 * COME BACK TO DODUO/DODRIO
	 */
	ArrayList<Card> makeDeck(){
		ArrayList<Card> deck = new ArrayList<Card>();
		for(int i=0; i<10; i++){
			deck.add(new Squirtle());
		}
		for(int i=0; i<7; i++){
			deck.add(new Wartortle());
		}
		for(int i=0; i<7; i++){
			deck.add(new PokeBall());
		}
		for(int i=0; i<10; i++){
			deck.add(new MrFuji());
		}
		for(int i=0; i<10; i++){
			deck.add(new WaterEnergy());
		}
		return deck;
	}
	
	ArrayList<Card> makeDeck2(){
		ArrayList<Card> deck = new ArrayList<Card>();
		for(int i=0; i<15; i++){
			deck.add(new Rattata());
		}
		for(int i=0; i<6; i++){
			deck.add(new Raticate());
		}
		for(int i=0; i<6; i++){
			deck.add(new DoubleColorlessEnergy());
		}
		for(int i=0; i<5; i++){
			//deck.add(new Weezing());
		}
		for(int i=0; i<14; i++){
			deck.add(new Lass());
		}
		return deck;
	}
	
	void activePokemonSettings(Player player){
		try{
			PokemonCard.dittoWrapException();
			PokemonCard pc = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
			if(pc.poisoned == true){
				JOptionPane.showMessageDialog(null, pc.name + " took damage from poisoning!");
				pc.takeDamage(player,10);
			}
			else if(pc.toxic == true){
				JOptionPane.showMessageDialog(null, pc.name + " took damage from poisoning!");
				pc.takeDamage(player,20);
			}
			pc.agility = false;
			pc.powerPerformed = false;
		
			for(int i=0; i< player.benchAndActiveCardPanel.benchSize; i++){
				PokemonCard p = (PokemonCard) player.benchAndActiveCardPanel.bench[i];
				p.powerPerformed = false;
			}
			pc.shield = 0;
			
		}
		catch(Exception e){}
	}
	
	boolean toxicGasException(){ //the Muk exception
		PokemonCard pc;
		for(int i=0; i<player1.benchAndActiveCardPanel.benchSize; i++){
			pc = (PokemonCard) player1.benchAndActiveCardPanel.bench[i];
			if(pc.pokeId == 89){
				return true;
			}
		}
		try{
			pc = (PokemonCard) player1.benchAndActiveCardPanel.activeCard;
			if(pc.pokeId == 89 && pc.status == Card.NONE){
				return true;
			}
		}
		catch(Exception e) {}
		for(int i=0; i<player2.benchAndActiveCardPanel.benchSize; i++){
			pc = (PokemonCard) player2.benchAndActiveCardPanel.bench[i];
			if(pc.pokeId == 89){
				return true;
			}
		}
		try{
			pc = (PokemonCard) player2.benchAndActiveCardPanel.activeCard;
			if(pc.pokeId == 89 && pc.status == Card.NONE){
				return true;
			}
		}
		catch(Exception e) {}
		return false;
	}
	
	boolean prehistoricPowerException(){
		PokemonCard pc;
		for(int i=0; i<player1.benchAndActiveCardPanel.benchSize; i++){
			pc = (PokemonCard) player1.benchAndActiveCardPanel.bench[i];
			if(pc.pokeId == 142){
				return true;
			}
		}
		pc = (PokemonCard) player1.benchAndActiveCardPanel.activeCard;
		if(pc.pokeId == 142 && pc.status == Card.NONE){
			return true;
		}
		for(int i=0; i<player2.benchAndActiveCardPanel.benchSize; i++){
			pc = (PokemonCard) player2.benchAndActiveCardPanel.bench[i];
			if(pc.pokeId == 142){
				return true;
			}
		}
		pc = (PokemonCard) player2.benchAndActiveCardPanel.activeCard;
		if(pc.pokeId == 142 && pc.status == Card.NONE){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		game = new PokemonGame();
	}
}



abstract class Card extends JLabel{
	

	private static final long serialVersionUID = 1L;
	final static int FIRE = 0, WATER = 1, LEAF = 2,
	FIGHTING = 3, PSYCHIC = 4, THUNDER = 5, COLORLESS = 6, NONE = 7;
	final static int DECK = 0, PRIZE = 1, BENCH = 2, HAND = 3, ACTIVE = 4, DISCARD = 5;
	final static int BASIC_POKEMON = 0, STAGE_1_POKEMON =1, STAGE_2_POKEMON = 2, ENERGY = 3, TRAINER = 4;
	final static int NO_STATUS=0, CONFUSED=1, ASLEEP=2 ,PARALYZED=3;
	//ImageIcon front,back;
	ImageIcon front, bigFront; 
	ImageIcon back, bigBack;
	Popup popup;
	boolean masquerade = false;
	String path;
	int location, cardType; //where on the gameboard is it?
	void setCardImage(String path){
		this.path = path;
		Border paddingBorder = BorderFactory.createEmptyBorder(5,5,5,5);
		setBorder(BorderFactory.createCompoundBorder(getBorder(),paddingBorder));

		java.net.URL imageURL = PokemonGame.class.getResource("images/back.jpg");
		
		if (imageURL != null) {
		    bigBack = new ImageIcon(imageURL);
		    back = new ImageIcon();
		}
		Image image = bigBack.getImage().getScaledInstance(65, 90, Image.SCALE_SMOOTH);
		back.setImage(image);
		//image = bigBack.getImage().getScaledInstance(300, 500, Image.SCALE_SMOOTH);
		//bigBack.setImage(image);
		
		imageURL = PokemonGame.class.getResource("images/" + path);
		
		if (imageURL != null) {
		    bigFront = new ImageIcon(imageURL);
		    front = new ImageIcon();
		}
		Image image2 = bigFront.getImage().getScaledInstance(65, 90, Image.SCALE_SMOOTH);
		front.setImage(image2);
		image2 = bigFront.getImage().getScaledInstance(225, 300, Image.SCALE_SMOOTH);
		bigFront.setImage(image2);
	}
	
	boolean isAPokemon(){
		return cardType == BASIC_POKEMON ||cardType == STAGE_1_POKEMON ||cardType == STAGE_2_POKEMON;
	}
	
	
	void showPopup(){
		PopupFactory factory = PopupFactory.getSharedInstance();
		popup = factory.getPopup(null, this, 0, 0);
		popup.show();
		//popup.hide();
	}
	
	void hidePopup(){
		popup.hide();
	}
	
	void setLocation(int i){
		location = i;
	}
	
	void setCardType(int i){
		cardType = i;
	}
	
	void showFront(){
		setIcon(front);
	}
	
	void showBack(){
		setIcon(back);
	}
	
	void showUpClose(){
		JDialog d = new JDialog();
		d.setSize(500,400);
		d.setModal(true);
		d.setResizable(false);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setLayout(new BorderLayout());
		d.setLocationRelativeTo(PokemonGame.game);
		JLabel l = new JLabel();
		l.setIcon(bigFront);
		JPanel p1 = new JPanel();
		p1.add(l);
		
		d.add(p1);
		d.setVisible(true);
	}

}




class PlaceHolder extends Card {
	private static final long serialVersionUID = 1L;

	public PlaceHolder(){
		setCardImage("placeHolder.png");
	}

}

class DummyCard extends Card {
	private static final long serialVersionUID = 1L;
	String name;
	int value, value2;
	Card originalCard;
	public DummyCard(Card pc){
		setCardImage(pc.path);
		showFront();
		originalCard = pc;
	}

	public DummyCard(Card pc, String cName){
		setCardImage(pc.path);
		name = cName;
		showFront();
		originalCard = pc;
	}
	
	public DummyCard(Card pc, int cValue){
		setCardImage(pc.path);
		value = cValue;
		showFront();
		originalCard = pc;
	}
	
	public DummyCard(Card pc, int cValue, int cValue2){
		setCardImage(pc.path);
		value = cValue;
		value2 = cValue2;
		showFront();
		originalCard = pc;
	}

	
}

