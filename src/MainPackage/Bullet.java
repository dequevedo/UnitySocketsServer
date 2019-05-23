package MainPackage;

import java.util.List;

public class Bullet {
    public int bulletId;
    public float x, y;
    
    public Bullet(int bulletId, float x, float y){
        this.bulletId = bulletId;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Bullet{" + bulletId + "," + x + "," + y + '}';
    }
    
    public boolean alreadyExists(List<Bullet> bulletList){
        for(Bullet x : bulletList){
            if(x.bulletId == this.bulletId) return true;
        }
        
        return false;
    }
}
