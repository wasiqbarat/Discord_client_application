package Console;

import java.io.IOException;
import java.util.Scanner;
import Exceptions.InvalidInput;
import Windows.LoggedInWindow;
import Windows.LoginWindow;
import Windows.SignUpWindow;
import org.json.JSONObject;


public class Console implements Runnable {
    private final Responder responder;

    public Console() throws IOException {
        responder = Responder.getInstance(this);
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
                e.printStackTrace();
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
                LoginWindow loginWindow = new LoginWindow(data);
                loginWindow.action();
            }
            case 2 -> {
                SignUpWindow signUpWindow = new SignUpWindow(data);
                signUpWindow.action();
            }

            default -> throw new InvalidInput();
        }

        //send command to server using responder
        Thread responderThread = new Thread(responder);
        responderThread.start();

        try {
            responder.sendCommand(data);

            responderThread.join();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    void loggedIn(JSONObject dataFromServer) throws Exception {
        LoggedInWindow loggedInWindow = new LoggedInWindow(dataFromServer);
        loggedInWindow.run();

        //when there is no need to send command to sever and want to go to main menu
        if (dataFromServer.getString("method").equals("loggedIn")) {
            loggedInWindow.action();
        }

        responder.sendCommand(dataFromServer);
    }

    //main
    public static void main(String[] args) throws IOException {
        //Console console = Console.getInstance();
        Console console = new Console();
        Thread consoleThread = new Thread(console);
        consoleThread.start();
    }

}
