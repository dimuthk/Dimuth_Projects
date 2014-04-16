package spades;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

class Player extends JPanel {
	

	private static final long serialVersionUID = 1L;

	private String type;
	private ArrayList<Card> hand;
	private Card actionCard, placeHolder;
	
	private JPanel actionCardLocation;
	private JLabel trickNumLocation;
	
	private int numTricks =-1;
	private int contract;
	
	private boolean human = false;
	private boolean currentPlayer = false;
	private boolean trying = true;	// trying to take tricks

	public Player(String type, JPanel NactionCardLocation, JLabel NtrickNumLocation) {
		super();
		hand = new ArrayList<Card>();
		this.setType(type);
		if(type.equals("South")) human = true;
		this.setActionCardLocation(NactionCardLocation);
		this.trickNumLocation = NtrickNumLocation;
		Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		trickNumLocation.setBorder(BorderFactory.createCompoundBorder(getBorder(),paddingBorder));

		placeHolder = new Card(1);
		placeHolder.showFront();
		
		/*if (getType().equals("East")) {
			placeHolder.rotateImage(placeHolder, placeHolder.getCurr(), 90);
		} else if (getType().equals("West")) {
			placeHolder.rotateImage(placeHolder, placeHolder.getCurr(), 270);
		}*/
		
		getActionCardLocation().removeAll();
		getActionCardLocation().add(placeHolder);
		getActionCardLocation().revalidate();
		getActionCardLocation().repaint();
	}


	boolean isNorth(){
		return getType().equals("North");
	}
	
	boolean isEast(){
		return getType().equals("East");
	}
	
	boolean isWest(){
		return getType().equals("West");
	}
	
	boolean isSouth(){
		return getType().equals("South");
	}
	
	void callNextMove(){
		Player nextPlayer;
		if(Spades.getTurnCount() == 52){ //round finished
			Spades.winner();
			Spades.showScore();
			return;
		}
		if(Spades.getTurnCount()%4 == 0){
			
			nextPlayer = Spades.winner();
		}
		else{
			nextPlayer =Spades.nextPlayer(this);
		}
		
		nextPlayer.setCurrentPlayer(true);
		if(!nextPlayer.getType().equals("South")) {
			nextPlayer.computerPlay();
		}
		else{ //curr player is east
			
		}
	}
	
	void reset(){
		setNumTricks(0);
		setContract(0);
		trickNumLocation.setText("0");
		trickNumLocation.revalidate();
	}
	
	void setContract(int contract){
		this.contract = contract;
		setNumTricks(0);
		trickNumLocation.setText("Current Tricks: " + getNumTricks() + " || Contract: " + contract);
		trickNumLocation.revalidate();
	}

	void sortHand(){
		for(int i=0; i<hand.size();i++){
			for(int j=0;j<hand.size();j++){
				if(j<i&&hand.get(i).comesBefore(hand.get(j))){
					Card temp = hand.get(i);
					hand.remove(i);
					hand.add(i,hand.get(j));
					hand.remove(j);
					hand.add(j,temp);
				}
			}
		}
	}
	

	
	void addTrick(){
		setNumTricks(getNumTricks() + 1);
		trickNumLocation.setText("Current Tricks: " + getNumTricks() + " || Contract: " + getContract());
		trickNumLocation.revalidate();
	}
	
	boolean isHuman(){
		return human;
	}
	
	
	void add(Card c) {
		hand.add(c);
		c.setOwner(this);
		if(!Spades.debugMode())
			c.showBack();	// normal play
		else
			c.showFront();	// see all cards
		/*if (getType().equals("East")) {
			c.rotateImage(c, c.getCurr(), 90);
		} else if (getType().equals("West")) {
			c.rotateImage(c, c.getCurr(), 270);
		}*/
	}
	
	/*
	 * Visually displays the hand to the user. 
	 */
	void showHand(){
		if(human){
			for(int i=0;i<hand.size();i++){
				super.add(hand.get(i));
			}
		}
		else if(Spades.debugMode()){
			super.removeAll();
			String club="\u2663: ",diamond="\u2666: ",heart="\u2764: ",spade="\u2660: ";
			for(int i=0;i<hand.size();i++){
				
				Card c = hand.get(i);
				if(c.getSuit().equals("clubs")){
					club += " " + c.getValueString();
				}
				if(c.getSuit().equals("diamonds")){
					diamond += " " + c.getValueString();
				}
				if(c.getSuit().equals("hearts")){
					heart += " " + c.getValueString();
				}
				if(c.getSuit().equals("spades")){
					spade += " " + c.getValueString();
				}
			}
				String result = "<html>" + club + "<br>" + diamond + "<br>" + heart + "<br>" + spade + "</html>";
				JLabel res = new JLabel(result);
			
				super.add(res);
				super.revalidate();
				super.repaint();
				/*Card c = hand.get(i);
				c.showFront();
				super.add(c);*/
			
		}
		else{
			super.removeAll();
			Card background = new Card(0);
			background.showBack();
			super.add(background);
		}
	}

	void clearAll(){
		this.remove(placeHolder);
	}
	
	void showCards(){
		// display front of cards
		for(int i=0;i<hand.size();i++){
			hand.get(i).showFront();
		}
	}
	

	public void hideCards() {
		// display back of cards
		for(int i=0;i<hand.size();i++){
			hand.get(i).showBack();
		}

	}
	
	boolean hasSuit(String suit){
		for(int i =0; i<hand.size();i++){
			if(hand.get(i).getSuit().equals(suit)) return true;
		}
		return false;
	}
	
	void playCard(Card c){
			if(isCurrentPlayer()){
				//you can choose suit
				if(!c.getSuit().equals(Spades.getStartSuite())){	// if suit to play is not equal to start suit
					if(Spades.getTurnCount()%4 != 0){		// if not leading
						for(int i=0; i<hand.size();i++){
							if(hand.get(i).getSuit().equals(Spades.getStartSuite())){		// if he has the start suit
								JOptionPane.showMessageDialog(new JFrame(), "You must follow suit.");
								return;
							}
						}
					}
					else{
						Spades.setStartSuite(c.getSuit());// leader chooses suit
						Spades.getActionCards().clear();//clear list of showing cards
					}
					
				}	
				
				if(c.getSuit().equals("spades")&&(!Spades.getSpadesBroken())){
					//card to lead is a spade, spades not broken
					if(hasSuit(Spades.getStartSuite())&&(numSuit("spades")!=hand.size())){
						//
						JOptionPane.showMessageDialog(new JFrame(), "Spades has not been broken.");
						return;
					}
					else Spades.setSpadesBroken(true);
				}
			
				
				
				setActionCard(c);
				
				if(human){
					this.remove(getActionCard());
					revalidate();
					repaint();
				}
				if(Spades.isShowAllCards()){
					showHand();
				}
				
				hand.remove(getActionCard());
				
				getActionCard().showFront();
				
				getActionCardLocation().remove(placeHolder);
					this.getActionCardLocation().add(getActionCard());
					this.getActionCardLocation().revalidate();
					this.getActionCardLocation().repaint();
					
					
				
				Spades.getActionCards().add(getActionCard());
				setCurrentPlayer(false);
				Spades.setTurnCount(Spades.getTurnCount() + 1);
				callNextMove();	
				
				
				
		}
	}
	
	
	Card getHighCard(String suit){
		Card curr = null;
		for(int i=hand.size()-1; i>=0;i--){
			if(hand.get(i).getSuit().equals(suit)){ 	// if card is right suit
				curr = hand.get(i);				// select card
			}			
		}
		return curr;
	}

	Card getLowCard(String suit){
		Card curr = null;
		for(int i=0; i<hand.size();i++){
			if(hand.get(i).getSuit().equals(suit)) curr = hand.get(i);
		}
		return curr;
	}

	int computerBid(){
		// TODO: advanced strategies would change bid depending on position, short and long suits, and a low number of spades
		int spadeCount=0;
		int highCardCount =0;
		Card c;
		for(int i=0; i<hand.size();i++){
			c = hand.get(i);
			if(c.spade()){
				spadeCount++;
				if(c.highSpade()) highCardCount++;
			}
			else{
				if(c.highCard()) highCardCount++;
			}
		}
		if(spadeCount < 3) spadeCount = 3;
		if(highCardCount+(spadeCount-3)>0)
			setTrying(true);
		else
			setTrying(false);
		return highCardCount + (spadeCount - 3);
	}
	
	void clearCard(){
		getActionCardLocation().removeAll();
		getActionCardLocation().add(placeHolder);
		getActionCardLocation().revalidate();
		getActionCardLocation().repaint();
	}
	
	int numSuit(String suit){
		int count = 0;
		for(int i=0; i<hand.size();i++){
			if(hand.get(i).getSuit().equals(suit)) count++;
		}
		return count;
	}
	
	void computerPlay(){
		// TODO: add third and fourth strategies, including:
		//	partner nil and showing high card; take trick regardless of bags if partner shows highest card
		//  if going last, sometimes let partner take the trick, or throw away highest card
		//  (depending on number of tricks still needed)
		Card c = hand.get(0);
		if(isTrying()){
			if(Spades.getTurnCount()%4 == 0) // starting the turn
				c = firstPlayStrat();
			else// if(Spades.turnCount%4 == 1){ // going second
				c = secondPlayStrat();
		}else
			c = nilStrat();
		playCard(c);
	}
	
	private Card nilStrat() {
		Card c = hand.get(0);
		if(Spades.getTurnCount()%4==0)
			c=getLowest();			//play lowest card when going first
		else
			c=getHighestLosing();	//else play highest losing card
		return c;
	}


	private Card getHighestLosing() {	// tries to find highest card that will still lose
		Card c = hand.get(0);
		Card cardPlayed = Spades.getActionCards().get(0);
		if(numSuit(Spades.getStartSuite())>0)
		{											// if player has start suit
			c=getLowCard(Spades.getStartSuite());				// set lowest as card to play
			
			if (cardPlayed.compareTo(c)<0)
			{ 												// if lowest card will still win (so far)
				if(Spades.getTurnCount()%4==3)
						c=getHighCard(Spades.getStartSuite());	// and going last, play highest card
			}
			else
			{													// if lowest card won't win
				for(int j=0;j<hand.size();j++){					// check each card in hand
					if (hand.get(j).getSuit()==Spades.getStartSuite()){	// if right suit
						if (cardPlayed.compareTo(c)>0 && hand.get(j).compareTo(c)>0) {
						// 		card will not win,	  and 	is higher than last card
							c = hand.get(j);					// set as new card to play
						}
					}
				}
			}
		}else{										// player is void in start suit
			c = getLowest();
			if (cardPlayed.compareTo(c)<0){ 		// if lowest card will win (so far)
				if(Spades.getTurnCount()%4==3)
						c=getHighest();					// when going last, drop highest card
			}
			else
			{													// if lowest card won't win
				for(int j=0;j<hand.size();j++){					// check each card in hand
					if (cardPlayed.compareTo(c)>0){				// if card will not win	
						
						if(hand.get(j).getValue()>c.getValue() && c.getSuit()!="spades")		// if higher value card, c not spades
							c = hand.get(j);					// set as new card

						if(hand.get(j).getSuit()=="spades" && hand.get(j).compareTo(c)>0)	// or is spades, and beats c
							c = hand.get(j);					// set as new card to play
					}
				}
			}
		}
		return c;		
	}


	boolean updateChoice(int priority, Card newCard, int currPriority, Card currentCard){
		if(currentCard == null) return true;
		if(priority>currPriority) return true;
		return false;
	}
	
	Card firstPlayStrat(){
		Suit[] s = {Spades.getClubs(), Spades.getDiamonds(), Spades.getHearts(), Spades.getSpades()};
		Card highCard, lowCard, currChoice=null;
		
		int numSuit, roundsPlayed, difference, priority=0;
		boolean partnerEmpty;
		for(int i=0; i<3; i++){
			numSuit = numSuit(s[i].type);
			roundsPlayed = s[i].roundsPlayed;
			if(numSuit>0){
				highCard = getHighCard(s[i].type);
				lowCard = getLowCard(s[i].type);
				difference = s[i].rankDifference(highCard.getValue());
				partnerEmpty = s[i].playerEmpty(Spades.partner(this));
				
				if(roundsPlayed<2&&difference==0) if(updateChoice(11,highCard,priority,currChoice)){priority = 11; currChoice = highCard;}
				
				if(roundsPlayed<1&&difference==1&&numSuit>1) if(updateChoice(10,lowCard,priority,currChoice)){priority = 10; currChoice = lowCard;}
				
				if(roundsPlayed<3&&partnerEmpty) if(updateChoice(9,lowCard,priority,currChoice)){priority = 9; currChoice = lowCard;}
				
				if(roundsPlayed<3&&difference ==0) if(updateChoice(8,highCard,priority,currChoice)){priority = 8; currChoice = highCard;}
				
				if(roundsPlayed<2&&difference == 1&&numSuit>1) if(updateChoice(7,lowCard,priority,currChoice)){priority = 7; currChoice = lowCard;}
				
				if(!Spades.getSpadesBroken()){
					if(partnerEmpty){ if(updateChoice(6,lowCard,priority,currChoice)){priority = 6; currChoice = lowCard;}}
					else if(updateChoice(5,lowCard,priority,currChoice)){priority = 5; currChoice = lowCard;}
				}
			}
		}
		if(Spades.getSpadesBroken()||currChoice == null){
			numSuit = numSuit(s[3].type);
			if(numSuit>0){
				highCard = getHighCard(s[3].type);
				lowCard = getLowCard(s[3].type);
				difference = s[3].rankDifference(highCard.getValue());
				
				if(difference==0) if(updateChoice(9,highCard,priority,currChoice)){priority = 9; currChoice = highCard;}
				
				if(difference==2&&numSuit>1) if(updateChoice(8,lowCard,priority,currChoice)){priority = 8; currChoice = lowCard;}
				
				if(difference==3&&numSuit>2) if(updateChoice(7,lowCard,priority,currChoice)){priority = 7; currChoice = lowCard;}
			}
		}
		
		if(currChoice == null) {
			return hand.get(0);
		}
		else return currChoice;
	}
	
	Card secondPlayStrat(){
		//TODO: add nil strategy for self and partner
		Suit s = Spades.getSuit(Spades.getStartSuite());
		Card highCard, lowCard, c;
		int numSuit = numSuit(s.type);
		if(numSuit > 0){ //he has cards of the corresponding suit
			highCard = getHighCard(s.type);
			lowCard = getLowCard(s.type);

			int difference = s.rankDifference(highCard.getValue());
			if(difference == 0) c = highCard;
			else c = lowCard;
		}
		else{	// does not have starting suit
			if(!s.type.equals("spades")){ //he is emptied in the suit, and the suit wasn't spades
				numSuit = numSuit(Spades.getSpades().type);
				if(numSuit > 0){
					c = getLowCard("spades");	// play low spade
				}
				else  c = getLowest(); //he doesn't have spades. toss something else
			}
			else{ //he has no spades, start suit is spades
				return hand.get(0);
			}
		}
		return c;
	}

	private Card getLowest() {
		// finds lowest value non-spade in hand
		Card newCard = hand.get(0);
		Card current = hand.get(0);
		Suit[] s = {Spades.getClubs(), Spades.getDiamonds(), Spades.getHearts()};
		
		for(int i=0;i<3;i++){
			if (numSuit(s[i].type)>0){
				newCard = getLowCard(s[i].type);
			}
			if (current.getValue()>newCard.getValue())
				current = newCard;
		}
		return current;
	}

	private Card getHighest() {
		// finds highest card in hand
		Card newCard = hand.get(0);
		Card current = hand.get(0);
		Suit[] s = {Spades.getClubs(), Spades.getDiamonds(), Spades.getHearts()};
		
		if(numSuit("spades")>0)
			current = getHighCard("spades");
		else{
			for(int i=0;i<3;i++){
				if (numSuit(s[i].type)>0){
					newCard = getHighCard(s[i].type);
				}
				if (current.getValue()<newCard.getValue())
					current = newCard;
			}
		}			
		return current;
	}

	public void disableClicks() {
		// TODO Auto-generated method stub
		for(int i=0;i<hand.size();i++)
			hand.get(i).setClickable(false);
	}
	public void enableClicks() {
		// TODO Auto-generated method stub
		for(int i=0;i<hand.size();i++)
			hand.get(i).setClickable(true);
	}


	public boolean isTrying() { // if player wants more tricks
		return trying;
	}


	public void setTrying(boolean trying) {
		this.trying = trying;
	}


	public void setActionCard(Card actionCard) {
		this.actionCard = actionCard;
	}


	public Card getActionCard() {
		return actionCard;
	}


	public void setCurrentPlayer(boolean currentPlayer) {
		this.currentPlayer = currentPlayer;
	}


	public boolean isCurrentPlayer() {
		return currentPlayer;
	}


	public int getContract() {
		return contract;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getType() {
		return type;
	}


	public void setNumTricks(int numTricks) {
		this.numTricks = numTricks;
	}


	public int getNumTricks() {
		return numTricks;
	}


	public void setActionCardLocation(JPanel actionCardLocation) {
		this.actionCardLocation = actionCardLocation;
	}


	public JPanel getActionCardLocation() {
		return actionCardLocation;
	}

}