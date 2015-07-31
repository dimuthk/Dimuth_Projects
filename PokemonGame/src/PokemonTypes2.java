import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class Ponyta extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Ponyta(){
		super("Ponyta",null,null,77,40,false,Card.FIRE,Card.WATER,Card.NONE,1,"ponyta.jpg","Smash Kick","Flame Tail");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
}

class Rapidash extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Rapidash(){
		super("Rapidash","Ponyta",null,78,70,false,Card.FIRE,Card.WATER,Card.NONE,0,"rapidash.jpg","Stomp","Agility");
	}
	void moveOne() {
		int damage = 20;
		if(flipCoin() == true){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		tryAgility();
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
}

class Slowpoke extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Slowpoke(){
		super("Slowpoke",null,null,79,50,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,1,"slowpoke.jpg","Spacing Out","Scavenge");
		attack1Malicious = false; attack2Malicious = false;
	}
	void moveOne() {
		if(flipCoin() == true){
			heal(10);
		}
	}
	void moveTwo() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.SCAVENGE,"Select a trainer card from your discard pile.",this);
		energyDiscardAttack(modifiedDamage(0),Card.PSYCHIC, 1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1) && currPlayerCard().currHp < currPlayerCard().maxHp;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 2);
	}
}

class Slowbro extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Slowbro(){
		super("Slowbro","Slowpoke",null,80,60,true,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,1,"slowbro.jpg","Strange Behavior","Psyshock");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.STRANGE_BEHAVIOR,"Move damage counters to Slowbro as desired.",this);
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().status == Card.NONE && (location == Card.BENCH || location == Card.ACTIVE);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 2);
	}
}

class Magnemite extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Magnemite(){
		super("Magnemite",null,null,81,40,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"magnemite.jpg","Thunder Wave","Selfdestruct");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		selfDestruct(modifiedDamage(40),10);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
}

class MagnetonClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public MagnetonClassic(){
		super("Magneton","Magnemite",null,82,60,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"magnetonc.jpg","Thunder Wave","Selfdestruct");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(30));
	}
	void moveTwo() {
		selfDestruct(modifiedDamage(80),20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.THUNDER, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.THUNDER, 2);
	}
}

class MagnetonFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public MagnetonFossil(){
		super("Magneton","Magnemite",null,82,80,false,Card.THUNDER,Card.FIGHTING,Card.NONE,2,"magnetonf.jpg","Sonicboom","Selfdestruct");
	}
	void moveOne() {
		standardAttack(20);
	}
	void moveTwo() {
		selfDestruct(modifiedDamage(100),20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.THUNDER, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.THUNDER, 2);
	}
}

class Farfetched extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Farfetched(){
		super("Farfetch'd",null,null,83,50,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,1,"farfetched.jpg","Leek Slap","Pot Smash");
	}
	void moveOne() {
		currPlayerCard().leekSlap = false;
		multipleChanceAttack(modifiedDamage(30),1);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1) && currPlayerCard().leekSlap == true;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Doduo extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Doduo(){
		super("Doduo",null,null,84,50,false,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,0,"doduo.jpg","Fury Attack",null);
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(10),2);
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Dodrio extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Dodrio(){
		super("Dodrio","Doduo",null,85,70,true,Card.COLORLESS,Card.THUNDER,Card.FIGHTING,0,"dodrio.jpg","Retreat Aid","Rage");
	}
	void moveOne() {}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = 10;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
}

class Seel extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Seel(){
		super("Seel",null,null,86,60,false,Card.WATER,Card.THUNDER,Card.NONE,1,"seel.jpg","Headbutt",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Dewgong extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Dewgong(){
		super("Dewgong","Seel",null,87,80,false,Card.WATER,Card.THUNDER,Card.NONE,3,"dewgong.jpg","Aurora Beam","Ice Beam");
	}
	void moveOne() {
		standardAttack(modifiedDamage(50));
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.WATER, 2);
	}
}

class Grimer extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Grimer(){
		super("Grimer",null,null,88,50,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"grimer.jpg","Nasty Goo","Minimize");
		attack2Malicious = false;
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		shield(20);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
}

class Muk extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Muk(){
		super("Muk","Grimer",null,89,70,true,Card.LEAF,Card.PSYCHIC,Card.NONE,2,"muk.jpg","Toxic Gas","Sludge");
	}
	void moveOne() {}
	void moveTwo() {
		poisonAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Shellder extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Shellder(){
		super("Shellder",null,null,90,30,false,Card.WATER,Card.THUNDER,Card.NONE,1,"shellder.jpg","Supersonic","Hide in Shell");
		attack2Malicious = false;
	}
	void moveOne() {
		confuseAttack(0);
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
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Cloyster extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Cloyster(){
		super("Cloyster","Shellder",null,91,50,false,Card.WATER,Card.THUNDER,Card.NONE,2,"cloyster.jpg","Clamp","Spike Cannon");
	}
	void moveOne() {
		if(flipCoin() == true){
			paralyzeAttackGuaranteed(modifiedDamage(30));
		}
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 2);
	}
}

class GastlyClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public GastlyClassic(){
		super("Gastly",null,null,92,30,false,Card.PSYCHIC,Card.NONE,Card.FIGHTING,0,"gastly.jpg","Sleeping Gas","Destiny Bond");
	}
	void moveOne() {
		sleepAttack(0);
	}
	void moveTwo() {
		specialStatusAttack(0,"Destiny Bond");
		energyDiscardAttack(0,Card.PSYCHIC,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
}

class GastlyFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public GastlyFossil(){
		super("Gastly",null,null,92,50,false,Card.PSYCHIC,Card.NONE,Card.FIGHTING,0,"gastlyf.jpg","Lick","Energy Conversion");
		attack2Malicious = false;
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		hurtSelfAttackGuaranteed(0,10);
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.ENERGY_CONVERSION,"Select up to 2 energy cards from your discard pile.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 2);
	}
}

class HaunterClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public HaunterClassic(){
		super("Haunter","Gastly",null,93,60,false,Card.PSYCHIC,Card.NONE,Card.FIGHTING,1,"haunterc.jpg","Hypnosis","Dream Eater");
	}
	void moveOne() {
		sleepAttackGuaranteed(0);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));

	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 2) && opponentCard().status == Card.ASLEEP;
	}
}

class HaunterFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public HaunterFossil(){
		super("Haunter","Gastly",null,930,50,true,Card.PSYCHIC,Card.NONE,Card.FIGHTING,0,"haunterf.jpg","Transparency","Nightmare");
	}
	void moveOne() {}
	void moveTwo() {
		sleepAttackGuaranteed(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
}

class Gengar extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Gengar(){
		super("Gengar","Gastly","Haunter",94,80,true,Card.PSYCHIC,Card.NONE,Card.FIGHTING,1,"gengar.jpg","Curse","Dark Mind");
	}
	void moveOne() {
		powerPerformed = true;
		new PokemonPowerFrame(opponent(),PokemonPowerFrame.CURSE,"Transfer a damage counter between enemy pokemon.",this);
	}
	void moveTwo() {
		if(opponent().benchAndActiveCardPanel.benchSize > 0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.SPARK,"Select a bench pokemon to attack.",this);
		}
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().status == Card.NONE && (location == Card.BENCH || location == Card.ACTIVE) && powerPerformed == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
}

class Onix extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Onix(){
		super("Onix",null,null,95,90,false,Card.FIGHTING,Card.LEAF,Card.NONE,3,"onix.jpg","Rock Throw","Harden");
		attack2Malicious = false;
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		shield(30);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
}

class Drowzee extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Drowzee(){
		super("Drowzee",null,null,96,50,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,1,"drowzee.jpg","Pound","Confuse Ray");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		confuseAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 2);
	}
}

class Hypno extends PokemonCard {
	private static final long serialVersionUID = 1L;
	public Hypno(){
		super("Hypno","Drowzee",null,97,90,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,2,"hypno.jpg","Prophecy","Dark Mind");
		attack1Malicious = false;
	}
	void moveOne() {
		final JDialog d = new JDialog();
		d.setSize(200,200);
		d.setResizable(false);
		d.setLocationRelativeTo(PokemonGame.game);
		d.setTitle("Prophecy");
		d.setModal(true);
		JPanel p = new JPanel(new GridLayout(2,1));
		JRadioButton selfDeck = new JRadioButton(currPlayer().name + " prophecy");
		selfDeck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.PROPHECY,"Arrange cards from the top of your deck as desired.",null); d.dispose();}
		});
		JRadioButton opponentDeck = new JRadioButton(opponent().name + " prophecy");
		opponentDeck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {new PokemonPowerFrame(opponent(),PokemonPowerFrame.PROPHECY,"Arrange cards from the top of your opponent's deck as desired.",null); d.dispose();} 
		});
		p.add(selfDeck); p.add(opponentDeck);
		d.add(p);
		d.setVisible(true);	
	}
	void moveTwo() {
		if(opponent().benchAndActiveCardPanel.benchSize > 0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.SPARK,"Select a bench pokemon to attack.",this);
		}
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 3);
	}
}

class Krabby extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Krabby(){
		super("Krabby",null,null,98,50,false,Card.WATER,Card.THUNDER,Card.NONE,2,"krabby.jpg","Call for Family","Irongrip");
		attack1Malicious = false;
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.CALL_FOR_FAMILY,"Select a Krabby from the deck to add to your bench.",this);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1) && PokemonGame.game.currPlayer.benchAndActiveCardPanel.benchSize < 5;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Kingler extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kingler(){
		super("Kingler","Krabby",null,99,60,false,Card.WATER,Card.THUNDER,Card.NONE,3,"kingler.jpg","Flail","Crabhammer");
	}
	void moveOne() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = pc.maxHp - pc.currHp;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 2);
	}
}

class Voltorb extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Voltorb(){
		super("Voltorb",null,null,100,40,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"voltorb.jpg","Tackle",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class ElectrodeClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public ElectrodeClassic(){
		super("Electrode","Voltorb",null,101,80,true,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"electrodec.jpg","Buzzap","Electric Shock");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.BUZZAP,"Select a bench pokemon to sacrifice Electrode to.",this);
	}
	void moveTwo() {
		hurtSelfAttack(modifiedDamage(50),10);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().status == Card.NONE && (location == Card.BENCH || location == Card.ACTIVE);	
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 3);
	}
}

class ElectrodeJungle extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public ElectrodeJungle(){
		super("Electrode","Voltorb",null,101,90,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"electrodej.jpg","Tackle","Chain Lightning");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		for(int i=0; i<opponent().benchAndActiveCardPanel.benchSize;i++){
			PokemonCard pc = (PokemonCard) opponent().benchAndActiveCardPanel.bench[i];
			if(opponentCard().type == pc.type){
				pc.benchTakeDamage(opponent(), 10, i);
			}
		}
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 3);
	}
}

class Exeggcute extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Exeggcute(){
		super("Exeggcute",null,null,102,50,false,Card.LEAF,Card.FIRE,Card.NONE,1,"exeggcute.jpg","Hypnosis","Leech Seed");
	}
	void moveOne() {
		sleepAttackGuaranteed(0);
	}
	void moveTwo() {
		int modifiedD, heal =0;
		if((modifiedD = modifiedDamage(20))>0){
			heal = 10;
		}
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Exeggutor extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Exeggutor(){
		super("Exeggutor","Exeggcute",null,103,80,false,Card.LEAF,Card.FIRE,Card.NONE,3,"exeggutor.jpg","Teleport","Big Eggsplosion");
		attack1Malicious = false;
	}
	void moveOne() {
		if(currPlayer().benchAndActiveCardPanel.benchSize >0){
			new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.WHIRLWIND, currPlayer().name+ ", select a bench pokemon with which to switch the opponent's active one.",this);
		}
		else{
			JOptionPane.showMessageDialog(null, "No bench pokemon to switch with!");
		}
	}
	void moveTwo() {
		int count =0;
		while(currPlayerCard().hasEnergy(Card.COLORLESS,count)){
			count++;
		}
		count--;
		multipleChanceAttack(modifiedDamage(20),count);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
}

class Cubone extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Cubone(){
		super("Cubone",null,null,104,40,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,1,"cubone.jpg","Snivel","Rage");
		attack1Malicious = false;
	}
	void moveOne() {
		specialStatusAttack(0,"Sniveled");
	}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = 10;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
}

class Marowak extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Marowak(){
		super("Marowak","Cubone",null,105,60,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,1,"marowak.jpg","Bonemerang","Call for Friend");
		attack2Malicious = false;
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	void moveTwo() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.CALL_FOR_FRIEND,"Select a basic pokemon from the deck to add to your bench.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
}

class Hitmonlee extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Hitmonlee(){
		super("Hitmonlee",null,null,106,60,false,Card.FIGHTING,Card.PSYCHIC,Card.NONE,1,"hitmonlee.jpg","Stretch Kick","High Jump Kick");
	}
	void moveOne() {
		if(opponent().benchAndActiveCardPanel.benchSize > 0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.SPARK,"Select a bench pokemon to attack.",this);
		}
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 3);
	}
}

class Hitmonchan extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Hitmonchan(){
		super("Hitmonchan",null,null,107,70,false,Card.FIGHTING,Card.PSYCHIC,Card.NONE,2,"hitmonchan.jpg","Jab","Special Punch");
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIGHTING, 2);
	}
}

class Lickitung extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Lickitung(){
		super("Lickitung",null,null,108,90,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,3,"lickitung.jpg","Tongue Wrap","Supersonic");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		confuseAttack(0);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
}

class Koffing extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Koffing(){
		super("Koffing",null,null,109,50,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"koffing.jpg","Foul Gas",null);
	}
	void moveOne() {
		if(flipCoin() == true){
			poisonAttackGuaranteed(modifiedDamage(10));
		}
		else{
			confuseAttackGuaranteed(modifiedDamage(10));
		}
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Weezing extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Weezing(){
		super("Weezing","Koffing",null,110,60,false,Card.LEAF,Card.PSYCHIC,Card.NONE,1,"weezing.jpg","Smog","Selfdestruct");
	}
	void moveOne() {
		poisonAttack(modifiedDamage(20));
	}
	void moveTwo() {
		selfDestruct(modifiedDamage(60),10);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.LEAF, 2);
	}
}

class Rhyhorn extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Rhyhorn(){
		super("Rhyhorn",null,null,111,70,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,3,"rhyhorn.jpg","Leer","Horn Attack");
		attack1Malicious = false;
	}
	void moveOne() {
		tryAgility();
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIGHTING, 1);
	}
}

class Rhydon extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Rhydon(){
		super("Rhydon","Rhyhorn",null,112,100,false,Card.FIGHTING,Card.LEAF,Card.THUNDER,3,"rhydon.jpg","Horn Attack","Ram");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		hurtSelfAttackGuaranteed(modifiedDamage(50),20);
		if(opponent().benchAndActiveCardPanel.benchSize >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.WHIRLWIND, opponent().name+ ", select a bench pokemon with which to switch the opponent's active one.",this);
		}
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIGHTING, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING, 4);
	}
}

class Chansey extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Chansey(){
		super("Chansey",null,null,113,120,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"chansey.jpg","Scrunch","Double-edge");
		attack1Malicious = false;
	}
	void moveOne() {
		if(flipCoin() == true){
			shield(500);
		}
	}
	void moveTwo() {
		hurtSelfAttackGuaranteed(modifiedDamage(80),80);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4);
	}
}

class Tangela extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Tangela(){
		super("Tangela",null,null,114,50,false,Card.LEAF,Card.FIRE,Card.NONE,2,"tangela.jpg","Bind","Poisonpowder");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(20));
	}
	void moveTwo() {
		poisonAttackGuaranteed(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 3);
	}
}

class Kangaskhan extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kangaskhan(){
		super("Kangaskhan",null,null,115,90,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,3,"kangaskhan.jpg","Fetch","Comet Punch");
		attack1Malicious = false;
	}
	void moveOne() {
		currPlayer().benchAndActiveCardPanel.addCardToHand(currPlayer().deckAndDiscardPanel.removeCardFromDeck());
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(20),4);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4);
	}
}

class Horsea extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Horsea(){
		super("Horsea",null,null,116,40,false,Card.WATER,Card.THUNDER,Card.NONE,0,"horsea.jpg","Smokescreen",null);
	}
	void moveOne() {
		specialStatusAttack(modifiedDamage(10),"Smokescreened");
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Seadra extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Seadra(){
		super("Seadra","Horsea",null,117,60,false,Card.WATER,Card.THUNDER,Card.NONE,1,"seadra.jpg","Water Gun","Agility");
	}
	void moveOne() {
		int damage = 20;
		if(currPlayerCard().hasEnergy(Card.WATER, 3) || (currPlayerCard().hasEnergy(Card.WATER, 2) && currPlayerCard().hasEnergy(Card.COLORLESS,3))) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 4) || (currPlayerCard().hasEnergy(Card.WATER, 3) && currPlayerCard().hasEnergy(Card.COLORLESS,4))) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		tryAgility();
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Goldeen extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Goldeen(){
		super("Goldeen",null,null,118,40,false,Card.WATER,Card.THUNDER,Card.NONE,0,"goldeen.jpg","Horn Attack",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Seaking extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Seaking(){
		super("Seaking","Goldeen",null,119,70,false,Card.WATER,Card.THUNDER,Card.NONE,1,"seaking.jpg","Horn Attack","Waterfall");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Staryu extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Staryu(){
		super("Staryu",null,null,120,40,false,Card.WATER,Card.THUNDER,Card.NONE,1,"staryu.jpg","Slap",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(20));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Starmie extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Starmie(){
		super("Starmie","Staryu",null,121,60,false,Card.WATER,Card.THUNDER,Card.NONE,1,"starmie.jpg","Recover","Star Freeze");
		attack1Malicious = false;
	}
	void moveOne() {
		healRemoveEnergy(currPlayerCard().maxHp, Card.WATER);
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class MrMime extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public MrMime(){
		super("Mr. Mime",null,null,122,40,true,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,1,"mrmime.jpg","Invisible Wall","Meditate");
	}
	void moveOne() {}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) opponent().benchAndActiveCardPanel.activeCard;
		int damage = 10;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
}

class Scyther extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Scyther(){
		super("Scyther",null,null,123,70,false,Card.LEAF,Card.FIRE,Card.FIGHTING,0,"scyther.jpg","Swords Dance","Slash");
		attack1Malicious = false;
	}
	void moveOne() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		pc.attackBoost += 30;
		pc.attackBoostSwitch = true;
	}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Jynx extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Jynx(){
		super("Jynx",null,null,124,70,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,2,"jynx.jpg","Doubleslap","Meditate");
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(10),2);
	}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) opponent().benchAndActiveCardPanel.activeCard;
		int damage = 20;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.PSYCHIC, 2);
	}
}

class Electabuzz extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Electabuzz(){
		super("Electabuzz",null,null,125,70,false,Card.THUNDER,Card.FIGHTING,Card.NONE,2,"electabuzz.jpg","Thundershock","Thunderpunch");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(10));
	}
	void moveTwo() {
		int damage = 30;
		if(flipCoin() == true){
			damage += 10;
		}
		else{
			takeDamage(currPlayer(),10);
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.THUNDER, 1);
	}
}

class MagmarClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public MagmarClassic(){
		super("Magmar",null,null,126,50,false,Card.FIRE,Card.WATER,Card.NONE,2,"magmarc.jpg","Fire Punch","Flamethrower");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(50),Card.FIRE,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
}

class MagmarFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public MagmarFossil(){
		super("Magmar",null,null,126,70,false,Card.FIRE,Card.WATER,Card.NONE,1,"magmarf.jpg","Smokescreen","Smog");
	}
	void moveOne() {
		specialStatusAttack(modifiedDamage(10),"Smokescreened");
	}
	void moveTwo() {
		poisonAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE, 2);
	}
}

class Pinsir extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Pinsir(){
		super("Pinsir",null,null,127,60,false,Card.LEAF,Card.FIRE,Card.NONE,1,"pinsir.jpg","Irongrip","Guillotine");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(20));
	}
	void moveTwo() {
		standardAttack(modifiedDamage(50));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.LEAF,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.LEAF,2);
	}
}

class Tauros extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Tauros(){
		super("Tauros",null,null,128,60,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,2,"tauros.jpg","Stomp","Rampage");
	}
	void moveOne() {
		int damage = 20;
		if(flipCoin() == true){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = 20;
		for(int i=pc.currHp; i<pc.maxHp; i+=10){
			damage += 10;
		}
		standardAttack(modifiedDamage(damage));
		if(pc.status != Card.CONFUSED){
			if(flipCoin() == false){
				confuseSelf();
			}
		}
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 3);
	}
}

class Magikarp extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Magikarp(){
		super("Magikarp",null,null,129,30,false,Card.WATER,Card.THUNDER,Card.NONE,1,"magikarp.jpg","Tackle","Flail");
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		int damage = pc.maxHp - pc.currHp;
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 1);
	}
}

class Gyarados extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Gyarados(){
		super("Gyarados","Magikarp",null,130,100,false,Card.WATER,Card.LEAF,Card.FIGHTING,3,"gyarados.jpg","Dragon Rage","Bubblebeam");
	}
	void moveOne() {
		standardAttack(modifiedDamage(50));
	}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(40));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 4);
	}
}

class Lapras extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Lapras(){
		super("Lapras",null,null,131,80,false,Card.WATER,Card.THUNDER,Card.NONE,2,"lapras.jpg","Water Gun","Confuse Ray");
	}
	void moveOne() {
		int damage = 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 2)) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 3)) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		confuseAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER, 2);
	}
}

class Ditto extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Ditto(){
		super("Ditto",null,null,132,50,true,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"ditto.jpg","Transform",null);
	}
	void moveOne() {}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Eevee extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Eevee(){
		super("Eevee",null,null,133,50,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"eevee.jpg","Tail Wag","Quick Attack");
	}
	void moveOne() {
		tryAgility();
	}
	void moveTwo() {
		int damage = 10;
		if(flipCoin() == true){
			damage += 20;
		}
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
}

class Vaporeon extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Vaporeon(){
		super("Vaporeon","Eevee",null,134,80,false,Card.WATER,Card.THUNDER,Card.NONE,1,"vaporeon.jpg","Quick Attack","Water Gun");
	}
	void moveOne() {
		int damage = 10;
		if(flipCoin() == true){
			damage += 20;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		int damage = 30;
		if(currPlayerCard().hasEnergy(Card.WATER, 4) || (currPlayerCard().hasEnergy(Card.WATER, 3) && currPlayerCard().hasEnergy(Card.COLORLESS,4))) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 5) || (currPlayerCard().hasEnergy(Card.WATER, 4) && currPlayerCard().hasEnergy(Card.COLORLESS,5))) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.WATER,2);
	}
}

class Jolteon extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Jolteon(){
		super("Jolteon","Eevee",null,135,70,false,Card.THUNDER,Card.FIGHTING,Card.NONE,1,"jolteon.jpg","Quick Attack","Pin Missile");
	}
	void moveOne() {
		int damage = 10;
		if(flipCoin() == true){
			damage += 20;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(20),4);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3) && currPlayerCard().hasEnergy(Card.THUNDER,2);
	}
}

class Flareon extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Flareon(){
		super("Flareon","Eevee",null,136,70,false,Card.FIRE,Card.WATER,Card.NONE,1,"flareon.gif","Quick Attack","Flamethrower");
	}
	void moveOne() {
		int damage = 10;
		if(flipCoin() == true){
			damage += 20;
		}
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		energyDiscardAttack(modifiedDamage(60),Card.FIRE,1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.FIRE,2);
	}
}

class Porygon extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Porygon(){
		super("Porygon",null,null,137,30,false,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,1,"porygon.jpg","Conversion 1","Conversion 2");
		attack2Malicious = false;
	}
	void moveOne() {
		if(opponentCard().weakness != Card.NONE){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.CONVERSION_1,"Switch " + opponentCard().name + "'s weakness to a type of your choice.",this);
		}
		else{
			JOptionPane.showMessageDialog(null,opponentCard().name + " does not have a weakness!");
		}
	}
	void moveTwo() {
		new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.CONVERSION_2,"Switch " + currPlayerCard().name + "'s resistance to a type of your choice.",this);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2);
	}
}

class Omanyte extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Omanyte(){
		super("Omanyte","Mysterious Fossil",null,138,40,true,Card.WATER,Card.LEAF,Card.NONE,1,"omanyte.jpg","Clairvoyance","Water Gun");
	}
	void moveOne() {}
	void moveTwo() {
		int damage = 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 2)) damage += 10;
		if(currPlayerCard().hasEnergy(Card.WATER, 3)) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,1);
	}
}

class Omastar extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Omastar(){
		super("Omastar","Mysterious Fossil","Omanyte",139,70,false,Card.WATER,Card.LEAF,Card.NONE,1,"omastar.jpg","Water Gun","Spike Cannon");
	}
	void moveOne() {
		int damage = 20;
		if(currPlayerCard().hasEnergy(Card.COLORLESS, 3) && currPlayerCard().hasEnergy(Card.WATER, 2)) damage += 10;
		if(currPlayerCard().hasEnergy(Card.COLORLESS, 4) && currPlayerCard().hasEnergy(Card.WATER, 3)) damage += 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS, 2) && currPlayerCard().hasEnergy(Card.WATER, 1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,2);
	}
}

class Kabuto extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kabuto(){
		super("Kabuto","Mysterious Fossil",null,140,30,true,Card.FIGHTING,Card.LEAF,Card.NONE,1,"kabuto.jpg","Kabuto Armor","Scratch");
	}
	void moveOne() {}
	void moveTwo() {
		standardAttack(modifiedDamage(10));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,1);
	}
}

class Kabutops extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Kabutops(){
		super("Kabutops","Mysterious Fossil","Kabuto",141,60,false,Card.FIGHTING,Card.LEAF,Card.NONE,1,"kabutops.jpg","Sharp Sickle","Absorb");
	}
	void moveOne() {
		standardAttack(modifiedDamage(30));
	}
	void moveTwo() {
		int modifiedD = modifiedDamage(40);
		int	heal = modifiedD/2;
		if(heal % 10 != 0) heal += 5;
		drainAttack(modifiedD, heal);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,2);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIGHTING,4);
	}
}

class Aerodactyl extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Aerodactyl(){
		super("Aerodactyl","Mysterious Fossil",null,142,60,true,Card.FIGHTING,Card.LEAF,Card.FIGHTING,2,"aerodactyl.jpg","Prehistoric Power","Wing Attack");
	}
	void moveOne() {}
	void moveTwo() {
		standardAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3);
	}
}

class Snorlax extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Snorlax(){
		super("Snorlax",null,null,143,90,true,Card.COLORLESS,Card.FIGHTING,Card.PSYCHIC,4,"snorlax.jpg","Thick Skinned","Body Slam");
		statusResistant = true;
	}
	void moveOne() {}
	void moveTwo() {
		paralyzeAttack(modifiedDamage(30));
	}
	boolean moveOneRequirements() {
		return false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4);
	}
}

class Articuno extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Articuno(){
		super("Articuno",null,null,144,70,false,Card.WATER,Card.NONE,Card.FIGHTING,2,"articuno.jpg","Freeze Dry","Blizzard");
	}
	void moveOne() {
		paralyzeAttack(modifiedDamage(30));
	}
	void moveTwo() {
		blizzard(modifiedDamage(50),10);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.WATER,4);
	}
}

class ZapdosClassic extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public ZapdosClassic(){
		super("Zapdos",null,null,145,90,false,Card.THUNDER,Card.NONE,Card.FIGHTING,3,"zapdosc.jpg","Thunder","Thunderbolt");
	}
	void moveOne() {
		hurtSelfAttack(modifiedDamage(60),30);
	}
	void moveTwo() {
		standardAttack(modifiedDamage(100));
		for(int i=0;i<currPlayerCard().energy.size();i++){
			currPlayer().deckAndDiscardPanel.addToDiscardPile(currPlayerCard().energy.get(i));
		}
		currPlayerCard().energy.clear();
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4) && currPlayerCard().hasEnergy(Card.THUNDER,3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER,4);
	}
}

class ZapdosFossil extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public ZapdosFossil(){
		super("Zapdos",null,null,145,80,false,Card.THUNDER,Card.NONE,Card.FIGHTING,2,"zapdosf.jpg","Thunderstorm",null);
	}
	void moveOne() {
		zapdosSpecialAttack();
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.THUNDER,4);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Moltres extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Moltres(){
		super("Moltres",null,null,146,70,false,Card.FIRE,Card.NONE,Card.FIGHTING,2,"moltres.jpg","Wildfire","Dive Bomb");
	}
	void moveOne() {
		new PokemonPowerFrame(PokemonGame.game.currPlayer,PokemonPowerFrame.WILDFIRE,"Detach fire energies as desired.",this);
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(80),1);
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.FIRE,4);
	}
}

class Dratini extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Dratini(){
		super("Dratini",null,null,147,40,false,Card.COLORLESS,Card.NONE,Card.PSYCHIC,1,"dratini.jpg","Pound",null);
	}
	void moveOne() {
		standardAttack(modifiedDamage(10));
	}
	void moveTwo() {}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,1);
	}
	boolean moveTwoRequirements() {
		return false;
	}
}

class Dragonair extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Dragonair(){
		super("Dragonair","Dratini",null,148,80,false,Card.COLORLESS,Card.NONE,Card.PSYCHIC,2,"dragonair.jpg","Slam","Hyper Beam");
	}
	void moveOne() {
		multipleChanceAttack(modifiedDamage(30),2);
	}
	void moveTwo() {
		if(opponentCard().energy.size() >0){
			new PokemonPowerFrame(opponent(),PokemonPowerFrame.HYPER_BEAM,"Choose an energy card to discard.",opponentCard());
		}
		standardAttack(modifiedDamage(20));
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,3);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4);
	}
}

class Dragonite extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Dragonite(){
		super("Dragonite","Dratini","Dragonair",149,100,true,Card.COLORLESS,Card.NONE,Card.FIGHTING,1,"dragonite.jpg","Step In","Slam");
	}
	void moveOne() {
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize; i++){
			if(currPlayer().benchAndActiveCardPanel.bench[i] == this){
				currPlayer().benchAndActiveCardPanel.activeToBenchTrade(i, currPlayer().benchAndActiveCardPanel.activeCard);
			}
		}
		powerPerformed = true;
	}
	void moveTwo() {
		multipleChanceAttack(modifiedDamage(40),2);
	}
	boolean moveOneRequirements() {
		return status  == Card.NONE && (location  == Card.BENCH) && powerPerformed == false;
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,4);
	}
}

class Mewtwo extends PokemonCard {
	private static final long serialVersionUID = 1L;

	public Mewtwo(){
		super("Mewtwo",null,null,150,60,false,Card.PSYCHIC,Card.PSYCHIC,Card.NONE,3,"mewtwo.jpg","Psychic","Barrier");
	}
	void moveOne() {
		int damage = 10;
		int count = 1;
		while(opponentCard().hasEnergy(Card.COLORLESS, count)){
			count++;
		}
		count--;
		damage += count * 10;
		standardAttack(modifiedDamage(damage));
	}
	void moveTwo() {
		currPlayerCard().removeEnergy(Card.PSYCHIC, currPlayer());
		currPlayerCard().agility = true;
	}
	boolean moveOneRequirements() {
		return currPlayerCard().hasEnergy(Card.COLORLESS,2) && currPlayerCard().hasEnergy(Card.PSYCHIC,1);
	}
	boolean moveTwoRequirements() {
		return currPlayerCard().hasEnergy(Card.PSYCHIC,2);
	}
}