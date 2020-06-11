package az.edutech.broker.client;

import az.edutech.broker.model.Request;
import az.edutech.broker.model.User;
import az.edutech.broker.registry.UserRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static az.edutech.broker.util.Utility.mapper;

public class ClientHandler implements Runnable {

    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    User user;
    private boolean authorized;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.authorized = false;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                handleMessage(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(String s) {
        try {
            Request request = mapper.readValue(s, Request.class);
            if (authorized) {
//                Data data = mapper.convertValue(request.getData(),Data.class);
                System.out.println(request.getData());
            } else {
                checkLoginAttempt(request);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void checkLoginAttempt(Request request) {
        try {
            User user = mapper.convertValue(request.getData(), User.class);
            if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPass() == null || user.getPass().isEmpty())
                out.writeUTF("Username or password is incorrect.");
            authorized = UserRegistry.validate(user.getUsername(), user.getPass());
            if (authorized) out.writeUTF("Successful login!"); else
                out.writeUTF("Username or password is incorrect.");
        } catch (Exception e) {
            try {
                out.writeUTF("Username or password is incorrect.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
