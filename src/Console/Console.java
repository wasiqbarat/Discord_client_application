package Console;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import Exceptions.InvalidInput;
import Windows.LoggedInWindow;
import Windows.LoginWindow;
import Windows.SignUpWindow;
import org.json.JSONObject;

/**
 * Console class manages user interface and windows that open
 *
 * @author wasiq
 * @see Windows.Window
 * @see SignUpWindow
 * @see LoginWindow
 * @see LoggedInWindow
 */
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

        System.err.println("......................................Discord.........................................");
        System.out.print("""
                1. LOGIN
                2. SIGNUP
                 ...........
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

    void loggedIn(JSONObject dataFromServer) throws Exception {
        LoggedInWindow loggedInWindow = new LoggedInWindow(dataFromServer);
        loggedInWindow.run();
        if (dataFromServer.getString("method").equals("sendFile") ) {
            sendFile(dataFromServer);
        }
        responder.sendCommand(dataFromServer);
    }

    private void sendFile(JSONObject dataFromServer) {
        try {
            Socket socket = responder.getSocket();
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.print("Enter file directory: ");
            String directory = new Scanner(System.in).nextLine();

            outputStream.writeUTF(dataFromServer.toString());

            File file = new File(directory);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[6000];

            outputStream.writeLong(file.length());
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Main method of program
     */
    public static void main(String[] args) throws IOException {
        Console console = Console.getInstance();
        console.run();
    }

}
