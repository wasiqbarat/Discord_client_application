package Console;

import Client.Client;
import Windows.LoggedInWindow;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;


public class Responder implements Runnable {
    private static Responder responder = null;
    private final Client client;
    private final Console console;

    private Responder(Console console) throws IOException {
        client = new Client(new Socket("localHost", 6060));
        this.console = console;
    }

    //singleton class structure
    public static Responder getInstance(Console console) throws IOException {
        if (responder == null) {
            responder = new Responder(console);
        }
        return responder;
    }

    public void sendCommand(JSONObject data) throws IOException {
        client.sendCommand(data);
    }


    private void loggedIn(JSONObject dataFromServer) throws Exception {
        JSONObject data = new JSONObject();
        Thread loggedInWindow = new Thread(new LoggedInWindow(dataFromServer));
        loggedInWindow.start();

        loggedInWindow.join();

        client.sendCommand(dataFromServer);

    }

    @Override
    public void run() {
        while (client.isConnected()) {
            try {
                JSONObject jsonObject = client.nonFileDataReceiver();

                if (jsonObject.getBoolean("exception")) {
                    System.out.println(jsonObject.getString("cause"));
                    console.loggedIn(jsonObject);
                }

                switch (jsonObject.getString("method")) {
                    case "signUp" -> {
                        System.out.println("Successfully signed up.");
                        console.loggedIn(jsonObject);
                    }

                    case "logIn" -> {
                        System.out.println("Successfully logged in");
                        console.loggedIn(jsonObject);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
