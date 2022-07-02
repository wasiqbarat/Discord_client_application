package Console;

import Client.Client;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


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
    private ArrayList<String> privateMessages;
    private ArrayList<String> channelMessages;

    private Responder(Console console) throws IOException {
        client = new Client(new Socket("localHost", 6060));
        socket = client.getSocket();
        this.console = console;
        privateMessages = new ArrayList<>();
        channelMessages = new ArrayList<>();
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
                    } else if (jsonObject.getString("cause").contains("allowed")) {

                        jsonObject.put("method", "loggedIn");
                        jsonObject.put("process", "serverPanel");
                        System.out.println(jsonObject.getString("cause"));
                        jsonObject.put("exception", false);
                        jsonObject.remove("cause");
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
                System.out.println("_______________________________________________________");


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
                        newMessage(jsonObject);
                    }
                    case "channelMessage" -> {
                        newChannelMessage(jsonObject);
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

    private void newPrivateMessage(JSONObject jsonObject) {
        privateMessages.add(jsonObject.getString("sender") + ": " + jsonObject.getString("message"));
    }
    public void getOlderPrivateMessages() {
        if (privateMessages.isEmpty()) {
            return;
        }
        System.out.println("................Older messages.................");
        for (String message : privateMessages) {
            System.out.println(message);
        }
        privateMessages.clear();
        System.out.println("...............................................");
    }

    private void newChannelMessage(JSONObject jsonObject) {
        channelMessages.add(jsonObject.getString("sender") + ": " + jsonObject.getString("message"));
    }

    public void getNewChannelMessages() throws InterruptedException {

        if (channelMessages.isEmpty()) {
            return;
        }
        System.err.println("..........New message from channels...........");
        Thread.sleep(50);
        for (String message : channelMessages) {
            System.out.println(message);
        }

        channelMessages.clear();
        System.out.println("..........Go to channel for chatting..........");
        System.out.println("..............................................");
    }

    public void newMessage(JSONObject jsonObject) throws Exception {
        JSONObject message1 = null;

        if (!jsonObject.isEmpty()) {
            message1 = jsonObject;
        }

        if (message1 != null) {
            System.err.println("......New private message...........");
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
                console.loggedIn(message1);
            }

            if (input == 1) {
                message1.put("process", "chatting");
                message1.put("friendToChat", jsonObject.getString("friendToChat"));
                message1.put("reply", true);
                console.loggedIn(message1);
            }

        }


    }
}
