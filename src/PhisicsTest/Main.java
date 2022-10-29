package PhisicsTest;

import Engine2D.*;
import Engine2D.Rectangle;
import UnityMath.Vector2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main extends JFrame{
    public static Scene scene;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    PhisicsBody ball;
    PhisicsBody floor;
    Force up;
    Force forward;
    Force backward;
    boolean uIsDown = false;
    boolean rIsDown = false;
    boolean lIsDown = false;
    Main(){
        scene = new Scene(WIDTH, HEIGHT);
        scene.setCoordVisible(true);
        scene.setCenterVisible(true);
        this.setSize(new Dimension(WIDTH+100, HEIGHT+100));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(scene);

        ball = new PhisicsBody("Ball", 10);
        ball.add(new Circle(10, new Vector2(0,100), Color.RED));
        scene.add(ball);
        //Scene.camera.position = ball.position;

        floor = new PhisicsBody("floor", 10);
        floor.add(new Rectangle(0,300, new Vector2(0,0), Color.DARK_GRAY));
        scene.add(floor);

        up = new Force(new Vector2(0, 1), ball.mass * ball.g, "up");
        forward = new Force(new Vector2(1, 0), ball.mass * ball.g, "forward");
        backward = new Force(new Vector2(-1, 0), ball.mass * ball.g, "backward");

        this.addKeyListener(new KeyAdapter() {

            public synchronized void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP -> {
                        uIsDown = true;
                        if(!ball.forceExist(up)) {
                            up = new Force(new Vector2(0, 1), ball.mass * ball.g, "up");
                            ball.addForce(up);
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        rIsDown = true;
                            forward = new Force(new Vector2(1, 0), ball.mass * ball.g, "forward");
                            ball.addForce(forward);
                    }
                    case KeyEvent.VK_LEFT -> {
                        lIsDown = true;
                        backward = new Force(new Vector2(-1, 0), ball.mass * ball.g, "backward");
                        ball.addForce(backward);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP -> {
                        uIsDown = false;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        rIsDown = false;
                    }
                    case KeyEvent.VK_LEFT -> {
                        lIsDown = false;
                    }
                }
            }
        });

        this.setVisible(true);
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Main main = new Main();
        main.start();
    }
    public void start(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        long start_time = 0;
        long end_time;
        long summ_time = 0;
        scene.repaint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Force go = new Force(new Vector2(0.5f, 3), 120, "go");
        ball.addForce(go);
        for(;;) {
            end_time = System.currentTimeMillis();
            summ_time += end_time - start_time;
            int delay = 10;
            if (summ_time >= delay) {
                tk.sync();
                ball.tick();
                scene.repaint();
                summ_time = 0;
                //System.out.println(go);
            }
            start_time = end_time;
        }
    }
}