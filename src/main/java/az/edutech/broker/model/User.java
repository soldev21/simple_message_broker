package az.edutech.broker.model;

import lombok.Data;

@Data
public class User {

    public User(){}
    public User(String username,String pass){
        setUsername(username);
        setPass(pass);
    }
    String username;
    String pass;
}
