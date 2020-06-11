package az.edutech.broker;

import az.edutech.broker.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User();
        user.setUsername("sherif");
        user.setPass("123456");
        String userJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user);
        System.out.println(userJson);
    }
}
