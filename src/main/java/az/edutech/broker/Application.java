package az.edutech.broker;

import az.edutech.broker.model.User;
import az.edutech.broker.ui.ChatApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application {
    public static void main(String[] args) throws JsonProcessingException {
        ChatApp chatApp = new ChatApp("127.0.0.1",8080);
    }
}
