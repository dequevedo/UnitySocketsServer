package MainPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerAtivosMain extends Thread {

    private List<TCPServerConnection> clientes;
    private ServerSocket server;

    public TCPServerAtivosMain(int porta) throws IOException {
        this.server = new ServerSocket(porta);
        this.clientes = new ArrayList<TCPServerConnection>();
    }

    @Override
    public void run() {
        Socket socket;
        String playerName;
        while (true) {
            try {
                socket = this.server.accept();
                TCPServerConnection cliente = new TCPServerConnection(socket, clientes.size());
                novoCliente(cliente);
                (new TCPServerAtivosHandler(cliente, this)).start();
            } catch (IOException ex) {
                System.out.println("Erro 4: " + ex.getMessage());
            }
        }
    }

    public synchronized void novoCliente(TCPServerConnection cliente) throws IOException {
        clientes.add(cliente);
    }

    public synchronized void removerCliente(TCPServerConnection cliente) {
        clientes.remove(cliente);
        try {
            cliente.getInput().close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Cliente removido:" + cliente.client.id);
        cliente.getOutput().close();
        try {
            cliente.getSocket().close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public synchronized void removerCliente(String clientName) {
        TCPServerConnection cliente = null;

        for (TCPServerConnection x : clientes) {
            if (x.client.playerName.equals(clientName)) {
                cliente = x;
            }
        }

        if (cliente != null) {
            clientes.remove(cliente);

            try {
                cliente.getInput().close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Cliente removido:" + cliente.client.id);
            cliente.getOutput().close();
            try {
                 cliente.getSocket().close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public List getClientes() {
        return clientes;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.server.close();
    }
}
