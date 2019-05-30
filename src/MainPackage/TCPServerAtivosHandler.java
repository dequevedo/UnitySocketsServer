package MainPackage;

import java.io.IOException;
import java.util.List;

public class TCPServerAtivosHandler extends Thread {

    boolean bulletDebugIn = true;
    boolean inputDebugIn = false;
    boolean connectDebugIn = false;

    boolean sentMessageDebug = false;

    private TCPServerConnection cliente;
    private TCPServerAtivosMain caller;
    private List<TCPServerConnection> clientes;

    public TCPServerAtivosHandler(TCPServerConnection cliente, TCPServerAtivosMain caller) throws IOException {
        this.caller = caller;
        this.cliente = cliente;
        this.clientes = caller.getClientes();
    }

    public synchronized void messageDispatcherClientDisconnected(String playerRemoved) throws IOException {
        String message
                = "#removePlayer_S" + "|"
                + playerRemoved + "|"
                + "&";
        char[] messageChar = message.toCharArray();

        List<TCPServerConnection> clientes = this.caller.getClientes();

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                messageChar = message.toCharArray();
                cli.getOutput().println(messageChar);
                cli.getOutput().flush();
            }
        }
    }

    public synchronized void messageDispatcher() throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append(
                    "#move_S" + "|"
                    + cli.client.id + "|"
                    + cli.client.playerName + "|"
                    + cli.client.x + "|"
                    + cli.client.y + "|"
                    + cli.client.inc + "|"
                    + cli.client.rotation + "|"
                    + cli.client.alive + "|"
                    + "&");
            sb.append(";");
            message = String.valueOf(sb);
        }

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                //messageChar = message.toCharArray();

                char[] msg2 = new char[message.toCharArray().length - 1];

                for (int i = 0; i < msg2.length; i++) {
                    msg2[i] = message.toCharArray()[i];
                }

                cli.getOutput().println(msg2);
                cli.getOutput().flush();
            }
        }

        if (sentMessageDebug) {
            System.out.println("Mensagem Enviada: " + message);
        }
    }

    public synchronized void messageDispatcherInstantiate() throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append("#instantiate_S|"
                    + cliente.client.id + "|"
                    + cliente.client.playerName + "|"
                    + cliente.client.x + "|"
                    + cliente.client.y + "|"
                    + cliente.client.rotation + "|"
                    + "&");
            sb.append(";");
            message = String.valueOf(sb);
        }

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {

                char[] msg2 = new char[message.toCharArray().length - 1];

                for (int i = 0; i < msg2.length; i++) {
                    msg2[i] = message.toCharArray()[i];
                }

                cli.getOutput().println(msg2);
                cli.getOutput().flush();
            }
        }
    }

    public synchronized void messageDispatcherShot() throws IOException {
        String message
                = "#shoot_S" + "|"
                + cliente.client.id + "|"
                + cliente.client.playerName + "|"
                + "delete" + "|"
                + cliente.client.x + "|"
                + cliente.client.y + "|"
                + cliente.client.rotation + "|"
                + "&";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                messageChar = message.toCharArray();
                cli.getOutput().println(messageChar);
                cli.getOutput().flush();
            }
        }
    }

    public synchronized void messageDispatcherBulletPosition(Bullet bullet) throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append("#bulletmove_S|"
                    + cliente.client.id + "|"
                    + cliente.client.playerName + "|"
                    + bullet.bulletId + "|"
                    + bullet.x + "|"
                    + bullet.y + "|"
                    + "&");
            sb.append(";");
            message = String.valueOf(sb);
        }

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                //messageChar = message.toCharArray();

                char[] msg2 = new char[message.toCharArray().length - 1];

                for (int i = 0; i < msg2.length; i++) {
                    msg2[i] = message.toCharArray()[i];
                    //char n = msg2[i];
                }

                //messageChar[messageChar.length - 1] = ' ';
                cli.getOutput().println(msg2);
                cli.getOutput().flush();
            }
        }

        if (sentMessageDebug) {
            System.out.println("Mensagem Enviada: " + message);
        }
    }

    @Override
    public void run() {

        String fullMessage = "";
        while (true) {
            try {
                if (this.cliente.getSocket().isConnected() && this.cliente.getInput() != null) {
                    fullMessage = this.cliente.getInput().readLine();
                } else {
                    break;
                }
                if (fullMessage == null || fullMessage.equals("")) {
                    break;
                }

                String[] subMessages = fullMessage.split("\\|");

                if (subMessages[0].equals("#connect_C")) {
                    cliente.client.playerName = subMessages[1];
                    messageDispatcherInstantiate();
                }

                if (subMessages[0].equals("#playerStatus_C")) {
                    updatePlayerStatus(subMessages);
                }

                if (subMessages[0].equals("#shotFired_C")) {
                    messageDispatcherShot();
                }

                if (subMessages[0].equals("#disconnect_C")) {
                    System.out.println(fullMessage);
                    
                    for(TCPServerConnection x : clientes){
                        if(x.client.playerName.equals(subMessages[1])){
                            x.client.alive = "false";
                            messageDispatcherClientDisconnected(subMessages[1]);
                            break;
                        }
                    }
                }

                if (subMessages[0].equals("#bulletStatus_C")) {
                    //Não existe a necessidade de criar um objeto para comparar, basta comparar a string id
                    Bullet newBullet = new Bullet(
                            subMessages[1], //PlayerId
                            Integer.parseInt(subMessages[2]), //BulletId
                            Float.parseFloat(subMessages[3].replace(',', '.')), //X position
                            Float.parseFloat(subMessages[4].replace(',', '.')) //Y position
                    );

                    if (!newBullet.alreadyExists(cliente.client.bulletList)) {
                        cliente.client.bulletList.add(newBullet);
                    }

                    messageDispatcherBulletPosition(newBullet);
                }
            } catch (Exception ex) {
                System.out.println("exception error (1): " + ex.getMessage());
                break;
            }
        }
    }

    public void updatePlayerStatus(String[] subMessages) {
        try {
            cliente.client.x = Float.parseFloat(subMessages[2].replace(',', '.'));
            cliente.client.y = Float.parseFloat(subMessages[3].replace(',', '.'));
            cliente.client.rotation = Float.parseFloat(subMessages[4].replace(',', '.'));

            messageDispatcher();
        } catch (Exception e) {
        }
    }
}
