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

    private Console() throws IOException {
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

        Responder responder = Responder.getInstance(this);

        Thread responderThread = new Thread(responder);
        responderThread.start();
        responder.sendCommand(data);

        responderThread.join();
    }

    void loggedIn(JSONObject dataFromServer) throws Exception {
        JSONObject data = new JSONObject();
        Thread loggedInWindow = new Thread(new LoggedInWindow(dataFromServer));
        loggedInWindow.start();

        loggedInWindow.join();

        Responder responder = Responder.getInstance(this);
        Thread responderThread = new Thread(responder);
        responderThread.start();
        responder.sendCommand(data);

        responderThread.join();
    }


    public static void main(String[] args) throws IOException {
        Console console = Console.getInstance();

        Thread consoleThread = new Thread(console);
        consoleThread.start();
    }

}
