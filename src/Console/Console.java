package Console;

import java.io.IOException;
import java.util.Scanner;
import Exceptions.InvalidInput;
import Windows.LoggedInWindow;
import Windows.LoginWindow;
import Windows.SignUpWindow;
import org.json.JSONObject;


public class Console {
    private static Console console = null;
    private final Responder responder;


    private Console() throws IOException {
        responder = Responder.getInstance(this);
        Thread responderThread = new Thread(responder);
        responderThread.start();
    }

    public static Console getInstance() throws IOException {
        if (console == null) {
            console = new Console();
        }
        return console;
    }

    public void run() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }

        System.out.println("---------------------------------------Discord----------------------------------------");
        System.out.print("""
                1. LOGIN
                2. SIGNUP
                 ----------
                """);

        try {
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            int input = Integer.parseInt(scanner.nextLine());
            userInputHandle(input);

        } catch (InvalidInput e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            this.run();
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

        try {
            responder.sendCommand(data);
            //responderThread.join();
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
        Console console = Console.getInstance();

        console.run();
    }

}
