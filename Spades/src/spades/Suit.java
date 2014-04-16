package spades;

/*
 * 
 * - improve AI (especially for nil bids, partnership)
 * 		- use compare to avoid wasting high cards (want to take tricks with K and A)
 * 		- if partner bid nil, try to take tricks if partner is going to take it
 * 		- if self bid nil, play high cards when they will lose
 * 
 */
//represents a computer's counting of any suit. Gives useful information that the computer can deduct from gameplay.
class Suit {
	String type;
	int roundsPlayed;
	int highCard;
	int currValue;
	boolean nPlayerEmpty, sPlayerEmpty, ePlayerEmpty, wPlayerEmpty;
	
	public Suit(String type){
		this.type = type;
		roundsPlayed =0;
		highCard = 14;
		currValue = 0;
		nPlayerEmpty = false; sPlayerEmpty = false; ePlayerEmpty = false; wPlayerEmpty = false;
	}
	
	//is used to return if highest to fourth highest card in suit
	int rankInComparison(int value){
		int rank = highCard - value;
		switch(rank){
		case 0: return 8;
		case 1: return 4;
		case 2: return 2;
		case 3: return 1;
		default: return 0;
		}
	}
	
	
	//these tell a computer when a player is empty in the suit (when it is obvious so)
	void setPlayerEmpty(Player player){
		if(player.getType().equals("North")) nPlayerEmpty = true;
		else if(player.getType().equals("South")) sPlayerEmpty = true;
		else if(player.getType().equals("East")) ePlayerEmpty = true;
		else wPlayerEmpty = true;
	}
	
	boolean playerEmpty(Player player){
		if(player.getType().equals("North")) return nPlayerEmpty;
		else if(player.getType().equals("South")) return sPlayerEmpty;
		else if(player.getType().equals("East")) return ePlayerEmpty;
		else return wPlayerEmpty;
	}
	
	void reduceHighCard(int change){
		highCard -= change;
	}
	
	//returns the highest card of the suit that hasn't yet been played
	int rankDifference(int value){
		
		return highCard-value;
	}
}