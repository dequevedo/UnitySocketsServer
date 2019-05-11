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
        //System.out.println("entrou no msgDispatcher");
        String message = "";
        char[] messageChar = message.toCharArray();
        List<TCPServerConnection> clientes = this.caller.getClientes();

        StringBuilder sb = new StringBuilder();

        for (TCPServerConnection cli : clientes) {
            sb.append(cli.client.toString());
            sb.append(";");
            message = String.valueOf(sb);
        }

        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getOutput() != null) {
                messageChar = message.toCharArray();
                messageChar[messageChar.length - 1] = ' ';
                cli.getOutput().println(messageChar);
                cli.getOutput().flush();
            }
        }
    }

    @Override
    public void run() {

        String message = "";
        while (true) {
            try {
                if (this.cliente.getSocket().isConnected() && this.cliente.getInput() != null) {
                    message = this.cliente.getInput().readLine();
                } else {
                    break;
                }
                if (message == null || message.equals("")) {
                    break;
                }
                //System.out.println("message: " + message);
                if (message.matches("[0-9]+")) {
                    int tecla = Integer.parseInt(message);
                    //System.out.println("entrou no switch case");
                    switch (tecla) {
                        case KeyEvent.VK_A:
                            if (cliente.client.inc > 0) {
                                cliente.client.inc--;
                            }
                            break;
                        case KeyEvent.VK_S:
                            if (cliente.client.inc < 1) {
                                cliente.client.inc++;
                            }
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
                            break;
                        case KeyEvent.VK_RIGHT:
                            cliente.client.x += cliente.client.inc;
                            break;
                        case KeyEvent.VK_LEFT:
                            cliente.client.x -= cliente.client.inc;
                            break;
                        case KeyEvent.VK_DOWN:
                            cliente.client.y -= cliente.client.inc;
                            break;
                        case KeyEvent.VK_UP:
                            cliente.client.y += cliente.client.inc;
                            break;
                        case KeyEvent.VK_Z:
                            cliente.client.rotation -= 1f;
                            System.out.println("Apertou Z, valor da Rotação: " + cliente.client.rotation);
                            break;
                        case KeyEvent.VK_X:
                            cliente.client.rotation += 1f;
                            System.out.println("Apertou X, valor da Rotação: " + cliente.client.rotation);
                            break;
                        default:
                            break;
                    }
                } else {
                    cliente.client.playerName = message;
                    System.out.println("playerName: " + cliente.client.playerName);
                }

                messageDispatcher();
            } catch (Exception ex) {
                System.out.println("exception error: " + ex.getMessage());
                break;
            }
        }
        encerrar();
    }
}
