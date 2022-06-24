package Console;

import Client.Client;
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



    @Override
    public void run() {

        while (client.isConnected()) {
            try {
                JSONObject jsonObject = client.nonFileDataReceiver();

                if (jsonObject.getBoolean("exception")) {
                    System.err.println(jsonObject.getString("cause"));
                    Thread.sleep(50);
                    console.run();
                    continue;
                }

                switch (jsonObject.getString("method")) {
                    case "signUp" -> {
                        System.out.println("-------------------------------< Successfully signed up >-----------------------------");
                        console.loggedIn(jsonObject);
                    }

                    case "logIn" -> {
                        System.out.println("-------------------------------< Successfully logged in >-----------------------------");
                        console.loggedIn(jsonObject);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!client.isConnected()) {
                System.err.println("Connection Lost!");
            }

        }

    }
}
