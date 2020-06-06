package az.edutech.broker;

import az.edutech.broker.server.ChatServer;

import java.io.IOException;

public class ChatServerPlayground {
    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(8080);
    }
}
