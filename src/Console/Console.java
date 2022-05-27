package Console;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import Client.Client;
import Exceptions.InvalidInput;
import Windows.LoggedInWindow;
import Windows.LoginWindow;
import Windows.SignUpWindow;
import Windows.Window;
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
                System.out.println("> ");
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

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(" 1. LOGIN\n 2. SIGNUP" + "\n----------");
        }
    }

    private void userInputHandle(int input) throws Exception {
        switch (input) {
            case 1 -> login();
            case 2 -> signUp();
            default -> throw new Exception();
        }
    }

    private void login() throws Exception {
        Window window = new LoginWindow();
        JSONObject identity = window.action();
        client.sendCommand(identity);
    }


    private void signUp() throws Exception {
        JSONObject jsonObject = new SignUpWindow().action();
        client.sendCommand(jsonObject);
    }


    public void getMessage() {
        while (client.isConnected()) {
            try {
                JSONObject jsonObject = client.nonFileDataReceiver();
                if (jsonObject.getBoolean("exception")) {
                    System.out.println(jsonObject.getString("cause"));
                    continue;
                }
                switch (jsonObject.getString("method")) {
                    case "signUp" -> {
                        System.out.println("Successfully signed up.");
                        LoggedInWindow loggedInWindow = new LoggedInWindow();
                        JSONObject jsonObject1 = loggedInWindow.action();
                        client.sendCommand(jsonObject1);
                    }
                    case "logIn" -> {
                        System.out.println("Successfully logged in");

                    }
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
