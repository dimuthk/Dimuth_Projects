package spades;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


class Card extends JLabel implements MouseListener, Comparable<Card>{

	private static final long serialVersionUID = 1L;
	private ImageIcon front, back, curr;
	private Player owner;
	private String suit;
	private int value;
	private static int cardCount = 1;
	private boolean isClickable=false;

	public Card(int num){
		super();
		if(num == 1){
			front = new ImageIcon("images/emptyslot"+num+".png"); 
			back = new ImageIcon("images/emptyslot"+num+".png");
			setCurr(front);
		}
		else{
			front = new ImageIcon("images/background.jpg"); 
			back = new ImageIcon("images/background.jpg");
			Image image = back.getImage().getScaledInstance(60, 95,
					Image.SCALE_SMOOTH);
			back.setImage(image);
		}
		
	}
	
	public Card() {
		super();
		front = new ImageIcon("images/" + cardCount + ".png"); 
		
		switch(cardCount%4){
		case 0: setSuit("diamonds"); break;
		case 1: setSuit("clubs"); break;
		case 2: setSuit("spades"); break;
		case 3: setSuit("hearts"); break;
		}
		
		int temp = (int) Math.ceil(((double)cardCount/4));
		setValue(15-temp);
				
		back = new ImageIcon("images/background.jpg");
		cardCount++;
		Image image = back.getImage().getScaledInstance(60, 95,
				Image.SCALE_SMOOTH);
		back.setImage(image);
		this.addMouseListener(this);
	}
	
	String getValueString(){
		if(getValue() == 11) return "J";
		if(getValue() == 12) return "Q";
		if(getValue() == 13) return "K";
		if(getValue() == 14) return "A";
		return getValue()+"";
	}
	
	//specifically for sorting cards. do not use for actual game
	boolean comesBefore(Card otherCard){
		//rank: D C H S
		if(this.getSuit().equals(otherCard.getSuit())){
			if(this.getValue() < otherCard.getValue()) return false;
			else return true;
		}
		else{
			if(this.getSuit().equals("diamonds")) return true;
			if(this.getSuit().equals("clubs")){
				if(otherCard.getSuit().equals("diamonds")) return false;
				else return true;
			}		
			if(this.getSuit().equals("hearts")){
				if(otherCard.getSuit().equals("spades")) return true;
				else return false;
			}
			else return false;
		}
	}
	
	boolean highCard(){
		return getValue() > 12;
	}
	
	boolean spade(){
		return getSuit().equals("spades");
	}
	
	boolean highSpade(){
		return (getSuit().equals("spades")&&getValue()>11);
	}
	
	void showFront(){
		setCurr(front);
		setIcon(front);
	}
	
	void showBack(){
		setCurr(back);
		setIcon(back);
	}
	  
	Player getOwner(){
		return owner;
	}
	
	void setOwner(Player owner){
		this.owner = owner;
	}

	ImageIcon getCover() {
		return getCurr();
	}

	void rotateImage(Component c, Icon icon, double rotatedAngle) {
		// convert rotatedAngle to a value from 0 to 360
		
		double originalAngle = rotatedAngle % 360;
		if (rotatedAngle != 0 && originalAngle == 0) {
			originalAngle = 360.0;
		}

		// convert originalAngle to a value from 0 to 90
		double angle = originalAngle % 90;
		if (originalAngle != 0.0 && angle == 0.0) {
			angle = 90.0;
		}

		double radian = Math.toRadians(angle);

		int iw = icon.getIconWidth();
		int ih = icon.getIconHeight();
		int w;
		int h;

		if ((originalAngle >= 0 && originalAngle <= 90)
				|| (originalAngle > 180 && originalAngle <= 270)) {
			w = (int) (iw * Math.sin(90 - radian) + ih * Math.sin(radian));
			h = (int) (iw * Math.sin(radian) + ih * Math.sin(90 - radian));
		} else {
			w = (int) (ih * Math.sin(90 - radian) + iw * Math.sin(radian));
			h = (int) (ih * Math.sin(radian) + iw * Math.sin(90 - radian));
		}
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		Graphics2D g2d = (Graphics2D) g.create();

		// calculate the center of the icon.
		int cx = iw / 2;
		int cy = ih / 2;

		// move the graphics center point to the center of the icon.
		g2d.translate(w / 2, h / 2);

		// rotate the graphcis about the center point of the icon
		g2d.rotate(Math.toRadians(90));

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		icon.paintIcon(c, g2d, -cx, -cy);

		g2d.dispose();
		if(getCurr() == back){
			back = new ImageIcon(image);
			showBack();
		}
		else{
			front = new ImageIcon(image);
			showFront();
		}
		}

	
	@Override
	public void mouseClicked(MouseEvent arg0) {
//		if(owner.human == true){
		if(isClickable){
			owner.playCard(this);
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {	
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}


	@Override
	public int compareTo(Card otherCard) {
		if(this.getSuit().equals(otherCard.getSuit())) return this.getValue() - otherCard.getValue();
		if(this.getSuit().equals("spades")&&!otherCard.getSuit().equals("spades")) return 1;
		if(!this.getSuit().equals("spades")&&otherCard.getSuit().equals("spades")) return -1;
		if(this.getSuit().equals(Spades.getStartSuite()) && !otherCard.getSuit().equals(Spades.getStartSuite())) return 1;		
		else return -1;
	}

	public static void reset() {
		cardCount=1;
	}

	public void setClickable(boolean b) {
		// TODO Auto-generated method stub
		isClickable = b;
	}

	public void setCurr(ImageIcon curr) {
		this.curr = curr;
	}

	public ImageIcon getCurr() {
		return curr;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getSuit() {
		return suit;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
