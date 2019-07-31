package pong;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.Perceptron;
import scala.Int;

public class Pong extends Applet implements Runnable, KeyListener {

    private static final long serialVersionUID = 8462509484849384576L;

    public final static int WIDTH = 700, HEIGHT = 500;
    private Thread thread;
    private Paddle p1, p2;
    private Ball ball;
    long timeSinceLastScore;

    public static int p1Score, p2Score;

    private Writer fw;

    private Perceptron neuralNet;

    public void init() {
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);

        p1 = new Paddle(true);
        p2 = new Paddle(false);
        ball = new Ball();

        p1Score = 0;
        p2Score = 0;
        timeSinceLastScore = System.currentTimeMillis();

        neuralNet = new Perceptron(5, 0.1);

        thread = new Thread(this);
        thread.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        p1.draw(g);
        p2.draw(g);
        ball.draw(g);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void run() {
        try {
            fw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\neild\\eclipse-workspace\\PongAI\\src\\pong\\log1.txt"), "utf-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List<double[]> inputs = new ArrayList<>();
        List<Integer> desiredOutputs = new ArrayList<>();
        while (true) {
            try {

                double[] input = new double[] { ball.getX(), ball.getY(), ball.getXVel(), ball.getYVel(), p2.getY() };
                int output = neuralNet.classify(input);

                inputs.add(input);
                desiredOutputs.add(idealMove());

                botMove();
                robotMove(output);

                p1.move();
                p2.move();
                ball.move();

                ball.checkPaddleCollision(p1, p2);

                if (ball.getX() < -1 * Ball.getRadius()) {
                    p2Score += 1;
                    System.out.println(p1Score + "-" + p2Score);
                    fw.write(p1Score + "-" + p2Score + "\n");
                    ball = new Ball();

                    neuralNet.update(inputs, desiredOutputs);
                    System.out.println(neuralNet.iteratedError());
                    fw.write(Arrays.toString(neuralNet.weights()));
                } else if (ball.getX() > WIDTH + Ball.getRadius()) {
                    p1Score += 1;
                    System.out.println(p1Score + "-" + p2Score);
                    fw.write(p1Score + "-" + p2Score + "\n");
                    ball = new Ball();

                    neuralNet.update(inputs, desiredOutputs);
                    System.out.println(inputs.size() + " " + neuralNet.iteratedError());
                    fw.write(Arrays.toString(neuralNet.weights()));
                }

                this.repaint();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            p1.setUpAccel(true);
            break;
        case KeyEvent.VK_S:
            p1.setDownAccel(true);
            break;
        case KeyEvent.VK_UP:
            p2.setUpAccel(true);
            break;
        case KeyEvent.VK_DOWN:
            p2.setDownAccel(true);
            break;
        case KeyEvent.VK_SPACE:
            try {
                fw.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            p1.setUpAccel(false);
            break;
        case KeyEvent.VK_S:
            p1.setDownAccel(false);
            break;
        case KeyEvent.VK_UP:
            p2.setUpAccel(false);
            break;
        case KeyEvent.VK_DOWN:
            p2.setDownAccel(false);
            break;
        }
    }

    private void robotMove(double[] output) {
        if (output[0] > 0.5) {
            p2.setUpAccel(true);
        } else {
            p2.setUpAccel(false);
        }

        if (output[1] > 0.5) {
            p2.setDownAccel(true);
        } else {
            p2.setDownAccel(false);
        }
    }

    private void robotMove(int output) {
        if (output == 1) {
            p2.setUpAccel(true);
            p2.setDownAccel(false);
        } else {
            p2.setUpAccel(false);
            p2.setDownAccel(true);

        }

    }

    private int[] idealMove_Hard() {
        if (ball.getXVel() < 0) {
            if (p2.getY() + Paddle.getHeight() / 2.0 < Pong.HEIGHT / 2) {
                return new int[] { 0, 1 };
            } else {
                return new int[] { 1, 0 };
            }
        }

        double finalY = ball.getYVel() / ball.getXVel() * (p2.getX() - ball.getX()) + ball.getY();
        while (finalY < 0) {
            finalY += 2 * Pong.HEIGHT;
        }
        finalY %= 2 * Pong.HEIGHT;
        if (finalY > Pong.HEIGHT) {
            finalY = 2 * Pong.HEIGHT - finalY;
        }

        if (p2.getY() + Paddle.getHeight() / 2.0 > finalY) {
            return new int[] { 1, 0 };
        } else {
            return new int[] { 0, 1 };
        }
    }

    private int idealMove() {
        if (p2.getY() + Paddle.getHeight() / 2.0 > ball.getY()) {
            return 1;
        } else {
            return 0;
        }
    }

    private void botMove() {
        if (ball.getXVel() > 0) {
            if (p1.getY() + Paddle.getHeight() / 2.0 < Pong.HEIGHT / 2) {
                p1.setUpAccel(false);
                p1.setDownAccel(true);
            } else {
                p1.setUpAccel(true);
                p1.setDownAccel(false);
            }
            return;
        }

        double finalY = ball.getYVel() / ball.getXVel() * (p1.getX() + Paddle.getWidth() - ball.getX()) + ball.getY();
        while (finalY < 0) {
            finalY += 2 * Pong.HEIGHT;
        }
        finalY %= 2 * Pong.HEIGHT;
        if (finalY > Pong.HEIGHT) {
            finalY = 2 * Pong.HEIGHT - finalY;
        }

        if (p1.getY() + Paddle.getHeight() / 2.0 > finalY) {
            p1.setUpAccel(true);
            p1.setDownAccel(false);
        } else {
            p1.setUpAccel(false);
            p1.setDownAccel(true);

        }
    }
}
