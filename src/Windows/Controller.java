package Windows;

import Client.Client;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    Window window;
    Client client;

    public void login() {
    }


    public void show() {

    }


    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 6060);


            //client.dataListener();
            //client.sendCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
