package Windows;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class LoggedInWindow extends Window {

    public LoggedInWindow(JSONObject data) throws IOException {
        super(data);
    }

    @Override
    public void run() {
        switch (data.getString("process")) {
            case "loggedIn" -> action();
            case "myFriendRequests" -> friendRequests();
            case "sendFriendRequest" -> sendFriendRequest();
            case "friendsList" -> friendsList();
            case "blockUser" -> blockUser();
            //
            case "privateChat" -> privateChat();
            case "selectFriend" -> selectFriendToChat();

            //case "createServer" -> userInput = loggedInWindow.createServer(jsonObject);
            //case "myServers" -> userInput = loggedInWindow.myServers(jsonObject);
        }

    }

    private void selectFriendToChat() {

    }

    private void privateChat() {

    }


    @Override
    public void action() {

        System.out.println("""
                1. My Friend requests
                2. Send friend request
                3. Friends list
                4. Create private chat
                5. Block or Unblock friends
                6. Create a server
                7. My servers
                8. Log out
                -------------------------------< select by number >""");
        //include online and offline or status. if a friend got offline it shows
        //if a user blocked you, you can't message to him/her.
        //with manage my servers we can create channels.

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
            case 3 -> data.put("method", "friendsList");

            case 4 -> data.put("method", "privateChat");

            case 5 -> {
                data.put("method", "blockUser");
                data.put("process", "blocking");
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
    }


    private void blockUser() {
        JSONArray friendsList = data.getJSONArray("friendsList");

        System.out.println("----<Select user to block>-------------------------");

        JSONArray applyBlocking = new JSONArray();

        for (Object object : friendsList) {
            String userName = object.toString().split(" ")[0];
            String blockStatus = object.toString().split(" ")[1];

            if (blockStatus.equals("true")) {
                System.out.println("-" + userName + "  " + "(User is blocked)");
                System.out.println("Do you want to unblock");
                System.out.println("  1.Yes    2.NO");
                System.out.print("> ");

                int input = 0;

                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input.");
                }
                switch (input) {
                    case 1 -> {
                        applyBlocking.put(userName + " " + false);
                        System.out.println("Unblocked");
                    }
                    case 2 -> {}
                    case 3 -> System.err.println("Invalid input");
                }
                data.put("method", "blockUser");
                data.put("process", "applyBlocking");

            }

            if (blockStatus.equals("false")) {
                System.out.println("-" + userName);
                System.out.println("Do you want to block " + userName + "?");
                System.out.println("  1.Yes    2.No");
                System.out.print("> ");
                int input = 0;
                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid Input.");
                }
                switch (input) {
                    case 1 -> {
                        applyBlocking.put(userName + " " + true);
                        System.out.println("You blocked " + userName);
                    }
                    case 2 -> {
                    }
                    case 3 -> System.out.println("Invalid input");
                }

            }


        }

        data.put("method", "blockUser");
        data.put("process", "applyBlocking");

        data.remove("friendsList");
        data.put("applyBlocking", applyBlocking);
    }

    private void friendsList() {
        JSONArray friendsList = data.getJSONArray("friends");
        System.out.println("----<My friends>-------------------------");
        int listOrder = 1;
        for (Object object : friendsList) {
            System.out.println(listOrder + ". " + object);
            listOrder++;
        }
        System.out.println(">> press any key to go main menu <<");
        System.out.print("> ");
        scanner.nextLine();
        data.remove("friendsList");
        data.put("method", "loggedIn");
        data.put("process", "applyBlocking");
    }

    public void friendRequests() {
        JSONArray friends = data.getJSONArray("friendRequests");

        Iterator<Object> iterator = friends.iterator();
        ArrayList<String> friendsList = new ArrayList<>();

        while (iterator.hasNext()) {
            String tmp = (String) iterator.next();
            friendsList.add(tmp);
        }

        JSONArray friendsToConfirm = new JSONArray();
        JSONArray friendsToApprove = new JSONArray();


        System.out.println("Your friends requests: ");
        for (String string : friendsList) {
            System.out.println("-" + string);
            System.out.println(" 1. confirm\n 2. approve\n 3. nothing\n_________");
            System.out.print("> ");

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

        }

        data.put("method", "friendRequests");
        data.put("process", "confirmOrApproveFriendRequests");
        data.put("confirmedRequests", friendsToConfirm);
        data.put("approvedRequests", friendsToApprove);
    }

    public void sendFriendRequest() {
        JSONArray users = data.getJSONArray("users");

        Iterator<Object> iterator = users.iterator();
        ArrayList<String> arrayList = new ArrayList<>();

        String tmp;
        while (iterator.hasNext()) {
            tmp = (String) iterator.next();
            arrayList.add(tmp);
        }

        JSONArray friendsToAddJsonArray = new JSONArray();

        System.out.println("Users that you can send friend request: ");

        for (String userName : arrayList) {
            System.out.println("-" + userName);
            System.out.println("  1. send friend request \n  2. nothing");
            System.out.print("> ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    friendsToAddJsonArray.put(userName);
                    System.out.println("friend request send.");
                } else {
                    System.out.println("Invalid input");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        data.remove("users");
        data.put("method", "friendRequests");
        data.put("process", "applyRequests");
        data.put("friendsToAdd", friendsToAddJsonArray);

    }

}
