package PhisicsTest;

import UnityMath.Vector2;

import java.util.Comparator;

public class Force extends Vector2{
    public String name;
    public float scalar;
    Force(Vector2 vector, float scalar){
        super(vector);
        this.scalar = scalar;
        this.name = "force";
    }
    Force(Force force){
        super(force);
        this.scalar = force.scalar;
        this.name = force.name;
    }
    Force(Vector2 vector, float scalar, String name){
        super(vector);
        this.scalar = scalar;
        this.name = name;
    }
    Force(){
        super();
        this.scalar = 0.0f;
        this.name = "force";
    }
    public float sumScl(Force force){
        /*this.x = 0;
        this.y = 1;
        this.scalar = 1;
        force.x = 0;
        force.y = -1;
        force.scalar = 1;*/

        var tmp = new Force(force);
        float angel = 180 - this.angleDeg(tmp);
        return  (float)Math.sqrt(this.scalar*this.scalar + force.scalar*force.scalar - 2*this.scalar*force.scalar*Math.cos(Math.toRadians(angel)));
    }
}
