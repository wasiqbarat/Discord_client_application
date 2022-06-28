package Console;

import Client.Client;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Responder class manages received data from server
 *
 * @author wasiq
 * @see Client
 */
public class Responder implements Runnable {
    private static Responder responder = null;
    private final Client client;
    private final Socket socket;
    private final Console console;

    private Responder(Console console) throws IOException {
        client = new Client(new Socket("localHost", 6060));
        socket = client.getSocket();
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
        synchronized (data) {
            System.out.println("_________send to server___________________");
            System.out.println(data);
            System.out.println("__________________________________________");
            client.sendCommand(data);
        }

    }


    /**
     * run method is waiting for new data from server
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String dataFromServer = new DataInputStream(socket.getInputStream()).readUTF();
                JSONObject jsonObject = new JSONObject(dataFromServer);

                if (jsonObject.getBoolean("exception")) {
                    if (jsonObject.getString("cause").contains("loggedInException")) {
                        String[] causeSplit = jsonObject.getString("cause").split(" ");
                        String cause = causeSplit[0] + " " + causeSplit[1] + " " + causeSplit[2];
                        System.err.println(cause);

                        jsonObject.put("process", "action");
                        jsonObject.put("method", "loggedIn");
                        jsonObject.put("exception", false);
                        jsonObject.remove("cause");

                        Thread.sleep(50);
                        console.loggedIn(jsonObject);
                    } else {
                        System.err.println(jsonObject.getString("cause"));

                        jsonObject.put("exception", false);
                        jsonObject.remove("cause");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ignored) {
                        }
                        console.run();
                    }
                    continue;

                }

                System.out.println("________________________received from server___________");
                System.out.println(jsonObject);
                System.out.println("________________________________________________________");


                switch (jsonObject.getString("method")) {
                    case "signUp" -> {
                        System.out.println("-------------------------------< Successfully signed up >-----------------------------");
                        console.loggedIn(jsonObject);
                    }

                    case "logIn" -> {
                        System.out.println("-------------------------------< Successfully logged in >-----------------------------");
                        console.loggedIn(jsonObject);
                    }
                    case "loggedIn" -> console.loggedIn(jsonObject);
                    case "newMessage" -> {
                        console.newMessage(jsonObject);
                    }
                    case "logOut" -> {
                        System.out.println("...Logged Out...");
                        console.run();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
