package az.edutech.broker;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
//        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            outputStream.writeUTF(scanner.nextLine());
        }
    }
}
