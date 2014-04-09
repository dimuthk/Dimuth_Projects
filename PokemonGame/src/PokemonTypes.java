import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class Bulbasaur extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Bulbasaur(){
		super("Bulbasaur",null,null,1,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"bulbasaur.jpg","Leech Seed",null);
	}
	void moveOne() {
		int modifiedD, heal =0;
		if((modifiedD = modifiedDamage(20))>0){
			heal = 10;
		}
		drainAttack(modifiedD, heal);
	}
	void moveTwo() {}
	
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Ivysaur extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Ivysaur(){
		super("Ivysaur","Bulbasaur",null,2,60,false,Card.LEAF,Card.FIRE,Card.NONE,1,"ivysaur.jpg","Vine Whip","Poisonpowder");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		poisonAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Venusaur extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Venusaur(){
		super("Venusaur","Bulbasaur","Ivysaur",3,100,true,Card.LEAF,Card.FIRE,Card.NONE,2,"venusaur.jpg","Energy Trans","Solarbeam");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.ENERGY_TRANS,"Drag leaf energies between pokemon to transfer energy.",this);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(60));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 4);
	}
}

class Charmander extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Charmander(){
		super("Charmander",null,null,4,50,false,Card.FIRE,Card.WATER,Card.NONE,1,"charmander.jpg","Scratch","Ember");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(30),Card.FIRE,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.FIRE, 1);
	}
}

class Charmeleon extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Charmeleon(){
		super("Charmeleon","Charmander",null,5,80,false,Card.FIRE,Card.WATER,Card.NONE,1,"charmeleon.jpg","Slash","Flamethrower");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(50),Card.FIRE,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
}

class Charizard extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Charizard(){
		super("Charizard","Charmander","Charmeleon",6,120,true,Card.FIRE,Card.WATER,Card.FIGHTING,3,"charizard.jpg","Energy Burn","Fire Spin");
	}
	void moveOne() {}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(100),Card.COLORLESS,2);
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return (status == Card.NONE && currPlayerCard().hasEnergy(Card.COLORLESS, 4) && PokemonGame.game.toxicGasException() == false) || currPlayerCard().hasEnergy(Card.FIRE, 4);
	}
}

class Squirtle extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Squirtle(){
		super("Squirtle",null,null,7,40,false,Card.WATER,Card.THUNDER,Card.NONE,1,"squirtle.jpg","Bubble","Withdraw");
		attack2Malicious = false;
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		if(flipCoin() == true){
			shield(500);
		}
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Wartortle extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Wartortle(){
		super("Wartortle","Squirtle",null,8,70,false,Card.WATER,Card.THUNDER,Card.NONE,1,"wartortle.jpg","Withdraw","Bite");
		attack1Malicious = false;
	}
	void moveOne() {
		if(flipCoin() == true){
			shield(500);
		}
	}
	void moveTwo() {
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Blastoise extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Blastoise(){
		super("Blastoise","Squirtle","Wartortle",9,100,true,Card.WATER,Card.THUNDER,Card.NONE,3,"blastoise.jpg","Rain Dance","Hydro Pump");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.RAIN_DANCE,"Attach water energies to your water pokemon as desired.",this);
	}
	void moveTwo() {
		int damage = 40;
		if(currPlayerCard().hasEnergy(Card.WATER, 4)) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 5)) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 3);
	}
}

class Caterpie extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Caterpie(){
		super("Caterpie",null,null,10,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"caterpie.jpg","String Shot",null);
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Metapod extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Metapod(){
		super("Metapod","Caterpie",null,11,70,false,Card.LEAF,Card.FIRE,Card.NONE,2,"metapod.jpg","Stiffen","Stun Spore");
		attack1Malicious = false;
	}
	void moveOne() {
		if(flipCoin() == true){
			shield(500);
		}
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Butterfree extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Butterfree(){
		super("Butterfree","Caterpie","Metapod",12,70,false,Card.LEAF,Card.FIRE,Card.FIGHTING,0,"butterfree.jpg","Whirlwind","Mega Drain");
	}
	void moveOne() {
		Player opponent = opponent();
		standardAttack(modifiedDamage(20));
		if(opponent.benchAndActiveCardPanel.benchSize == 0 || opponent.benchAndActiveCardPanel.activeCard == opponent.benchAndActiveCardPanel.activeHolder){
			return;
		}
		else{
			new PokemonPowerFrame(opponent,PokemonPowerFrame.WHIRLWIND,"Select a bench pokemon with which to switch the active one.",this);
		}
	}
	void moveTwo() {
		int modifiedD = modifiedDamage(40);
		int	heal = modifiedD/2;
		if(heal % 10 != 0) heal += 5;
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 4);
	}
}

class Weedle extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Weedle(){
		super("Weedle",null,null,13,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"weedle.jpg","Poison Sting",null);
	}
	void moveOne() {
		poisonAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Kakuna extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kakuna(){
		super("Kakuna","Weedle",null,14,80,false,Card.LEAF,Card.FIRE,Card.NONE,2,"kakuna.jpg","Stiffen","Poisonpowder");
		attack1Malicious = false;
	}
	void moveOne() {
		if(flipCoin() == true){
			shield(500);
		}
	}
	void moveTwo() {
		poisonAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Beedrill extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Beedrill(){
		super("Beedrill","Weedle","Kakuna",15,80,false,Card.LEAF,Card.FIRE,Card.FIGHTING,0,"beedrill.jpg","Twineedle","Poison Sting");
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	void moveTwo() {
		poisonAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Pidgey extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Pidgey(){
		super("Pidgey",null,null,16,40,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,1,"pidgey.jpg","Whirlwind",null);
	}
	void moveOne() {
		Player opponent = opponent();
		standardAttack(modifiedDamage(10));
		if(opponent.benchAndActiveCardPanel.benchSize == 0 || opponent.benchAndActiveCardPanel.activeCard == opponent.benchAndActiveCardPanel.activeHolder){
			return;
		}
		else{
			new PokemonPowerFrame(opponent,PokemonPowerFrame.WHIRLWIND,"Select a bench pokemon with which to switch the active one.",this);
		}
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Pidgeotto extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Pidgeotto(){
		super("Pidgeotto","Pidgey",null,17,60,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,1,"pidgeotto.jpg","Whirlwind","Mirror Move");
	}
	void moveOne() {
		Player opponent = opponent();
		standardAttack(modifiedDamage(20));
		if(opponent.benchAndActiveCardPanel.benchSize == 0 || opponent.benchAndActiveCardPanel.activeCard == opponent.benchAndActiveCardPanel.activeHolder){
			return;
		}
		else{
			new PokemonPowerFrame(opponent,PokemonPowerFrame.WHIRLWIND,"Select a bench pokemon with which to switch the active one.",this);
		}
	}
	void moveTwo() {
		mirrorMoveAttack();
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3);
	}
}

class Pidgeot extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Pidgeot(){
		super("Pidgeot","Pidgey","Pidgeotto",18,80,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,0,"pidgeot.gif","Wing Attack","Hurricane");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
		Player opponent = opponent();
		if(opponent.benchAndActiveCardPanel.activeCard != opponent.benchAndActiveCardPanel.activeHolder){
			opponent.benchAndActiveCardPanel.activeToHand();
		}
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3);
	}
}

class Rattata extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Rattata(){
		super("Rattata",null,null,19,30,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,0,"rattata.jpg","Bite",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Raticate extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Raticate(){
		super("Raticate","Rattata",null,20,60,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"raticate.jpg","Bite","Super Fang");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		PokemonCard opponentCard = opponentCard();
		int damage = opponentCard.currHp/2;
		if(damage % 10 != 0) damage += 5;
		standardAttack(damage);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Spearow extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Spearow(){
		super("Spearow",null,null,21,50,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,0,"spearow.jpg","Peck","Mirror Move");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		mirrorMoveAttack();
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Fearow extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Fearow(){
		super("Fearow","Spearow",null,22,70,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,0,"fearow.jpg","Agility","Drill Peck");
	}
	void moveOne() {
		tryAgility();
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4);
	}
}

class Ekans extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Ekans(){
		super("Ekans",null,null,23,40,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"ekans.jpg","Spit Poison","Wrap");
	}
	void moveOne() {
		poisonAttack(0);
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
}

class Arbok extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Arbok(){
		super("Arbok","Ekans",null,24,60,false,Card.LEAF,Card.PSYCHIC,Card.NONE,3,"arbok.jpg","Terror Strike","Poison Fang");
	}
	void moveOne() {
		Player opponent = opponent();
		standardAttack(modifiedDamage(10));
		if(opponent.benchAndActiveCardPanel.benchSize == 0 || opponent.benchAndActiveCardPanel.activeCard == opponent.benchAndActiveCardPanel.activeHolder){
			return;
		}
		else{
			new PokemonPowerFrame(opponent,PokemonPowerFrame.WHIRLWIND,"Select a bench pokemon with which to switch the active one.",this);
		}
	}
	void moveTwo() {
		poisonAttackGuaranteed(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class PikachuClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public PikachuClassic(){
		super("Pikachu",null,null,25,40,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"pikachu_classic.jpg","Gnaw","Thunder Jolt");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		hurtSelfAttack(modifiedDamage(30),10);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
}

class PikachuJungle extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public PikachuJungle(){
		super("Pikachu",null,null,25,50,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"pikachu_jungle.jpg","Spark",null);
	}
	void moveOne() {
		if(opponent().benchAndActiveCardPanel.benchSize > 0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.SPARK,"Select a bench pokemon to attack.",this);
		}
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class RaichuClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public RaichuClassic(){
		super("Raichu","Pikachu",null,26,80,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"raichu_classic.jpg","Agility","Thunder");
	}
	void moveOne() {
		tryAgility();
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		hurtSelfAttack(modifiedDamage(60),30);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.THUNDER, 3);
	}
}

class RaichuFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public RaichuFossil(){
		super("Raichu","Pikachu",null,26,90,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"raichu_fossil.jpg","Gigashock",null);
	}
	void moveOne() {
		if(opponent().benchAndActiveCardPanel.benchSize >3){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.GIGA_SHOCK,"Select 3 bench pokemon to attack.",this);
		}
		else{
			for(int i=0; i<opponent().benchAndActiveCardPanel.benchSize;i++){
				PokemonCard pc = (PokemonCard) opponent().benchAndActiveCardPanel.bench[i];
				 pc.benchTakeDamage(opponent(),10,i);
			}
		}
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 4);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Sandshrew extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Sandshrew(){
		super("Sandshrew",null,null,27,40,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,1,"sandshrew.jpg","Sand-attack",null);
	}
	void moveOne() {
		specialStatusAttack(modifiedDamage(10),"Sand-attack");
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Sandslash extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Sandslash(){
		super("Sandslash","Sandshrew",null,28,70,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,1,"sandslash.jpg","Slash","Fury Swipes");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(20),3);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
}

class NidoranFemale extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public NidoranFemale(){
		super("Nidoran \u2640",null,null,29,60,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"nidoranf.jpg","Fury Swipes","Call for Family");
		attack2Malicious = false;
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(10),3);
	}
	void moveTwo() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.CALL_FOR_FAMILY_SPECIAL,"Select a Nidoran \u2640 or Nidoran \u2642 from the deck to add to your bench.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2) && PokemonGame.game.currPlayer.benchAndActiveCardPanel.benchSize < 5;
	}
}

class Nidorina extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Nidorina(){
		super("Nidorina","Nidoran \u2640",null,30,70,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"nidorina.jpg","Supersonic","Double Kick");
	}
	void moveOne() {
		confuseAttack(0);
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
}

class Nidoqueen extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Nidoqueen(){
		super("Nidoqueen","Nidoran \u2640","Nidorina",31,90,false,Card.LEAF,Card.PSYCHIC,Card.NONE,3,"nidoqueen.jpg","Boyfriends","Mega Punch");
	}
	void moveOne() {
		int damage = 20;
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
			if(pc.name.equals("Nidoking")){
				damage+= 20;
			}
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class NidoranMale extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public NidoranMale(){
		super("Nidoran \u2642",null,null,32,40,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"nidoranm.jpg","Horn Hazard",null);
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(30),1);
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Nidorino extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Nidorino(){
		super("Nidorino","Nidoran \u2642",null,33,60,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"nidorino.jpg","Double Kick","Horn Drill");
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Nidoking extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Nidoking(){
		super("Nidoking","Nidoran \u2642","Nidorino",34,90,false,Card.LEAF,Card.PSYCHIC,Card.NONE,3,"nidoking.jpg","Thrash","Toxic");
	}
	void moveOne() {
		int damage = 30;
		if(flipCoin() == true){
			damage += 10;
		}
		else{
			takeDamage(currPlayer(),10);
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		toxicAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Clefairy extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Clefairy(){
		super("Clefairy",null,null,35,40,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"clefairy.jpg","Sing","Metronome");
	}
	void moveOne() {
		sleepAttack(modifiedDamage(0));
	}
	void moveTwo() {
		new PokemonPowerFrame(opponent(),PokemonPowerFrame.METRONOME,"Select an attack to mimic.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && opponentCard().pokeId != 132;
	}
}

class Clefable extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Clefable(){
		super("Clefable","Clefairy",null,36,70,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,2,"clefable.jpg","Metronome","Minimize");
		attack2Malicious = false;
	}
	void moveOne() {
		new PokemonPowerFrame(opponent(),PokemonPowerFrame.METRONOME,"Select an attack to mimic.",this);
	}
	void moveTwo() {
		shield(20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1) && opponentCard().pokeId != 132;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
}

class Vulpix extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Vulpix(){
		super("Vulpix",null,null,37,50,false,Card.FIRE,Card.WATER,Card.NONE,1,"vulpix.jpg","Confuse Ray",null);
	}
	void moveOne() {
		confuseAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Ninetales extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Ninetales(){
		super("Ninetales","Vulpix",null,38,80,false,Card.FIRE,Card.WATER,Card.NONE,1,"ninetales.jpg","Lure","Fire Blast");
	}
	void moveOne() {
		if(opponent().benchAndActiveCardPanel.benchSize >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.WHIRLWIND, currPlayer().name+ ", select a bench pokemon with which to switch the opponent's active one.",this);
		}
		else{
			JOptionPane.showMessageDialog(null, "No bench pokemon to switch with!");
		}
	}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(80),Card.FIRE,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 4);
	}
}

class Jigglypuff extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Jigglypuff(){
		super("Jigglypuff",null,null,39,60,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"jigglypuff.jpg","Lullaby","Pound");
	}
	void moveOne() {
		sleepAttackGuaranteed(modifiedDamage(0));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
}

class Wigglytuff extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Wigglytuff(){
		super("Wigglytuff","Jigglypuff",null,40,80,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,2,"wigglytuff.jpg","Lullaby","Do the Wave");
	}
	void moveOne() {
		sleepAttackGuaranteed(modifiedDamage(0));
	}
	void moveTwo() {
		int damage = 10;
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
			damage+= 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
}

class Zubat extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Zubat(){
		super("Zubat",null,null,41,40,false,Card.LEAF,Card.PSYCHIC,Card.FIGHTING,0,"zubat.jpg","Supersonic","Leech Life");
	}
	void moveOne() {
		confuseAttack(modifiedDamage(0));
	}
	void moveTwo() {
		int modifiedD = modifiedDamage(10);
		int	heal = modifiedD;
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
}

class Golbat extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Golbat(){
		super("Golbat","Zubat",null,42,60,false,Card.LEAF,Card.PSYCHIC,Card.FIGHTING,0,"golbat.jpg","Wing Attack","Leech Life");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		int modifiedD = modifiedDamage(20);
		int	heal = modifiedD;
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Oddish extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Oddish(){
		super("Oddish",null,null,43,50,false,Card.LEAF,Card.FIRE,Card.NONE,1,"oddish.jpg","Stun Spore","Sprout");
		attack2Malicious = false;
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.CALL_FOR_FAMILY,"Select an Oddish from the deck to add to your bench.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Gloom extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Gloom(){
		super("Gloom","Oddish",null,44,60,false,Card.LEAF,Card.FIRE,Card.NONE,1,"gloom.jpg","Poisonpowder","Foul Odor");
	}
	void moveOne() {
		poisonAttackGuaranteed(0);
	}
	void moveTwo() {
		confuseSelf();
		confuseAttackGuaranteed(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Vileplume extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Vileplume(){
		super("Vileplume","Oddish","Gloom",45,80,true,Card.LEAF,Card.FIRE,Card.NONE,2,"vileplume.jpg","Heal","Petal Dance");
	}
	void moveOne() {
		if(flipCoin() == true){
			new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.HEAL,"Select a pokemon to heal.",this);
		}
		 powerPerformed  = true;
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(40),3);
		confuseSelf();
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE) &&  powerPerformed  == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Paras extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Paras(){
		super("Paras",null,null,46,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"paras.jpg","Scratch","Spore");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		sleepAttackGuaranteed(modifiedDamage(0));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Parasect extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Parasect(){
		super("Parasect","Paras",null,47,60,false,Card.LEAF,Card.FIRE,Card.NONE,1,"parasect.jpg","Spore","Slash");
	}
	void moveOne() {
		sleepAttackGuaranteed(0);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Venonat extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Venonat(){
		super("Venonat",null,null,48,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"venonat.jpg","Stun Spore","Leech Life");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		int modifiedD = modifiedDamage(10);
		int	heal = modifiedD;
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
}

class Venomoth extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Venomoth(){
		super("Venomoth","Venonat",null,49,70,true,Card.LEAF,Card.FIRE,Card.FIGHTING,0,"venomoth.jpg","Shift","Venom Powder");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.SHIFT,"Choose a type for Venomoth.",this);
		 powerPerformed  = true;
	}
	void moveTwo() {
		confuseAndPoisonAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE) &&  powerPerformed  == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}


class Diglett extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Diglett(){
		super("Diglett",null,null,50,30,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,0,"diglett.jpg","Dig","Mud Slap");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
}

class Dugtrio extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Dugtrio(){
		super("Dugtrio","Diglett",null,51,70,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,2,"dugtrio.jpg","Slash","Earthquake");
	}
	void moveOne() {
		standardAttack(modifiedDamage(40));
	}
	void moveTwo() {
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
			pc.benchTakeDamage(currPlayer(), 10, i);
		}
		standardAttack(modifiedDamage(70));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,4);
	}
}

class Meowth extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Meowth(){
		super("Meowth",null,null,52,50,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"meowth.jpg","Pay Day",null);
	}
	void moveOne() {
		if(flipCoin() == true){
			try{
				currPlayer().benchAndActiveCardPanel.addCardToHand(currPlayer().deckAndDiscardPanel.deck.remove(0));
			}
			catch(Exception e){}
		}
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Persian extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Persian(){
		super("Persian","Meowth",null,53,70,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,0,"persian.jpg","Scratch","Pounce");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		specialStatusAttack(modifiedDamage(30),"Pounced");
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3);
	}
}

class Psyduck extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Psyduck(){
		super("Psyduck",null,null,54,50,false,Card.WATER,Card.THUNDER,Card.NONE,1,"psyduck.jpg","Headache","Fury Swipes");
	}
	void moveOne() {
		JOptionPane.showMessageDialog(null, currPlayer().name + " can't play trainer cards next round.");
		currPlayer().canPlayTrainer = false;
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(10),3);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
}

class Golduck extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Golduck(){
		super("Golduck","Psyduck",null,55,70,false,Card.WATER,Card.THUNDER,Card.NONE,1,"golduck.jpg","Psyshock","Hyper Beam");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		if(opponentCard().energy.size() >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.HYPER_BEAM,"Choose an energy card to discard.",this);
		}
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
}

class Mankey extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Mankey(){
		super("Mankey",null,null,56,30,true,Card.FIGHTING,Card.PSYCHIC,Card.NONE,0,"mankey.jpg","Peek","Scratch");
	}
	void moveOne() {
		final JDialog d = new JDialog();
		d.setSize(200,200);
		d.setResizable(false);
		d.setLocationRelativeTo(PokemonGame.game);
		d.setTitle("Peek");
		d.setModal(true);
		JPanel p = new JPanel(new GridLayout(4,1));
		ButtonGroup b = new ButtonGroup();
		JRadioButton selfPrize = new JRadioButton(currPlayer().name + " peek prize");
		selfPrize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.PEEK_PRIZE,"Select a prize card to look at.",null); d.dispose();}
		});
		JRadioButton opponentPrize = new JRadioButton(opponent().name + " peek prize");
		opponentPrize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(opponent(),PokemonPowerFrame.PEEK_PRIZE,"Select a prize card to look at.",null); d.dispose();} 
		});
		JRadioButton selfDeck = new JRadioButton(currPlayer().name + " peek deck");
		selfDeck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.PEEK_DECK,"Top card of your deck",null); d.dispose();}
		});
		JRadioButton opponentDeck = new JRadioButton(opponent().name + " peek deck");
		opponentDeck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(opponent(),PokemonPowerFrame.PEEK_DECK,"Top card of your deck",null); d.dispose();}
		});
		b.add(selfPrize); b.add(opponentPrize); b.add(selfDeck); b.add(opponentDeck);
		p.add(selfPrize); p.add(opponentPrize); p.add(selfDeck); p.add(opponentDeck);
		d.add(p);
		d.setVisible(true);
		 powerPerformed  = true;
	}
	void moveTwo() {
		standardAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE) &&  powerPerformed  == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,1);
	}
}

class Primeape extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Primeape(){
		super("Primeape","Mankey",null,57,70,false,Card.FIGHTING,Card.PSYCHIC,Card.NONE,1,"primeape.jpg","Fury Swipes","Tantrum");
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(20),3);
	}
	void moveTwo() {
		confuseSelfChance();
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
}

class Growlithe extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Growlithe(){
		super("Growlithe",null,null,58,60,false,Card.FIRE,Card.WATER,Card.NONE,1,"growlithe.jpg","Flare",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2) && currPlayerCard().hasEnergy(Card.FIRE,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Arcanine extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Arcanine(){
		super("Arcanine","Growlithe",null,59,100,false,Card.FIRE,Card.WATER,Card.NONE,3,"arcanine.jpg","Flamethrower","Take Down");
	}
	void moveOne() {
		energyDiscardAttack(modifiedDamage(50),Card.FIRE,1);
	}
	void moveTwo() {
		hurtSelfAttackGuaranteed(modifiedDamage(80),30);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.FIRE,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.FIRE,2);
	}
}

class Poliwag extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Poliwag(){
		super("Poliwag",null,null,60,40,false,Card.WATER,Card.LEAF,Card.NONE,1,"poliwag.jpg","Water Gun",null);
	}
	void moveOne() {
		int damage = 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 2)) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 3)) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Poliwhirl extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Poliwhirl(){
		super("Poliwhirl","Poliwag",null,61,60,false,Card.WATER,Card.LEAF,Card.NONE,1,"poliwhirl.jpg","Amnesia","Doubleslap");
	}
	void moveOne() {
		new PokemonPowerFrame(opponent(),PokemonPowerFrame.AMNESIA,"Choose an attack to nullify.",null);
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.WATER,2);
	}
}

class Poliwrath extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Poliwrath(){
		super("Poliwrath","Poliwag","Poliwhirl",62,90,false,Card.WATER,Card.LEAF,Card.NONE,3,"poliwrath.jpg","Water Gun","Whirlpool");
	}
	void moveOne() {
		int damage = 30;
		if(currPlayerCard().hasEnergy(Card.WATER, 4) || (currPlayerCard().hasEnergy(Card.WATER, 3) && currPlayerCard().hasEnergy(Card.COLORLESS,4))) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 5) || (currPlayerCard().hasEnergy(Card.WATER, 4) && currPlayerCard().hasEnergy(Card.COLORLESS,5))) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		if(opponentCard().energy.size() >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.HYPER_BEAM,"Choose an energy card to discard.",this);
		}
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.WATER,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.WATER,2);
	}
}

class Abra extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Abra(){
		super("Abra",null,null,63,30,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,0,"abra.jpg","Psyshock",null);
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Kadabra extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kadabra(){
		super("Kadabra","Abra",null,64,60,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,3,"kadabra.jpg","Recover","Super Psy");
		attack1Malicious = false;
	}
	void moveOne() {
		PokemonCard pc = (PokemonCard) PokemonGame.game.currPlayer.benchAndActiveCardPanel.activeCard;
		healRemoveEnergy(pc.maxHp, Card.PSYCHIC);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.PSYCHIC,2);
	}
}

class Alakazam extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Alakazam(){
		super("Alakazam","Abra","Kadabra",65,80,true,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,3,"alakazam.jpg","Damage Swap","Confuse Ray");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.DAMAGE_SWAP,"Swap damage between your pokemon as desired.",this);
	}
	void moveTwo() {
		confuseAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,3);
	}
}

class Machop extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Machop(){
		super("Machop",null,null,66,50,false,Card.FIGHTING,Card.PSYCHIC,Card.NONE,1,"machop.jpg","Low Kick",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Machoke extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Machoke(){
		super("Machoke","Machop",null,67,80,false,Card.FIGHTING,Card.PSYCHIC,Card.NONE,3,"machoke.jpg","Karate Chop","Submission");
	}
	void moveOne() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = 50;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage -= 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		hurtSelfAttackGuaranteed(modifiedDamage(60),20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
}

class Machamp extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Machamp(){
		super("Machamp","Machop","Machoke",68,100,true,Card.FIGHTING,Card.PSYCHIC,Card.NONE,3,"machamp.jpg","Strikes Back","Seismic Toss");
	}
	void moveOne() {}
	void moveTwo() {
		standardAttack(modifiedDamage(60));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.FIGHTING,3);
	}
}

class Bellsprout extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Bellsprout(){
		super("Bellsprout",null,null,69,40,false,Card.LEAF,Card.FIRE,Card.NONE,1,"bellsprout.jpg","Vine Whip","Call for Family");
		attack2Malicious = false;
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.CALL_FOR_FAMILY,"Select a Bellsprout from the deck to add to your bench.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,1);
	}
}

class Weepinbell extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Weepinbell(){
		super("Weepinbell","Bellsprout",null,70,70,false,Card.LEAF,Card.FIRE,Card.NONE,1,"weepinbell.jpg","Poisonpowder","Razor Leaf");
	}
	void moveOne() {
		poisonAttack(modifiedDamage(10));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,2);
	}
}

class Victreebel extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Victreebel(){
		super("Victreebel","Bellsprout","Weepinbell",71,80,false,Card.LEAF,Card.FIRE,Card.NONE,2,"victreebel.jpg","Lure","Acid");
	}
	void moveOne() {
		if(opponent().benchAndActiveCardPanel.benchSize >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.WHIRLWIND, currPlayer().name+ ", select a bench pokemon with which to switch the opponent's active one.",this);
		}
		else{
			JOptionPane.showMessageDialog(null, "No bench pokemon to switch with!");
		}
		
	}
	void moveTwo() {
		if(flipCoin() == true){
			opponent().benchAndActiveCardPanel.activeToBenchBan = true;
			JOptionPane.showMessageDialog(null, opponent().name + " cannot retreat his active pokemon next turn!");
		}
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,2);
	}
}

class Tentacool extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Tentacool(){
		super("Tentacool",null,null,72,30,true,Card.WATER,Card.THUNDER,Card.NONE,0,"tentacool.jpg","Cowardice","Acid");
		powerPerformed  = true;
	}
	void moveOne() {
		 powerPerformed  = true;
		if(currPlayer().benchAndActiveCardPanel.activeCard == this){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
			int size = pc.energy.size();
			for(int j=0; j<size; j++){
				currPlayer().deckAndDiscardPanel.addToDiscardPile(pc.energy.remove(0));
			}
			pc.setText("");
			pc.renew();
			currPlayer().benchAndActiveCardPanel.setActiveCard(currPlayer().benchAndActiveCardPanel.activeHolder);
			currPlayer().benchAndActiveCardPanel.addCardToHand(pc);
		}
		else{
			for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize; i++){
				if(currPlayer().benchAndActiveCardPanel.bench[i] == this){
					PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
					int size = pc.energy.size();
					for(int j=0; j<size; j++){
						currPlayer().deckAndDiscardPanel.addToDiscardPile(pc.energy.remove(0));
					}
					pc.setText("");
					pc.renew();
					currPlayer().benchAndActiveCardPanel.removeFromBench(i);
					currPlayer().benchAndActiveCardPanel.addCardToHand(pc);
				}
			}
		}
	}
	void moveTwo() {
		standardAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && ( location  == Card.BENCH || location   == Card.ACTIVE) &&  powerPerformed  == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
}

class Tentacruel extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Tentacruel(){
		super("Tentacruel","Tentacool",null,73,60,false,Card.WATER,Card.THUNDER,Card.NONE,0,"tentacruel.jpg","Supersonic","Jellyfish Sting");
	}
	void moveOne() {
		confuseAttack(modifiedDamage(0));
	}
	void moveTwo() {
		poisonAttackGuaranteed(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,2);
	}
}

class Geodude extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Geodude(){
		super("Geodude",null,null,74,50,false,Card.FIGHTING,Card.LEAF,Card.NONE,1,"geodude.jpg","Stone Barrage",null);
	}
	void moveOne() {
		int damage = 0;
		while(flipCoin() == true){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2) && currPlayerCard().hasEnergy(Card.FIGHTING,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Graveler extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Graveler(){
		super("Graveler","Geodude",null,75,60,false,Card.FIGHTING,Card.LEAF,Card.NONE,2,"graveler.jpg","Harden","Rock Throw");
		attack1Malicious = false;
	}
	void moveOne() {
		shield(30);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
}

class Golem extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Golem(){
		super("Golem","Geodude","Graveler",76,80,false,Card.FIGHTING,Card.LEAF,Card.NONE,4,"golem.jpg","Avalanche","Selfdestruct");
	}
	void moveOne() {
		standardAttack(modifiedDamage(60));
	}
	void moveTwo() {
		selfDestruct(modifiedDamage(100),20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.FIGHTING,3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,4);
	}
}
