package az.edutech.broker.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Request {

    public Request(){
        metaData = new HashMap<>();
    }

    private Map<String,Object> metaData;
    private Object data;
}
