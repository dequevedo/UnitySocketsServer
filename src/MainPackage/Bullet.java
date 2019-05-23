package MainPackage;

import java.util.List;

public class Bullet {

    public int bulletId;
    public String playerId;
    public float x, y;

    public Bullet(String playerId, int bulletId, float x, float y) {
        this.playerId = playerId;
        this.bulletId = bulletId;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Bullet{" + playerId + "," + bulletId + "," + x + "," + y + '}';
    }

    public boolean alreadyExists(List<Bullet> bulletList) {
        for (Bullet x : bulletList) {
            if (x.bulletId == this.bulletId) {
                return true;
            }
        }

        return false;
    }
}
