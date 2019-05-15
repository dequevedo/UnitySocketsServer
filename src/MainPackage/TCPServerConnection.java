package MainPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerConnection {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    public Client client;

    public TCPServerConnection(Socket socket, int id) {
        this.socket = socket;
        //System.out.println("Client: " + socket.getInetAddress());
        this.client = new Client(id, "Desconhecido", 0, 0, 0, (float) 0.1, 0);
        try {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.input.close();
        this.output.close();
        this.socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

}
