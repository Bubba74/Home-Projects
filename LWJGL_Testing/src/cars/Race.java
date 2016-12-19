package cars;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import Utilities.Notify;

import finished.BowMaster;

public class Race {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	
	static Car c = new Car(false,"ME",0,1,0);
	
	public void start(){
		initGL();
//		Font awtFont = new Font("Times new Roman",Font.BOLD,18);
//		print = new TrueTypeFont(awtFont,false);
//		printHeight = print.getHeight("T");	
		
//		display = new Notify();
		
		long lastTime = System.currentTimeMillis()-5;
		
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			float dt = 1000/(float)(System.currentTimeMillis()-lastTime);
			lastTime = System.currentTimeMillis();
			
			poll();
			update(dt);
			render();
			
			Display.update();
			Display.sync(30);

			if (!Keyboard.isKeyDown(Keyboard.KEY_C))
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		}

		Display.destroy();
		System.exit(0);
	}//start method
	
	public void poll (){
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
			c.forward();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			c.reverse();
		} else {
			c.stopDrive();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			c.right();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			c.left();
		} else {
			c.stopTurn();
		}
		
	}//poll method
	
	public void update(float dt){
		
		c.update(dt);
		System.out.println(dt);
	}//update method
	
	public void render(){
		c.render();
	}//render method
	
	
	
	public void initGL(){
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("BowMaster ["+WIDTH+","+HEIGHT+"]");
			Display.setInitialBackground(4,4,4);
			Display.create();
			///////^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^/////////////
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
	}//initGL method
	public static void main(String Args[]){
		Race race = new Race();
		race.start();
	}//main method
	
}//Race class
