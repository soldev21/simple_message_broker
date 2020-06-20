package az.edutech.broker;

import az.edutech.broker.model.Data;
import az.edutech.broker.model.Request;
import az.edutech.broker.model.User;
import az.edutech.broker.util.Utility;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import static az.edutech.broker.util.Utility.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        boolean b = false;
        while (!b){
            System.out.print("username: ");
            String userName = scanner.nextLine();
            System.out.print("pass: ");
            String password = scanner.nextLine();
            User user = new User(userName,password);
            Request request = new Request();
            request.setData(user);
            String json = Utility.mapper.writeValueAsString(request);
            outputStream.writeUTF(json);
            String res = inputStream.readUTF();
            System.out.println(res);
            Request response =  mapper.readValue(res,Request.class);
            String auth = (String) response.getMetaData().get("Authorization");
            if (Objects.isNull(auth) || auth.isEmpty()) continue;
            b = auth.equals("Success");
            System.out.println(response.getData());
        }

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println(inputStream.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true){
            Request r = new Request();
            r.setData(scanner.nextLine());
//            r.setData(new Data(scanner.nextLine()));
            outputStream.writeUTF(Utility.mapper.writeValueAsString(r));
        }

    }
}
