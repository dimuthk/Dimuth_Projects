import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.TransferHandler;

class Player extends JPanel{
	
	
	
	private static final long serialVersionUID = 1L;
	String name;
	Player opponent;
	boolean north;
	boolean canPlayTrainer;
	PrizePanel prizePanel;
	DeckAndDiscardPanel deckAndDiscardPanel;
	//HandPanel handPanel;
	BenchAndActiveCardPanel benchAndActiveCardPanel;
	
	
	
	public Player(String pName, ArrayList<Card> pDeck, boolean pNorth){
		north = pNorth;
		deckAndDiscardPanel = new DeckAndDiscardPanel(pDeck);
		prizePanel = new PrizePanel();
		//handPanel = new HandPanel(); //this won't be visible on the gameboard. 
		benchAndActiveCardPanel = new BenchAndActiveCardPanel();
		name = pName;
		
		distributeDeck();
		addPanelsToBoard();
		//distributeDeck();
		
	}
	
	void setOrientation(boolean north){
		benchAndActiveCardPanel.setOrientation(north);
		deckAndDiscardPanel.setDeckCount();
		this.north = north;
		removeAll();
		addPanelsToBoard();
	}
	
	void addPanelsToBoard(){
		//there are prize cards to the west, bench and main pokemon cards in the center, deck on the right.
		setLayout(new BorderLayout());
		JPanel border = new JPanel();
		JLabel l = new JLabel("-----");
		//border.add(l);
		if(north == true){
			add(border, BorderLayout.SOUTH);
			add(prizePanel, BorderLayout.EAST);
			add(deckAndDiscardPanel, BorderLayout.WEST);
		}
		else{
			add(border, BorderLayout.NORTH);
			add(prizePanel, BorderLayout.WEST);
			add(deckAndDiscardPanel, BorderLayout.EAST);
		}
		add(benchAndActiveCardPanel, BorderLayout.CENTER);
		//add(border, BorderLayout.NORTH);
		
	}
	
	void distributeDeck(){
		deckAndDiscardPanel.shuffleDeck();
		//handPanel.addCardsToHand(7);
		benchAndActiveCardPanel.addCardsToHand(7);
		prizePanel.addPrizeCards(6);
	}
	
	class DeckAndDiscardPanel extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		ArrayList<Card> discardPile, deck;
		PlaceHolder discardHolder, deckHolder;
		JCheckBox inspect;
		JLabel cardsLeft;
		JLabel prizesLeft;
		
		public DeckAndDiscardPanel(ArrayList<Card> pDeck){
			discardPile = new ArrayList<Card>();
			deck = pDeck;
			setLayout(new GridLayout(3,2));
			discardHolder = new PlaceHolder();
			deckHolder = new PlaceHolder();
			discardHolder.showFront();
			cardsLeft = new JLabel();
			deckHolder.showBack();
			inspect = new JCheckBox("Inspect");
			inspect.addItemListener(new ItemListener() {
			      public void itemStateChanged(ItemEvent e) {
			        if(inspect.isSelected()){
			        	benchAndActiveCardPanel.inspect = true;
			        }
			        else{
			        	benchAndActiveCardPanel.inspect = false;
			        }
			      }
			    });
			add(deckHolder);
			add(discardHolder);
			add(cardsLeft);
			add(inspect);
			prizesLeft = new JLabel();
			updateInfo();
			add(prizesLeft);
			//j.setText("north");
			//add(j);
		}
		
		void repaintPanel(){
			removeAll();
			add(deckHolder);
			add(discardCard());
			add(cardsLeft);
			add(inspect);
			add(prizesLeft);
			revalidate();
			repaint();
		}
		
		Card discardCard(){
			if(discardPile.size()>0){
				return discardPile.get(discardPile.size()-1);
			}
			else{
				return discardHolder;
			}
		}
		
		void setDeckCount(){
			cardsLeft.setText("<html>Cards left<br>in deck: " + deck.size()+"</html>");
		}
		
		Card removeFromDiscardPile(int i){
			Card c = discardPile.get(i);
			for(int j=0; j<discardPile.size();j++){
				discardPile.get(j).removeMouseListener(this);
			}
			discardPile.remove(c);
			if(discardPile.size()>0){
				discardPile.get(discardPile.size()-1).addMouseListener(this);
			}
			repaintPanel();
			return c;
		}
		
		Card removeCardFromDeck(){
			return deck.remove(0);
		}
		
		void shuffleDeck(){
			Collections.shuffle(deck);
		}
		
		Card undoMasquerade(Card c){
			if(c.masquerade == true){
				if(c.cardType == Card.ENERGY){
					EnergyMasquerade e = (EnergyMasquerade) c;
					c = e.originalCard;
				}
				else if(c.cardType == Card.BASIC_POKEMON){
					try{
						PokemonMasquerade e = (PokemonMasquerade) c;
						c = e.originalCard;
					}
					catch(Exception e){
						PokemonCard pc = (PokemonCard) c;
						c = PokemonCard.unwrapDitto(pc);
					}
				}
			}
			return c;
		}
		
		void addToDiscardPile(Card c){
			c = undoMasquerade(c);
			c.removeMouseListener(benchAndActiveCardPanel);
			
			if(c.isAPokemon()){
				PokemonCard pc = (PokemonCard) c;
				for(int i=0; i<pc.energy.size();i++){
					Card t = pc.energy.get(i);
					t = undoMasquerade(pc.energy.get(i));
					pc.energy.get(i).removeMouseListener(benchAndActiveCardPanel);
				}
				discardPile.addAll(pc.energy);
				for(int i=0; i<pc.stages.size();i++){
					Card t = pc.stages.get(i);
					t = undoMasquerade(pc.stages.get(i));
					pc.stages.get(i).removeMouseListener(benchAndActiveCardPanel);
				}
				discardPile.addAll(pc.stages);
				pc.energy.clear();
				pc.stages.clear();
			}
			
			discardPile.add(c);
			for(int i=0; i<discardPile.size();i++){
				discardPile.get(i).removeMouseListener(this);
			}
			discardPile.get(discardPile.size()-1).addMouseListener(this);
			c.showFront();
			c.setText("");
			c.location = Card.DISCARD;
			repaintPanel();
		}
		
		void addToDeck(Card c){
			c = undoMasquerade(c);
			c.removeMouseListener(benchAndActiveCardPanel);
			if(c.isAPokemon()){
				PokemonCard pc = (PokemonCard) c;
				for(int i=0; i<pc.energy.size();i++){
					Card t = pc.energy.get(i);
					t = undoMasquerade(pc.energy.get(i));
					pc.energy.get(i).removeMouseListener(benchAndActiveCardPanel);
				}
				deck.addAll(pc.energy);
				for(int i=0; i<pc.stages.size();i++){
					Card t = pc.stages.get(i);
					t = undoMasquerade(pc.stages.get(i));
					pc.stages.get(i).removeMouseListener(benchAndActiveCardPanel);
				}
				deck.addAll(pc.stages);
				pc.energy.clear();
				pc.stages.clear();
			}
			deck.add(c);
			
		}
		
		void addToTopOfDeck(Card c){ 
			c.removeMouseListener(benchAndActiveCardPanel);
			deck.add(0, c);
		}
		
		void updateInfo(){
			try{
				prizesLeft.setText(prizePanel.prizesOpen + " prizes open");
			}
			catch(Exception e) {
				prizesLeft.setText(0 + " prizes open");
			}
			cardsLeft.setText("Cards left in deck: " + deck.size());
		 }

		@Override
		public void mouseClicked(MouseEvent arg0) {
			new CardList(discardPile);
			
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
		private class CardList extends JDialog{
			private static final long serialVersionUID = 1L;

			public CardList(ArrayList<Card> list){
				
				setSize(500,150);
				setLocationRelativeTo(PokemonGame.game);
				setLayout(new GridLayout(1,1));
				JPanel p = new JPanel();
				ArrayList<Card> cloneList = new ArrayList<Card>();
				for(int i=0; i<list.size();i++){
					cloneList.add(new DummyCard(list.get(i)));
				}
				
				for(int i=cloneList.size()-1; i>-1; i--){
					p.add(cloneList.get(i));
				}
				JScrollPane scroller = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				add(scroller);
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setResizable(false);
				setVisible(true);
				
			}
		}
		
	}
	
	class PrizePanel extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		Card[] prizes;
		PlaceHolder[] placeHolders;
		int prizesOpen = 0;
		
		boolean oppositePrize = false;
		public PrizePanel(){
			prizes = new Card[6];
			placeHolders = new PlaceHolder[6];
			for(int i=0; i<6; i++){
				placeHolders[i] = new PlaceHolder();
			}
			setLayout(new GridLayout(3,2));
			
			//addPrizeCards(6);
		}
		
		public void addPrizeCards(int num){
			for(int i=0; i<num; i++){
				prizes[i] = deckAndDiscardPanel.removeCardFromDeck();
				prizes[i].addMouseListener(this);
			}
			updatePanel();
		}
		
		 void updatePanel(){
			 removeAll();
			for(int i=0; i<6; i++){
				if(prizes[i] != placeHolders[i]){
					prizes[i].showBack();
				}
				else{
					prizes[i].showFront();
				}
				
				add(prizes[i]);
			}
			
			revalidate();
			repaint();
		}
		 
		 void removePrize(Card c){
			 for(int i=0; i<6; i++){
				 if(prizes[i] == c){
					 prizes[i] = placeHolders[i];
					 updatePanel();
					 return;
				 }
			 }
		 }
		 
		 
		 
		 void addPrizes(int num){
			 prizesOpen += num;
			 deckAndDiscardPanel.updateInfo();
		 }
		 

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(prizesOpen > 0){
				Card c = (Card) arg0.getSource();
				c.showUpClose();
				benchAndActiveCardPanel.addCardToHand(c);
				removePrize(c);
				prizesOpen--;
				deckAndDiscardPanel.updateInfo();
				//if(prizesOpen == 0) PokemonCard.concludeTurn();
			}
		}
		
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	
	
	class BenchAndActiveCardPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		Card activeCard;
		Card[] bench;
		ArrayList<Card> hand;
		PlaceHolder activeHolder;
		JPanel benchPanel, activeCardPanel, handPanel;
		HandToBenchListener handToBenchListener;
		HandToActiveListener handToActiveListener;
		BenchToActiveListener benchToActiveListener;
		ActiveToBenchListener activeToBenchListener;
		EnergyAttachListener energyAttachListener;
		EvolutionListener evolutionListener;
		Card movingCard;
		DropTarget[] movingDropTarget;
		DragSource ds;
		int benchSize;
		boolean inspect;
		boolean playerTurn;
		boolean pickActive = true;
		boolean activeToBenchBan = false;
		JPanel p;
		JScrollPane scroller;
		//JPanel temp;
		
		PlaceHolder[] benchHolders;
		public BenchAndActiveCardPanel(){
			setLayout(new BorderLayout());
			//t = new JPanel();
			handToBenchListener = new HandToBenchListener();
			handToActiveListener = new HandToActiveListener();
			benchToActiveListener = new BenchToActiveListener();
			activeToBenchListener = new ActiveToBenchListener();
			energyAttachListener = new EnergyAttachListener();
			evolutionListener = new EvolutionListener();
			
			handPanel = new JPanel();
			scroller = new JScrollPane(handPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			//temp = new JPanel();
			//temp.add(scroller);
			
			benchSize = 0;
			benchPanel = new JPanel(new GridLayout(1,5));
			activeCardPanel = new JPanel();
			
			hand = new ArrayList<Card>();
			activeHolder = new PlaceHolder();
			activeHolder.showFront();
			activeHolder.setText("<html><font color=white>00/00</font></html>");
			//activeHolder.setVerticalTextPosition(JLabel.BOTTOM);
			activeHolder.setHorizontalTextPosition(JLabel.RIGHT);
			benchHolders = new PlaceHolder[5];
			bench = new Card[5];
			for(int i=0; i<5; i++){
				benchHolders[i] = new PlaceHolder();
				benchHolders[i].showFront();
				benchHolders[i].setText("<html><font color=white>00/00</font></html>");
				//benchHolders[i].setVerticalTextPosition(JLabel.BOTTOM);
				//benchHolders[i].setHorizontalTextPosition(JLabel.CENTER);
				             
				setEmptyBenchSlot(i);
				benchPanel.add(bench[i]);
			}
			//DragGestureRecognizer d = new DragGestureRecognizer(ds);
			ds = new DragSource();
			inspect = false;
			//movingDropTarget = new DropTarget();
			//mt2 = new DropTarget();
			
			
			//ds.addDragSourceListener(this);
			//scroller.add(handPanel);
			setNoActiveCard();
			p = new JPanel();
			p.add(benchPanel);
			activeCardPanel.add(activeCard);
			setOrientation(north);
			add(p, BorderLayout.CENTER);
		
		}
		
		void reapplyPieces(){
			remove(activeCardPanel);
			p.removeAll();
			benchPanel.removeAll();
			//activeCardPanel.removeAll();
			handPanel.removeAll();
			//scroller = new JScrollPane(handPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			activeCard.removeMouseListener(PokemonCard.currPlayer().benchAndActiveCardPanel);
			activeCard.addMouseListener(PokemonCard.currPlayer().benchAndActiveCardPanel);
			for(int i=0; i<5; i++){
				bench[i].removeMouseListener(this);
				bench[i].addMouseListener(this);
				benchPanel.add(bench[i]);
			}
			p.add(benchPanel);
			//setActiveCard(activeCard);
			activeCardPanel.add(activeCard);
			//ArrayList<Card> temp = hand.;
			//hand.clear();
			for(int i=0; i< hand.size();i++){
				hand.get(i).removeMouseListener(this);
				hand.get(i).addMouseListener(this);
				handPanel.add(hand.get(i));
			}

			setOrientation(north);
			add(p, BorderLayout.CENTER);
			revalidate();
			repaint();
		}
		
		void updatePokemonStatuses(){
			for(int i=0; i<benchSize; i++){
				PokemonCard pc = (PokemonCard) bench[i];
				bench[i].setText(pc.toString());
			}
			if(activeCard != activeHolder){
				PokemonCard pc = (PokemonCard) activeCard;
				activeCard.setText(pc.toString());
			}
			
		}
		
		void setOrientation(boolean north){
			setHandVisible(!north);
			remove(activeCardPanel);
			
			remove(scroller);
			if(north == true){
				add(activeCardPanel, BorderLayout.SOUTH);
				add(scroller, BorderLayout.NORTH);
			}
			else{
				add(activeCardPanel, BorderLayout.NORTH);
				add(scroller, BorderLayout.SOUTH);
			}
		}
		
		boolean clairvoyanceException(){
			PokemonCard pc;
			for(int i=0; i<opponent.benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) opponent.benchAndActiveCardPanel.bench[i];
				if(pc.pokeId == 138){
					return true;
				}
			}
			try{
				pc = (PokemonCard) opponent.benchAndActiveCardPanel.activeCard;
				if(pc.pokeId == 138 && pc.status == Card.NONE){
					return true;
				}
			}
			catch(Exception e){}
			return false;
		}
		
		void setHandVisible(boolean yes){
				for(int i=0; i<hand.size(); i++){
					if(yes == true && clairvoyanceException() == false) hand.get(i).showFront();
					else hand.get(i).showBack();
				}
			repaintHand();
		}
		
		void setNoActiveCard(){
			activeCard = activeHolder;
			
		}
		
		void addCardsToHand(int num){
			for(int i=0; i<num; i++){
				hand.add(deckAndDiscardPanel.removeCardFromDeck());
				hand.get(i).showFront();
				hand.get(i).setLocation(Card.HAND);
				hand.get(i).addMouseListener(this);
				//hand.get(i).add
				handPanel.add(hand.get(i));
			}
			repaintHand();
		}
		
		void addCardToHand(Card c){
			c = undoMasquerade(c);
			c.showFront();
			c.setLocation(Card.HAND);
			c.addMouseListener(this);
			hand.add(c);
			repaintHand();
		}
		
		boolean addCardToBench(PokemonCard c){
			c = PokemonCard.unwrapDitto(c);
			if(benchSize < 5){
				c.showFront();
				c.setText(c.toString());
				c.setLocation(Card.BENCH);
				bench[benchSize] = c;
				benchSize++;
				repaintBench();
				if(c.currHp == 0){
					knockOutPokemon(c);
				}
				return true;
			}
			return false;
		}
		
		
		void setEmptyBenchSlot(int num){
			bench[num] = benchHolders[num];
		}
		
		void repaintBench(){
			benchPanel.removeAll();
			for(int i=0; i<5; i++){
				benchPanel.add(bench[i]);
			}
			benchPanel.revalidate();
			benchPanel.repaint();
		}
		
		void repaintHand(){
			handPanel.removeAll();
			for(int i=0; i<hand.size(); i++){
				handPanel.add(hand.get(i));
			}
			handPanel.revalidate();
			handPanel.repaint();
		}
		
		//switched with bench card
		void setActiveCard(Card c){
				activeCard = c;
				if(activeCard != activeHolder){
					PokemonCard pc = (PokemonCard) c;
					PokemonCard.dittoWrapException();
					activeCard.setText(activeCard.toString());
					activeCard.setLocation(Card.ACTIVE);
					if(pc.currHp <= 0) knockOutPokemon(pc);
					//activeCard.setHorizontalTextPosition(JLabel.RIGHT);
				}
				repaintActive();
				
		}
		
		
		void repaintActive(){
			activeCardPanel.removeAll();
			activeCardPanel.add(activeCard);
			activeCardPanel.revalidate();
			activeCardPanel.repaint();
		}

		void removeFromHand(Card c){
			hand.remove(c);
			repaintHand();
		}
		
		void removeStatuses(PokemonCard c){
			c.poisoned = false;
			c.specialStatus = null;
			c.status = Card.NONE;
		}
		
		
		
		void removeFromBench(int slot){
			bench[slot] = benchHolders[slot];
			int i;
			for(i=slot; i<4; i++){
				if(bench[i+1]!=benchHolders[i+1]){
					bench[i] = bench[i+1];
					bench[i+1] = benchHolders[i+1];
				}
			}
			bench[i] = benchHolders[i];
			benchSize--;
			repaintBench();
		}
		
		void attachEnergy(PokemonCard c, EnergyCard e){
			c.energy.add(e);
			c.setText(c.toString());
		}
		
		void destinyBondException(PokemonCard c){
			if(c.pokeId == 92){
				PokemonCard pc = (PokemonCard) opponent.benchAndActiveCardPanel.activeCard;
				if(pc.specialStatus != null){
					if(pc.specialStatus.equals("Destiny Bond")){	
						opponent.benchAndActiveCardPanel.knockOutPokemon(pc);
					}
				}
			}
		}
		
		void evolveCardOnBench(int slot, PokemonCard evolution){
			PokemonCard temp = (PokemonCard) bench[slot];
			evolution = temp.evolveCard(evolution);
			evolution.setText(evolution.toString());
			evolution.setLocation(Card.BENCH);
			bench[slot] = evolution;
			repaintBench();
		}
		
		void evolveActiveCard(PokemonCard evolution){
			PokemonCard temp = (PokemonCard) activeCard;
			evolution = temp.evolveCard(evolution);
			evolution.setText(evolution.toString());
			evolution.setLocation(Card.ACTIVE);
			setActiveCard(evolution);
		}
		
		
		
		void knockOutPokemon(PokemonCard c){
			boolean knockOut = true;
			if(c.masquerade == true){
				
				try{
					PokemonMasquerade mc = (PokemonMasquerade) c;
					TrainerCard tc = (TrainerCard) mc.originalCard;
					if(tc.id == TrainerCard.MYSTERIOUS_FOSSIL || tc.id == TrainerCard.CLEFAIRY_DOLL){
						knockOut = false;
					}
				}
				catch(Exception e){}
			}
			if(knockOut == true){
				JOptionPane.showMessageDialog(null, c.name + " has been knocked out! " + opponent.name + " has a new prize card available.");
				opponent.prizePanel.addPrizes(1);
			}
			
			
			c.removeMouseListener(this);
			if(c.location == Card.ACTIVE){
				c.renew();
				deckAndDiscardPanel.addToDiscardPile(c);
				setActiveCard(activeHolder);
			}
			else{
				int slot = -1;
				c.renew();
				deckAndDiscardPanel.addToDiscardPile(c);
				for(int i=0; i<benchSize; i++){
					if(bench[i] == c){
						slot = i;
					}
				}
				removeFromBench(slot);
			}
			destinyBondException(c);
		}
		
		void activeToHand(){
			PokemonCard pc = (PokemonCard) activeCard;
			pc.renew();
			setActiveCard(activeHolder);
			for(int i=0; i<pc.stages.size(); i++){
				addCardToHand(pc.stages.get(i));
			}
			pc.stages.clear();
			for(int i=0; i<pc.energy.size(); i++){
				addCardToHand(pc.energy.get(i));
			}
			pc.energy.clear();
			addCardToHand(pc);
		}
		

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(playerTurn == true){
				Card c = (Card) arg0.getSource();
				c.showUpClose();
			}
		}

		public void mouseEntered(MouseEvent arg0){}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}

		@Override
		public void mousePressed(final MouseEvent arg0) {
			final Card c = (Card) arg0.getSource();
			c.setTransferHandler(new TransferHandler(null));
			if(playerTurn == true && inspect == false){
				final int location = c.location;
				final int cardType = c.cardType;
				c.setTransferHandler(new TransferHandler(null));
				/*
				 * Drag/drop different possibilities: 
				 * move a pokemon card from hand to bench
				 * move a pokemon card from hand to active
				 * move a pokemon card from bench to active
				 * move a pokemon card from active to bench
				 * move an energy card from hand to pokemon card on bench
				 * move an energy card from hand to active pokemon
				 */
				
				for(int i=0; i<5; i++){
					bench[i].setDropTarget(null);
				}
				activeCard.setDropTarget(null);
				
				movingDropTarget = null;
					
					if(pickActive == true){
						if(location == Card.HAND && c.cardType == Card.BASIC_POKEMON){
		    						movingDropTarget = new DropTarget[1];
		    						movingDropTarget[0] = new DropTarget();
		    						try {movingDropTarget[0].addDropTargetListener(handToActiveListener);} 
		    						catch (TooManyListenersException e) {}
		    						movingDropTarget[0].setComponent(activeCard);
		    				}
						else if(cardType == Card.TRAINER){
	    					TrainerCard tc = (TrainerCard) c;
	    					if(tc.id == TrainerCard.MYSTERIOUS_FOSSIL || tc.id == TrainerCard.CLEFAIRY_DOLL){
	    						movingDropTarget = new DropTarget[1];
	    						movingDropTarget[0] = new DropTarget();
	    						try {movingDropTarget[0].addDropTargetListener(handToActiveListener);} 
	    						catch (TooManyListenersException e) {}
	    						movingDropTarget[0].setComponent(activeCard);
	    					}
						}	
					}
					else{
						if(location == Card.HAND){
		    				if(c.cardType == Card.BASIC_POKEMON){
		    					movingDropTarget = new DropTarget[2];
		    					movingDropTarget[0] = new DropTarget();
		    					try {movingDropTarget[0].addDropTargetListener(handToBenchListener);} 
		    					catch (TooManyListenersException e) {}
		    					movingDropTarget[0].setComponent(bench[benchSize]);
		    					if(activeCard == activeHolder){
		    						movingDropTarget[1] = new DropTarget();
		    						try {movingDropTarget[1].addDropTargetListener(handToActiveListener);} 
		    						catch (TooManyListenersException e) {}
		    						movingDropTarget[1].setComponent(activeCard);
		    					}
		    					
		    				}
		    				else if((cardType == Card.STAGE_1_POKEMON ||c.cardType == Card.STAGE_2_POKEMON) && PokemonGame.game.prehistoricPowerException() == false){
		    					movingDropTarget = new DropTarget[6];
		    					PokemonCard pc = (PokemonCard) c;
		    					for(int i=0; i<benchSize; i++){
		    						if(pc.matchesEarlyStage((PokemonCard)bench[i])){
		    							movingDropTarget[i] = new DropTarget();
		    							movingDropTarget[i].setComponent(bench[i]);
		    							try {movingDropTarget[i].addDropTargetListener(evolutionListener);} 
		    							catch (TooManyListenersException e) {}
		    						}
		    					}
		    					if(activeCard != activeHolder){
		    						if(pc.matchesEarlyStage((PokemonCard)activeCard)){
		    							movingDropTarget[5] = new DropTarget();
		    							movingDropTarget[5].setComponent(activeCard);
		    							try {movingDropTarget[5].addDropTargetListener(evolutionListener);} 
		    							catch (TooManyListenersException e) {}
		    						}
		    					}
		    				}
		    				else if(cardType == Card.ENERGY){
		    					movingDropTarget = new DropTarget[benchSize+1];
		    					for(int i=0; i<benchSize; i++){
		    						movingDropTarget[i] = new DropTarget();
		    						movingDropTarget[i].setComponent(bench[i]);
		    						try {movingDropTarget[i].addDropTargetListener(energyAttachListener);} 
		    						catch (TooManyListenersException e) {}
		    					}
		    					if(activeCard != activeHolder){
		    						movingDropTarget[benchSize] = new DropTarget();
		    						movingDropTarget[benchSize].setComponent(activeCard);
		    						try {movingDropTarget[benchSize].addDropTargetListener(energyAttachListener);} 
		    						catch (TooManyListenersException e) {}
		    					}
		    					
		    				}
		    				else if(cardType == Card.TRAINER){
		    					TrainerCard tc = (TrainerCard) c;
		    					if(tc.id == TrainerCard.MYSTERIOUS_FOSSIL || tc.id == TrainerCard.CLEFAIRY_DOLL){
		    						movingDropTarget = new DropTarget[2];
			    					movingDropTarget[0] = new DropTarget();
			    					try {movingDropTarget[0].addDropTargetListener(handToBenchListener);} 
			    					catch (TooManyListenersException e) {}
			    					movingDropTarget[0].setComponent(bench[benchSize]);
			    					if(activeCard == activeHolder){
			    						movingDropTarget[1] = new DropTarget();
			    						try {movingDropTarget[1].addDropTargetListener(handToActiveListener);} 
			    						catch (TooManyListenersException e) {}
			    						movingDropTarget[1].setComponent(activeCard);
			    					}
		    					}
		    				}
		    			}
		    			else if(location == Card.BENCH){ //bench to active
		    				if(c.isAPokemon() && activeCard == activeHolder){
		    					movingDropTarget = new DropTarget[1];
		    					movingDropTarget[0] = new DropTarget();
		    					try {movingDropTarget[0].addDropTargetListener(benchToActiveListener);} 
		    					catch (TooManyListenersException e) {}
		    					movingDropTarget[0].setComponent(activeCard);
		    				}
		    			}
		    			else if(location == Card.ACTIVE && c != activeHolder && activeToBenchBan == false){
		    				PokemonCard pc = (PokemonCard) c;
		    				if(pc.hasEnergy(Card.COLORLESS, pc.getRetreatCost()) == false){
		    					return;
		    				}
		    				movingDropTarget = new DropTarget[benchSize];
		    				if(benchSize == 0){
		    					return;
		    				}
		    				for(int i=0; i<benchSize; i++){
		    					movingDropTarget[i] = new DropTarget();
		    					movingDropTarget[i].setComponent(bench[i]);
		    					try {movingDropTarget[i].addDropTargetListener(activeToBenchListener);} 
		    					catch (TooManyListenersException e) {}
		    				}
		    			}
		    			else{
		    				return;
		    			}	
					}
					
	    			
	    			
	    			movingCard = c;
	    			movingCard.setTransferHandler(new TransferHandler("text"));
	    			TransferHandler th = movingCard.getTransferHandler();
	    			th.exportAsDrag(movingCard, arg0, TransferHandler.COPY);
	    			//th.
				}
			
			
                	
                
		}
		
		//does NOT remove energy for retreat cost
		void activeToBenchTrade(int slot, Card activeC){
			Card transfer = bench[slot];
			removeFromBench(slot);
			setActiveCard(transfer);
			if(activeC != activeHolder){
				PokemonCard pc = (PokemonCard) activeC;
				removeStatuses(pc);
				addCardToBench(pc);
			}
		}
		
		Card undoMasquerade(Card c){
			if(c.masquerade == true){
				if(c.cardType == Card.ENERGY){
					EnergyMasquerade e = (EnergyMasquerade) c;
					c = e.originalCard;
				}
				else if(c.cardType == Card.BASIC_POKEMON){
					try{
						PokemonMasquerade e = (PokemonMasquerade) c;
						c = e.originalCard;
					}
					catch(Exception e){
						PokemonCard pc = (PokemonCard) c;
						c = PokemonCard.unwrapDitto(pc);
					}
				}
			}
			return c;
		}
		
		private class ActiveToBenchListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				Card c = movingCard;
				PokemonCard pc = (PokemonCard) c;
				int costPaid = 0;
				if(pc.hasMultipleEnergyTypes() == false){
					while(costPaid < pc.getRetreatCost()){
						costPaid += pc.removeEnergy(Card.COLORLESS, PokemonCard.currPlayer());
					}
				}
				else{
					new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.RETREAT_COST,"Select energy cards to discard.",pc);
				}
				
				pc.setText(pc.toString());
				DropTarget dt = (DropTarget) dtde.getSource();
				int slot = -1;
				for(int i=0; i<benchSize; i++){
					if(movingDropTarget[i] == dt){
						slot = i;
					}
					bench[i].setDropTarget(null);
				}
				activeToBenchTrade(slot, c);
			}
			
		}
		
		void trainerMasqueradeException(){
			if(movingCard.cardType == Card.TRAINER){
				TrainerCard c = (TrainerCard) movingCard;
				if(c.id == TrainerCard.MYSTERIOUS_FOSSIL || c.id == TrainerCard.CLEFAIRY_DOLL){
					removeFromHand(movingCard);
					movingCard.removeMouseListener(this);
					movingCard = new PokemonMasquerade(c,c.name);
					movingCard.location = Card.HAND;
					movingCard.addMouseListener(this);
				}
			}
		}
		
		private class BenchToActiveListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				Card c = movingCard;
				activeCard.setDropTarget(null);
				if(c.isAPokemon()){
					//PokemonCard pc = (PokemonCard) c;
					if(c.location == Card.BENCH && activeCard == activeHolder){
						int slot = -1;
						for(int i=0;i<5;i++){
							if(bench[i] == c) slot = i;
						}
						removeFromBench(slot);
						setActiveCard(c);
					}
				}
				
			}
			
		}
		
		private class HandToActiveListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				trainerMasqueradeException();
				Card c = movingCard;
				
				activeCard.setDropTarget(null);
				for(int i=0; i<benchSize; i++){
					bench[i].setDropTarget(null);
				}
				
					PokemonCard pc = (PokemonCard) c;
					if(pc.location == Card.HAND){
						setActiveCard(c);
						removeFromHand(pc);
						if(pickActive == true){
							pickActive = false;
							if(PokemonGame.game.currPlayer == PokemonGame.game.player1){
								PokemonGame.game.pickActivePlayer2();
							}
							else{
								PokemonGame.game.player1StartTurn();
							}
							
						}
					}
				
				
			}

			
			
		}
		
		private class HandToBenchListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				trainerMasqueradeException();
				Card c = movingCard;
				
				for(int i=0; i<benchSize; i++){
					bench[i].setDropTarget(null);
				}
				activeCard.setDropTarget(null);
					PokemonCard pc = (PokemonCard) c;
					if(pc.location == Card.HAND){
						if(addCardToBench(pc)== true){
							removeFromHand(pc);
						}
					}
				
				
			}

			
			
		}
		
		private class EnergyAttachListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				//Card c = movingCard;
				DropTarget dt = (DropTarget) dtde.getSource();
				int slot = -1;
				for(int i=0; i<benchSize;i++){
					bench[i].setDropTarget(null);
					if(movingDropTarget[i] == dt){
						slot = i;
					}
				}
				EnergyCard e = (EnergyCard) movingCard;
				if(slot == -1){ //active pokemon
					slot = benchSize;
						attachEnergy((PokemonCard)activeCard, e);
				}
				else{
					attachEnergy((PokemonCard)bench[slot], e);
				}
				removeFromHand(e);
			}
		}
		
		private class EvolutionListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent dtde) {}
			public void dragExit(DropTargetEvent dte) {}
			public void dragOver(DropTargetDragEvent dtde) {}
			public void dropActionChanged(DropTargetDragEvent dtde) {}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				movingCard.setTransferHandler(new TransferHandler(null));
				Card c = movingCard;
				DropTarget dt = (DropTarget) dtde.getSource();
				int slot = -1;
				for(int i=0; i<5;i++){
					bench[i].setDropTarget(null);
					if(movingDropTarget[i] != null){
						if(movingDropTarget[i] == dt){
							slot = i;
						}
					}
				}
				if(slot == -1){
					evolveActiveCard((PokemonCard) c);
				}
				else{
					evolveCardOnBench(slot, (PokemonCard)c);
				}
				removeFromHand(movingCard);
			}
		}


	}

	
	
}