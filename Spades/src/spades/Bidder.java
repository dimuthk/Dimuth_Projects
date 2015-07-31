package spades;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Bidder extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7469943364526584982L;


	private class Contract extends JLabel{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3254021322112018894L;
		private int bid;
		private String type;
		private boolean blind;
		
		public Contract(String type){
			super();
			setResizable(false);
			this.type = type;
		}
		
		public void setContract(int bid, boolean blind){
			this.bid = bid;
			this.setBlind(blind);
			String value = blind ? "blind nil":String.valueOf(bid);
			this.setText(type + " bid: " + value);
		}
		
		public int getContract(){
			return bid;
		}
		
		public void clear(){
			this.setText(type+ " bid: ");
		}
		
		public boolean wasNil(){
			return bid == 0;
		}

		public boolean isBlind() {
			return blind;
		}

		public void setBlind(boolean blind) {
			this.blind = blind;
		}
	}
	
	private class BidButton extends JButton implements ActionListener{

		private static final long serialVersionUID = 1L;
		int bid;
		boolean blind;

		public BidButton(int input){
			super(""+input);
			bid = input;
			addActionListener(this);
		}
		public BidButton(String name){
			super(name);
			if(name.equals("blind nil")){
				bid = 0;
				blind = true;
			}else{
				blind = false;
			}
			addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Spades.simulateBidding(bid, blind);
		}
		
	}
	
	Contract nContract, sContract, eContract, wContract;
	JPanel bids;
	JButton blindButton = new BidButton("blind nil");

	public Bidder(){

		setSize(300, 400);
		setTitle("Bidder");
		setAlwaysOnTop(true);
		setLocation(350,100);
		JPanel info = new JPanel();
		info.setLayout(new GridLayout(3,2));
		add(new JLabel("Please select a bid."), BorderLayout.NORTH);
		
		nContract = new Contract("North");
		sContract = new Contract("South");
		eContract = new Contract("East");
		wContract = new Contract("West");
		
		info.add(nContract);
		info.add(sContract);
		info.add(eContract);
		info.add(wContract);
		
		add(info, BorderLayout.CENTER);
		
		bids = new JPanel();
		bids.setLayout(new GridLayout(8,2));
		for(int i=0; i<14; i++){
			bids.add(new BidButton(i));
		}
		if(Spades.isBlindEnabled()){
			bids.add(Spades.getShowButton());
			bids.add(blindButton);
			Spades.getShowButton().setEnabled(true);
			blindButton.setEnabled(true);
		}
		add(bids, BorderLayout.PAGE_END);
	}
	
	void reset(){
		nContract.clear();
		sContract.clear();
		eContract.clear();
		wContract.clear();
	}
	
	int[] evaluateContracts(int nTricks, int sTricks, int eTricks, int wTricks){
		//for NS:
		int nsScore=0, ewScore=0;
		int nsBags=0, ewBags=0;
		int northContract = nContract.getContract();
		int southContract = sContract.getContract();
		int eastContract = eContract.getContract();
		int westContract = wContract.getContract();

		// easiest to count any nils first

		if(nContract.wasNil()){		// north bid nil
			if(nTricks==0)			// and made it
				if(!nContract.isBlind())
					nsScore+=10;				// add 100
				else
					nsScore+=20;				// 200 for blind
			else{								// if they missed it
				if(!nContract.isBlind())
					nsScore-=10;				// subtract instead
				else
					nsScore-=20;		
				nsBags+=nTricks;				// the bags do count		
				nTricks=0;						// but the tricks don't
			}
		}
		if(sContract.wasNil()){
			if(sTricks==0)						// same for south
				if(!sContract.isBlind())
					nsScore+=10;
				else
					nsScore+=20;
			else{					
				if(!sContract.isBlind())
					nsScore-=10;
				else
					nsScore-=20;
				nsBags+=sTricks;				
				sTricks=0;
			}
		}

		// then score as usual
		if((nTricks+sTricks)<(northContract+southContract)){	// didn't get enough
			nsScore += -(northContract+southContract);
		}
		else{													// got enough
			nsScore += (northContract+southContract);
			nsBags += -((northContract+southContract)- (nTricks+sTricks));
		}
		if(eContract.wasNil()){		// east bid nil
			if(eTricks==0)			// and made it
				ewScore+=10;			// add 100
			else{						// if they missed it
				ewScore-=10;			// subtract 100
				ewBags+=eTricks;		// the bags do count		
				eTricks=0;			// but the tricks don't
			}
		}
		if(wContract.wasNil()){		
			if(wTricks==0)			// same for west
				ewScore+=10;		
			else{
				ewScore-=10;		
				ewBags+=wTricks;				
				wTricks=0;
			}
		}
		if((eTricks+wTricks)<(eastContract+westContract)){
			ewScore += -(eastContract+westContract);
		}
		else{
			ewScore += (eastContract+westContract);
			ewBags += -((eastContract+westContract)- (eTricks+wTricks));
		}
		int[] result = {nsScore, nsBags, ewScore, ewBags};
		return result;
	}
	
	void turnOff(){
		setVisible(false);
	}
	
	void update(){
		bids.revalidate();
		bids.repaint();
	}
	
	void turnOn(){
		setVisible(true);
	}

	void setBid(Player player, int bid, boolean blind){
		if(player.isNorth()) nContract.setContract(bid, blind);
		else if(player.isSouth()) sContract.setContract(bid, blind);
		else if(player.isEast()) eContract.setContract(bid, blind);
		else if(player.isWest()) wContract.setContract(bid, blind);
	}
}
