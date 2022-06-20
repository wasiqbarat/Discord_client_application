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
    static Thread thread;

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


        /*    try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            System.out.println("yeeeeeesssss");

            System.out.println("""
                    1. LOGIN
                    2. SIGNUP
                     ----------
                    """);

        }

    }


    private void userInputHandle(int input) throws Exception {
        JSONObject data = new JSONObject();
        switch (input) {
            case 1 -> {
                Window window = new LoginWindow(data);
                window.run();

                Thread.currentThread().join();
            }
            case 2 -> {
                Window window = new SignUpWindow(data);
                window.run();
                Thread.currentThread().join();
            }

            default -> throw new Exception();
        }

        client.sendCommand(data);
    }




    private void loggedIn(JSONObject jsonObject) throws Exception {
        JSONObject data = new JSONObject();
        LoggedInWindow loggedInWindow = new LoggedInWindow(data);
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
                    loggedIn(jsonObject);
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
            thread = new Thread(console);
            thread.start();

            console.getMessage();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
