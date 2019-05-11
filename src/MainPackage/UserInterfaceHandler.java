package MainPackage;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class UserInterfaceHandler implements Runnable {

    public int x = 50;
    public int y = 50;
    public int d = 10;
    public int inc = 3;

    Graphics g;
    JPanel jpanel;

    public UserInterfaceHandler(Graphics g, JPanel jpanel) {
        this.g = g;
        this.jpanel = jpanel;
    }
    
    private static UserInterfaceHandler uniqueInstance;
 
    private UserInterfaceHandler() {
    }
 
    public static synchronized UserInterfaceHandler getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new UserInterfaceHandler();
 
        return uniqueInstance;
    }

    public void run() {
        try {
            while (true) {
                apagaBolinha();
                x += 1;
                y += 1;
                d = 10;
                inc = 2;
                pintaBolinha();

                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pintaBolinha() {
        g.setColor(Color.green);
        g.fillOval(x, y, d, d);
    }

    public void apagaBolinha() {
        g.setColor(jpanel.getBackground());
        g.fillOval(x, y, d, d);
    }
}
