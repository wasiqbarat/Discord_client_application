package Windows;

import Console.Console;
import Console.Responder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LoggedInWindow extends Window {
    Responder responder = Responder.getInstance(Console.getInstance());

    public LoggedInWindow(JSONObject data) throws IOException {
        super(data);
    }

    @Override
    public void run() {
        switch (data.getString("process")) {
            case "loggedIn" -> action();
            case "myFriendRequests" -> friendRequests();
            case "sendFriendRequest" -> sendFriendRequest();
            //case "friendsList" -> userInput = loggedInWindow.friendsList(jsonObject);
            //case "createPrivateChat" -> userInput = loggedInWindow.createPrivateChat(jsonObject);
            //case "blockUser" -> userInput = loggedInWindow.bockUser(jsonObject);
            //case "createServer" -> userInput = loggedInWindow.createServer(jsonObject);
            //case "myServers" -> userInput = loggedInWindow.myServers(jsonObject);


        }
    }


    @Override
    public void action() {

        System.out.println("""
                1. My Friend requests
                2. Send friend request
                3. Friends list
                4. Create private chat
                5. Block a user
                6. Create a server
                7. My servers
                8. Log out
                -------------------------------< select by number >""");
        //include online and offline or status. if a friend got offline it shows
        //if a user blocked you, you can't message to him/her.
        //with manage my servers we can create channels.

        while (true) {
            System.out.print("> ");
            int input = Integer.parseInt(scanner.nextLine());

            switch (input) {
                case 1 -> {
                    data.put("method", "friendRequests");
                    data.put("process", "myFriendRequests");
                }
                case 2 -> {
                    data.put("method", "friendRequests");
                    data.put("process", "sendFriendRequest");
                }
                case 3 -> {
                    data.put("method", "friendsList");
                }
                case 4 -> {
                    data.put("method", "createPrivateChat");
                }
                case 5 -> {
                    data.put("method", "blockUser");
                }
                case 6 -> {
                    data.put("method", "createServer");
                }
                case 7 -> {
                    data.put("method", "myServers");
                }
                case 8 -> {
                    data.put("method", "logOut");
                }
            }

            Thread responderThread = new Thread(responder);
            responderThread.start();

            try {
                responder.sendCommand(data);
                responderThread.join();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

            System.out.println("""
                    1. My Friend requests
                    2. Send friend request
                    3. Friends list
                    4. Create private chat
                    5. Block a user
                    6. Create a server
                    7. My servers
                    8. Log out
                    -------------------------------< select by number >""");
        }

    }



    public JSONObject friendRequests() {
        JSONArray friends = data.getJSONArray("friendRequests");

        Iterator<Object> iterator = friends.iterator();
        ArrayList<String> friendsList = new ArrayList<>();

        while (iterator.hasNext()) {
            String tmp = (String) iterator.next();
            friendsList.add(tmp);
        }

        JSONObject friendRequestsToServer = new JSONObject();
        JSONArray friendsToConfirm = new JSONArray();
        JSONArray friendsToApprove = new JSONArray();
        int listOrder = 1;
        System.out.println("Your friends requests: ");
        for (String string : friendsList) {
            System.out.println(listOrder + " " + string);
            System.out.println(" 1. confirm \n2. approve\n3. nothing");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> {
                        friendsToConfirm.put(string);
                        System.out.println("confirmed");
                    }
                    case 2 -> {
                        friendsToApprove.put(string);
                        System.out.println("approved");
                    }
                    case 3 -> System.out.println();
                    default -> System.out.println("Invalid input");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            listOrder++;
        }

        friendRequestsToServer.put("method", "friendRequests");
        friendRequestsToServer.put("confirmedFriends", friendsToConfirm);
        friendRequestsToServer.put("approvedFriends", friendsToApprove);

        return friendRequestsToServer;
    }


    public JSONObject sendFriendRequest() {
        JSONArray users = data.getJSONArray("users");

        Iterator<Object> iterator = users.iterator();
        ArrayList<String> arrayList = new ArrayList<>();

        while (iterator.hasNext()) {
            String tmp = (String) iterator.next();
            arrayList.add(tmp);
        }

        JSONObject friendsToAdd = new JSONObject();
        JSONArray friendsToAdd2 = new JSONArray();

        int listOrder = 1;
        System.out.println("Users that you can send friend request: ");

        for (String userName : arrayList) {
            System.out.println(listOrder + " " + userName);
            System.out.println(" 1. add \n3. Suspend");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> {
                        friendsToAdd2.put(userName);
                        System.out.println("friend request send.");
                    }
                    case 3 -> System.out.println("Suspended");
                    default -> System.out.println("Invalid input");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            listOrder++;
        }

        friendsToAdd.put("method", "loggedIn");
        friendsToAdd.put("process", "sendFriendRequest");
        friendsToAdd.put("friendsToAdd", friendsToAdd2);

        return friendsToAdd;
    }

    public JSONObject logOut(JSONObject jsonObject) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("process", "logOut");

        return jsonObject1;
    }


    /*public JSONObject friendsList(JSONObject jsonObject) {

    }

    public JSONObject createPrivateChat(JSONObject jsonObject) {
    }

    public JSONObject bockUser(JSONObject jsonObject) {
    }

    public JSONObject createServer(JSONObject jsonObject) {
    }

    public JSONObject myServers(JSONObject jsonObject) {
    }

    public JSONObject logOut(JSONObject jsonObject) {
    }
*/


}
