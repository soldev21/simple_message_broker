package az.edutech.broker.server;

import az.edutech.broker.Client;
import az.edutech.broker.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private ServerSocket serverSocket;
    private int port;
    private boolean isRunning;

    public ChatServer(int port) throws IOException {
        this.port = port;
        init();
    }

    private void init() throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        while (isRunning){
            Socket socket = serverSocket.accept();
            System.out.println("New Connection accepted.");
            Thread t = new Thread(new ClientHandler(socket));
            t.setDaemon(true);
            t.start();
        }
    }

    public void stop(){
        isRunning = false;
    }
}
