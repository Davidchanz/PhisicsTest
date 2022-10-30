package PhisicsTest;



import Engine2D.ShapeObject;
import UnityMath.Vector2;
import org.w3c.dom.html.HTMLQuoteElement;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.TreeSet;

public class PhisicsBody extends ShapeObject {
    public final float G = 6.67f * (float)Math.pow(10.0, -11.0);
    public final float R = 6371000f;
    public final float M = 5.972E24f;
    public final float g = 10.0f;
    public final float m = 0.005f;
    public final float m_air = 0.0008f;
    public final float k = 0.105f;
    public final float l = 260f;
    public float mass;
    private float s_velocity = 0.0f;
    private Vector2 v_velocity = new Vector2(0,0);
    private float s_acceleration = 0.0f;
    private Vector2 v_acceleration = new Vector2(0,0);
    private Force varForce = new Force();
    private ArrayList<Force> constForces = new ArrayList<>();
    private final Force Ft;
    private Force Fn;
    private Force Ff;
    private Force Fo;
    private Force Fu;
    private Vector2 oldDir = new Vector2(0,0);
    private float time;
    private boolean afterFn = false;
    public boolean onFlat;
    PhisicsBody(String name, float mass){
        super(name, 2);
        this.mass = mass;
        this.time = 0.1f;
        Ft = new Force(new Vector2(0, -1), mass * g, "Ft");
        Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
        Ff = new Force(new Vector2(-1, 0), mass * g * m,"Ff");
        Fo = new Force(new Vector2(0, 0), (mass * g * m_air), "Fo");
        Fu = new Force(new Vector2(0, 1), (k * l), "Fu");
        constForces.add(Ft);
    }
    public void tick(){
        Force Rf = new Force();
        ArrayList<Force> allForces = new ArrayList<>();
        onFlat = false;
        if(position.y == 10) {
            allForces.add(Fn);
            onFlat = true;
            addForce(Fu);
        }

        Fo.x = varForce.x;
        Fo.y = varForce.y;
        Fo.mul(-1.0f).nor();
        var tmp = new Force(Fo);
        varForce.scalar = varForce.sumScl(tmp);
        varForce.add(tmp.nor().mul(tmp.scalar));
        //varForce.scalar -= Fo.scalar;
        if(onFlat) {
            Ff.x = varForce.x;
            Ff.y = varForce.y;
            Ff.mul(-1.0f).nor();
            var temp = new Force(Ff);
            varForce.scalar = varForce.sumScl(temp);
            varForce.add(temp.nor().mul(temp.scalar));
            //varForce.scalar -= Ff.scalar;
        }
        if(varForce.scalar <= 0.0) {
            varForce = new Force();
            System.out.println("rem");
        }

        /*if(constForces.contains(Fu)){
            Fu.scalar -= Fo.scalar;
            if(Fu.scalar <= 0.0) {
                varForces.remove(Fu);
                //System.out.println("rem");
            }
        }*/

        allForces.add(varForce);
        allForces.addAll(constForces);
        for(var force: allForces.toArray(new Force[0])){
            var temp = new Force(force);
            Rf.scalar = Rf.sumScl(temp);
            Rf.add(temp.nor().mul(temp.scalar));
        }
        if(Rf.scalar <= 0.1f) {
            //time = 0.8f;
            //return;
        }
        //System.out.println(Rf.scalar);

        v_acceleration = new Vector2(Rf);
        v_velocity = new Vector2(Rf);
        s_acceleration = Rf.scalar/mass;
        s_velocity = s_acceleration * time;

        v_velocity.nor();
        Vector2 dir = v_velocity.mul(s_velocity);
        //Vector2 dir = new Vector2(Rf.mul(0.2f));

        if(Rf.x > 0 && oldDir.x < 0 || Rf.y > 0 && oldDir.y < 0 || Rf.x < 0 && oldDir.x > 0 || Rf.y < 0 && oldDir.y > 0) {
            //time = 0.4f;
            //System.out.println("null");
        }
        if(position.y + dir.y < 10){
            //varForce.scalar *= -1;

            time = 0.8f;

            Fu = new Force(new Vector2(0, 1), (k * Rf.scalar * g), "Fu");

            Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
            //addForce(Fn);

            dir = new Vector2(0, 10).sub(0, position.y);
        }
        this.move(dir);
        time += 0.01;
        //System.out.println(Fu.scalar);
    }
    public void addForce(Force force){
        var tmp = new Force(force);
        varForce.scalar = varForce.sumScl(tmp);
        varForce.add(tmp.nor().mul(tmp.scalar));

    }
}
