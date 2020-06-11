package az.edutech.broker;

import az.edutech.broker.model.Data;
import az.edutech.broker.model.Request;
import az.edutech.broker.model.User;
import az.edutech.broker.util.Utility;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        String password = scanner.nextLine();
        User user = new User(userName,password);
        Request request = new Request();
        request.setData(user);
        String json = Utility.mapper.writeValueAsString(request);
        outputStream.writeUTF(json);
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
