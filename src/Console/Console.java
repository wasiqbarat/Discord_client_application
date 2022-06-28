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
            System.err.println("InvalidInput");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void newMessage(JSONObject jsonObject) throws Exception {
        JSONObject message1 = null;

        if (!jsonObject.isEmpty()) {
            message1 = jsonObject;
        }

        if (message1 != null) {
            System.err.println("......new message...........");
            System.err.println(message1.getString("sender") + ": " + message1.getString("message"));
            System.err.println("............................");

            Thread.sleep(50);
            System.out.println("Do you want to reply: \n1.Yes   2.No");
            System.out.print("> ");

            int input = 0;
            try {
                input = Integer.parseInt(new Scanner(System.in).nextLine());

            } catch (NumberFormatException e) {
                System.out.println("InvalidInput");
                message1.put("process", "action");
                loggedIn(message1);
            }

            if (input == 1) {
                message1.put("process", "chatting");
                message1.put("friendToChat", jsonObject.getString("friendToChat"));
                message1.put("reply", true);
                loggedIn(message1);
            }

        }
    }


    void loggedIn(JSONObject dataFromServer) throws Exception {
        LoggedInWindow loggedInWindow = new LoggedInWindow(dataFromServer);
        loggedInWindow.run();

        responder.sendCommand(dataFromServer);
    }

    //main
    public static void main(String[] args) throws IOException {
        Console console = Console.getInstance();
        console.run();
    }

}
