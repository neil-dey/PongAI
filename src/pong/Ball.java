package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {    
    private static final int RADIUS = 10;
    private double x, y;
    private double xVel, yVel;
    
    public Ball() {
        this.x = 350;
        this.y = 250;
        this.xVel = 5 + Math.random()*2;
        if(Math.random() < 0.5) {
            this.xVel *= -1;
        }
        this.yVel = Math.sqrt(49 - Math.pow(this.xVel, 2));
        if(Math.random() < 0.5) {
            this.yVel *= -1;
        }
    }
    
    public void checkPaddleCollision(Paddle p1, Paddle p2) {
        if(x < p1.getX() + Paddle.getWidth() + RADIUS && y >= p1.getY() && y <= p1.getY() + Paddle.getHeight() ) {
            yVel *= Math.random() > 0.5 ? 1 : -1;
            xVel *= -1.003;
        } else if(x > p2.getX() - RADIUS && y >= p2.getY() && y <= p2.getY() + Paddle.getHeight()) {
            yVel *= Math.random() > 0.5 ? 1 : -1;
            xVel *= -1.003;
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int)x - RADIUS, (int)y - RADIUS, 2*RADIUS, 2*RADIUS);
    }
    
    public void move() {
        x += xVel;
        y += yVel;
        
        if(y < RADIUS || y > Pong.HEIGHT - RADIUS) {
            yVel *= -1;
        }
    }
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    
    public double getXVel() {
        return xVel;
    }
    
    public double getYVel() {
        return yVel;
    }
    
    public static int getRadius() {
        return RADIUS;
    }
}
