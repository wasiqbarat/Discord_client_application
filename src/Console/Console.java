package Console;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import Client.Client;
import Exceptions.InvalidInput;
import Windows.LoginWindow;
import org.json.JSONObject;

public class Console implements Runnable {
    private final Client client;

    public Console(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("--------------Discord---------------");
        System.out.println(" 1. LOGIN\n 2. SIGNUP");
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                int input = Integer.parseInt(scanner.nextLine());
                userInputHandle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(" 1. LOGIN\n 2. SIGNUP");
        }
    }

    private void userInputHandle(int input) throws Exception {
        switch (input) {
            case 1:
                login();
            case 2:
                signUp();
            default:
                throw new Exception();
        }
    }

    private void login() throws Exception {
        JSONObject identity = new LoginWindow().action();
        client.sendCommand(identity);
        getMessage();
    }

    private void signUp() throws Exception {

    }


    public void sendMessage(JSONObject object) {

    }


    public void getMessage() {
        while (client.isConnected()) {
            try {
                JSONObject jsonObject = client.nonFileDataReceiver();
                switch (jsonObject.getString("method")) {
                    case "login":


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client(new Socket("localHost", 6060));
            Console console = new Console(client);
            new Thread(console).start();
            console.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
