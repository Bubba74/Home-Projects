package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Car {

	public static enum STATE {
		REVERSE, NEUTRAL, FORWARD
	}
	
	private boolean ai;
	private String name;
	private Color color;
	
	private float x;
	private float y;

	private float sf;
	private float vel;
		private float velDelta;
		public STATE vState;
		private static final float D_VELTA = 0.02f;
	private float dir;
		private float dirDelta;
		public STATE dState;
		private static float D_DIR = 0.05f;
		
	public void update(){

		//Update Car Fields
		x += (float)(vel*Math.cos(dir));
		y += (float)(vel*Math.sin(dir));
		
		vel += velDelta;
		vel *= sf;
		velDelta *= sf;
		
		dir += dirDelta;
		if (dirDelta != 0) dirDelta *= sf;

		switch (vState){
		case REVERSE:
			slowDown();
			break;
		case NEUTRAL:
			break;
		case FORWARD:
			speedUp();
			break;
		}
		switch (dState){
		case REVERSE:
			turnLeft();
			break;
		case NEUTRAL:
			break;
		case FORWARD:
			turnRight();
			break;
		}
		
		
	}//update method
	public void render(Graphics g){
		g.setColor(color);
		
		Polygon img = new Polygon();
		float cos = (float)Math.cos(dir+Math.PI/6);
		float sin = (float)Math.sin(dir+Math.PI/6);
		float cos2 = (float) (cos/2+sin*Math.sqrt(3)/2);
		float sin2 = (float) (sin/2-cos*Math.sqrt(3)/2);
		
		img.addPoint((int)(10*cos),(int)(10*sin));
//		img.addPoint(3*dx/2,0);
		img.addPoint((int)(10*cos2),(int)(10*sin2));
		img.addPoint((int)(-10*cos),(int)(-10*sin));
		img.addPoint((int)(-10*cos2),(int)(-10*sin2));
		
		img.translate((int)x,(int)y);
		
		g.fillPolygon(img);
	}//render method
		
	public void speedUp(){
		velDelta = D_VELTA;
	}//speedUp method
	public void slowDown(){
		velDelta = -D_VELTA;
	}//slowDown method
	public void turnLeft(){
		dirDelta = -D_DIR;
	}//turnLeft method
	public void turnRight(){
		dirDelta = D_DIR;
	}//turnRight method
	
	public Car (boolean isAiControlled, String __name, Color c){
		ai = isAiControlled;
		name = __name;
		color = c;
		
		x = 50;
		y = 50;
		
		sf = 0.9f;
		vel = 0;
		velDelta = 0;
		vState = STATE.NEUTRAL;
		dir = 0;
		dState = STATE.NEUTRAL;
		dirDelta = 0;
	}
	
}//Car class