package az.edutech.broker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(8080);
        while (true) {
            Socket clientSocket = socket.accept();
            String name = UUID.randomUUID().toString();
            Runnable runnable = () -> {
                try {
                    System.out.println("Connection accepted: "+name);
                    DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                    String s;
                    while ((s = reader.readUTF()) != null) {
                        System.out.println(String.format("incoming message (%s): %s",name,s));
                    }
                }catch (Exception e){

                }
            };
            new Thread(runnable).start();
        }
    }
}
