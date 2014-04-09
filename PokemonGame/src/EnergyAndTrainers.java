import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract class EnergyCard extends Card {
	private static final long serialVersionUID = 1L;
	int type;
	int energyIncrease = 1;
	public EnergyCard(int eType, String picture){
		type = eType;
		setCardType(Card.ENERGY);
		setCardImage(picture);
	}
	
	public EnergyCard(int eType){
		type = eType;
		setCardType(Card.ENERGY);
	}
	
	
}

abstract class TrainerCard extends Card {
	private static final long serialVersionUID = 1L;
	int type, id;
	String name;
	final static int NO_SPECIAL_NEEDS = -1, MYSTERIOUS_FOSSIL = 0, CLEFAIRY_DOLL = 1;
	public TrainerCard(String name, int id, String picture){
		this.name = name;
		this.id = id;
		setCardType(Card.TRAINER);
		setCardImage(picture);
	}
	
	abstract void function();
	abstract boolean functionRequirements();
	
	@Override
	void showUpClose(){
		final JDialog d = new JDialog();
		d.setSize(500,400);
		d.setModal(true);
		d.setResizable(false);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setLayout(new BorderLayout());
		d.setLocationRelativeTo(PokemonGame.game);
		JLabel l = new JLabel();
		l.setIcon(bigFront);
		JPanel p1 = new JPanel();
		final Card c = this;
		JPanel p2 = new JPanel(new GridLayout(4,1,5,5));
		JButton b1 = new JButton("Use");
		if(functionRequirements() == true){
			b1.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					function();
					d.dispose();
					PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(c);
					PokemonCard.currPlayer().deckAndDiscardPanel.addToDiscardPile(c);
				}
				
			});
		}
		else{
			b1.setEnabled(false);
		}
		
		
		p1.add(l);
		p2.add(b1);
		JPanel p3 = new JPanel();
		p3.add(p1);
		p3.add(p2);
		
		d.add(p3, BorderLayout.CENTER);
		d.setVisible(true);
	}
}

//a non-energy card masquerades as an energy card. original card data is stored, along with the energy type and number of energy
class EnergyMasquerade extends EnergyCard {
	private static final long serialVersionUID = 1L;
	Card originalCard;
	public EnergyMasquerade(int eType, int eEnergyIncrease, Card eOriginalCard){
		super(eType);
		originalCard = eOriginalCard;
		energyIncrease = eEnergyIncrease;
		masquerade = true;
		setCardImage(originalCard.path);
		showFront();
	}
}

class PokemonMasquerade extends PokemonCard {
	private static final long serialVersionUID = 1L;
	Card originalCard;
	public PokemonMasquerade(Card originalCard, String name) {
		super(name);
		currHp = 10;
		maxHp = 10;
		this.originalCard = originalCard;
		masquerade = true;
		setCardImage(originalCard.path);
		showFront();
	}
	void moveOne() {}
	boolean moveOneRequirements() {
		return false;
	}
	void moveTwo() {}
	boolean moveTwoRequirements() {
		return false;
	}
	
}

class DittoMasquerade extends PokemonCard {
	private static final long serialVersionUID = 1L;
	PokemonCard imitation, ditto;
	public DittoMasquerade(PokemonCard imitation, PokemonCard ditto) {
		super(imitation.name, imitation.basicStage, imitation.middleStage, imitation.pokeId, imitation.maxHp, imitation.hasPokemonPower, imitation.type,
				imitation.weakness, imitation.resistance, imitation.getRetreatCost(), imitation.path, imitation.moveOne, imitation.moveTwo);
		this.imitation = imitation;
		this.ditto = ditto;
		energy.addAll(ditto.energy);
		ditto.energy.clear();
		masquerade = true;
		this.statusResistant = imitation.statusResistant;
		this.attack1Malicious = imitation.attack1Malicious;
		this.attack2Malicious = imitation.attack2Malicious;
		showFront();
		currHp -= (ditto.maxHp - ditto.currHp);
		if(currHp<0) currHp = 0;
	}
	void moveOne() {
		imitation.moveOne();
	}
	boolean moveOneRequirements() {
		return imitation.moveOneRequirements();
	}
	@Override
	void moveTwo() {
		imitation.moveTwo();
	}
	boolean moveTwoRequirements() {
		return imitation.moveTwoRequirements();
	}
	
}

class FightingEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public FightingEnergy(){
		super(Card.FIGHTING,"fightingEnergy.jpg");
	}
}

class LeafEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public LeafEnergy(){
		super(Card.LEAF,"grass.jpg");
	}
}

class FireEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public FireEnergy(){
		super(Card.FIRE,"fire.jpg");
	}
}

class WaterEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public WaterEnergy(){
		super(Card.WATER,"water.jpg");
	}
}

class ThunderEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public ThunderEnergy(){
		super(Card.THUNDER,"thunder.jpg");
	}
}

class PsychicEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public PsychicEnergy(){
		super(Card.PSYCHIC,"psychic.jpg");
	}
}

class DoubleColorlessEnergy extends EnergyCard {
	private static final long serialVersionUID = 1L;

	public DoubleColorlessEnergy(){
		super(Card.COLORLESS,"doublecolorless.jpg");
		energyIncrease = 2;
	}
}

class MysteriousFossil extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public MysteriousFossil(){
		super("Mysterious Fossil",TrainerCard.MYSTERIOUS_FOSSIL,"mysteriousfossil.jpg");
	}

	void function() {}
	boolean functionRequirements() {
		return false;
	}
}

class ClefairyDoll extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ClefairyDoll(){
		super("Clefairy Doll",TrainerCard.CLEFAIRY_DOLL,"clefairydoll.jpg");
	}

	void function() {}
	boolean functionRequirements() {
		return false;
	}
}

class Switch extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Switch(){
		super("Switch",TrainerCard.NO_SPECIAL_NEEDS,"switch.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.WHIRLWIND, PokemonCard.currPlayer().name+ ", select a bench pokemon with which to switch the opponent's active one.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			return false;
		}
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize ==0){
			return false;
		}
		return true;
	}
}

class GustOfWind extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public GustOfWind(){
		super("Gust Of Wind",TrainerCard.NO_SPECIAL_NEEDS,"gustofwind.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.opponent(),PokemonPowerFrame.WHIRLWIND, PokemonCard.currPlayer().name+ ", select a bench pokemon with which to switch the opponent's active one.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.opponent().benchAndActiveCardPanel.activeCard == PokemonCard.opponent().benchAndActiveCardPanel.activeHolder){
			return false;
		}
		if(PokemonCard.opponent().benchAndActiveCardPanel.benchSize ==0){
			return false;
		}
		return true;
	}
}

class Potion extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Potion(){
		super("Potion",TrainerCard.NO_SPECIAL_NEEDS,"potion.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POTION, PokemonCard.currPlayer().name+ ", select a pokemon to heal.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize ==0){
				return false;
			}
		}
		return true;
	}
}

class EnergyRemoval extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public EnergyRemoval(){
		super("Energy Removal",TrainerCard.NO_SPECIAL_NEEDS,"energyremoval.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.opponent(),PokemonPowerFrame.ENERGY_REMOVAL_CHOOSE, PokemonCard.currPlayer().name+ ", select a pokemon from which to remove energy.",PokemonCard.opponentCard());
	}
	boolean functionRequirements() {
		PokemonCard pc;
		if(PokemonCard.opponent().benchAndActiveCardPanel.activeCard == PokemonCard.opponent().benchAndActiveCardPanel.activeHolder){
			for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					return true;
				}
			}
		}
		else{
			pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.activeCard;
			if(pc.energy.size() > 0){
				return true;
			}
			for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					return true;
				}
			}
		}
		return false;
	}
}

class Bill extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Bill(){
		super("Bill",TrainerCard.NO_SPECIAL_NEEDS,"bill.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.addCardToHand(PokemonCard.currPlayer().deckAndDiscardPanel.deck.remove(0));
		PokemonCard.currPlayer().benchAndActiveCardPanel.addCardToHand(PokemonCard.currPlayer().deckAndDiscardPanel.deck.remove(0));
		PokemonCard.currPlayer().deckAndDiscardPanel.setDeckCount();
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() >= 2;
	}
}

class SuperPotion extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public SuperPotion(){
		super("Super Potion",TrainerCard.NO_SPECIAL_NEEDS,"superpotion.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.SUPER_POTION_CHOOSE, PokemonCard.currPlayer().name+ ", select a pokemon from which to remove energy.",PokemonCard.currPlayerCard());
	}
	boolean functionRequirements() {
		PokemonCard pc;
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					return true;
				}
			}
		}
		else{
			pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard;
			if(pc.energy.size() > 0){
				return true;
			}
			for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					return true;
				}
			}
		}
		return false;
	}
}

class Revive extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Revive(){
		super("Revive",TrainerCard.NO_SPECIAL_NEEDS,"revive.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.REVIVE, PokemonCard.currPlayer().name+ ", select a pokemon to revive.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize < 5){
			ArrayList<Card> discard = PokemonCard.currPlayer().deckAndDiscardPanel.discardPile;
			for(int i=0; i<discard.size(); i++){
				if(discard.get(i).cardType == Card.BASIC_POKEMON){
					return true;
				}
			}
			
		}
		return false;
	}
}

class ProfessorOak extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ProfessorOak(){
		super("Professor Oak",TrainerCard.NO_SPECIAL_NEEDS,"professoroak.jpg");
	}

	void function() {
		for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size(); i++){
			PokemonCard.currPlayer().deckAndDiscardPanel.addToDiscardPile(PokemonCard.currPlayer().benchAndActiveCardPanel.hand.get(i));
		}
		PokemonCard.currPlayer().benchAndActiveCardPanel.hand.clear();
		
		for(int i=0; i<7; i++){
			PokemonCard.currPlayer().benchAndActiveCardPanel.addCardToHand(PokemonCard.currPlayer().deckAndDiscardPanel.deck.remove(0));
		}
		PokemonCard.currPlayer().deckAndDiscardPanel.setDeckCount();
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() >= 7;
	}
}

class Pokedex extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Pokedex(){
		super("Pokedex",TrainerCard.NO_SPECIAL_NEEDS,"pokedex.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POKEDEX,"Arrange cards from the top of your deck as desired.",null);
	}
	boolean functionRequirements() {
		return true;
	}
}

class PokemonFlute extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PokemonFlute(){
		super("PokemonFlute",TrainerCard.NO_SPECIAL_NEEDS,"pokemonflute.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.opponent(),PokemonPowerFrame.POKEMON_FLUTE, PokemonCard.currPlayer().name+ ", select an enemy pokemon to revive.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.opponent().benchAndActiveCardPanel.benchSize < 5){
			ArrayList<Card> discard = PokemonCard.opponent().deckAndDiscardPanel.discardPile;
			for(int i=0; i<discard.size(); i++){
				if(discard.get(i).cardType == Card.BASIC_POKEMON){
					return true;
				}
			}
		}
		return false;
	}
}

class PokemonCenter extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PokemonCenter(){
		super("Pokemon Center",TrainerCard.NO_SPECIAL_NEEDS,"pokemoncenter.jpg");
	}

	void function() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard != PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			if(PokemonCard.currPlayerCard().currHp < PokemonCard.currPlayerCard().maxHp){
				PokemonCard.currPlayerCard().heal(500);
				for(int i=0; i<PokemonCard.currPlayerCard().energy.size();i++){
					PokemonCard.currPlayer().deckAndDiscardPanel.addToDiscardPile(PokemonCard.currPlayerCard().energy.get(i));
				}
				PokemonCard.currPlayerCard().energy.clear();
				PokemonCard.currPlayerCard().setText(PokemonCard.currPlayerCard().toString());
			}
		}
		PokemonCard pc;
		for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize;i++){
			pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.bench[i];
			if(pc.currHp < pc.maxHp){
				pc.heal(500);
				for(int j=0; j<pc.energy.size();j++){
					PokemonCard.currPlayer().deckAndDiscardPanel.addToDiscardPile(pc.energy.get(j));
				}
				pc.energy.clear();
				pc.setText(pc.toString());
			}
		}
	}
	boolean functionRequirements() {
		return true;
	}
}

class PlusPower extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PlusPower(){
		super("Plus Power",TrainerCard.NO_SPECIAL_NEEDS,"pluspower.jpg");
	}

	void function() {
		PokemonCard.currPlayerCard().attackBoost += 10;
		PokemonCard.currPlayerCard().attackBoostSwitch = false;
		PokemonCard.currPlayerCard().setText(PokemonCard.currPlayerCard().toString());
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			return false;
		}
		return true;
	}
}

class Maintenance extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Maintenance(){
		super("Maintenance",TrainerCard.NO_SPECIAL_NEEDS,"maintenance.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.MAINTENANCE_CHOOSE, PokemonCard.currPlayer().name+ ", select two cards to remove.",null);
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size() > 2 && PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() > 0;
	}
}

class FullHeal extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public FullHeal(){
		super("Full Heal",TrainerCard.NO_SPECIAL_NEEDS,"fullheal.jpg");
	}

	void function() {
		PokemonCard.currPlayerCard().status = Card.NONE;
		PokemonCard.currPlayerCard().poisoned = false;
		PokemonCard.currPlayerCard().toxic = false;
		PokemonCard.currPlayerCard().setText(PokemonCard.currPlayerCard().toString());
		PokemonCard.dittoWrapException();
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard != PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder;
	}
}

class EnergyRetrieval extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public EnergyRetrieval(){
		super("Energy Retrieval",TrainerCard.NO_SPECIAL_NEEDS,"energyretrieval.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		PokemonCard.currPlayer().deckAndDiscardPanel.addToDiscardPile(this);
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.ENERGY_RETRIEVAL, PokemonCard.currPlayer().name+ ", select a card to discard.",null);
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size() > 0;
	}
}

class Defender extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Defender(){
		super("Defender",TrainerCard.NO_SPECIAL_NEEDS,"defender.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.DEFENDER, PokemonCard.currPlayer().name+ ", select a pokemon to add the shield to.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize ==0){
				return false;
			}
		}
		return true;
	}
}

class SuperEnergyRemoval extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public SuperEnergyRemoval(){
		super("Super Energy Removal",TrainerCard.NO_SPECIAL_NEEDS,"superenergyremoval.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.ENERGY_REMOVAL_CHOOSE, PokemonCard.currPlayer().name+ ", select a fellow pokemon from which to remove energy.",PokemonCard.opponentCard());
		new PokemonPowerFrame(PokemonCard.opponent(),PokemonPowerFrame.SUPER_ENERGY_REMOVAL_CHOOSE, PokemonCard.currPlayer().name+ ", select a pokemon from which to remove energy.",PokemonCard.opponentCard());
	}
	boolean functionRequirements() {
		PokemonCard pc;
		boolean opponent = false, currPlayer = false;
		
		if(PokemonCard.opponent().benchAndActiveCardPanel.activeCard == PokemonCard.opponent().benchAndActiveCardPanel.activeHolder){
			for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					opponent = true;
				}
			}
		}
		else{
			pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.activeCard;
			if(pc.energy.size() > 0){
				opponent = true;;
			}
			for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.opponent().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					opponent = true;
				}
			}
		}
		
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					currPlayer = true;
				}
			}
		}
		else{
			pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard;
			if(pc.energy.size() > 0){
				currPlayer = true;;
			}
			for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize; i++){
				pc = (PokemonCard) PokemonCard.currPlayer().benchAndActiveCardPanel.bench[i];
				if(pc.energy.size() > 0){
					currPlayer = true;
				}
			}
		}

		return opponent && currPlayer;
	}
}

class ScoopUp extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ScoopUp(){
		super("Scoop Up",TrainerCard.NO_SPECIAL_NEEDS,"scoopup.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.SCOOP_UP, PokemonCard.currPlayer().name+ ", select a pokemon to scoop up.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize ==0){
				return false;
			}
		}
		return true;
	}
}

class PokemonTrader extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PokemonTrader(){
		
		super("Pokemon Trader",TrainerCard.NO_SPECIAL_NEEDS,"pokemontrader.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POKEMON_TRADER_CHOOSE, PokemonCard.currPlayer().name+ ", select a pokemon to remove.",null);
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() == 0){
			return false;
		}
		for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size();i++){
			if(PokemonCard.currPlayer().benchAndActiveCardPanel.hand.get(i).isAPokemon()){
				return true;
			}
		}
		return false;
	}
}

class PokemonBreeder extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PokemonBreeder(){
		super("Pokemon Breeder",TrainerCard.NO_SPECIAL_NEEDS,"pokemonbreeder.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POKEMON_BREEDER, PokemonCard.currPlayer().name+ ", evolve a basic pokemon.",null);
	}
	boolean functionRequirements() {
		return true;
	}
}

class Lass extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Lass(){
		super("Lass",TrainerCard.NO_SPECIAL_NEEDS,"lass.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.LASS, "Both player's hands",null);
		Card c;
		ArrayList<Card> cardsToRemove = new ArrayList<Card>();
		for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size(); i++){
			c = PokemonCard.currPlayer().benchAndActiveCardPanel.hand.get(i);
			if(c.cardType == Card.TRAINER){
				cardsToRemove.add(c);
			}
		}
		for(int i=0; i<cardsToRemove.size();i++){
			PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(cardsToRemove.get(i));
			PokemonCard.currPlayer().deckAndDiscardPanel.addToDeck(cardsToRemove.get(i));
		}
		cardsToRemove.clear();
		for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.hand.size(); i++){
			c = PokemonCard.opponent().benchAndActiveCardPanel.hand.get(i);
			if(c.cardType == Card.TRAINER){
				cardsToRemove.add(c);
			}
		}
		for(int i=0; i<cardsToRemove.size();i++){
			PokemonCard.opponent().benchAndActiveCardPanel.removeFromHand(cardsToRemove.get(i));
			PokemonCard.opponent().deckAndDiscardPanel.addToDeck(cardsToRemove.get(i));
		}
		cardsToRemove.clear();
		
	}
	boolean functionRequirements() {
		return true;
	}
}

class ItemFinder extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ItemFinder(){
		
		super("Item Finder",TrainerCard.NO_SPECIAL_NEEDS,"itemfinder.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.ITEM_FINDER_CHOOSE, PokemonCard.currPlayer().name+ ", select two cards to discard.",null);
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size() >= 2;
	}
}

class ImposterProfessorOak extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ImposterProfessorOak(){
		super("Imposter Professor Oak",TrainerCard.NO_SPECIAL_NEEDS,"imposterprofessoroak.jpg");
	}

	void function() {
		for(int i=0; i<PokemonCard.opponent().benchAndActiveCardPanel.hand.size(); i++){
			PokemonCard.opponent().deckAndDiscardPanel.addToDiscardPile(PokemonCard.opponent().benchAndActiveCardPanel.hand.get(i));
		}
		PokemonCard.opponent().benchAndActiveCardPanel.hand.clear();
		
		for(int i=0; i<7; i++){
			
			PokemonCard.opponent().benchAndActiveCardPanel.addCardToHand(PokemonCard.opponent().deckAndDiscardPanel.deck.remove(0));
		}
		PokemonCard.opponent().setOrientation(true);
		PokemonCard.opponent().deckAndDiscardPanel.setDeckCount();
	}
	boolean functionRequirements() {
		return PokemonCard.opponent().deckAndDiscardPanel.deck.size() >= 7;
	}
}

class DevolutionSpray extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public DevolutionSpray(){
		
		super("Devolution Spray",TrainerCard.NO_SPECIAL_NEEDS,"devolutionspray.jpg");
	}

	void function() {
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.DEVOLUTION_SPRAY_CHOOSE, PokemonCard.currPlayer().name+ ", select a card to devolve.",PokemonCard.currPlayerCard());
	}
	boolean functionRequirements() {
		if(PokemonCard.currPlayer().benchAndActiveCardPanel.activeCard == PokemonCard.currPlayer().benchAndActiveCardPanel.activeHolder){
			if(PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize ==0){
				return false;
			}
		}
		return true;
	}
}

class ComputerSearch extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public ComputerSearch(){
		super("Computer Search",TrainerCard.NO_SPECIAL_NEEDS,"computersearch.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.COMPUTER_SEARCH_CHOOSE, PokemonCard.currPlayer().name+ ", select cards to discard.",PokemonCard.currPlayerCard());
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size() > 2 && PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() > 0;
	}
}

class PokeBall extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public PokeBall(){
		super("Poke Ball",TrainerCard.NO_SPECIAL_NEEDS,"pokeball.jpg");
	}

	void function() {
		if(PokemonCard.flipCoin() == true){
			new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.POKEMON_TRADER, PokemonCard.currPlayer().name+ ", add a pokemon to your hand.",PokemonCard.currPlayerCard());
		}
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() > 0;
	}
}

class Recycle extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Recycle(){
		super("Recycle",TrainerCard.NO_SPECIAL_NEEDS,"recycle.jpg");
	}

	void function() {
		if(PokemonCard.flipCoin() == true){
			new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.RECYCLE, PokemonCard.currPlayer().name+ ", select a card.",PokemonCard.currPlayerCard());
		}
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.discardPile.size() > 0;
	}
}

class Gambler extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public Gambler(){
		super("Gambler",TrainerCard.NO_SPECIAL_NEEDS,"gambler.jpg");
	}

	void function() {
		PokemonCard.currPlayer().benchAndActiveCardPanel.removeFromHand(this);
		for(int i=0; i<PokemonCard.currPlayer().benchAndActiveCardPanel.hand.size(); i++){
			PokemonCard.currPlayer().deckAndDiscardPanel.addToDeck(PokemonCard.currPlayer().benchAndActiveCardPanel.hand.get(i));
		}
		PokemonCard.currPlayer().benchAndActiveCardPanel.hand.clear();
		Collections.shuffle(PokemonCard.currPlayer().deckAndDiscardPanel.deck);
		if(PokemonCard.flipCoin() == true){
			for(int i=0; i<8; i++){
				PokemonCard.currPlayer().benchAndActiveCardPanel.addCardToHand(PokemonCard.currPlayer().deckAndDiscardPanel.deck.remove(0));
			}
			
		}
		else{
			for(int i=0; i<1; i++){
				PokemonCard.currPlayer().benchAndActiveCardPanel.addCardToHand(PokemonCard.currPlayer().deckAndDiscardPanel.deck.remove(0));
			}
		}
		PokemonCard.currPlayer().deckAndDiscardPanel.setDeckCount();
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() > 1;
	}
}

class EnergySearch extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public EnergySearch(){
		super("Energy Search",TrainerCard.NO_SPECIAL_NEEDS,"energysearch.jpg");
	}

	void function() {
			new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.ENERGY_SEARCH, PokemonCard.currPlayer().name+ ", select an energy card.",PokemonCard.currPlayerCard());
		
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().deckAndDiscardPanel.deck.size() > 0;
	}
}

class MrFuji extends TrainerCard {
	private static final long serialVersionUID = 1L;

	public MrFuji(){
		super("Mr. Fuji",TrainerCard.NO_SPECIAL_NEEDS,"mrfuji.jpg");
	}

	void function() {
			new PokemonPowerFrame(PokemonCard.currPlayer(),PokemonPowerFrame.MR_FUJI, PokemonCard.currPlayer().name+ ", select a pokemon.",PokemonCard.currPlayerCard());
		
	}
	boolean functionRequirements() {
		return PokemonCard.currPlayer().benchAndActiveCardPanel.benchSize > 0;
	}
}
