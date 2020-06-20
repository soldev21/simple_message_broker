package az.edutech.broker.registry;

import az.edutech.broker.client.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class ClientHandlerRegistry {
    private Map<String, ClientHandler> registry;

    public ClientHandlerRegistry(){
        registry = new HashMap<>();
    }

    public void add(String userName,ClientHandler clientHandler){
        registry.put(userName,clientHandler);
    }

    public ClientHandler get(String userName){
        return registry.get(userName);
    }

    public Map<String,ClientHandler> getRegistry(){
        return registry;
    }
}
