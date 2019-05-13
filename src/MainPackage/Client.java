package MainPackage;

import com.google.gson.Gson;

public class Client {

    public int id;
    public String playerName;
    public float x;
    public float y;
    public float z;
    public float inc;
    public float rotation;
    Gson gson;

    public Client(int id, String playerName, float x, float y, float z, float inc, float rotation) {
        System.out.println("New Client: " + id + "|" + playerName + "|" + x + "|" + y + "|" + z + "|" + inc);
        this.id = id;
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.inc = inc;
        this.rotation = rotation;
        Gson gson = new Gson();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append("|");
        sb.append(this.playerName);
        sb.append("|");
        sb.append(this.x);
        sb.append("|");
        sb.append(this.y);
        sb.append("|");
        sb.append(this.inc);
        sb.append("|");
        sb.append(this.rotation);
        return String.valueOf(sb);
    }

}
