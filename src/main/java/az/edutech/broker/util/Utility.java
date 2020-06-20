package az.edutech.broker.util;

import az.edutech.broker.model.Request;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static Request buildSuccessfulLoginResponse(){
        Request request = new Request();
        request.getMetaData().put("Authorization","Success");
        request.setData("Successful login.");
        return request;
    }

    public static Request buildUnSuccessfulLoginResponse(){
        Request request = new Request();
        request.getMetaData().put("Authorization","Failed");
        request.setData("Login failed.");
        return request;
    }
}