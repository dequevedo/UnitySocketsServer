package MainPackage;

import java.awt.event.KeyEvent;
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

    @Override
    protected void finalize() throws Throwable {
        encerrar();
    }

    private void encerrar() {
        this.caller.removerCliente(this.cliente);
    }

    public synchronized void messageDispatcher() throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append(
                    "#move|" + cli.client.toString() + "|" + "&");
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

    public synchronized void messageDispatcherShot() throws IOException {
        String message
                = "#shoot" + "|"
                + "idPlaceholder" + "|"
                + cliente.client.playerName + "|"
                + cliente.client.x + "|"
                + cliente.client.y + "|"
                + cliente.client.rotation + "|"
                + "&";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                messageChar = message.toCharArray();
                cli.getOutput().println(messageChar);
                cli.getOutput().flush();
            }
        }
    }

    public synchronized void messageDispatcherInstantiate() throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append("#instantiate|"
                    + "id" + "|"
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
    }

    public synchronized void messageDispatcherBulletPosition(Bullet bullet) throws IOException {
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        //#bullet|0|0,0001099587|2,703996|&
        for (TCPServerConnection cli : clientes) {
            sb.append("#bulletmove|"
                    + cliente.client.id + "|"
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
                //TERMINAR!!!

                /*if (!bulletList.isEmpty()) {
                 StringBuilder sb = new StringBuilder();
                 for (Bullet bullet : bulletList) {
                 sb.append(bullet.toString());
                       
                 }
                 //System.out.println("Bullets: " + sb);
                 }*/
                if (this.cliente.getSocket().isConnected() && this.cliente.getInput() != null) {
                    fullMessage = this.cliente.getInput().readLine();
                } else {
                    break;
                }
                if (fullMessage == null || fullMessage.equals("")) {
                    break;
                }

                //Split message received, and print obtained values
                //System.out.println("Full Message: " + fullMessage);
                String[] subMessages = fullMessage.split("\\|");
                //System.out.println("Sub[0]: " + subMessages[0]);
                for (int i = 0; i < subMessages.length; i++) {
                    //System.out.println("Message [" + i + "] :" + subMessages[i]);
                }

                //Check message type
                if (subMessages[0].equals("#connect")) {
                    cliente.client.playerName = subMessages[1];
                    if (connectDebugIn) {
                        System.out.println("Connect Message: " + fullMessage);
                    }
                    //System.out.println("PlayerName definido: " + cliente.client.playerName);

                    messageDispatcherInstantiate();
                }

                if (subMessages[0].equals("#connect2")) {
                    cliente.client.playerName = subMessages[1];
                    if (connectDebugIn) {
                        System.out.println("Connect2 Message: " + fullMessage);
                    }
                    //System.out.println("PlayerName definido: " + cliente.client.playerName);

                    messageDispatcherInstantiate();
                }

                if (subMessages[0].equals("#input")) {
                    if (inputDebugIn) {
                        System.out.println("Input Message: " + fullMessage);
                    }
                    messageInput(subMessages[1]);
                }

                if (subMessages[0].equals("#bullet")) {
                    if (bulletDebugIn) {

                        System.out.println("Bullet Message: " + fullMessage);
                        System.out.println("From client Id: " + cliente.client.id);

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
                    //messageInput(subMessages[1]);
                }
            } catch (Exception ex) {
                System.out.println("exception error (1): " + ex.getMessage());
                break;
            }
        }
        encerrar();
    }

    public void messageInput(String message) {
        try {
            if (message.matches("[0-9]+")) {
                int tecla = Integer.parseInt(message);
                //System.out.println("entrou no switch case");
                switch (tecla) {
                    case KeyEvent.VK_A:
                        if (cliente.client.inc > 0) {
                            cliente.client.inc--;
                        }
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_S:
                        if (cliente.client.inc < 1) {
                            cliente.client.inc++;
                        }
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_Q:
                        if (cliente.client.z > 2) {
                            cliente.client.z--;
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (cliente.client.z < 20) {
                            cliente.client.z++;
                        }
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_RIGHT:
                        cliente.client.x += cliente.client.inc;

                        messageDispatcher();
                        break;
                    case KeyEvent.VK_LEFT:
                        cliente.client.x -= cliente.client.inc;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_DOWN:
                        cliente.client.y -= cliente.client.inc;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_UP:
                        cliente.client.y += cliente.client.inc;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_Z:
                        cliente.client.rotation -= 1.5f;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_X:
                        cliente.client.rotation += 1.5f;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_C: //NUM 67
                        messageDispatcherShot();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {

        }
    }
}
