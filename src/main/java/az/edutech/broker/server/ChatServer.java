package az.edutech.broker.server;

import az.edutech.broker.Client;
import az.edutech.broker.client.ClientHandler;
import az.edutech.broker.registry.ClientHandlerRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private ServerSocket serverSocket;
    private int port;
    private boolean isRunning;
    private ClientHandlerRegistry registry;

    public ChatServer(int port) throws IOException {
        this.port = port;
        registry = new ClientHandlerRegistry();
        init();
    }

    private void init() throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
        Runnable runnable = () -> {
            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New Connection accepted.");
                    Thread t = new Thread(new ClientHandler(socket,registry));
                    t.setDaemon(true);
                    t.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void stop() throws IOException {
        isRunning = false;
        serverSocket.close();
    }
}
