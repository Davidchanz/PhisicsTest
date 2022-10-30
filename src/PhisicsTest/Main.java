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

        up = new Force(new Vector2(0, 1), 0, "up");
        forward = new Force(new Vector2(1, 0), 0, "forward");
        backward = new Force(new Vector2(-1, 0), 0, "backward");

        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP -> {
                        if(ball.position.y >= 10 && ball.position.y <= 12) {
                            uIsDown = true;
                            up = new Force(new Vector2(0, 1), ball.mass+1, "up");
                            ball.addForce(up);
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        rIsDown = true;
                        forward = new Force(new Vector2(1, 0), ball.mass+1, "forward");
                        ball.addForce(forward);
                    }
                    case KeyEvent.VK_LEFT -> {
                        lIsDown = true;
                        backward = new Force(new Vector2(-1, 0), ball.mass+1, "backward");
                        ball.addForce(backward);
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
        Force go = new Force(new Vector2(0.2f, 1), 110, "go");
        //ball.addForce(go);
        for(;;) {
            end_time = System.currentTimeMillis();
            summ_time += end_time - start_time;
            int delay = 20;
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