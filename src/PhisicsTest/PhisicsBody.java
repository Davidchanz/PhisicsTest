package PhisicsTest;



import Engine2D.ShapeObject;
import UnityMath.Vector2;
import static PhisicsTest.PhisicsConstants.*;

import java.util.ArrayList;

public class PhisicsBody extends ShapeObject {
    private final float k = 0.5f;
    public float mass;
    private Force varForce;
    private ArrayList<Force> constForces;
    private final Force Ft;
    private Force Fn;
    private Force Ff;
    private Force Fo;
    private Force Fu;
    private Vector2 oldDir = new Vector2(0,0);//TODO
    private float time;
    public boolean onFlat;
    public float Whm = 0.0f;
    PhisicsBody(String name, float mass){
        super(name, 2);
        this.mass = mass;
        this.time = 1f;
        constForces = new ArrayList<>();
        varForce = new Force();
        Ft = new Force(new Vector2(0, -1), mass * g, "Ft");
        Fn = new Force(new Vector2(0, 1), mass * g, "Fn");
        Ff = new Force(new Vector2(-1, 0), mass * g * m,"Ff");
        Fo = new Force(new Vector2(0, 0), (mass * g * m_air), "Fo");
        Fu = new Force(new Vector2(0, 1), (k), "Fu");
        constForces.add(Ft);
    }
    public void tick(){
        //System.out.println(mass * g * position.y);
        //Whm = Math.max(Whm, mass * g * position.y);

        Force Rf = new Force();
        ArrayList<Force> allForces = new ArrayList<>();
        onFlat = false;
        if(position.y == 10) {
            allForces.add(Fn);
            onFlat = true;
            addForce(Fu);
        }

        Fo.set(varForce);
        Fo.mul(-1.0f).nor();
        var tmp = new Force(Fo);
        varForce.sumScl(tmp);
        varForce.add(tmp.nor().mul(tmp.scalar));
        if(onFlat) {
            Ff.set(varForce);
            //Ff.y = 0;
            Ff.mul(-1.0f).nor();
            var temp = new Force(Ff);
            varForce.sumScl(temp);
            varForce.add(temp.nor().mul(temp.scalar));
        }
        if(varForce.scalar <= 0.0) {
            varForce = new Force();
            //System.out.println("rem");
        }

        allForces.add(varForce);
        allForces.addAll(constForces);
        for(var force: allForces.toArray(new Force[0])){
            var temp = new Force(force);
            Rf.sumScl(temp);
            Rf.add(temp.nor().mul(temp.scalar));
        }

        Vector2 v_acceleration = new Vector2(Rf);//TODO
        Vector2 v_velocity = new Vector2(Rf);
        float s_acceleration = Rf.scalar / mass;
        float s_velocity = s_acceleration * time;

        v_velocity.nor();
        Vector2 dir = v_velocity.mul(s_velocity);

        if(Rf.x > 0 && oldDir.x < 0 || Rf.y > 0 && oldDir.y < 0 || Rf.x < 0 && oldDir.x > 0 || Rf.y < 0 && oldDir.y > 0) {
            //System.out.println("null");
        }
        if(position.y + dir.y < 10){
           /* if(Rf.y < 0.0f){
                float angel = Rf.angleDeg(new Vector2(1,0));
                Rf.rotateDeg(360 - angel);
            }else {

            }*/

            /*System.out.println(mass * s_velocity * s_velocity / 2);

            float dv = (float)Math.sqrt(Whm / mass * 2);
            float dF = dv - s_velocity;
            Rf.scalar -= dF;*/
            //varForce.scalar -= time;

            Fu = new Force(new Vector2(0, 1), (k * Rf.scalar + 12), "Fu");

            Fn = new Force(new Vector2(0, 1), mass * g, "Fn");

            dir = new Vector2(0, 10).sub(0, position.y);

            time = 1f;
        }
        this.move(dir);
        time += 0.1;
        //System.out.println(Fu.scalar);
    }
    public void addForce(Force force){
        var tmp = new Force(force);
        varForce.sumScl(tmp);
        varForce.add(tmp.nor().mul(tmp.scalar));
    }
}
