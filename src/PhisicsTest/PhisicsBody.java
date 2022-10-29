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
    public final float m = 0.015f;
    public final float m_air = 0.001f;
    public final float k = 0.05f;
    public final float l = 260f;
    public float mass;
    private float s_velocity = 0.0f;
    private Vector2 v_velocity = new Vector2(0,0);
    private float s_acceleration = 0.0f;
    private Vector2 v_acceleration = new Vector2(0,0);
    private float s_impulse = 0.0f;
    private Vector2 v_impulse = new Vector2(0,0);
    private ArrayList<Force> varForces = new ArrayList<>();
    private ArrayList<Force> constForces = new ArrayList<>();
    public final Force Ft;
    public Force Fn;
    public Force Ff;
    public Force Fo;
    public Force Fu;
    private Force Rf;
    private Vector2 oldDir = new Vector2(0,0);
    public float time;
    PhisicsBody(String name, float mass){
        super(name, 2);
        this.mass = mass;
        this.time = 1f;
        Rf = new Force();
        Ft = new Force(new Vector2(0, -1), mass * g, "Ft");
        Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
        Ff = new Force(new Vector2(-1, 0), mass * g * m,"Ff");
        Fo = new Force(new Vector2(0, 0), (mass * g * m_air), "Fo");
        Fu = new Force(new Vector2(0, 1), (k * l), "Fu");
        constForces.add(Ft);
    }
    public void tick(){
        Rf = new Force();
        ArrayList<Force> allForces = new ArrayList<>();
        boolean onFlat = false;
        if(position.y == 10) {
            allForces.add(Fn);
            onFlat = true;
        }
        for(var force: varForces.toArray(new Force[0])){
            force.scalar -= Fo.scalar;
            if(onFlat)
                force.scalar -= Ff.scalar;
            if(force.scalar <= 0.0)
                varForces.remove(force);
        }
        allForces.addAll(varForces);
        allForces.addAll(constForces);
        for(var force: allForces.toArray(new Force[0])){
            var tmp = new Force(force);
            Rf.scalar = Rf.sumScl(tmp);
            Rf.add(tmp.nor().mul(tmp.scalar));
        }
        System.out.println(Rf);
        System.out.println(Rf.scalar);


        v_acceleration = new Vector2(Rf);
        v_velocity = new Vector2(Rf);
        s_acceleration = Rf.scalar/mass;
        s_velocity = s_acceleration * time * time / 2;

        Vector2 dir = v_velocity.nor().mul(s_velocity);

        if(Rf.x > 0 && oldDir.x < 0 || Rf.y > 0 && oldDir.y < 0 || Rf.x < 0 && oldDir.x > 0 || Rf.y < 0 && oldDir.y > 0 || position.y == 0.0f) {
            //time = 0;
            //System.out.println("null");
        }
        if(position.y + dir.y < 10){
            for(var force: varForces.toArray(new Force[0])) {
                /*force.mul(0.5f);
                force.scalar *= 0.5f;*/
            }
            //time = 0;
            Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
            //varForces.add(Fn);
            dir = new Vector2(0, 10).sub(0, position.y);
        }
        this.move(dir);
        time += 0.001;
    }
    public void addForce(Force force){
        time = 1f;
        this.varForces.add(force);
    }
    public void remForce(Force force){
        this.varForces.remove(force);
    }
    public boolean forceExist(Force force){
        for(var f: varForces.toArray(new Force[0])){
            if(f.name.equals(force.name))
                return true;
        }
        return false;
    }
}
