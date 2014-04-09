import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

abstract class PokemonCard extends Card {
	private static final long serialVersionUID = 1L;
	ArrayList<EnergyCard> energy;
	ArrayList<PokemonCard> stages;
	int currHp, maxHp;
	boolean hasPokemonPower = false;
	boolean poisoned = false, toxic = false;
	boolean agility = false;
	boolean metronome = false;
	int amnesia = 0;
	boolean powerPerformed = false;
	boolean attackBoostSwitch = false;
	boolean leekSlap = true;
	boolean attack1Malicious = true, attack2Malicious = true;
	boolean statusResistant = false;
	String specialStatus;
	int resistance, weakness;
	private int retreatCost;
	//int numMoves;
	int type;
	int pokeId;
	int status;
	int shield = 0; 
	int attackBoost = 0;
	int damageTaken;
	String name, basicStage, middleStage, moveOne, moveTwo;
	//boolean beginOrEnd;
	
	abstract void moveOne(); //is a pokemon power if hasPokemonPower is true
	abstract boolean moveOneRequirements();
	abstract void moveTwo();
	abstract boolean moveTwoRequirements();

	public PokemonCard(String cName){
		currHp = 10;
		maxHp = 10;
		statusResistant = true;
		energy = new ArrayList<EnergyCard>();
		stages = new ArrayList<PokemonCard>();
		name = cName;
		pokeId = -1;
		basicStage = null;
		middleStage = null;
		weakness = Card.NONE;
		resistance = Card.NONE;
		retreatCost = 500;
		moveOne = null;
		moveTwo = null;
		hasPokemonPower= false;
		type = Card.COLORLESS;
		status = Card.NONE;
		setLocation(Card.DECK);
		setCardType(Card.BASIC_POKEMON);
	}
	
	void renew(){
		currHp = maxHp;
		poisoned = false;
		toxic = false;
		agility = false;
		amnesia = 0;
		powerPerformed = false;
		shield = 0;
		attackBoost = 0;
		leekSlap = true;
		specialStatus = null;
		setText("");
		damageTaken = 0;
		status = Card.NONE;
		for(int i=0; i<stages.size();i++){
			stages.get(i).renew();
		}
	}
	
	static void dittoWrapException(){
		try{
		if(currPlayerCard().pokeId == 132 && currPlayerCard().status == Card.NONE){
			try{
				PokemonCard c = currPlayerCard();
				DittoMasquerade masquerade = new DittoMasquerade(PokemonCard.opponentCard(),c);
				masquerade.addMouseListener(PokemonCard.currPlayer().benchAndActiveCardPanel);
				c = masquerade;
				currPlayer().benchAndActiveCardPanel.setActiveCard(c);
				if(masquerade.currHp ==0) currPlayer().benchAndActiveCardPanel.knockOutPokemon(masquerade);
			}
			catch(Exception e){}
		}
		else if(currPlayerCard().masquerade == true && currPlayerCard().status == Card.NONE){
			try{
				DittoMasquerade oldMasquerade = (DittoMasquerade) currPlayerCard();
				if(oldMasquerade.imitation != opponentCard()){
					oldMasquerade.ditto.currHp = oldMasquerade.ditto.maxHp;
					oldMasquerade.ditto.currHp -= (oldMasquerade.maxHp - oldMasquerade.currHp);
					DittoMasquerade newMasquerade = new DittoMasquerade(PokemonCard.opponentCard(),oldMasquerade.ditto);
					newMasquerade.addMouseListener(PokemonCard.currPlayer().benchAndActiveCardPanel);
					currPlayer().benchAndActiveCardPanel.setActiveCard(newMasquerade);
					if(newMasquerade.currHp ==0) currPlayer().benchAndActiveCardPanel.knockOutPokemon(newMasquerade);
				}
			}
			catch(Exception e){}
		}
		else if(opponentCard().pokeId == 132 && opponentCard().status == Card.NONE){
			PokemonCard c = opponentCard();
			PokemonCard masquerade = new DittoMasquerade(PokemonCard.currPlayerCard(),c);
			masquerade.addMouseListener(PokemonCard.opponent().benchAndActiveCardPanel);
			c = masquerade;
			opponent().benchAndActiveCardPanel.setActiveCard(c);
			if(masquerade.currHp ==0) opponent().benchAndActiveCardPanel.knockOutPokemon(masquerade);
		}
		else if(opponentCard().masquerade == true && opponentCard().status == Card.NONE){
			try{
				DittoMasquerade oldMasquerade = (DittoMasquerade) opponentCard();
				if(oldMasquerade.imitation != currPlayerCard()){
					oldMasquerade.ditto.currHp = oldMasquerade.ditto.maxHp;
					oldMasquerade.ditto.currHp -= (oldMasquerade.maxHp - oldMasquerade.currHp);
					DittoMasquerade newMasquerade = new DittoMasquerade(PokemonCard.currPlayerCard(),oldMasquerade.ditto);
					newMasquerade.addMouseListener(PokemonCard.opponent().benchAndActiveCardPanel);
					opponent().benchAndActiveCardPanel.setActiveCard(newMasquerade);
					if(newMasquerade.currHp ==0) opponent().benchAndActiveCardPanel.knockOutPokemon(newMasquerade);
				}
			}
			catch(Exception e){}
		}
		}
		catch(Exception e){}
	}
	
	static PokemonCard unwrapDitto(PokemonCard pc){
		if(pc.masquerade == true){
			try{
				DittoMasquerade masquerade = (DittoMasquerade) pc;
				pc = masquerade.ditto;
				pc.status = masquerade.status;
				pc.energy.addAll(masquerade.energy);
				pc.currHp = pc.maxHp;
				pc.currHp -= (masquerade.maxHp - masquerade.currHp);
				if(pc.currHp < 0 || pc.currHp == 0){
					pc.currHp = 0;
					//currPlayer().benchAndActiveCardPanel.knockOutPokemon(pc);
				}
				
			}
			catch(Exception e){
				
			}
		}
		return pc;
	}
	
	void performMove(int move){
		//decide maliciousness
		if((move == 1 && attack1Malicious == true)||(move ==2 && attack2Malicious == true)){
			if(opponentCard().agility == true){
				JOptionPane.showMessageDialog(null, opponentCard().name + " cannot be hurt this round!");
				concludeTurn();
				return;
			}
			if(move == 1 && hasPokemonPower == true){
				moveOne();
				return;
			}
			if(status == Card.PARALYZED){
				JOptionPane.showMessageDialog(null, name + " is paralyzed!");
				status = Card.NONE;
				concludeTurn();
				return;
			}
			else if(status == Card.CONFUSED){
				if(flipCoin() == false){
					JOptionPane.showMessageDialog(null, name + " is confused and hurt itself!");
					takeDamage(PokemonGame.game.currPlayer,30);
					concludeTurn();
					return;
				}
			}
			else if(status == Card.ASLEEP){
				if(flipCoin() == false){
					JOptionPane.showMessageDialog(null, name + " didn't wake up!");
					concludeTurn();
					return;
				}
				else{
					JOptionPane.showMessageDialog(null, name + " woke up!");
					status = Card.NONE;
				}
			}
			if(specialStatus != null){
				String temp = specialStatus;
				if(temp.equals("Pounced") ||temp.equals("Sniveled")||temp.equals("Leered")){
					opponentCard().shield += 10;
					if(temp.equals("Sniveled")){
						opponentCard().shield += 10;
					}
					if(temp.equals("Leered")){
						opponentCard().shield += 200;
					}
					
					specialStatus = null;
				}
				else if(temp.equals("Destiny Bond")||temp.equals("Swords Danced")){

				}
				
				else if(flipCoin() == false){
					specialStatus = null;
					JOptionPane.showMessageDialog(null, name + " couldn't attack due to " + temp+"!");
					concludeTurn();
					return;
				}
			}
			if(opponentCard().pokeId == 930 && opponentCard().status == Card.NONE && PokemonGame.game.toxicGasException() == false){
				if(flipCoin() == true){
					JOptionPane.showMessageDialog(null, "Haunter prevented the attack due to transparecy!");
					concludeTurn();
					return;
				}
				}
			}
		

			if(move == 1){
				moveOne();
			}
			else{
				moveTwo();
			}
			
			specialStatus = null;
			//PokemonGame.game.currPlayer.prizePanel.addPrizes(kills);
			concludeTurn();
		}

	
	PokemonCard evolveCard(PokemonCard pc){		
		pc.energy.addAll(energy);
		energy.clear();
		int difference = maxHp - currHp;
		pc.currHp -= difference;
		status = Card.NONE;
		poisoned = false;
		powerPerformed = false;
		toxic = false;
		amnesia = 0;
		pc.stages.addAll(this.stages);
		this.stages.clear();
		pc.stages.add(this);
		return pc;
	}
	
	static PokemonCard devolveCard(PokemonCard pc, int devolutionLevel){
		PokemonCard prev = pc.stages.get(devolutionLevel);
		prev.energy.addAll(pc.energy);
		pc.energy.clear();
		prev.currHp -= (pc.maxHp - pc.currHp);
		prev.status = Card.NONE;
		prev.poisoned = false;
		prev.toxic = false;
		prev.powerPerformed = false;
		prev.amnesia = 0;
		if(devolutionLevel == 1){
			prev.stages.add(pc.stages.get(0));
		}
		else if(devolutionLevel == 0 && pc.stages.size() == 2){
			currPlayer().deckAndDiscardPanel.addToDiscardPile(pc.stages.get(1));
		}
		pc.stages.clear();
		currPlayer().deckAndDiscardPanel.addToDiscardPile(pc);
		return prev;
	}
	
	static void concludeTurn(){
		if(PokemonGame.game.currPlayer.benchAndActiveCardPanel.activeCard == PokemonGame.game.currPlayer.benchAndActiveCardPanel.activeHolder){
			JOptionPane.showMessageDialog(null, "Select a new active pokemon.");
			return;
		}
		PokemonCard pc = (PokemonCard) PokemonGame.game.currPlayer.benchAndActiveCardPanel.activeCard;
		pc.damageTaken = 0;	
		pc.amnesia = 0;
		currPlayer().benchAndActiveCardPanel.activeToBenchBan = false;
		
		try{
		if(opponentCard().masquerade == true && opponentCard().status != Card.NONE){
			opponent().benchAndActiveCardPanel.setActiveCard(unwrapDitto(opponentCard()));
		}
		if(currPlayerCard().masquerade == true && currPlayerCard().status != Card.NONE){
			currPlayer().benchAndActiveCardPanel.setActiveCard(unwrapDitto(currPlayerCard()));
		}
		}
		catch(Exception e){}
		
		if(pc.attackBoostSwitch == true){
			pc.attackBoostSwitch = false;
		}
		else{
			pc.attackBoost = 0;
		}
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
			pc = (PokemonCard)PokemonGame.game.currPlayer.benchAndActiveCardPanel.bench[i];
			if(pc.attackBoostSwitch == true){
				pc.attackBoostSwitch = false;
			}
			else{
				pc.attackBoost = 0;
			}
		}
		
		if(currPlayer().benchAndActiveCardPanel.activeCard == currPlayer().benchAndActiveCardPanel.activeHolder){
			if(currPlayer().benchAndActiveCardPanel.benchSize == 0){
				JOptionPane.showMessageDialog(null,currPlayer().name + " has no pokemon cards in play." + opponent().name + " wins!");
				PokemonGame.game.dispose();
			}
			else{
				new PokemonPowerFrame(currPlayer(),PokemonPowerFrame.WHIRLWIND,"Select a new active pokemon.",null);
			}
		}
		if(opponent().benchAndActiveCardPanel.activeCard == opponent().benchAndActiveCardPanel.activeHolder){
			if(opponent().benchAndActiveCardPanel.benchSize == 0){
				JOptionPane.showMessageDialog(null,opponent().name + " has no pokemon cards in play." + currPlayer().name + " wins!");
				PokemonGame.game.dispose();
			}
			else{
				new PokemonPowerFrame(opponent(),PokemonPowerFrame.WHIRLWIND,"Select a new active pokemon.",null);
			}
		}
		
		
		if(currPlayer() == (PokemonGame.game.player1)){
			PokemonGame.game.player2StartTurn();
		}
		else{
			PokemonGame.game.player1StartTurn();
		}
		PokemonGame.game.player1.benchAndActiveCardPanel.updatePokemonStatuses();
		PokemonGame.game.player2.benchAndActiveCardPanel.updatePokemonStatuses();
	}
	
	void shield(int amount){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		pc.shield += amount;
		JOptionPane.showMessageDialog(null, pc.name + " will take " + amount + " less damage next round.");
	}
	
	static PokemonCard currPlayerCard(){
		return (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
	}
	
	//the pokemon itself takes damage, not from an opponent
	void takeDamage(Player player, int damage){
		PokemonCard pc = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
		damage = kabutoArmorException(pc, damage);
		damage -= pc.shield;
		pc.currHp -= damage;
		if(pc.currHp <= 0){
			pc.currHp = 0;
			player.benchAndActiveCardPanel.knockOutPokemon((PokemonCard)player.benchAndActiveCardPanel.activeCard);
			//JOptionPane.showMessageDialog(null, pc.name + " has been knocked out. " + player.opponent.name + " select a prize card.");
			//player.opponent.prizePanel.addPrizes(1);
		}
	}
	
	boolean benchTakeDamage(Player player, int damage, int slot){
		PokemonCard pc = (PokemonCard) player.benchAndActiveCardPanel.bench[slot];
		damage = kabutoArmorException(pc, damage);
		damage -= pc.shield;
		if(pc.pokeId == 930 && pc.status == Card.NONE && PokemonGame.game.toxicGasException() == false){
			if(flipCoin() == true){
				JOptionPane.showMessageDialog(null, "Haunter eluded the attack due to transparecy!");
				return false;
			}
		}
		pc.currHp -= damage;
		if(pc.name.equals("Machamp") && pc.status == Card.NONE && PokemonGame.game.toxicGasException() == false){
			PokemonCard p = (PokemonCard) player.opponent.benchAndActiveCardPanel.activeCard;
			JOptionPane.showMessageDialog(null, "Machamp struck back at " + p.name +"!");
			takeDamage(player.opponent,10);
		}
		if(pc.currHp <= 0){
			pc.currHp = 0;
			player.benchAndActiveCardPanel.knockOutPokemon(pc);
			//JOptionPane.showMessageDialog(null, pc.name + " has been knocked out. " + player.opponent.name + " select a prize card.");
			//player.opponent.prizePanel.addPrizes(1);
			return true;
		}
		return false;
	}
	
	
	//after weakness/resistance applied
	int modifiedDamage(int initialDamage){
		if(PokemonGame.game.applyWAndR == false){
			return initialDamage;
		}
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.resistance == pc.type){
			initialDamage -= 30;
			if(initialDamage < 0){
				initialDamage = 0;
			}
		}
		else if(opponentCard.weakness == pc.type){
			initialDamage *= 2;
		}
		return initialDamage;
	}
	
	static PokemonCard opponentCard(){
		Player opponent;
		if(PokemonGame.game.currPlayer == (PokemonGame.game.player1)){
			opponent = PokemonGame.game.player2;
		}
		else{
			opponent = PokemonGame.game.player1;
		}
		
		return (PokemonCard)opponent.benchAndActiveCardPanel.activeCard;
	}
	
	static Player opponent(){
		Player opponent;
		if(PokemonGame.game.currPlayer == (PokemonGame.game.player1)){
			opponent = PokemonGame.game.player2;
		}
		else{
			opponent = PokemonGame.game.player1;
		}
		return opponent;
	}
	
	static boolean flipCoin(){
		Random generator = new Random();
		int x = generator.nextInt();
		if(x % 2 == 0){
			JOptionPane.showMessageDialog(null, "Heads!");
			return true;
		}
		JOptionPane.showMessageDialog(null, "Tails!");
		return false;
	}
	
	boolean hasEnergy(int type, int amount){
		int count =0;
		for(int i=0; i<energy.size(); i++){
			if(energy.get(i).type == type || type == Card.COLORLESS){
				count += energy.get(i).energyIncrease;
			}
		}
		if(count >= amount){
			return true;
		}
		//JOptionPane.showMessageDialog(null, "Not enough energy!");
		return false;
	}
	
	int invisibleWallException(PokemonCard opponentCard, int damage){
		if(opponentCard.pokeId == 122 && opponentCard.status == Card.NONE && damage >= 30){
			return 0;
		}
		return damage;
	}
	
	int kabutoArmorException(PokemonCard opponentCard, int damage){
		if(opponentCard.pokeId == 140 && opponentCard.status == Card.NONE){
			damage /= 2;
			if(damage % 10 != 0) damage -= 5;
		}
		return damage;
	}
	
	
	void standardAttack(int damage){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		PokemonCard opponentCard = opponentCard();
		if(damage!=0) damage += pc.attackBoost;
		damage -= opponentCard.shield;
		damage = invisibleWallException(opponentCard, damage);
		damage = kabutoArmorException(opponentCard, damage);
		
		
		if(damage < 0) damage = 0;
		opponentCard.currHp -= damage;
		opponentCard.damageTaken = damage;
		if(opponentCard.name.equals("Machamp") && opponentCard.status == Card.NONE && damage > 0 && PokemonGame.game.toxicGasException() == false){
			pc.takeDamage(currPlayer(),10);
		}
		
		if(opponentCard.currHp <= 0){
			opponent().benchAndActiveCardPanel.knockOutPokemon(opponentCard);
		}
	}
	
	void drainAttack(int damage, int heal){
		heal(heal);
		standardAttack(damage);
	}
	
	void heal(int hp){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		pc.currHp += hp;
		if(pc.currHp > pc.maxHp){
			pc.currHp = pc.maxHp;
		}
	}
	
	void healRemoveEnergy(int hp, int eType){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		pc.currHp += hp;
		if(pc.currHp > pc.maxHp){
			pc.currHp = pc.maxHp;
		}
		if(metronome == false){
			removeEnergy(eType, currPlayer());
		}
	}
	
	static Player currPlayer(){
		return PokemonGame.game.currPlayer;
	}
	
	void poisonAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.poisoned == false && opponentCard.statusResistant == false){
			if(flipCoin() == true){
				opponentCard.poisoned = true;
				opponentCard.toxic = false;
				JOptionPane.showMessageDialog(null, opponentCard.name + " has been poisoned.");
			}
		}
		standardAttack(damage);
	}
	
	void toxicAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.toxic == false && opponentCard.statusResistant == false){
				opponentCard.poisoned = false;
				opponentCard.toxic = true;
				JOptionPane.showMessageDialog(null, opponentCard.name + " has been badly poisoned.");
		}
		standardAttack(damage);
	}
	
	void poisonAttackGuaranteed(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.poisoned == false && opponentCard.statusResistant == false){
				opponentCard.poisoned = true;
				opponentCard.toxic = false;
				JOptionPane.showMessageDialog(null, opponentCard.name + " has been poisoned.");
		}
		
		standardAttack(damage);
	}
	
	void specialStatusAttack(int damage, String status){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.statusResistant == false){
			opponentCard.specialStatus = status;
		}
		standardAttack(damage);
	}
	
	void confuseAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.CONFUSED && opponentCard.statusResistant == false){
			if(flipCoin() == true){
				opponentCard.status = Card.CONFUSED;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now confused.");
			}
		}
		standardAttack(damage);
	}
	
	void confuseAttackGuaranteed(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.CONFUSED && opponentCard.statusResistant == false){
				opponentCard.status = Card.CONFUSED;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now confused.");
			
		}
		standardAttack(damage);
	}
	
	void confuseAndPoisonAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if((opponentCard.status != Card.CONFUSED ||opponentCard.poisoned == false) && opponentCard.statusResistant == false){
			if(flipCoin() == true){
				opponentCard.status = Card.CONFUSED;
				opponentCard.poisoned = true;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now confused and poisoned.");
			}
		}
		standardAttack(damage);
	}
	
	void confuseSelf(){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		pc.status = Card.CONFUSED;
		JOptionPane.showMessageDialog(null, pc.name + " is now confused.");
	}
	
	void confuseSelfChance(){
		if(flipCoin() == false){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
			pc.status = Card.CONFUSED;
			JOptionPane.showMessageDialog(null, pc.name + " is now confused.");
		}
	}
	
	void sleepAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.ASLEEP && opponentCard.statusResistant == false){
			if(flipCoin() == true){
				opponentCard.status = Card.ASLEEP;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now asleep.");
			}
		}
		standardAttack(damage);
	}
	
	void sleepAttackGuaranteed(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.ASLEEP && opponentCard.statusResistant == false){
				opponentCard.status = Card.ASLEEP;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now asleep.");
			
		}
		standardAttack(damage);
	}
	
	void paralyzeAttack(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.PARALYZED && opponentCard.statusResistant == false){
			if(flipCoin() == true){
				opponentCard.status = Card.PARALYZED;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now paralyzed.");
			}
		}
		standardAttack(damage);
	}
	
	void paralyzeAttackGuaranteed(int damage){
		PokemonCard opponentCard = opponentCard();
		if(opponentCard.status != Card.PARALYZED && opponentCard.statusResistant == false){
				opponentCard.status = Card.PARALYZED;
				JOptionPane.showMessageDialog(null, opponentCard.name + " is now paralyzed.");
		}
		standardAttack(damage);
	}
	
	void energyDiscardAttack(int damage, int energyType, int numDiscards){
		if(metronome == false){
			for(int i=0; i<numDiscards; i++){
				removeEnergy(energyType, currPlayer());
			}
		}
		standardAttack(damage);
	}
	
	void multipleChanceAttack(int damage, int chances){
		int finalDamage =0;
		for(int i=0; i<chances; i++){
			if(flipCoin() == true){
				finalDamage += damage;
			}
		}
		standardAttack(finalDamage);
	}
	
	void hurtSelfAttack(int damage, int penalty){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		if(flipCoin() == false){
			JOptionPane.showMessageDialog(null, pc.name + " did " + penalty + " damage to itself!");
			takeDamage(PokemonGame.game.currPlayer,penalty);
		}
		standardAttack(damage);
	}
	
	void hurtSelfAttackGuaranteed(int damage, int penalty){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
			JOptionPane.showMessageDialog(null, pc.name + " did " + penalty + " damage to itself!");
			takeDamage(PokemonGame.game.currPlayer,penalty);
		standardAttack(damage);
	}
	
	void selfDestruct(int damage, int benchDamage){
		PokemonCard p;
		standardAttack(damage);
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
			p = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
			if(p.benchTakeDamage(currPlayer(), benchDamage, i) == true){
				i--;
			}
			p.setText(p.toString());
		}
		for(int i=0; i<opponent().benchAndActiveCardPanel.benchSize;i++){
			p = (PokemonCard) opponent().benchAndActiveCardPanel.bench[i];
			if(p.benchTakeDamage(opponent(), benchDamage, i) == true){
				i--;
			}
			p.setText(p.toString());
		}
		takeDamage(currPlayer(),damage);
	}
	
	void blizzard(int damage, int benchDamage){
		PokemonCard p;
		standardAttack(damage);
		if(flipCoin() == false){
			for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize;i++){
				p = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
				if(p.benchTakeDamage(currPlayer(), benchDamage, i) == true){
					i--;
				}
				p.setText(p.toString());
			}
		}
		else{
			for(int i=0; i<opponent().benchAndActiveCardPanel.benchSize;i++){
				p = (PokemonCard) opponent().benchAndActiveCardPanel.bench[i];
				if(p.benchTakeDamage(opponent(), benchDamage, i) == true){
					i--;
				}
				p.setText(p.toString());
			}
		}
	}
	
	void zapdosSpecialAttack(){
		PokemonCard p;
		standardAttack(modifiedDamage(40));
		int damageCounter = 0;
			for(int i=0; i<opponent().benchAndActiveCardPanel.benchSize;i++){
				p = (PokemonCard) opponent().benchAndActiveCardPanel.bench[i];
				if(flipCoin() == true){
					if(p.benchTakeDamage(opponent(), 20, i) == true){
						i--;
					}
					p.setText(p.toString());
				}
				else{
					damageCounter += 10;
				}
				
			}
		takeDamage(currPlayer(),damageCounter);
	}
	
	void mirrorMoveAttack(){
		PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
		JOptionPane.showMessageDialog(null, "Mirrored previous damage to " + pc.name + " for " + pc.damageTaken + " damage!");
		standardAttack(pc.damageTaken);
	}
	
	int retreatAidException(){
		int aid = 0;
		for(int i=0; i<currPlayer().benchAndActiveCardPanel.benchSize; i++){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.bench[i];
			if(pc.pokeId == 85){
				aid++;
			}
		}
		return aid;
	}
	
	int getRetreatCost(){
		int cost = retreatCost - retreatAidException();
		if(cost < 0){
			cost = 0;
		}
		return cost;
	}
	
	int removeEnergy(int energyType, Player player){
		PokemonCard pc = (PokemonCard) player.benchAndActiveCardPanel.activeCard;
		EnergyMasquerade e = null;
		for(int i=0; i< pc.energy.size(); i++){
			if(pc.energy.get(i).type == energyType || energyType == Card.COLORLESS){
				if(pc.energy.get(i).masquerade == false){
					player.deckAndDiscardPanel.addToDiscardPile(pc.energy.get(i));
					int value = pc.energy.get(i).energyIncrease;
					pc.energy.remove(pc.energy.get(i));
					return value;
				}
				else{
					e = (EnergyMasquerade) pc.energy.get(i);
				}
			}
		}
		int value = e.energyIncrease;
		player.deckAndDiscardPanel.addToDiscardPile(e);
		pc.energy.remove(e);
		return value;
	}
	
	boolean hasMultipleEnergyTypes(){
		PokemonCard pc = currPlayerCard();
		if(pc.energy.size() == 0) return false;
		int type = pc.energy.get(0).type;
		for(int i=0; i<pc.energy.size();i++){
			if(pc.energy.get(i).masquerade == true || pc.energy.get(i).type != type){
				return true;
			}
		}
		return false;
	}
	
	void tryAgility(){
		if(flipCoin()==true){
			PokemonCard pc = (PokemonCard) currPlayer().benchAndActiveCardPanel.activeCard;
			JOptionPane.showMessageDialog(null, "Agility successful! " + pc.name + " will not be affected in any way next round.");
			pc.agility = true;
		}
	}
	
	public PokemonCard(String cName, String cBasicStage, String cMiddleStage,int cPokeId, int Hp, boolean cHasPokemonPower, int cType, int cWeakness, int cResistance, int cRetreatCost, String picture, String cMoveOne, String cMoveTwo){ //second stage
		energy = new ArrayList<EnergyCard>();
		stages = new ArrayList<PokemonCard>();
		currHp = Hp;
		maxHp= Hp;
		name = cName;
		pokeId = cPokeId;
		basicStage = cBasicStage;
		middleStage = cMiddleStage;
		weakness = cWeakness;
		resistance = cResistance;
		retreatCost = cRetreatCost;
		moveOne = cMoveOne;
		moveTwo = cMoveTwo;
		hasPokemonPower= cHasPokemonPower;
		type = cType;
		status = Card.NONE;
		setLocation(Card.DECK);
		if(cMiddleStage != null){
			setCardType(Card.STAGE_2_POKEMON);
		}
		else if(cBasicStage != null){
			setCardType(Card.STAGE_1_POKEMON);
		}
		else{
			setCardType(Card.BASIC_POKEMON);
		}
		setCardImage(picture);
	}
	
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
		p1.add(l);
		
		JPanel p2 = new JPanel(new GridLayout(4,1,5,5));
		JLabel info = new JLabel(toString());
		JButton b1 = new JButton(moveOne);
		if((location == Card.ACTIVE || (hasPokemonPower == true && PokemonGame.game.toxicGasException() == false)) && moveOneRequirements() == true && amnesia != 1){		
			b1.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
						performMove(1);
						d.dispose();
				}
				
			});
		}
		else{
			b1.setEnabled(false);	
		}
		
		p2.add(info);
		p2.add(b1);
		if(moveTwo != null){
			JButton b2 = new JButton(moveTwo);
			if(location == Card.ACTIVE && moveTwoRequirements() == true && amnesia != 2){
				b2.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						performMove(2);
						d.dispose();
					}
					
				});
			}
			else{
				b2.setEnabled(false);
			}
			
			//two.setBorder(BorderFactory.createCompoundBorder(getBorder(),paddingBorder));
			p2.add(b2);
		}
		
		if(location == Card.ACTIVE){
			JButton b3 = new JButton("Pass");
			b3.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					specialStatus = null;
					if(status == Card.ASLEEP){
						if(flipCoin() == true){
							status = Card.NONE;
							JOptionPane.showMessageDialog(null, name + " woke up!");
						}
						else{
							JOptionPane.showMessageDialog(null, name + " is still asleep!");
						}
					}
					if(status == Card.PARALYZED){
						status = Card.NONE;
					}
					concludeTurn();
					d.dispose();
				}
				
			});
			p2.add(b3);
		}
		
		
		//d.add(p2, BorderLayout.CENTER);
		JPanel p3 = new JPanel();
		p3.add(p1);
		p3.add(p2);
		d.add(p3, BorderLayout.CENTER);
		d.setVisible(true);
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("<html>"+currHp+ "/" + maxHp);
		if(poisoned == true){
			sb.append("<br><font color=purple size = 1>Poison</font>");
		}
		else if(toxic == true){
			sb.append("<br><font color=purple size = 1>Toxic</font>");
		}
		if(shield > 0){
			if(shield == 500){
				sb.append("<br><font color=purple size = 1>Damage resistant</font>");
			}
			else{
				sb.append("<br><font color=purple size = 1>" + shield+ "shield </font>");
			}
		}
		if(attackBoost > 0){
			sb.append("<br><font color=purple size = 1>" + attackBoost+ "attack boost </font>");
		}
		if(specialStatus != null){
			sb.append("<br><font color=black size = 1>" + specialStatus + "</font>");
		}
		if(agility == true){
			sb.append("<br><font color=black size = 1>Protected</font>");
		}
		if(status == Card.ASLEEP){
			sb.append("<br><font color=gray size = 1>Asleep</font>");
		}
		else if(status == Card.PARALYZED){
			sb.append("<br><font color=yellow size = 1>Paralyze</font>");
		}
		else if(status == Card.CONFUSED){
			sb.append("<br><font color=black size = 1>Confused</font>");
		}
		
		int[] energies = {0,0,0,0,0,0,0};
		for(int i=0; i<energy.size();i++){
			switch(energy.get(i).type){
			case Card.COLORLESS: energies[0]+=energy.get(i).energyIncrease; break;
			case Card.WATER: energies[1]+=energy.get(i).energyIncrease; break;
			case Card.LEAF: energies[2]+=energy.get(i).energyIncrease; break;
			case Card.PSYCHIC: energies[3]+=energy.get(i).energyIncrease; break;
			case Card.FIRE: energies[4]+=energy.get(i).energyIncrease; break;
			case Card.FIGHTING: energies[5]+=energy.get(i).energyIncrease; break;
			case Card.THUNDER: energies[6]+=energy.get(i).energyIncrease; break;
			}
		}
		
		for(int i=0; i<7;i++){
			if(energies[i]>0){
				switch(i){
				case 0: sb.append("<br><font color=gray>\u25A0</font>: " + energies[i]); break;
				case 1: sb.append("<br><font color=blue>\u25A0</font>: " + energies[i]); break;
				case 2: sb.append("<br><font color=green>\u25A0</font>: " + energies[i]); break;
				case 3: sb.append("<br><font color=purple>\u25A0</font>: " + energies[i]); break;
				case 4: sb.append("<br><font color=red>\u25A0</font>: " + energies[i]); break;
				case 5: sb.append("<br><font color=orange>\u25A0</font>: " + energies[i]); break;
				case 6: sb.append("<br><font color=yellow>\u25A0</font>: " + energies[i]); break;
				}
			}
		}
		sb.append("</html>");
		return sb.toString();
	}
	
	boolean matchesEarlyStage(PokemonCard otherCard){
		if(cardType == Card.STAGE_1_POKEMON){
			if(basicStage.equals(otherCard.name)){
				return true;
			}
		}
		else{
			if(middleStage.equals(otherCard.name)){
				return true;
			}
		}
		return false;
	}
	
	
}



