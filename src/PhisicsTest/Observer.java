package PhisicsTest;

import Engine2D.Line;
import UnityMath.Vector2;

public class Observer {
    PhisicsBody body;
    PhisicsBody borders;
    Observer(PhisicsBody body, PhisicsBody borders){
        this.body = body;
        this.borders = borders;
    }
    public void observe(){
        Vector2 dir = body.action();
        body.onFlat2 = false;
        for (var border: borders.body){
            if(body.position.y + dir.y <= body.spriteRadius + border.position.y
                    && body.position.x <= ((Line)border).end.x + border.position.x
                    && body.position.x >= ((Line)border).start.x + border.position.x
                    && body.position.y >= border.position.y){
                body.time = 0.0f;
            }
            if(body.position.y + dir.y <= body.spriteRadius + border.position.y
                    && body.position.x <= ((Line)border).end.x + border.position.x
                    && body.position.x >= ((Line)border).start.x + border.position.x
                    && !body.onFlat
                    && body.position.y >= border.position.y){
                dir = new Vector2(0, body.spriteRadius + border.position.y).sub(0, body.position.y);

                body.onFlat2 = true;
                body.varForce = new Force();
                break;
            }
        }
        body.move(dir);
        body.time += 0.1;
    }
}
