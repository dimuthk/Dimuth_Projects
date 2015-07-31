package spades;
import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Scoreboard extends JFrame implements ActionListener{
	
	private JPanel nsScorecard;
	private JPanel ewScorecard;
	private JLabel finalScoreLabel;
	private int nsBags, ewBags;
	private int nsScore, ewScore;
	private static int finalScore=50;		//score to play up to/10
	private JButton done;
	private JButton close;
	private JButton newGame;
	private static final long serialVersionUID = 1L;

	public Scoreboard(){
		setSize(400, 300);
		//setModal(true);
	
		setTitle("Score");
		setLocation(300,200);
		setLayout(new BorderLayout());
		setAlwaysOnTop(true);
		
		constructScoreboard();
		clearScore();
	}
	
	static void setFinalScore(int points){
		finalScore = points;
	}
	void showScore(){
		setVisible(true);
		getDone().setVisible(true);
		getClose().setVisible(false);
		getNewGame().setVisible(false);
	}
	void clearScore(){
		nsScore = 0;
		ewScore = 0;
		nsBags = 0;
		ewBags = 0;
	}
	
	void updateScore(int[] information){
		//nsScore, nsbags, ewscore, ewbags
		nsScore += information[0];
		nsBags += information[1];
		if(nsBags > 9){
			nsBags -= 10;
			nsScore -= 10;
		}
		ewScore += information[2];
		ewBags += information[3];
		if(ewBags > 9){
			ewBags -= 10;
			ewScore -= 10;
		}
		
		nsScorecard.add(new JLabel(nsScore*10 + " (" + nsBags + ")"));
		ewScorecard.add(new JLabel(ewScore*10 + " (" + ewBags + ")"));
		showScore();
		if ((ewScore>=finalScore||nsScore>=finalScore)&&ewScore+ewBags!=nsScore+nsBags){
			getDone().setVisible(false);
			getNewGame().setVisible(true);
			getClose().setVisible(false);
			String champion=null;
			if((ewScore+ewBags)-(nsScore+nsBags)>0){
				champion = "EW";
			}else{
				champion = "NS";
			}
			JOptionPane.showMessageDialog(rootPane, "Winning team is "+champion);
		}
	}
	
	void constructScoreboard(){
		JPanel middle = new JPanel();
		JPanel bottom = new JPanel();
		JPanel top = new JPanel();
		finalScoreLabel = new JLabel("Game ends at: "+finalScore*10+" points");
		top.add(finalScoreLabel);
		middle.setLayout(new GridLayout(0,2));
		
		ewScorecard = new JPanel();
		ewScorecard.setLayout(new BoxLayout(ewScorecard,BoxLayout.Y_AXIS));
		nsScorecard = new JPanel();
		nsScorecard.setLayout(new BoxLayout(nsScorecard,BoxLayout.Y_AXIS));
		nsScorecard.add(new JLabel("North/South"));
		ewScorecard.add(new JLabel("East/West"));
		middle.add(ewScorecard,BorderLayout.WEST);
		middle.add(nsScorecard,BorderLayout.EAST);

		setDone(new JButton("Next Round"));
		getDone().addActionListener(this);
		setClose(new JButton("Close"));
		getClose().addActionListener(this);
		setNewGame(new JButton("New Game"));
		getNewGame().addActionListener(this);
		bottom.add(getDone());
		bottom.add(getClose());
		bottom.add(getNewGame());
		
		add(top, BorderLayout.NORTH);
		add(middle, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if(source==getDone()){
			setVisible(false);
			Spades.nextDealer();
			Spades.startRound();
		}
		else if(source == getClose()){
			setVisible(false);
		}else if(source == getNewGame()){
			Spades.newGameScreen();
		}
	}

	public void setDone(JButton done) {
		this.done = done;
	}

	public JButton getDone() {
		return done;
	}

	public void setNewGame(JButton newGame) {
		this.newGame = newGame;
	}

	public JButton getNewGame() {
		return newGame;
	}

	public void setClose(JButton close) {
		this.close = close;
	}

	public JButton getClose() {
		return close;
	}
	
}



 