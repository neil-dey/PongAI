package pong;
import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
    private static final int DELTA_V = 2;
    private static final double FRICTION = 0.95;
    private static final int WIDTH = 20, HEIGHT = 80;
    
    private double x, y;
    private double yVel;
    private boolean upAccel, downAccel;
    private boolean isFirstPlayer;

    public Paddle(boolean isFirstPlayer) {
        this.upAccel = false;
        this.downAccel = false;
        this.y = 210;
        this.yVel = 0;
        
        this.isFirstPlayer = isFirstPlayer;
        
        if(isFirstPlayer) {
            x = 20;
        } else {
            x = 660;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int)x, (int)y, WIDTH, HEIGHT);
    }
    
    public void setUpAccel(boolean upAccel) {
        this.upAccel = upAccel;
    }
    
    public void setDownAccel(boolean downAccel) {
        this.downAccel =  downAccel;
    }
    
    public void move() {
        if(this.upAccel) {
            this.yVel -= DELTA_V;
        } else if(this.downAccel) {
            this.yVel += DELTA_V;
        } else {
            this.yVel *= FRICTION;
        }
        
        yVel = clamp(yVel, -2*DELTA_V, 2*DELTA_V);
        y = clamp(y, 0, Pong.HEIGHT - Paddle.HEIGHT);
        
        y += yVel;
        
    }
    
    public int getX() {
        return (int)x;
    }
    
    public int getY() {
        return (int)y;
    }
    
    public static int getWidth() {
        return WIDTH;
    }
    
    public static int getHeight() { 
        return HEIGHT;
    }
    
    private double clamp(double val, double lower, double upper) {
        if(val < lower) return lower;
        if(val > upper) return upper;
        
        return val;
    }
}
