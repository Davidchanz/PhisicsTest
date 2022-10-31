package PhisicsTest;

import Engine2D.*;
import Engine2D.Rectangle;
import UnityMath.Vector2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Queue;
import static PhisicsTest.PhisicsConstants.g;

public class Main extends JFrame{
    private static Scene scene;
    private static int WIDTH = 800;
    private static int HEIGHT = 600;
    private PhisicsBody ball;
    private PhisicsBody floor;
    private Force up;
    private Force forward;
    private Force backward;
    private boolean uIsDown = false;
    private boolean rIsDown = false;
    private boolean lIsDown = false;
    private Queue<Force> commands = new ArrayDeque<>();
    private Observer observer;
    Main(){
        scene = new Scene(WIDTH, HEIGHT);
        scene.setCoordVisible(true);
        scene.setCenterVisible(true);
        this.setSize(new Dimension(WIDTH+100, HEIGHT+100));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(scene);

        ball = new PhisicsBody("Ball", 10, 10);
        ball.add(new Circle(10, new Vector2(-300,100), Color.RED));
        scene.add(ball);
        //Scene.camera.position = ball.position;

        floor = new PhisicsBody("floor", 0, 10);
        floor.add(new Line(new Vector2(-150,0), new Vector2(50, 0), new Vector2(0,0) , Color.DARK_GRAY));
        floor.add(new Line(new Vector2(-50,0), new Vector2(50, 0), new Vector2(100,20) , Color.DARK_GRAY));
        floor.add(new Line(new Vector2(-50,0), new Vector2(50, 0), new Vector2(200,40) , Color.DARK_GRAY));
        floor.add(new Line(new Vector2(-50,0), new Vector2(50, 0), new Vector2(300,20) , Color.DARK_GRAY));
        scene.add(floor);

        up = new Force(new Vector2(0, 1), 0, "up");
        forward = new Force(new Vector2(1, 0), 0, "forward");
        backward = new Force(new Vector2(-1, 0), 0, "backward");

        observer = new Observer(ball, floor);
        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP -> {
                        if (ball.onFlat2) {
                            uIsDown = true;
                            if(ball.s_velocity <= 1.0f) {
                                up = new Force(new Vector2(0, 1), (ball.mass * g) + ball.mass*3, "up");
                                if (!commands.contains(up)/* && ball.s_velocity == 0.0f*/) {
                                    commands.add(up);
                                }
                            }else {
                                up = new Force(new Vector2(0, 1), (ball.mass*g)+ball.mass*3, "up");
                                if (!commands.contains(up)/* && ball.s_velocity == 0.0f*/) {
                                    commands.add(up);
                                    ball.varForce.x *= 0.1;
                                    ball.varForce.y *= 0.02;
                                    ball.varForce.scalar *= 0.02;
                                }
                            }
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        rIsDown = true;
                        if (ball.onFlat2) {
                            forward = new Force(new Vector2(1, 0), ball.mass*2, "forward");
                            if (!commands.contains(forward) && ball.s_velocity <= 2.0f) {
                                commands.add(forward);
                            }
                        }else{
                            forward = new Force(new Vector2(1, 0), 1, "forward");
                            if (!commands.contains(forward) && ball.s_velocity <= 4.0f) {
                                commands.add(forward);
                            }
                        }
                    }
                    case KeyEvent.VK_LEFT -> {
                        lIsDown = true;
                        if (ball.onFlat2) {
                            backward = new Force(new Vector2(-1, 0), ball.mass*2, "backward");
                            if (!commands.contains(backward) && ball.s_velocity <= 2.0f) {
                                commands.add(backward);
                            }
                        }else{
                            backward = new Force(new Vector2(-1, 0), 1, "backward");
                            if (!commands.contains(backward) && ball.s_velocity <= 4.0f) {
                                commands.add(backward);
                            }
                        }
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
        ball.addForce(go);
        for(;;) {
            end_time = System.currentTimeMillis();
            summ_time += end_time - start_time;
            int delay = 20;
            if (summ_time >= delay) {
                tk.sync();

                if(!commands.isEmpty()) {
                    System.out.println(commands.size());
                    ball.addForce(commands.peek());
                    commands.remove();
                }
                observer.observe();

                scene.repaint();
                summ_time = 0;
                //System.out.println(go);
            }
            start_time = end_time;
        }
    }
}