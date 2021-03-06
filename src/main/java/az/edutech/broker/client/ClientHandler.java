package az.edutech.broker.client;

import az.edutech.broker.model.Request;
import az.edutech.broker.model.User;
import az.edutech.broker.registry.ClientHandlerRegistry;
import az.edutech.broker.registry.UserRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static az.edutech.broker.util.Utility.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    User user;
    private boolean authorized;
    private ClientHandlerRegistry registry;

    public ClientHandler(Socket socket,ClientHandlerRegistry registry) throws IOException {
        this.registry = registry;
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
                registry.getRegistry().remove(user.getUsername());
                break;
            }
        }
    }

    private void handleMessage(String s) {
        System.out.println(s);
        try {
            Request request = mapper.readValue(s, Request.class);
            if (authorized) {
//                Data data = mapper.convertValue(request.getData(),Data.class);
                System.out.println(String.format("%s: %s",this.user.getUsername(),request.getData()));
                registry.getRegistry().entrySet().stream().filter(e->!e.getKey().equals(user.getUsername())).forEach(
                        e->e.getValue().sendMessage(String.format("%s: %s",this.user.getUsername(),request.getData()))
                );
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
                out.writeUTF(mapper.writeValueAsString(buildUnSuccessfulLoginResponse()));
            authorized = UserRegistry.validate(user.getUsername(), user.getPass());
            if (authorized) {
                this.user = new User();
                this.user.setUsername(user.getUsername());
                registry.add(user.getUsername(),this);
                out.writeUTF(mapper.writeValueAsString(buildSuccessfulLoginResponse()));
            }
            else
                out.writeUTF(mapper.writeValueAsString(buildUnSuccessfulLoginResponse()));
        } catch (Exception e) {
            try {
                out.writeUTF(mapper.writeValueAsString(buildUnSuccessfulLoginResponse()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
