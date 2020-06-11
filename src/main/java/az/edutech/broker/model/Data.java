package az.edutech.broker.model;

@lombok.Data
public class Data {

    public Data(){}
    public Data(String message){
        setMessage(message);
    }
    private String message;
}
