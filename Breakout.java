package test;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;


import hsa2.GraphicsConsole;
public class Breakout {
	public static void main(String[] args) {
		new Breakout();
	}
	GraphicsConsole gc=new GraphicsConsole(800,600);
	Rectangle r1 = new Rectangle(500,550,80,10);
	int bn=50;
	
	block[] b=new block[bn];
	ball ball1= new ball(400, 300, 10, 10);
	int FPS=37;
	double averageFPS;
	long startTime;
	long URDtimeMills;
	long waitTime;
	long totalTime=0;
	long targetTime=1000/FPS;
	int frameCount=0;
	int maxframeCount=30;
	int speedx=4;
	int speedy=4;
	int score=0;
	int life=3;
	boolean boost1=true;
	boolean boost2=true;
	String name;
	public Breakout() {
		setup();
		while (true) {
			startTime=System.nanoTime();
			move();
			graph();
			if (score>10&&boost1) {
				speedx*=1.5;
				speedy*=1.5;
				boost1=false;
			}
			if (score>30&&boost2) {
				speedx*=1.5;
				speedy*=1.5;
				boost2=false;
			}
			URDtimeMills =(System.nanoTime()-startTime)/1000000;
			waitTime=targetTime-URDtimeMills;
			try {
				gc.sleep(waitTime);
			}catch (Exception e) {
			}
			totalTime+=System.nanoTime()-startTime;
			frameCount++;
			if (frameCount==maxframeCount) {
				averageFPS=1000.0/((totalTime/frameCount)/1000000);
				frameCount=0;
				totalTime=0;
			}
			
		}
	}
	void graph() {

		synchronized (gc) {
			gc.clear();
			gc.setColor(Color.WHITE);
			gc.drawString("Score: " +score, 20, 50);
			gc.setColor(Color.WHITE);
			gc.drawString("FPS: "+averageFPS,20,20);
			gc.drawString("Life: "+life, 20, 80);
			gc.setColor(Color.BLACK);
			gc.fillRect(r1.x, r1.y, r1.width, r1.height);
			gc.fillOval(ball1.x, ball1.y, ball1.width, ball1.height);
			for (int i = 0; i < bn; i++) {
				if (b[i].sign) {
					gc.setColor(b[i].color);
					gc.fillRect(b[i].x, b[i].y, b[i].width, b[i].height);
				}
			}
		}
		
				
	}
	
	void move() {
		ball1.x+=speedx;
		ball1.y+=speedy;
		if(ball1.x+10>800) speedx=-speedx;
		if(ball1.x<0) speedx=-speedx;
		if(ball1.y<0) speedy=-speedy;
		if(ball1.y+10>600) {
			life-=1;
			if (life==0) {
				gc.showDialog("You Lose\n"+"Youur name is: "+name+"\n"+"Your Score is: "+score, "Game Over");
				gc.close();
			}
			ball1.y=300;
			ball1.x=400;
			r1.x=ball1.x;
			r1.y=550;
			while(gc.getKeyCode()!=' ') {
				gc.setColor(Color.WHITE);
				gc.drawString("Press Space to Countinue", 300, 400);
				gc.sleep(0);
			}
			
		}
		if (r1.intersects(ball1)) speedy=-speedy;
		for (int i = 0; i < bn; i++) {
			if (ball1.intersects(b[i])&&b[i].sign) {
				gc.clearRect(b[i].x, b[i].y, b[i].width, b[i].height);
				score+=1;
				speedy=-speedy;
				b[i].sign=false;
				
			}
		}
		
		if(gc.getKeyCode()==gc.VK_RIGHT) {
			r1.x+=5;
			if(r1.x>=740) r1.x=740;		
		}
		if (gc.getKeyCode()==gc.VK_LEFT) {
			r1.x-=5;
			if(r1.x<=0) r1.x=0;			
		}	
	}
	void setup() {
		gc.setBackgroundColor(Color.GRAY);
		gc.clear();
		gc.setAntiAlias(true);
		gc.setLocationRelativeTo(null);
		gc.setColor(Color.BLACK);
		gc.fillRect(r1.x, r1.y, r1.width, r1.height);	
		name=gc.showInputDialog("Type your name to start the game", "powered by Yu-Cheng");
		int yy=0;
		for (int i = 0; i < bn; i++) {
			if (i>=0&&i<=9) {
				yy=100;
			}
			if (i>=10&&i<=19) {
				yy=120;
			}
			if (i>=20&&i<=29) {
				yy=140;
			}
			if (i>=30&&i<=39) {
				yy=160;
			}
			if (i>=40&&i<=49) {
				yy=180;
			}
			b[i]=new block((i%10)*80, yy, 80, 20);
			System.out.print(b[i].x);
			System.out.print(" ");
			System.out.println(b[i].y);
		}

	}
	
	class ball extends Rectangle {
		ball(int X,int Y,int W,int H){
			super(X, Y, W, H);
			this.x=X;
			this.y=Y;
			this.width=W;
			this.height=H;	
		}
	}
	class block extends Rectangle{
		boolean sign=true;
		Color color=Color.getHSBColor((float)Math.random(), 1.0f, 1.0f);
		block(int X,int Y,int W,int H){
			super(X, Y, W, H);
			this.x=X;
			this.y=Y;
			this.width=W;
			this.height=H;	
		}
	}
}
