package Windows;

import Console.Console;
import Exceptions.InvalidInput;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * LoggedInWindow is user interface when user log in its account
 * @author wasiq
 * @version 1.0
 */
public class LoggedInWindow extends Window {

    public LoggedInWindow(JSONObject data) throws IOException {
        super(data);
    }

    /**
     * new data from server that is related to logged in person manages with this method
     */

    public void run() {
        switch (data.getString("process")) {
            case "loggedIn", "action" -> action();
            case "myFriendRequests" -> friendRequests();
            case "sendFriendRequest" -> sendFriendRequest();
            case "friendsList" -> friendsList();
            case "blockUser" -> blockUser();
            case "newChat" -> selectFriendToChat();
            case "myChats" -> myChats();
            case "chatting" -> Chatting();
            case "messagesDisplay" -> messagesDisplay();

            //case "createServer" -> userInput = loggedInWindow.createServer(jsonObject);
            //case "myServers" -> userInput = loggedInWindow.myServers(jsonObject);
        }

    }

    private void messagesDisplay() {
        JSONArray messages = data.getJSONArray("messages");

        for (Object o : messages) {
            String message = (String) o;
            System.out.println(message);
        }

        System.out.print("Press Enter to main menu: ");
        scanner.nextLine();
        data.remove("messages");
        data.put("method", "loggedIn");

    }


    private void myChats() {
        JSONArray myMessages = data.getJSONArray("myChats");
        ArrayList<String> chatFriends = new ArrayList<>();

        System.out.println("Select a chat to show : ");

        int listOrder = 1;
        for (Object o : myMessages) {
            chatFriends.add((String) o);
            System.out.println(listOrder + ". " + o);
            listOrder++;
        }

        System.out.print("> ");
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        data.remove("myChats");
        data.put("method", "chat");
        data.put("process", "getMessagesWith");
        data.put("messagesWith", chatFriends.get(input - 1));
    }

    private void Chatting() {
        System.err.println("type (exit) to leave chat.");
        System.out.println("Enter your message:  (press Enter to send message)");
        System.out.print("> ");
        String message = scanner.nextLine();

        if (!message.equals("exit")) {
            data.put("method", "chat");
            data.put("message" , message);
            data.put("process", "chatting");
        }else {
            action();
        }
    }


    private void selectFriendToChat() {
        JSONArray friends = data.getJSONArray("friends");
        ArrayList<String> friendsArrayList = new ArrayList<>();

        for (Object object : friends) {
            friendsArrayList.add((String) object);
        }

        System.out.println("Select Friend to chat: ");

        int listOrder = 1;
        for (String userName : friendsArrayList) {
            System.out.println(listOrder + "-" + " " + userName);
            listOrder++;
        }
        System.out.print("> ");
        int input = Integer.parseInt(scanner.nextLine());

        data.remove("friends");


        data.put("friendToChat", friendsArrayList.get(input - 1));
        data.put("method", "chat");
        data.put("process", "chatAuthentication");
    }


    @Override
    public void action() {

        //before go to main menu checks if message received or not
        try {
            Console.getInstance().newMessage(new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("""
                1. My Friend requests
                2. Send friend request
                3. Friends list
                4. Chat
                5. Block or Unblock friends
                6. Create a server
                7. My servers
                8. Change password
                9. Log out
                -------------------------------< select by number >""");
        //include online and offline or status. if a friend got offline it shows
        //if a user blocked you, you can't message to him/her.
        //with manage my servers we can create channels.

        System.out.print("> ");
        int input = 0;

        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            action();
        }

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

            case 4 -> {
                System.out.println("1. new chat\n2. My chats");
                System.out.print("> ");
                int input2 = 0;
                try {
                    input2 = Integer.parseInt(scanner.nextLine());
                    if (input2 > 2) {
                        throw new InvalidInput();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("InvalidInput");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }
                    run();
                } catch (InvalidInput e) {
                    System.err.println(e.getMessage());
                }

                switch (input2) {
                    case 1 -> {
                        data.put("method", "chat");
                        data.put("process", "newChat");
                    }
                    case 2 -> {
                        data.put("method", "chat");
                        data.put("process", "myChats");
                    }
                    default -> {
                        System.err.println("InvalidInput.");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ignored) {
                        }

                        run();
                    }

                }
            }

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
                data.put("method", "changePassword");
                changePassword();
            }
            case 9 -> {
                data.put("method", "logOut");
            }

        }
    }

    private void changePassword() {
        System.out.println("ChangePassword: .....................");
        System.out.print("Enter Old password: ");

        if (scanner.nextLine().equals(data.getString("password"))) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm new password: ");

            if (newPassword.equals(scanner.nextLine())) {
                data.put("newPassword", newPassword);
            }else {
                System.out.println("not match.");
                action();
            }
        }else {
            System.out.println("Invalid password.");
            action();
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
