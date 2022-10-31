package PhisicsTest;



import Engine2D.ShapeObject;
import UnityMath.Vector2;
import static PhisicsTest.PhisicsConstants.*;

import java.util.ArrayList;

public class PhisicsBody extends ShapeObject {
    public float mass;
    public Force varForce;
    private ArrayList<Force> constForces;
    private final Force Ft;
    private Force Fn;
    private Force Ff;
    private Force Fo;
    public float time;
    public boolean onFlat;
    public boolean onFlat2;
    public float s_velocity;
    public float spriteRadius;
    PhisicsBody(String name, float spriteRadius, float mass){
        super(name, 2);
        this.spriteRadius = spriteRadius;
        this.mass = mass;
        this.time = 0.0f;
        constForces = new ArrayList<>();
        varForce = new Force();
        Ft = new Force(new Vector2(0, -1), mass * g, "Ft");
        Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
        Ff = new Force(new Vector2(-1, 0), mass * g * m,"Ff");
        Fo = new Force(new Vector2(0, 0), (mass * g * m_air), "Fo");
        constForces.add(Ft);
    }
    public Vector2 action(){
        System.out.println(s_velocity);
        Force Rf = new Force();
        ArrayList<Force> allForces = new ArrayList<>();
        onFlat = false;
        if(/*position.y == 10*/onFlat2) {
            allForces.add(Fn);
            onFlat = true;
        }

        if(varForce.scalar >= 1.0f) {
            Fo.set(varForce);
            Fo.mul(-1.0f).nor();
            var tmp = new Force(Fo);
            varForce.sumScl(tmp);
            varForce.add(tmp.nor().mul(tmp.scalar));
            if (onFlat) {
                Ff.set(varForce);
                Ff.mul(-1.0f).nor();
                var temp = new Force(Ff);
                varForce.sumScl(temp);
                varForce.add(temp.nor().mul(temp.scalar));
            }
        }
        if (varForce.scalar <= 5.0) {
            varForce = new Force();
        }

        allForces.add(varForce);
        allForces.addAll(constForces);
        for(var force: allForces.toArray(new Force[0])){
            var temp = new Force(force);
            Rf.sumScl(temp);
            Rf.add(temp.nor().mul(temp.scalar));
        }

        Vector2 v_velocity = new Vector2(Rf);
        float s_acceleration = Rf.scalar / mass;
        s_velocity = s_acceleration * time;

        return v_velocity.nor().mul(s_velocity);
    }
    public void addForce(Force force){
        var tmp = new Force(force);
        varForce.sumScl(tmp);
        varForce.add(tmp.nor().mul(tmp.scalar));
    }
}
