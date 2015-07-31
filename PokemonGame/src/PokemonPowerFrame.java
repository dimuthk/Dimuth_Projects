import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

public class PokemonPowerFrame extends JDialog implements MouseListener{
	private static final long serialVersionUID = 1L;
		Card[] bench;
		Card activeCard;
		ArrayList<Card> hand, realDeck, realDiscard;
		ArrayList<DummyCard> dummyList;
		static final int ENERGY_TRANS = 0, RAIN_DANCE = 1, WHIRLWIND = 2, SPARK = 3, GIGA_SHOCK = 4, CALL_FOR_FAMILY = 5, METRONOME = 6, CALL_FOR_FAMILY_SPECIAL = 7,
				HEAL = 8, SHIFT = 9, HYPER_BEAM = 10, PEEK_PRIZE = 11, PEEK_DECK = 12, AMNESIA = 13, DAMAGE_SWAP = 14, SCAVENGE = 15, STRANGE_BEHAVIOR = 16, ENERGY_CONVERSION = 17,
				CURSE = 18, PROPHECY = 19, BUZZAP = 20, CALL_FOR_FRIEND = 21, CONVERSION_1 = 22, CONVERSION_2 = 23, WILDFIRE = 24, RETREAT_COST = 25, POTION = 26, ENERGY_REMOVAL = 27,
				ENERGY_REMOVAL_CHOOSE = 28, SUPER_POTION = 29, SUPER_POTION_CHOOSE = 30, REVIVE = 31, POKEDEX = 32, POKEMON_FLUTE = 33, MAINTENANCE_CHOOSE = 34, COMPUTER_SEARCH = 35, ENERGY_RETRIEVAL = 36,
				DEFENDER = 37, SUPER_ENERGY_REMOVAL = 38, SUPER_ENERGY_REMOVAL_CHOOSE = 39, SCOOP_UP = 40, POKEMON_TRADER = 41, POKEMON_TRADER_CHOOSE = 42, POKEMON_BREEDER = 43, LASS = 44,
				ITEM_FINDER = 45, ITEM_FINDER_CHOOSE = 46, DEVOLUTION_SPRAY = 47, DEVOLUTION_SPRAY_CHOOSE = 48, COMPUTER_SEARCH_CHOOSE = 49, RECYCLE = 50, ENERGY_SEARCH = 51, MR_FUJI = 52;
		JPanel p3;
		int protocol, counter =0;
		Player player;
		Card movingCard;
		PokemonCard selfReference;
		ArrayList<PokemonCard> random;
		ArrayList<Integer> slots;
		DropTarget movingDropTarget[];
		JPanel handPanel;
		PokemonPowerFrame frame;


		public PokemonPowerFrame(Player cPlayer,int pokemon, String message, PokemonCard pSelfReference){
			setModal(true);
			setLocationRelativeTo(PokemonGame.game);
			player = cPlayer;
			selfReference = pSelfReference;
			frame = this;
			
			addActiveCardComponents();
			addBenchComponents();
			addHandComponents();
			
			protocol = pokemon;
			setLayout(new BorderLayout());
			JLabel info = new JLabel();
			info.setText(message);
			info.setHorizontalAlignment(JLabel.CENTER);
			add(info, BorderLayout.NORTH);
			
			
			if(protocol == SCAVENGE || protocol == ENERGY_CONVERSION || protocol == REVIVE || protocol == POKEMON_FLUTE || protocol == RECYCLE){
				p3 = new JPanel();
				realDiscard = player.deckAndDiscardPanel.discardPile;
				setDummyList(realDiscard);
				JPanel center = new JPanel(new GridLayout(1,1));
				JPanel p2 = new JPanel();
					JButton okay = getCancelButton("Done");
					p2.add(okay);
				
				
				
				JScrollPane scroller = new JScrollPane(p3, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				add(p2, BorderLayout.SOUTH);
				setSize(600,210);
			}
			else if(protocol == DEVOLUTION_SPRAY){
				p3 = new JPanel();
				JPanel center = new JPanel(new GridLayout(1,1));
				ArrayList<DummyCard> list = new ArrayList<DummyCard>();
				for(int i=0; i<selfReference.stages.size();i++){
					list.add(new DummyCard(selfReference.stages.get(i),i));
					p3.add(list.get(i));
					list.get(i).addMouseListener(this);
				}
				JPanel p2 = new JPanel();
				JButton okay = getCancelButton("Done");
				p2.add(okay);
				center.add(p3);
				add(center, BorderLayout.CENTER);
				add(p2, BorderLayout.SOUTH);
				setSize(600,210);
			}
			else if(protocol == MAINTENANCE_CHOOSE || protocol == ENERGY_RETRIEVAL || protocol == POKEMON_TRADER_CHOOSE || protocol == LASS || protocol == ITEM_FINDER_CHOOSE || protocol == COMPUTER_SEARCH_CHOOSE){
				p3 = new JPanel();
				realDiscard = player.deckAndDiscardPanel.discardPile;
				setDummyList(hand);
				JPanel center = new JPanel(new GridLayout(1,1));
				
				if(protocol == LASS){
					center = new JPanel(new GridLayout(2,1));
					JPanel p5 = new JPanel();
					for(int i=0; i<player.opponent.benchAndActiveCardPanel.hand.size();i++){
						p5.add(new DummyCard(player.opponent.benchAndActiveCardPanel.hand.get(i)));
					}
					JScrollPane scroller2 = new JScrollPane(p5, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					JButton okay = getCancelButton("Done");
					JPanel p2 = new JPanel();
					center.add(scroller2);
					p2.add(okay);
					add(p2, BorderLayout.SOUTH);
					setSize(600,380);
				}
				else{
					setSize(600,210);
				}
				JScrollPane scroller = new JScrollPane(p3, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				
			}
			else if(protocol == WILDFIRE || protocol == RETREAT_COST){
				p3 = new JPanel();
				realDiscard = player.deckAndDiscardPanel.discardPile;
				ArrayList<Card> energy = new ArrayList<Card>();
				for(int i=0; i<PokemonCard.currPlayerCard().energy.size();i++){
					energy.add(PokemonCard.currPlayerCard().energy.get(i));
				}
				dummyList = new ArrayList<DummyCard>();
				for(int i=0; i<PokemonCard.currPlayerCard().energy.size(); i++){
					if(protocol != WILDFIRE || PokemonCard.currPlayerCard().energy.get(i).type == Card.FIRE){
						dummyList.add(new DummyCard(PokemonCard.currPlayerCard().energy.get(i),i));
					}					
				}
				for(int i=0; i<dummyList.size(); i++){
					dummyList.get(i).addMouseListener(this);
					p3.add(dummyList.get(i));
				}
				JPanel center = new JPanel(new GridLayout(1,1));
				JButton okay = getCancelButton("Done");
				JPanel p2 = new JPanel();
				p2.add(okay);
				JScrollPane scroller = new JScrollPane(p3, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				add(p2, BorderLayout.SOUTH);
				setSize(600,210);
			}
			else if(protocol == ENERGY_REMOVAL || protocol == SUPER_POTION || protocol == SUPER_ENERGY_REMOVAL){
				p3 = new JPanel();
				realDiscard = player.deckAndDiscardPanel.discardPile;
				ArrayList<Card> energy = new ArrayList<Card>();
				for(int i=0; i<selfReference.energy.size();i++){
					energy.add(selfReference.energy.get(i));
				}
				dummyList = new ArrayList<DummyCard>();
				for(int i=0; i<selfReference.energy.size(); i++){
					if(protocol != WILDFIRE || selfReference.energy.get(i).type == Card.FIRE){
						dummyList.add(new DummyCard(selfReference.energy.get(i),i));
					}					
				}
				for(int i=0; i<dummyList.size(); i++){
					dummyList.get(i).addMouseListener(this);
					p3.add(dummyList.get(i));
				}
				if(protocol == SUPER_ENERGY_REMOVAL){
					JPanel p4 = new JPanel();
					JButton okay = getCancelButton("Done");
					p4.add(okay);
					add(p4, BorderLayout.SOUTH);
				}
				JPanel center = new JPanel(new GridLayout(1,1));
				JScrollPane scroller = new JScrollPane(p3, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				setSize(600,210);
			}
			else if(protocol == METRONOME || protocol == AMNESIA){
				final PokemonCard pc = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
				setSize(500,400);
				JLabel l = new JLabel();
				l.setIcon(pc.bigFront);
				JPanel p1 = new JPanel();
				p1.add(l);
				JPanel p2 = new JPanel(new GridLayout(4,1,5,5));
				JButton b1 = new JButton(pc.moveOne);
				boolean condition = false;
				if(protocol == METRONOME){
					condition = pc.hasPokemonPower == false && pc.moveOne.equals("Metronome") == false;
				}
				else if(protocol == AMNESIA){
					condition = pc.hasPokemonPower == false;
				}
				if(condition == true){	
					b1.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if(protocol == METRONOME){
								pc.metronome = true;
								pc.moveOne();
								pc.metronome = false;
							}
							else if(protocol == AMNESIA){
								pc.amnesia = 1;
							}
							removeMouseListeners();
							player.benchAndActiveCardPanel.reapplyPieces();
							dispose();	
						}
						
					});
				}
				else{
					b1.setEnabled(false);	
				}
				
				p2.add(b1);
				if(pc.moveTwo != null){
					if(protocol == METRONOME){
						condition = pc.moveTwo.equals("Metronome") == false;
					}
					else if(protocol == AMNESIA){
						condition = true;
					}
					JButton b2 = new JButton(pc.moveTwo);
					if(condition == true){
						b2.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent arg0) {
								if(protocol == METRONOME){
									pc.metronome = true;
									pc.moveTwo();
									pc.metronome = false;
								}
								else if(protocol == AMNESIA){
									pc.amnesia = 2;
								}
								removeMouseListeners();
								player.benchAndActiveCardPanel.reapplyPieces();
								dispose();	
							}
							
						});
					}
					else{
						b2.setEnabled(false);
					}
					p2.add(b2);
				}

				JPanel p3 = new JPanel();
				p3.add(p1);
				p3.add(p2);
				add(p3, BorderLayout.CENTER);
			}
			else if(protocol == ENERGY_TRANS || protocol == HEAL || protocol == DAMAGE_SWAP || protocol == STRANGE_BEHAVIOR || protocol == CURSE || protocol == BUZZAP || protocol == POTION || protocol == ENERGY_REMOVAL_CHOOSE 
					|| protocol == SUPER_POTION_CHOOSE || protocol == DEFENDER || protocol == SUPER_ENERGY_REMOVAL_CHOOSE || protocol == SCOOP_UP || protocol == DEVOLUTION_SPRAY_CHOOSE){
				JPanel p2 = new JPanel();
				JPanel p3 = new JPanel();
				JPanel p4 = new JPanel();
				JPanel center = new JPanel(new GridLayout(2,1));
				p2.add(activeCard);
				for(int i=0; i<5; i++){
					p3.add(bench[i]);
				}
				center.add(p2);
				center.add(p3);
				add(center, BorderLayout.CENTER);
				if(protocol != ENERGY_REMOVAL_CHOOSE && protocol != SUPER_ENERGY_REMOVAL_CHOOSE){
					JButton okay = getCancelButton("Done");
					p4.add(okay);
					add(p4, BorderLayout.SOUTH);
				}
				
				
				setSize(600,400);
			}
			else if(protocol == RAIN_DANCE || protocol == POKEMON_BREEDER){
				JPanel p2 = new JPanel();
				JPanel p3 = new JPanel();
				JPanel p4 = new JPanel();
				handPanel = new JPanel();
				JPanel center = new JPanel(new GridLayout(3,1));
				p2.add(activeCard);
				for(int i=0; i<5; i++){
					p3.add(bench[i]);
				}
				for(int i=0; i<hand.size(); i++){
					handPanel.add(hand.get(i));
				}
				center.add(p2);
				center.add(p3);
				JScrollPane scroller = new JScrollPane(handPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				JButton okay = getCancelButton("Done");
				p4.add(okay);
				add(p4, BorderLayout.SOUTH);
				setSize(600,500);
			}
			else if(protocol == WHIRLWIND||protocol == SPARK||protocol == GIGA_SHOCK || protocol == MR_FUJI){
				p3 = new JPanel();
				JPanel center = new JPanel(new GridLayout(1,1));
				for(int i=0; i<5; i++){
					p3.add(bench[i]);
				}
				center.add(p3);
				setSize(600,175);

				add(center, BorderLayout.CENTER);
				
				if(protocol == GIGA_SHOCK){
					random = new ArrayList<PokemonCard>();
					slots = new ArrayList<Integer>();
				}
			}
			else if(protocol == CALL_FOR_FAMILY||protocol == CALL_FOR_FAMILY_SPECIAL||protocol == PROPHECY ||protocol == CALL_FOR_FRIEND || protocol == POKEDEX || protocol == COMPUTER_SEARCH || protocol == POKEMON_TRADER||
					protocol == ENERGY_SEARCH){
				p3 = new JPanel();
				realDeck = player.deckAndDiscardPanel.deck;
				
				dummyList = new ArrayList<DummyCard>();
				int size = realDeck.size();
				if(protocol == PROPHECY || protocol == POKEDEX && realDeck.size() >0){
					size = 1;
					if(realDeck.size() > 1){
						size = 2;
					}
					if(realDeck.size() > 2){
						size = 3;
					}
					if(protocol == POKEDEX){
						if(realDeck.size() > 3){
							size = 4;
						}
						if(realDeck.size() > 4){
							size = 5;
						}
					}
				}
				
				for(int i=0; i<size;i++){
					if(realDeck.get(i).isAPokemon()){
						PokemonCard pc = (PokemonCard) realDeck.get(i);
						dummyList.add(new DummyCard(realDeck.get(i), pc.pokeId, i));
					}
					else{
						dummyList.add(new DummyCard(realDeck.get(i), -1, i));
					}
					
				}
				JPanel center = new JPanel(new GridLayout(1,1));
				for(int i=0; i<dummyList.size(); i++){
					dummyList.get(i).addMouseListener(this);
					p3.add(dummyList.get(i));
				}
				JButton okay = getCancelButton("Done");
				JPanel p2 = new JPanel();
				p2.add(okay);
				JScrollPane scroller = new JScrollPane(p3, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				center.add(scroller);
				add(center, BorderLayout.CENTER);
				add(p2, BorderLayout.SOUTH);
				setSize(600,210);
			}
			else if(protocol == SHIFT || protocol == HYPER_BEAM || protocol == CONVERSION_1 || protocol == CONVERSION_2){
				p3 = new JPanel();
				JPanel center = new JPanel(new GridLayout(1,1));
				dummyList = new ArrayList<DummyCard>();
				if(protocol == SHIFT || protocol == CONVERSION_1 || protocol == CONVERSION_2){
					dummyList.add(new DummyCard(new LeafEnergy(),Card.LEAF));
					dummyList.add(new DummyCard(new FireEnergy(),Card.FIRE));
					dummyList.add(new DummyCard(new WaterEnergy(),Card.WATER));
					dummyList.add(new DummyCard(new PsychicEnergy(),Card.PSYCHIC));
					dummyList.add(new DummyCard(new ThunderEnergy(),Card.THUNDER));
					dummyList.add(new DummyCard(new FightingEnergy(),Card.FIGHTING));
				}
				else if(protocol == HYPER_BEAM){
					PokemonCard p = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
					for(int i=0; i<p.energy.size();i++){
						dummyList.add(new DummyCard(p.energy.get(i),p.energy.get(i).type));
					}
				}
				
				for(int i=0; i< dummyList.size(); i++){
					dummyList.get(i).addMouseListener(this);
					p3.add(dummyList.get(i));
				}
				center.add(p3);
				add(center, BorderLayout.CENTER);
				setSize(600,175);
			}
			else if(protocol == PEEK_PRIZE){
				JPanel center = new JPanel(new GridLayout(1,1));
				p3 = new JPanel(new GridLayout(3,2));
				Card[] prizes = player.prizePanel.prizes;
				dummyList = new ArrayList<DummyCard>();
				for(int i=0; i<6; i++){
					DummyCard d = new DummyCard(prizes[i]); d.showBack();
					dummyList.add(d);
				}
				for(int i=0; i< dummyList.size(); i++){
					dummyList.get(i).addMouseListener(this);
					p3.add(dummyList.get(i));
				}
				center.add(p3, BorderLayout.CENTER);
				add(center, BorderLayout.CENTER);
				setSize(200,400);
			}
			else if(protocol == PEEK_DECK){
				JPanel p = new JPanel(new BorderLayout());
				realDeck = player.deckAndDiscardPanel.deck;
				DummyCard dc = new DummyCard(realDeck.get(0));
				dc.showFront();
				
				JButton okay = new JButton("Done");
				okay.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					
				});
				
				p.add(dc, BorderLayout.CENTER);
				p.add(okay, BorderLayout.SOUTH);
				add(p);
				setSize(200,200);
			}
			setResizable(false);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setVisible(true);
			
		}
		
		void addActiveCardComponents(){
			activeCard = player.benchAndActiveCardPanel.activeCard;
			activeCard.addMouseListener(this);
			activeCard.removeMouseListener(player.benchAndActiveCardPanel);
		}
		
		void addBenchComponents(){
			bench = player.benchAndActiveCardPanel.bench;
			for(int i=0; i<5; i++){
				bench[i].addMouseListener(this);
				bench[i].removeMouseListener(player.benchAndActiveCardPanel);
			}
		}
		
		void addHandComponents(){
			hand = player.benchAndActiveCardPanel.hand;
			for(int i=0; i<hand.size(); i++){
				hand.get(i).addMouseListener(this);
				hand.get(i).removeMouseListener(player.benchAndActiveCardPanel);
			}
		}
		
		void setDummyList(ArrayList<Card> list){
			dummyList = new ArrayList<DummyCard>();
			for(int i=0; i<list.size();i++){
				dummyList.add(new DummyCard(list.get(i), list.get(i).cardType, i));
			}
			for(int i=0; i<dummyList.size(); i++){
				dummyList.get(i).addMouseListener(this);
				p3.add(dummyList.get(i));
			}
		}
		
		JButton getCancelButton(String name){
			JButton okay = new JButton(name);
			okay.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					removeMouseListeners();
					if(protocol == CALL_FOR_FAMILY || protocol == CALL_FOR_FAMILY_SPECIAL || protocol == CALL_FOR_FRIEND || protocol == LASS || protocol == ENERGY_SEARCH){
						Collections.shuffle(player.deckAndDiscardPanel.deck);
					}
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}
				
			});
			return okay;
		}
		
		void repaintHand(){
			handPanel.removeAll();
			for(int i=0; i<hand.size(); i++){
				handPanel.add(hand.get(i));
			}
			handPanel.revalidate();
			handPanel.repaint();
		}
		
		void removeMouseListeners(){
			activeCard.removeMouseListener(this);
			for(int i=0; i<5;i++){
				bench[i].removeMouseListener(this);
			}
			for(int i=0; i<hand.size(); i++){
				hand.get(i).removeMouseListener(this);
			}
		}
		
		void setTransfer(MouseEvent arg0){
			movingCard.setTransferHandler(new TransferHandler("text"));
			TransferHandler th = movingCard.getTransferHandler();
			th.exportAsDrag(movingCard, arg0, TransferHandler.COPY);
		}
		
	
		
		boolean hasLeafEnergy(PokemonCard pc){
			for(int i=0; i<pc.energy.size();i++){
				if(pc.energy.get(i).type == Card.LEAF){
					return true;
				}
			}
			return false;
		}
		
		void addEnergy(PokemonCard c, EnergyCard e){
			c.energy.add(e);
			c.setText(c.toString());
		}
		
		void transferLeafEnergy(PokemonCard transfer, PokemonCard source){
			boolean done = false;
			for(int i=0; i<transfer.energy.size() && done == false; i++){
				if(transfer.energy.get(i).type == Card.LEAF){
					source.energy.add(transfer.energy.remove(i));
					done = true;
					transfer.setText(transfer.toString());
					source.setText(source.toString());
				}
			}
		}
		
		void repaintP3(){
			p3.removeAll();
			for(int i=0; i<dummyList.size();i++){
				p3.add(dummyList.get(i));
			}
			p3.revalidate();
			p3.repaint();
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
		@Override
		public void mouseClicked(MouseEvent arg0) { 
				if(protocol == DEVOLUTION_SPRAY){
					DummyCard dc = (DummyCard) arg0.getSource();
					PokemonCard pc = (PokemonCard) dc.originalCard;
					
					
					
					for(int i=0; i<player.benchAndActiveCardPanel.benchSize; i++){
						if(bench[i] == selfReference){
							if(pc == selfReference.stages.get(0)){
								selfReference = PokemonCard.devolveCard(selfReference, 0);
							}
							else{
								selfReference = PokemonCard.devolveCard(selfReference, 1);
							}
							selfReference.setText(selfReference.toString());
							player.benchAndActiveCardPanel.bench[i] = selfReference;
						}
					}
					if(activeCard == selfReference){
						if(pc == selfReference.stages.get(0)){
							selfReference = PokemonCard.devolveCard(selfReference, 0);
						}
						else{
							selfReference = PokemonCard.devolveCard(selfReference, 1);
						}
						selfReference.setText(selfReference.toString());
						player.benchAndActiveCardPanel.setActiveCard(selfReference);
					}
					
					removeMouseListeners();			
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}
				else if(protocol == MR_FUJI){
					PokemonCard pc;
					try{pc = (PokemonCard) arg0.getSource();}
					catch(Exception e){return;}
					int slot = -1;
					for(int i=0; i<5; i++){
						if (pc == bench[i]){
							slot = i;
						}
					}
					player.benchAndActiveCardPanel.removeFromBench(slot);
					player.deckAndDiscardPanel.addToDeck(pc);
					removeMouseListeners();			
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}
				else if(protocol == WHIRLWIND || protocol == SPARK || protocol == GIGA_SHOCK || protocol == BUZZAP){
					PokemonCard pc;
					try{pc = (PokemonCard) arg0.getSource();}
					catch(Exception e){return;}
					int slot = -1;
					for(int i=0; i<5; i++){
						if (pc == bench[i]){
							slot = i;
						}
					}
					if(protocol == WHIRLWIND){
						removeMouseListeners();
						player.benchAndActiveCardPanel.activeToBenchTrade(slot, player.benchAndActiveCardPanel.activeCard);				
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					else if(protocol == SPARK){
						removeMouseListeners();
						PokemonCard p = (PokemonCard) player.benchAndActiveCardPanel.bench[slot];
						p.benchTakeDamage(player,10,slot);
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					else if(protocol == BUZZAP){
						PokemonCard p;
						if(slot != -1){
							p = (PokemonCard) player.benchAndActiveCardPanel.bench[slot];
						}
						else{
							p = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
						}
						
						if(p == selfReference){
							return;
						}
						player.benchAndActiveCardPanel.knockOutPokemon(selfReference);
						for(int i=0; i< player.deckAndDiscardPanel.discardPile.size();i++){
							if(player.deckAndDiscardPanel.discardPile.get(i) == selfReference){
								slot = i;
							}
						}
						EnergyMasquerade e = new EnergyMasquerade(p.type,2,player.deckAndDiscardPanel.removeFromDiscardPile(slot));
						p.energy.add(e);
						p.setText(p.toString());
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					else if(protocol == GIGA_SHOCK){
					PokemonCard p = (PokemonCard) bench[slot];
					if(random.contains(p) == false){
						random.add(p);
						slots.add(slot);
						p.benchTakeDamage(player,10,slot);
						p3.removeAll();
						for(int i=0; i<5; i++){
							p3.add(bench[i]);
						}
						p3.revalidate();
						p3.repaint();
						p.setText(p.toString());
					}
					if(random.size() == 3){
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
						}
					}
				}
				else if(protocol == SCAVENGE){
					DummyCard dc = (DummyCard) arg0.getSource();
					if(dc.value == Card.TRAINER){
						player.benchAndActiveCardPanel.addCardToHand(player.deckAndDiscardPanel.removeFromDiscardPile(dc.value2));		
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
				}
				else if(protocol == RECYCLE){
					DummyCard dc = (DummyCard) arg0.getSource();
						player.deckAndDiscardPanel.addToTopOfDeck(player.deckAndDiscardPanel.removeFromDiscardPile(dc.value2));		
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
				}
				else if(protocol == ENERGY_CONVERSION){
					DummyCard dc = (DummyCard) arg0.getSource();
					if(dc.value == Card.ENERGY){
						dummyList.remove(dc);
						repaintP3();
						counter++;
						for(int i=0; i<dummyList.size();i++){
							dummyList.get(i).value2--;
						}
						player.benchAndActiveCardPanel.addCardToHand(player.deckAndDiscardPanel.removeFromDiscardPile(dc.value2));
						if(counter == 2){
							removeMouseListeners();
							player.benchAndActiveCardPanel.reapplyPieces();
							dispose();	
						}
					}
				}
				else if(protocol == MAINTENANCE_CHOOSE || protocol == ENERGY_RETRIEVAL || protocol == POKEMON_TRADER_CHOOSE || protocol == ITEM_FINDER_CHOOSE || protocol == COMPUTER_SEARCH_CHOOSE){
					DummyCard dc = (DummyCard) arg0.getSource();
					Card c = hand.get(dc.value2);
					if(protocol == MAINTENANCE_CHOOSE || protocol == POKEMON_TRADER_CHOOSE){
						if(protocol == POKEMON_TRADER_CHOOSE){
							if(c.isAPokemon() == false){
								return;
							}
						}
						player.deckAndDiscardPanel.addToDeck(c);
					}
					else if(protocol == ENERGY_RETRIEVAL || protocol == ITEM_FINDER_CHOOSE || protocol == COMPUTER_SEARCH_CHOOSE){
						player.deckAndDiscardPanel.addToDiscardPile(c);
					}
					player.benchAndActiveCardPanel.removeFromHand(c);
					counter++;
					dummyList.remove(dc);
					repaintP3();
					for(int i=0; i<dummyList.size();i++){
						dummyList.get(i).value2--;
					}
					if(counter == 2 && protocol == MAINTENANCE_CHOOSE){
						Collections.shuffle(player.deckAndDiscardPanel.deck);
						//new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.MAINTENANCE, PokemonCard.currPlayer().name+ ", select a card to add to your hand.",null);
						player.benchAndActiveCardPanel.addCardToHand(player.deckAndDiscardPanel.deck.remove(0));
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					else if(counter == 2 && (protocol == ITEM_FINDER_CHOOSE || protocol == COMPUTER_SEARCH_CHOOSE)){
						if(protocol == ITEM_FINDER_CHOOSE) new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.SCAVENGE, PokemonCard.currPlayer().name+ ", select a card to add to your hand.",null);
						else if(protocol == COMPUTER_SEARCH_CHOOSE) new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.COMPUTER_SEARCH, PokemonCard.currPlayer().name+ ", select a card to add to your hand.",null);
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					else if(protocol == POKEMON_TRADER_CHOOSE){
						new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POKEMON_TRADER, PokemonCard.currPlayer().name+ ", select a pokemon to add to your hand.",null);
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();
					}
					else if(protocol == ENERGY_RETRIEVAL){
						new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.ENERGY_CONVERSION, PokemonCard.currPlayer().name+ ", select energy cards to add to your hand.",null);
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
				}
				else if(protocol == REVIVE || protocol == POKEMON_FLUTE){
					DummyCard dc = (DummyCard) arg0.getSource();
					if(dc.value == Card.BASIC_POKEMON){
						dummyList.remove(dc);
						repaintP3();
						counter++;
						PokemonCard c = (PokemonCard) dc.originalCard;
						player.deckAndDiscardPanel.removeFromDiscardPile(dc.value2);
						if(protocol == REVIVE){
							c.currHp = c.maxHp/2;
							if(c.currHp % 10 != 0) c.currHp -= 5;
						}
						player.benchAndActiveCardPanel.addCardToBench(c);
							removeMouseListeners();
							player.benchAndActiveCardPanel.reapplyPieces();
							dispose();	
					}
				}
				else if(protocol == SCOOP_UP){
					PokemonCard c = (PokemonCard) arg0.getSource();
					if(c == player.benchAndActiveCardPanel.activeCard){
						player.benchAndActiveCardPanel.setActiveCard(player.benchAndActiveCardPanel.activeHolder);
					}
					else{
						for(int i=0; i<player.benchAndActiveCardPanel.benchSize;i++){
							if(c == player.benchAndActiveCardPanel.bench[i]){
								player.benchAndActiveCardPanel.removeFromBench(i);
							}
						}
					}
					player.deckAndDiscardPanel.addToDiscardPile(c);
					player.benchAndActiveCardPanel.addCardToHand(player.deckAndDiscardPanel.removeFromDiscardPile(player.deckAndDiscardPanel.discardPile.size()-1));
					removeMouseListeners();
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}
				else if(protocol == WILDFIRE || protocol == RETREAT_COST || protocol == ENERGY_REMOVAL || protocol == SUPER_POTION || protocol == SUPER_ENERGY_REMOVAL){
					DummyCard dc = (DummyCard) arg0.getSource();
						dummyList.remove(dc);
						repaintP3();
						EnergyCard e = (EnergyCard) dc.originalCard;
						if(protocol == WILDFIRE){
							for(int i=0; i<e.energyIncrease; i++){
								player.opponent.deckAndDiscardPanel.addToDiscardPile(player.opponent.deckAndDiscardPanel.deck.remove(0));
							}
						}
						if(protocol == ENERGY_REMOVAL || protocol == SUPER_POTION || protocol == SUPER_ENERGY_REMOVAL){
							player.deckAndDiscardPanel.addToDiscardPile(selfReference.energy.remove(dc.value));
							if(protocol == SUPER_POTION){
								selfReference.heal(40);
							}
							selfReference.setText(selfReference.toString());
						}

						else{
							player.deckAndDiscardPanel.addToDiscardPile(PokemonCard.currPlayerCard().energy.remove(dc.value));
						}
	
						
						for(int i=0; i<dummyList.size(); i++){
							dummyList.get(i).value--;
						}
						if(protocol == SUPER_ENERGY_REMOVAL){
							counter++;
						}
						else{
							counter += e.energyIncrease;
						}
						
						if(protocol == ENERGY_REMOVAL|| protocol == SUPER_POTION|| (protocol == RETREAT_COST && counter >= selfReference.getRetreatCost()) || 
								(protocol == SUPER_ENERGY_REMOVAL && counter==2)){
							removeMouseListeners();
							player.benchAndActiveCardPanel.reapplyPieces();
							dispose();	
						}
				}
				
				else if(protocol == CALL_FOR_FAMILY_SPECIAL || protocol == CALL_FOR_FAMILY || protocol == CALL_FOR_FRIEND || protocol == COMPUTER_SEARCH || protocol == POKEMON_TRADER||
						protocol == ENERGY_SEARCH){
					DummyCard dc = (DummyCard) arg0.getSource();
					PokemonCard pc;
					boolean decision = false;
					if(protocol == CALL_FOR_FAMILY_SPECIAL){
						decision = dc.value == 32 || dc.value == 29;
					}
					else if(protocol == ENERGY_SEARCH){
						Card c = (Card) realDeck.get(dc.value2);
						decision = (c.cardType == Card.ENERGY);
					}
					else if(protocol == CALL_FOR_FRIEND){
						if(dc.value == -1){
							decision = false;
						}
						else{
							pc = (PokemonCard) realDeck.get(dc.value2);
							decision = (pc.cardType == Card.BASIC_POKEMON);
						}
					}
					else if(protocol == CALL_FOR_FAMILY){
						decision = dc.value == selfReference.pokeId;
					}
					if(protocol != ENERGY_SEARCH && decision == true){
						pc = (PokemonCard) realDeck.get(dc.value2);
						player.deckAndDiscardPanel.deck.remove(pc);
						player.benchAndActiveCardPanel.addCardToBench(pc);
						Collections.shuffle(player.deckAndDiscardPanel.deck);
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					if(protocol == ENERGY_SEARCH && decision == true){
						EnergyCard e = (EnergyCard) realDeck.get(dc.value2);
						player.deckAndDiscardPanel.deck.remove(e);
						player.benchAndActiveCardPanel.addCardToHand(e);
						Collections.shuffle(player.deckAndDiscardPanel.deck);
						removeMouseListeners();
						player.benchAndActiveCardPanel.reapplyPieces();
						dispose();	
					}
					if(protocol == COMPUTER_SEARCH || protocol == POKEMON_TRADER){
						Card c = realDeck.get(dc.value2);
						if(protocol != POKEMON_TRADER ||c.isAPokemon()){
							player.deckAndDiscardPanel.deck.remove(c);
							player.benchAndActiveCardPanel.addCardToHand(c);
							Collections.shuffle(player.deckAndDiscardPanel.deck);
							removeMouseListeners();
							player.benchAndActiveCardPanel.reapplyPieces();
							dispose();
						}
					}
			}
			else if(protocol == HEAL || protocol == POTION || protocol == DEFENDER){
				PokemonCard pc;
				try{pc = (PokemonCard) arg0.getSource();}
				catch(Exception e){return;}
				if(protocol == HEAL){
					pc.heal(10);
				}
				else if(protocol == POTION){
					pc.heal(20);
				}	
				else if(protocol == DEFENDER){
					pc.shield += 20;
				}
				pc.setText(pc.toString());
				removeMouseListeners();			
				player.benchAndActiveCardPanel.reapplyPieces();
				dispose();	
			}
			else if(protocol == ENERGY_REMOVAL_CHOOSE || protocol == SUPER_ENERGY_REMOVAL_CHOOSE){
				PokemonCard pc;
				try{pc = (PokemonCard) arg0.getSource();}
				catch(Exception e){return;}
					if(protocol == ENERGY_REMOVAL_CHOOSE) new PokemonPowerFrame(player,PokemonPowerFrame.ENERGY_REMOVAL, PokemonCard.currPlayer().name+ ", discard an energy card.",pc);
					else new PokemonPowerFrame(player,PokemonPowerFrame.SUPER_ENERGY_REMOVAL, PokemonCard.currPlayer().name+ ", discard up to 2 energy cards.",pc);
					removeMouseListeners();		
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}	
			else if(protocol == SUPER_POTION_CHOOSE){
				PokemonCard pc;
				try{pc = (PokemonCard) arg0.getSource();}
				catch(Exception e){return;}
					new PokemonPowerFrame(player,PokemonPowerFrame.SUPER_POTION, PokemonCard.currPlayer().name+ ", discard an energy card.",pc);
					removeMouseListeners();		
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
				}	
			else if(protocol == DEVOLUTION_SPRAY_CHOOSE){
				PokemonCard pc;
				try{pc = (PokemonCard) arg0.getSource();}
				catch(Exception e){return;}
					new PokemonPowerFrame(player,PokemonPowerFrame.DEVOLUTION_SPRAY, PokemonCard.currPlayer().name+ ", devolve the pokemon.",pc);
					removeMouseListeners();		
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();	
					
			}
			else if(protocol == SHIFT || protocol == CONVERSION_1 || protocol == CONVERSION_2){
				DummyCard dc = (DummyCard) arg0.getSource();
				if(protocol == SHIFT) selfReference.type = dc.value;
				else if(protocol == CONVERSION_1) selfReference.weakness = dc.value;
				else if(protocol == CONVERSION_2) selfReference.resistance = dc.value;
				removeMouseListeners();
				player.benchAndActiveCardPanel.reapplyPieces();
				dispose();	
			}
			else if(protocol == HYPER_BEAM){
				DummyCard dc = (DummyCard) arg0.getSource();
				PokemonCard p = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
				p.removeEnergy(dc.value, player);
				p.setText(p.toString());
				removeMouseListeners();
				player.benchAndActiveCardPanel.reapplyPieces();
				dispose();	
			}
			else if(protocol == PEEK_PRIZE){
				DummyCard dc = (DummyCard) arg0.getSource();
				if(dc.path.equals("placeHolder") == false){
					JDialog d = new JDialog();
					d.setModal(true);
					JPanel p = new JPanel();
					dc.showFront();
					p.add(dc);
					d.add(p);
					d.setSize(200,200);
					d.setLocationRelativeTo(PokemonGame.game);
					d.setVisible(true);
					removeMouseListeners();
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();
				}
			}
		}

		

		@Override
		public void mousePressed(MouseEvent arg0) {
			for(int i=0; i<5; i++){
				bench[i].setDropTarget(null);
			}
			activeCard.setDropTarget(null);
			if(protocol == PROPHECY || protocol == POKEDEX){
					movingDropTarget = new DropTarget[5];
			
				movingCard = (Card) arg0.getSource();
				ProphecyListener proph = new ProphecyListener();
				for(int i=0; i<dummyList.size();i++){
					movingDropTarget[i] = new DropTarget();
					movingDropTarget[i].setComponent(dummyList.get(i));
					try {movingDropTarget[i].addDropTargetListener(proph);} 
					catch (TooManyListenersException e) {}
				}
				setTransfer(arg0);
			}
			else if(protocol == ENERGY_TRANS){
				//anything goes to anything
				movingCard = (Card) arg0.getSource();
				PokemonCard pc = null;
				try{pc = (PokemonCard) movingCard;}
				catch(Exception e){return;}

				if(hasLeafEnergy(pc)){
					movingDropTarget = new DropTarget[6];
					EnergyTransListener energyTrans = new EnergyTransListener();
					for(int i=0;i<5;i++){
						if(bench[i] != player.benchAndActiveCardPanel.benchHolders[i]){
								movingDropTarget[i] = new DropTarget();
								movingDropTarget[i].setComponent(bench[i]);
								try {movingDropTarget[i].addDropTargetListener(energyTrans);} 
		    					catch (TooManyListenersException e) {}
							
						}
					}
					if(activeCard != player.benchAndActiveCardPanel.activeHolder){
							movingDropTarget[5] = new DropTarget();
							movingDropTarget[5].setComponent(activeCard);
							try {movingDropTarget[5].addDropTargetListener(energyTrans);} 
	    					catch (TooManyListenersException e) {}
						
					}
					setTransfer(arg0);
				}	
			}
			else if(protocol == DAMAGE_SWAP || protocol == STRANGE_BEHAVIOR || protocol == CURSE){
				//anything goes to anything
				movingCard = (Card) arg0.getSource();
				PokemonCard pc = null;
				try{pc = (PokemonCard) movingCard;}
				catch(Exception e){return;}

				if(pc.currHp < pc.maxHp){
					movingDropTarget = new DropTarget[6];
					DamageSwapListener damageSwap = new DamageSwapListener();
					for(int i=0;i<5;i++){
						if(bench[i] != player.benchAndActiveCardPanel.benchHolders[i]){
							PokemonCard p = (PokemonCard) bench[i];
							if(protocol != STRANGE_BEHAVIOR || (p.pokeId == 80 && p.status == Card.NONE)){
								if(p.currHp > 10 || protocol == CURSE){
									movingDropTarget[i] = new DropTarget();
									movingDropTarget[i].setComponent(bench[i]);
									try {movingDropTarget[i].addDropTargetListener(damageSwap);} 
			    					catch (TooManyListenersException e) {}
								}
							}
							
						}
					}
					if(activeCard != player.benchAndActiveCardPanel.activeHolder){
						PokemonCard p = (PokemonCard) activeCard;
						if(protocol != STRANGE_BEHAVIOR || (p.pokeId == 80 && p.status == Card.NONE)){
							if(p.currHp > 10 || protocol == CURSE){
								movingDropTarget[5] = new DropTarget();
								movingDropTarget[5].setComponent(activeCard);
								try {movingDropTarget[5].addDropTargetListener(damageSwap);} 
		    					catch (TooManyListenersException e) {}
							}
						}
					}
					setTransfer(arg0);
				}	
			}
			else if(protocol == POKEMON_BREEDER){
				movingCard = (Card) arg0.getSource();
				if(movingCard.cardType != Card.STAGE_2_POKEMON){
					return;
				}
				PokemonCard pCard = (PokemonCard) movingCard;
				movingDropTarget = new DropTarget[6];
				EvolutionListener evolve = new EvolutionListener();
				PokemonCard pc;
				for(int i=0;i<5;i++){
					if(bench[i] != player.benchAndActiveCardPanel.benchHolders[i]){
						pc = (PokemonCard) bench[i];
						if(pc.cardType == Card.BASIC_POKEMON && pCard.basicStage.equals(pc.name) && PokemonGame.game.prehistoricPowerException() == false){
							movingDropTarget[i] = new DropTarget();
							movingDropTarget[i].setComponent(bench[i]);
							try {movingDropTarget[i].addDropTargetListener(evolve);} 
	    					catch (TooManyListenersException e) {}
						}
					}
				}
				if(activeCard != player.benchAndActiveCardPanel.activeHolder){
					pc = (PokemonCard) activeCard;
					if(pc.cardType == Card.BASIC_POKEMON && pCard.basicStage.equals(pc.name) && PokemonGame.game.prehistoricPowerException() == false){
						movingDropTarget[5] = new DropTarget();
						movingDropTarget[5].setComponent(activeCard);
						try {movingDropTarget[5].addDropTargetListener(evolve);} 
    					catch (TooManyListenersException e) {}
					}
				}
				setTransfer(arg0);
			}
			else if(protocol == RAIN_DANCE){
				movingCard = (Card) arg0.getSource();
				EnergyCard eCard = null;
				try{eCard = (EnergyCard) movingCard;}
				catch(Exception e){return;}
				movingDropTarget = new DropTarget[6];
				RainDanceListener rainDance = new RainDanceListener();
				PokemonCard pc;
				for(int i=0;i<5;i++){
					if(bench[i] != player.benchAndActiveCardPanel.benchHolders[i]){
						pc = (PokemonCard) bench[i];
						if(pc.type == Card.WATER){
							movingDropTarget[i] = new DropTarget();
							movingDropTarget[i].setComponent(bench[i]);
							try {movingDropTarget[i].addDropTargetListener(rainDance);} 
	    					catch (TooManyListenersException e) {}
						}
					}
				}
				if(activeCard != player.benchAndActiveCardPanel.activeHolder){
					pc = (PokemonCard) activeCard;
					if(pc.type == Card.WATER){
						movingDropTarget[5] = new DropTarget();
						movingDropTarget[5].setComponent(activeCard);
						try {movingDropTarget[5].addDropTargetListener(rainDance);} 
    					catch (TooManyListenersException e) {}
					}
				}
				setTransfer(arg0);
			}
			
		}
		
		private class ProphecyListener implements DropTargetListener {
			public void dragEnter(DropTargetDragEvent arg0) {}
			public void dragExit(DropTargetEvent arg0) {}
			public void dragOver(DropTargetDragEvent arg0) {}
			public void dropActionChanged(DropTargetDragEvent arg0) {}
			
			@Override
			public void drop(DropTargetDropEvent arg0) {
				movingCard.setTransferHandler(new TransferHandler(null));
				DummyCard dropCard, sourceCard;
				sourceCard = (DummyCard) movingCard;
				DropTarget dt = (DropTarget) arg0.getSource();
				int slot = -1;
		
				for(int i=0; i<5; i++){
					if(movingDropTarget[i] != null){
						if(movingDropTarget[i] == dt){
							slot = i;
						}
					}
				}
				dropCard = dummyList.get(slot);
				int sourceSlot = dummyList.indexOf(sourceCard);
				int dropSlot = dummyList.indexOf(dropCard);
				
					dummyList.remove(sourceCard);
					dummyList.add(dropSlot, sourceCard);
					Card c = realDeck.remove(sourceSlot);
					realDeck.add(dropSlot, c);
					repaintP3();
				
			}	
		}

		private class EnergyTransListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent arg0) {}
			public void dragExit(DropTargetEvent arg0) {}
			public void dragOver(DropTargetDragEvent arg0) {}
			public void dropActionChanged(DropTargetDragEvent arg0) {}
			
			@Override
			public void drop(DropTargetDropEvent arg0) {
				movingCard.setTransferHandler(new TransferHandler(null));
				PokemonCard dropCard, sourceCard;
				sourceCard = (PokemonCard) movingCard;
				DropTarget dt = (DropTarget) arg0.getSource();
				int slot = -1;
				for(int i=0; i<5; i++){
					if(movingDropTarget[i] != null){
						if(movingDropTarget[i] == dt){
							slot = i;
						}
					}
				}
				if(slot == -1){
					dropCard = (PokemonCard) activeCard;
				}
				else{
					dropCard = (PokemonCard) bench[slot];
				}
				transferLeafEnergy(sourceCard, dropCard);
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
				
				Card c =movingCard;
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
				activeCard.setDropTarget(null);
				if(slot == -1){
					player.benchAndActiveCardPanel.evolveActiveCard((PokemonCard) c);
					c.removeMouseListener(frame);
				}
				else{
					player.benchAndActiveCardPanel.evolveCardOnBench(slot, (PokemonCard)c);
				}
			
				player.benchAndActiveCardPanel.removeFromHand(movingCard);
				removeMouseListeners();
				player.benchAndActiveCardPanel.reapplyPieces();
				dispose();
			}
		}
		
		
		private class RainDanceListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent arg0) {}
			public void dragExit(DropTargetEvent arg0) {}
			public void dragOver(DropTargetDragEvent arg0) {}
			public void dropActionChanged(DropTargetDragEvent arg0) {}
			
			@Override
			public void drop(DropTargetDropEvent arg0) {
				movingCard.setTransferHandler(new TransferHandler(null));
				PokemonCard dropCard; 
				EnergyCard sourceCard = (EnergyCard) movingCard;
				DropTarget dt = (DropTarget) arg0.getSource();
				int slot = -1;
				for(int i=0; i<5; i++){
					if(movingDropTarget[i] != null){
						if(movingDropTarget[i] == dt){
							slot = i;
						}
					}
				}
				if(slot == -1){
					dropCard = (PokemonCard) activeCard;
				}
				else{
					dropCard = (PokemonCard) bench[slot];
				}
				addEnergy(dropCard, sourceCard);
				hand.remove(sourceCard);
				repaintHand();
			}	
		}
		
		private class DamageSwapListener implements DropTargetListener {

			public void dragEnter(DropTargetDragEvent arg0) {}
			public void dragExit(DropTargetEvent arg0) {}
			public void dragOver(DropTargetDragEvent arg0) {}
			public void dropActionChanged(DropTargetDragEvent arg0) {}
			
			@Override
			public void drop(DropTargetDropEvent arg0) {
				movingCard.setTransferHandler(new TransferHandler(null));
				PokemonCard dropCard; 
				PokemonCard sourceCard = (PokemonCard) movingCard;
				DropTarget dt = (DropTarget) arg0.getSource();
				int slot = -1;
				for(int i=0; i<5; i++){
					if(movingDropTarget[i] != null){
						if(movingDropTarget[i] == dt){
							slot = i;
						}
					}
				}
				if(slot == -1){
					dropCard = (PokemonCard) activeCard;
					dropCard.takeDamage(player, 10);
				}
				else{
					dropCard = (PokemonCard) bench[slot];
					dropCard.benchTakeDamage(player, 10, slot);
				}
				sourceCard.currHp += 10;
				sourceCard.setText(sourceCard.toString());
				dropCard.setText(dropCard.toString());
				if(protocol == CURSE){
					removeMouseListeners();
					player.benchAndActiveCardPanel.reapplyPieces();
					dispose();
				}
			}	
		}
		
	}
