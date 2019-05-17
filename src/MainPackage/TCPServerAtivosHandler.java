package MainPackage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class TCPServerAtivosHandler extends Thread {

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

        System.out.println("Mensagem Enviada: " + message);
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

    /*public synchronized void messageDispatcherInstantiate(String playerName) throws IOException {
     float x = cliente.client.x;
     float y = cliente.client.y;
     float r = cliente.client.rotation;
     String message
     = "&instantiate|"
     + x + "|"
     + y + "|"
     + r + "|"
     + playerName;
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
     }*/
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

                //Split message received, and print obtained values
                System.out.println("Full Message: " + fullMessage);
                String[] subMessages = fullMessage.split("\\|");
                for (int i = 0; i < subMessages.length; i++) {
                    //System.out.println("Message [" + i + "] :" + subMessages[i]);
                }

                //Check message type
                if (subMessages[0].equals("#connect")) {
                    cliente.client.playerName = subMessages[1];
                    System.out.println("PlayerName definido: " + cliente.client.playerName);

                    messageDispatcherInstantiate();
                }

                if (subMessages[0].equals("#connect2")) {
                    cliente.client.playerName = subMessages[1];
                    System.out.println("PlayerName definido: " + cliente.client.playerName);

                    messageDispatcherInstantiate();
                }

                if (subMessages[0].equals("#input")) {
                    messageInput(subMessages[1]);
                }
            } catch (Exception ex) {
                System.out.println("exception error: " + ex.getMessage());
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
                        cliente.client.rotation -= 1f;
                        messageDispatcher();
                        break;
                    case KeyEvent.VK_X:
                        cliente.client.rotation += 1f;
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
