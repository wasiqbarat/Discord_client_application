package Console;

import java.io.IOException;
import java.util.Scanner;

import Exceptions.InvalidInput;
import Windows.LoggedInWindow;
import Windows.LoginWindow;
import Windows.SignUpWindow;
import org.json.JSONObject;


public class Console implements Runnable {
    private static Console console = null;
    private final Responder responder;
    private final Thread thread;

    private Console() throws IOException {
        responder = Responder.getInstance(this);
        thread = new Thread(responder);
        thread.start();
    }

    public static Console getInstance() throws IOException {
        if (console == null) {
            console = new Console();
        }
        return console;
    }


    @Override
    public void run() {

        System.out.println("---------------------------------------Discord----------------------------------------");
        System.out.print("""
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
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("Please enter correct Number.");
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.print("""
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
                Thread loginWindow = new Thread(new LoginWindow(data));
                loginWindow.start();
                loginWindow.join();
            }
            case 2 -> {
                Thread signUpWindow = new Thread(new SignUpWindow(data));
                signUpWindow.start();
                signUpWindow.join();
            }

            default -> throw new InvalidInput();
        }

        responder.sendCommand(data);

        thread.join();
    }

    void loggedIn(JSONObject dataFromServer) throws Exception {
        JSONObject data = new JSONObject();
        Thread loggedInWindow = new Thread(new LoggedInWindow(dataFromServer));
        loggedInWindow.start();

        loggedInWindow.join();
        responder.sendCommand(data);

        thread.join();
    }

/*

    public void getMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();

    }

*/

    public static void main(String[] args) throws IOException {
        //Client client = new Client(new Socket("localHost", 6060));
        //Responder responder = new Responder(client);
        //Console console = new Console(client, responder);

        //Thread thread = new Thread(console);
        //console.getMessage();
        //thread.start();

        Console console = Console.getInstance();

        Thread thread = new Thread(console);
        thread.start();
    }

}
