package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Race {

	private static int width = 800;
	private static int height = 600;
	
	private static JFrame frame;
	private static JPanel panel;
	
	private static Car player = new Car(false, "Henry", Color.green);
	
	
	public static void main(String[] args){
		init();
		
//		long lastTime = System.currentTimeMillis();
		while (true){
//			if (System.currentTimeMillis()-lastTime > 5){
				
				player.update();
				panel.repaint();
//				lastTime = System.currentTimeMillis();
//			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}//main method
	
	public static void init(){
		frame = new JFrame("Race Track");
		frame.setBounds(800,100,width,height);
		frame.setBackground(Color.yellow);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()){
				case KeyEvent.VK_LEFT:
//					player.turnLeft();
					player.dState = Car.STATE.REVERSE;
					break;
				case KeyEvent.VK_RIGHT:
					player.dState = Car.STATE.FORWARD;
//					player.turnRight();
					break;
				case KeyEvent.VK_UP:
//					player.speedUp();
					player.vState = Car.STATE.FORWARD;
					break;
				case KeyEvent.VK_DOWN:
//					player.slowDown();
					player.vState = Car.STATE.REVERSE;
					break;
				}
			}
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()){
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					player.dState = Car.STATE.NEUTRAL;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
					player.vState = Car.STATE.NEUTRAL;
					break;

				}
			}
		});
		
		panel = new JPanel(){
			@Override
			public void paint(Graphics g){
//				super.paint(g);
				g.setColor(getBackground());
				g.fillRect(getX(),getY(),getWidth(),getHeight());
				g.setColor(Color.white);
				g.fillRect(20,20,200,200);
				
				player.render(g);
				
			}
		};
		panel.setBounds(0,0,width,height);
		panel.setBackground(Color.black);
		
		frame.add(panel);
		frame.setVisible(true);
		
		
	}
	
}//Race class