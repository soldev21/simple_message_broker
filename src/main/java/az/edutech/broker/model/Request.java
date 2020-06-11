package az.edutech.broker.model;

import lombok.Data;

import java.util.Map;

@Data
public class Request {
    private Map<String,Object> metaData;
    private Object data;
}
