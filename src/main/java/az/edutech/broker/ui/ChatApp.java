package az.edutech.broker.ui;

import az.edutech.broker.model.Request;
import az.edutech.broker.model.User;
import az.edutech.broker.util.Utility;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import static az.edutech.broker.util.Utility.mapper;

public class ChatApp {
    private JFrame f;
    private JTextField usernameField;
    private JTextField passField;
    private JLabel statusLabel;
    private JButton connectButton;
    private JList chatBox;
    private JTextArea typeBox;
    private DefaultListModel<String> defaultListModel;
    private JButton sendButton;
    private JButton exitButton;
    private String ip;
    private int port;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public ChatApp(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initUi();
    }

    private void initUi() {
        // ************************ BUILD UI ************************
        f = new JFrame();//creating instance of JFrame

        usernameField = new JFormattedTextField("username");
        usernameField.setBounds(30, 30, 90, 30);
        f.add(usernameField);

        passField = new JFormattedTextField("password");
        passField.setBounds(130, 30, 80, 30);
        f.add(passField);

        connectButton = new JButton("connect");//creating instance of JButton
        connectButton.setBounds(230, 30, 120, 30);//x axis, y axis, width, height
        f.add(connectButton);//adding button in JFrame

        statusLabel = new JLabel("status: not connected.");
        statusLabel.setBounds(30, 60, 250, 20);
        statusLabel.setForeground(Color.RED);
        f.add(statusLabel);


        defaultListModel = new DefaultListModel<>();
        chatBox = new JList(defaultListModel);
        chatBox.setBounds(30, 80, 320, 200);
        chatBox.setBorder(new LineBorder(Color.RED));
        f.add(chatBox);


        typeBox = new JTextArea();
        typeBox.setBorder(new LineBorder(Color.BLUE));
        typeBox.setBounds(30, 300, 320, 80);
        f.add(typeBox);

        exitButton = new JButton("exit");
        exitButton.setBounds(30, 400, 70, 30);
        f.add(exitButton);

        sendButton = new JButton("send");//creating instance of JButton
        sendButton.setBounds(280, 400, 70, 30);//x axis, y axis, width, height
        f.add(sendButton);//adding button in JFrame


        f.setSize(400, 500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //************************ END OF BUILDING UI ************************

        connectButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                socket = new Socket(ip, port);
                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());
                String username = usernameField.getText();
                String password = passField.getText();
                boolean b = login(username, password);
                if (b) {
                    setStatusToSuccess();
                    startChat();
                } else {
                    setStatusToFail();
                }
            }
        });


        sendButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String s = typeBox.getText();
                Request r = new Request();
                defaultListModel.addElement(String.format("\t\t\t%s", s));
                r.setData(s);
//            r.setData(new Data(scanner.nextLine()));
                outputStream.writeUTF(Utility.mapper.writeValueAsString(r));
                typeBox.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (socket != null) {
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                }
                setStatusToNotConnected();
                f.dispose();
            }
        });
    }

    private boolean login(String userName, String password) throws IOException {
        User user = new User(userName, password);
        Request request = new Request();
        request.setData(user);
        String json = Utility.mapper.writeValueAsString(request);
        outputStream.writeUTF(json);
        String res = inputStream.readUTF();
//        System.out.println(res);
        Request response = mapper.readValue(res, Request.class);
        String auth = (String) response.getMetaData().get("Authorization");
        if (Objects.isNull(auth) || auth.isEmpty()) return false;
        return auth.equals("Success");
    }

    private void startChat() {
        new Thread(() -> {
            while (true) {
                try {
                    defaultListModel.addElement(inputStream.readUTF());
//                    System.out.println(inputStream.readUTF());

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    private void setStatusToFail() {
        statusLabel.setText("status: auth failed.");
        statusLabel.setForeground(Color.RED);
    }

    private void setStatusToNotConnected() {
        statusLabel.setText("status: not connected.");
        statusLabel.setForeground(Color.RED);
    }

    private void setStatusToSuccess() {
        statusLabel.setText("status: connected.");
        statusLabel.setForeground(Color.GREEN);
    }
}
