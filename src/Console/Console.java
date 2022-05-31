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
        System.out.println("---------------------------------------Discord----------------------------------------");
        System.out.println("""
                     1. LOGIN
                     2. SIGNUP
                      ----------
                     """);
        while (true) {
            try {
                System.out.print("> ");
                Scanner scanner = new Scanner(System.in);
                int input = Integer.parseInt(scanner.nextLine());
                userInputHandle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("""
                     1. LOGIN
                     2. SIGNUP
                      ----------
                     """);
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

    private void loggedIn(JSONObject jsonObject) throws Exception {
        LoggedInWindow loggedInWindow = new LoggedInWindow();
        JSONObject userInput = null;

        switch (jsonObject.getString("process")) {
            case "logIn" -> userInput = loggedInWindow.action();
            case "friendRequests" -> userInput = loggedInWindow.friendRequests(jsonObject);
            case "sendFriendRequest" -> userInput = loggedInWindow.sendFriendRequest(jsonObject);
            //case "friendsList" -> userInput = loggedInWindow.friendsList(jsonObject);
            //case "createPrivateChat" -> userInput = loggedInWindow.createPrivateChat(jsonObject);
            //case "blockUser" -> userInput = loggedInWindow.bockUser(jsonObject);
            //case "createServer" -> userInput = loggedInWindow.createServer(jsonObject);
            //case "myServers" -> userInput = loggedInWindow.myServers(jsonObject);
            //case "logOut" -> userInput = loggedInWindow.logOut(jsonObject);
        }

        assert userInput != null;
        client.sendCommand(userInput);
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
                        loggedIn(jsonObject);
                    }
                    case "logIn" -> {
                        System.out.println("Successfully logged in");
                        loggedIn(jsonObject);
                    }
                }

            } catch (Exception e) {
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
