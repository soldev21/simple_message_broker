package az.edutech.broker.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRegistry {

    private static final Map<String,String> registry;

    static {
        registry = new HashMap<>();
        registry.put("sherif","123");
        registry.put("nazrin","26");
        registry.put("fatma","28");
    }

    public static boolean validate(String username,String password){
        String pass = registry.get(username);
        return (Objects.isNull(pass)) ? false : pass.equals(password);
    }
}
