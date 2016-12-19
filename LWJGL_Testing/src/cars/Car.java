package cars;

import static org.lwjgl.opengl.GL11.*;


public class Car {

	public static enum STATE {
		REVERSE, NEUTRAL, FORWARD
	}
	
	private boolean ai;
	private String name;
	
	private int r;
	private int g;
	private int b;
	
	private float x;
	private float y;

	private float sf;
	private float vel;
		private float velDelta;
		public STATE vState;
		private static final float D_VEL = 0.05f;
	private float dir;
		private float dirDelta;
		public STATE dState;
		private static float D_DIR = 0.002f;
		
	public void update(float dt){

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

		
		//Update Car Fields
		x += dt*(float)(vel*Math.cos(dir));
		y += dt*(float)(vel*Math.sin(dir));
		
		vel += dt*velDelta;
		vel *= (float)(Math.pow(sf,dt));
		velDelta *= (float)(Math.pow(sf,dt));
		
		dir += dt*dirDelta;
		if (dirDelta != 0) dirDelta *= (float)(Math.pow(sf,dt));

		
	}//update method
	public void render(){
		
		glColor3f(r,g,b);

		glPushMatrix();
		glTranslatef(x,y,0);
		
		glRotatef((float)(dir*180/Math.PI), 0, 0, 1);
		glBegin(GL_POLYGON);
			
//			float cos = (float)	Math.cos(dir+Math.PI/6);
//			float sin = (float)Math.sin(dir+Math.PI/6);
//			float cos2 = (float) (cos/2+sin*Math.sqrt(3)/2);
//			float sin2 = (float) (sin/2-cos*Math.sqrt(3)/2);
//			
//			glVertex2f(10*cos,10*sin);
////			glVertex2f()
//			glVertex2f(10*cos2,10*sin2);
//			glVertex2f(-10*cos,-10*sin);
//			glVertex2f(-10*cos2,-10*sin2);
			
			glVertex2f(10,5);
			glVertex2f(15,0);
			glVertex2f(10,-5);
			glVertex2f(-10,-5);
			glVertex2f(-10,5);
		
		glEnd();
		
//		glRotatef((float)(6*dirDelta*180/Math.PI),0,0,1);
		glTranslatef(0,dirDelta*50000,0);
		glColor3f(1,0,0);
		
		glBegin(GL_QUADS);
			glVertex2f(0,-1);
			glVertex2f(0,1);
			glVertex2f(1,1);
			glVertex2f(1,-1);
		glEnd();
		
		glPopMatrix();
		
	}//render method
		
	public void forward(){
		vState = STATE.FORWARD;
	}
	public void reverse(){
		vState = STATE.REVERSE;
	}
	public void stopDrive(){
		vState = STATE.NEUTRAL;
	}
	public void right(){
		dState = STATE.FORWARD;
	}
	public void left(){
		dState = STATE.REVERSE;
	}
	public void stopTurn(){
		dState = STATE.NEUTRAL;
	}
	
	public void speedUp(){
		velDelta = D_VEL;
	}//speedUp method
	public void slowDown(){
		velDelta = -D_VEL;
	}//slowDown method
	public void turnLeft(){
		dirDelta = -D_DIR;
	}//turnLeft method
	public void turnRight(){
		dirDelta = D_DIR;
	}//turnRight method
	
	public Car (boolean isAiControlled, String __name, int red, int green, int blue){
		ai = isAiControlled;
		name = __name;
		
		r = red;
		g = green;
		b = blue;
		
		x = 50;
		y = 50;
		
		sf = 0.98f;
		vel = 0;
		velDelta = 0;
		vState = STATE.NEUTRAL;
		dir = 0;
		dState = STATE.NEUTRAL;
		dirDelta = 0;
	}
	
}//Car class