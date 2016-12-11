package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetworkGraphics extends JPanel {

	public static int width = 800;
	public static int height = width;
	
	public static JLabel word = new JLabel();
	public static JLabel data = new JLabel();
	public static Rectangle zone = new Rectangle(120,70,500,700);
	public static NetworkGraphics screen;
	
//	static boolean working = false;
	public static LangDict english;
	public static LangDict dutch;
	
	public static volatile boolean paused = true;
	
	public static int trainingCycle = 0;
	public static int currentWord = 0;
	public static int setSize = 0;
	
	public synchronized void paint(Graphics g){
		super.paint(g);
		
		int[] xs = {(int) zone.getX(),(int) (zone.getX()+zone.getWidth()/2),
				(int) (zone.getX()+zone.getWidth())};
		//ys = zone.getY() + j/16;
		
		//Draw Inputs
		for (int i=0;i<16;i++){
			int x = xs[0]-10;
			int y = (int) (zone.getY()+i*zone.getHeight()/16-10);
			g.setColor(Color.white);
			g.fillOval(x-10,y-10,20,20);
			
			g.setColor(Color.black);
			synchronized (this){
			if (i < Network.testWord.length()) g.drawString(""+Network.testWord.charAt(i), x-3, y+6);
			}
		}
		
		//Draw Mid Values
		for (int i=0;i<16;i++){
			int x = xs[1]-10;
			int y = (int) (zone.getY()+i*zone.getHeight()/16-10);
			g.setColor(Color.white);
			g.fillOval(x-10,y-10,80,20);
			
			g.setColor(Color.black);
			g.drawString(String.format("%.2f",Network.mid[i]), x-4, y+4);
		}
		
		//Draw End Values
		for (int i=0;i<12;i++){
			int x = xs[2]-10;
			int y = (int) (zone.getY()+i*zone.getHeight()/16-10);
			g.setColor(Color.white);
			g.fillRect(x-10,y-10,160,20);
			
			g.setColor(Color.black);
			g.drawString(String.format("%.2f -- %s",Network.outputs[i],Network.outputNames[i]), x-4, y+4);
		}
		
		//Draw Synapse1
		for (int i=0;i<16;i++){
			if (i >= Network.testWord.length()) break;
			
			int in = (int)Network.testWord.charAt(i);
			if ('A' <= in && in <= 'Z') in += 32;
			in -= 97;
			
			for (int j=0;j<16;j++){
				float val = Network.synapses[0][26*16*i+16*in+j];
//				val = val*val;
				
				g.setColor(new Color(val,val,val));//0 == Black
				int x = xs[0]+10;
				int x1 = xs[1]-20;
				int y = (int) (zone.getY()+i*zone.getHeight()/16-10);
				int y1 = (int) (zone.getY()+j*zone.getHeight()/16-10);
				g.drawLine(x,y,x1,y1);
			}
		}
		//Draw Synapse2
		for (int i=0;i<16;i++){
			for (int j=0;j<12;j++){
				float val = Network.synapses[1][12*i+j];
//				val = 0.5f;
				g.setColor(new Color(val,val,val));//0 == Black
				int x = xs[1]+60;
				int x1 = xs[2]-20;
				int y = (int) (zone.getY()+i*zone.getHeight()/16-10);
				int y1 = (int) (zone.getY()+j*zone.getHeight()/16-10);
				g.drawLine(x,y,x1,y1);
			}
		}
		
		//Draw Word Tracker
		g.setColor(Color.white);
		g.fillRect(10,10,100,10);
		g.setColor(Color.black);
		int x = 10+(currentWord*100)/setSize;
//		System.out.println(x);
		g.drawLine(x,10,x,20);
		
		//If paused
		if (paused){
			g.setColor(Color.blue);
			g.fillOval(0,height-100,70,70);
		}
		
//		if (working){
//			g.setColor(Color.red);
//			g.fillOval(70,height-100,70,70);
//		}
		
		
	}//paint method
	
	public static void main(String[] args){
		
		english = new LangDict("English");
		dutch = new LangDict("Dutch");
		
		setSize = Math.min(english.count, dutch.count);
		
		Network.init();
		Network.test("Revolutionary");
		
		screen = new NetworkGraphics();
		
		screen.initGraphics();
		
		for (trainingCycle=0;trainingCycle<Network.timesToTrain;trainingCycle++){
			for (currentWord=0;currentWord<setSize;currentWord++){
				if (paused){
					while(paused);
				}
				step(true);
			}
		}
		
	}//main method
	
	public static void step(boolean paint){
		Network.train(english.getWord(currentWord),1);
		Network.train(dutch.getWord(currentWord),7);
		
		if (paint){
			word.setText(wordProgress()+Network.testWord);
			data.setText("Success Rate: "+Network.successRate());
			screen.repaint();
		}
		
		currentWord++;
		if (currentWord >= setSize){
			currentWord -= setSize;
			trainingCycle++;
		}
	}//step method
	public static void step(int count){
		for (int i=0;i<count;i++){
			step(false);
		}
	}
	public static void stepSet(){
//		working = true;
		screen.repaint();

		for (int i=0;i<setSize;i++){
			step(false);
		}
//		working = false;
		screen.repaint();
	}//setSet

	public static String wordProgress(){
		return String.format("%5d/%5d  ",currentWord+1,setSize);
	}//wordProgress method
	
	public void initGraphics(){
		JFrame frame = new JFrame("Neural Network");
		frame.setSize(800,800);
		frame.setBackground(Color.yellow);
		frame.setLocation(1000,100);
		frame.addKeyListener(new KeyListener(){

			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
				case KeyEvent.VK_SPACE:
//					System.out.println(paused?"UNPAUSED":"PAUSED");
					paused = !paused;
					break;
				case KeyEvent.VK_RIGHT:
					if (e.isControlDown()){
						if (e.isShiftDown()){
							if (e.isAltDown()){
								step(1000);
							} else {
								step(100);
							}
						} else {
							step(10);
						}
					} else {
						step(true);
					}
					word.setText(wordProgress()+Network.testWord);
					data.setText("Success Rate: "+Network.successRate());
					screen.repaint();
					break;
				case KeyEvent.VK_DOWN:
					stepSet();
					break;
				case KeyEvent.VK_R:
					Network.reset();
					currentWord = 0;
					word.setText(wordProgress()+english.getWord(0));
					data.setText("Success Rate: "+Network.successRate());
					screen.repaint();
					break;
				}
			}
			public void keyReleased(KeyEvent e) {
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(0,0,width,height);
		setBackground(Color.black);
		setLayout(null);
		
		word.setBounds(10,30,200,20);
		word.setText(wordProgress()+Network.testWord);
//		word.setBackground(Color.white);
		word.setForeground(Color.white);
		add(word);
		
		data.setBounds(210,10,200,20);
		data.setText("Success Rate: "+Network.successRate());
		data.setForeground(Color.white);
		add(data);
		
		frame.add(this);
		frame.setVisible(true);
	}
	
}//NetworkGraphics class