package Windows;

import Console.Console;
import Console.Responder;
import Exceptions.InvalidInput;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * LoggedInWindow is user interface when user log in its account
 *
 * @author wasiq
 * @version 1.0
 */
public class LoggedInWindow extends Window {

    public LoggedInWindow(JSONObject data) {
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
            case "serverPanel" -> serverPanel();
            case "selectFriendToAddServer" -> selectFriendToAddServer();
            case "chooseServer" -> chooseServer();
            case "membersActiveStatus" -> displayMembersActiveStatus();
            case "selectUserToRemoveFromServer" -> selectUserToRemoveFromServer();
            case "panelForCreatingChannel" -> panelForCreatingNewChannel();
            case "channelPanel" -> channelPanel();
            case "serverChannelsPanel" -> serverChannelsPanel();
            case "displayPinnedMessage" -> displayPinnedMessage();
            case "displayChannelMessages" -> displayChannelMessages();
            case "reactToMessage" -> reactToMessage();
            case "pinMessage" -> displayMessagesToPin();
        }
    }

    private void displayMessagesToPin() {
        JSONArray messages = data.getJSONArray("messages");
        if (messages.isEmpty()) {
            System.out.println("No messages to show");
            data.put("method", "loggedIn");
            data.put("process", "channelPanel");
            return;
        }
        data.remove("messages");

        System.out.println("....Select message for pin......");
        int listOrder = 1;
        for (Object o : messages) {
            System.out.println(listOrder + ". " +o);
        }
        System.out.println(".........");
        System.out.print("> ");
        int input = Integer.parseInt(scanner.nextLine());

        String messageForPin = messages.getString(input - 1);

        data.put("messageForPin", messageForPin);

        data.put("method", "server");
        data.put("process", "applyPinMessage");

    }

    private void reactToMessage() {
        JSONArray messages = data.getJSONArray("messages");
        if (messages.isEmpty()) {
            System.out.println("No messages to show");
            data.put("method", "loggedIn");
            data.put("process", "channelPanel");
            return;
        }

        System.out.println("....Select message for reaction......");
        int listOrder = 1;
        for (Object o : messages) {
            System.out.println(listOrder + ". " +o);
        }
        System.out.println(".........");
        System.out.print("> ");
        int input = Integer.parseInt(scanner.nextLine());

        String messageToReact = messages.getString(input - 1);
        System.out.println("1. Like    2. DisLike    3.HaHa");
        System.out.print("> ");
        int input2 = Integer.parseInt(scanner.nextLine());
        switch (input2) {
            case 1 -> data.put("reaction", "like");
            case 2 -> data.put("reaction", "disLike");
            case 3 -> data.put("reaction", "haha");
        }
        data.put("messageForReaction", messageToReact);

        data.put("method", "server");
        data.put("process", "applyReaction");

    }

    private void displayChannelMessages() {
        JSONArray messages = data.getJSONArray("messages");
        if (messages.isEmpty()) {
            System.out.println("No messages to show");
            data.put("method", "loggedIn");
            data.put("process", "channelPanel");
            return;
        }

        System.out.println("......" + data.getString("channelName") + " messages history........");
        for (Object o : messages) {
            System.out.println(o);
        }
        System.out.println("............................");
        System.out.print("Press enter to back main menu...");
        scanner.nextLine();

        data.remove("messages");
        data.put("method", "loggedIn");
        data.put("process", "channelPanel");
    }

    private void displayPinnedMessage() {
        String pinnedMessage = data.getString("pinnedMessage");
        if (pinnedMessage.isEmpty() || pinnedMessage.contains("empty")) {
            data.remove("pinnedMessage");
            data.put("method", "loggedIn");
            data.put("process", "channelPanel");
        }
        System.out.println("..............");
        System.out.println("pinned message: " + pinnedMessage);
        System.out.println("..............");
        data.put("method", "loggedIn");
        data.put("process", "channelPanel");
    }

    private void serverChannelsPanel() {
        JSONArray channels = data.getJSONArray("channels");
        if (channels.isEmpty()) {
            System.out.println("No channels to show");
            data.remove("channels");
            serverPanel();
            return;
        }

        System.out.println("............" + data.getString("serverName") + " channels" + "............");
        int listOrder = 1;
        for (Object o : channels) {
            System.out.println(listOrder + ". " + o);
        }

        System.out.println(".............");
        System.out.print("> ");

        try {
            int input = Integer.parseInt(scanner.nextLine());
            if (input < 1 || input > channels.length()) {
                throw new Exception();
            }
            data.put("channelName", channels.get(input - 1));
        } catch (Exception e) {
            System.out.println("Invalid input");
            chooseServer();
        }

        data.remove("channels");
        data.put("method", "server");
        data.put("process", "channelPanel");
    }

    private void panelForCreatingNewChannel() {
        System.out.println(".................Creating new channel..................");
        System.out.print("Enter channel name: ");
        String channelName = scanner.nextLine();
        System.out.println("Select channel type: ");
        System.out.println("1. Text Channel\n2. Voice channel");
        try {
            System.out.println("..........");
            System.out.print("> ");
            int input = Integer.parseInt(scanner.nextLine());
            switch (input) {
                case 1 -> data.put("channelType", "text");
                case 2 -> data.put("channelType", "voice");
                default -> {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("invalid Input");
            panelForCreatingNewChannel();
        }

        data.put("channelName", channelName);
        data.put("method", "server");
        data.put("process", "applyCreatingChannel");
    }


    @Override
    public void action() {

        //before go to menu checks if new message received or not
        try {
            Responder.getInstance(Console.getInstance()).getNewChannelMessages();
            Responder.getInstance(Console.getInstance()).getOlderPrivateMessages();
            Responder.getInstance(Console.getInstance()).newMessage(new JSONObject());
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
        System.out.print("> ");
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
            if (input < 1 || input > 9) {
                System.out.println("Invalid Input.");
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
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
                    System.out.println("InvalidInput");
                    run();
                } catch (InvalidInput e) {
                    System.out.println(e.getMessage());
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
                        System.out.println("InvalidInput.");
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
                createServer();
            }
            case 7 -> {
                data.put("method", "server");
                data.put("process", "myServers");
            }
            case 8 -> {
                data.put("method", "changePassword");
                changePassword();
            }
            case 9 -> data.put("method", "logOut");
        }
    }

    private void selectUserToRemoveFromServer() {
        JSONArray serverUsers = data.getJSONArray("serverUsers");
        if (serverUsers.isEmpty()) {
            System.out.println("No member to remove.");
            data.remove("serverUsers");
            data.remove("action");
            data.put("method", "loggedIn");
            data.put("process", "serverPanel");
            return;
        }
        System.out.println("Select user to remove: ");
        int listOrder = 1;
        for (Object user : serverUsers) {
            System.out.println(listOrder + ". " + user);
            listOrder++;
        }
        try {
            int input = Integer.parseInt(scanner.nextLine());
            if (input < 1 || input > serverUsers.length()) {
                throw new Exception();
            }
            data.put("memberToRemove", serverUsers.get(input - 1));
        } catch (Exception e) {
            System.out.println("Invalid Input");
            selectUserToRemoveFromServer();
        }

        data.put("method", "server");
        data.put("process", "removeMemberFromServer");
    }

    private void displayMembersActiveStatus() {
        JSONArray members = data.getJSONArray("activeStatus");

        if (members.isEmpty()) {
            System.out.println("No members to display.");
            data.remove("activeStatus");
            serverPanel();
        } else {
            System.out.println(".........Active status.........");
            int listOrder = 1;
            for (Object o : members) {
                System.out.println(listOrder + ". " + o);
                listOrder++;
            }
            System.out.print("press Enter to go server panel...");
            scanner.nextLine();

            data.remove("activeStatus");
            serverPanel();
        }

    }

    private void chooseServer() {
        JSONArray servers = data.getJSONArray("servers");
        if (servers.isEmpty()) {
            System.out.println("............You have no server............");
            System.out.println("..........................................");
            data.remove("method");
            data.remove("process");
            data.remove("servers");
            action();
            return;
        }

        System.out.println(".........select server to log.........");
        int listOrder = 1;
        for (Object o : servers) {
            System.out.println(listOrder + ". " + o);
            listOrder++;
        }
        System.out.println(".............");
        System.out.print("> ");

        try {
            int input = Integer.parseInt(scanner.nextLine());
            if (input < 1 || input > servers.length()) {
                throw new Exception();
            }
            data.put("serverName", servers.get(input - 1));
        } catch (Exception e) {
            System.out.println("Invalid input");
            chooseServer();
        }

        data.remove("servers");
        data.put("method", "server");
        data.put("process", "serverPanel");
    }

    private void channelPanel() {
        ChannelWindow channelPanel = new ChannelWindow(data);
        channelPanel.action();
    }

    private void serverPanel() {
        ServerWindow serverWindow = new ServerWindow(data);
        serverWindow.action();
    }

    private void selectFriendToAddServer() {
        JSONArray friends = data.getJSONArray("friends");
        if (friends.isEmpty()) {
            System.out.println("You have no friend to add");
            data.remove("friends");
            data.put("method", "loggedIn");
            data.put("process", "serverPanel");
            serverPanel();
            return;
        }
        System.out.println("Select Friend to add to server: ");

        int listOrder = 1;

        for (Object o : friends) {
            System.out.println(listOrder + "- " + o);
            listOrder++;
        }
        System.out.println(friends.length() + 1 + "- Cancel");
        System.out.print("> ");
        int input;
        try {
            input = Integer.parseInt(scanner.nextLine());
            if (input > friends.length()) {
                System.out.println("Invalid Input.");
                data.put("method", "server");
                data.put("process", "serverPanel");
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid Input");
            return;
        }

        System.out.println("Select Role: ");
        JSONArray roles = data.getJSONArray("roles");
        int listOrder2 = 1;
        for (Object o : roles) {
            System.out.println(listOrder2 + ". " + o);
            listOrder2++;
        }
        int input2;

        try {
            System.out.print("> ");
            input2 = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid Input.");
            data.put("method", "loggedIn");
            data.put("process", "serverPanel");
            return;
        }

        data.remove("friends");

        data.remove("roles");
        data.put("role", roles.get(input2 - 1));
        data.put("friendToAdd", friends.get(input - 1));
        data.put("method", "server");
        data.put("process", "applyMemberAdd");
    }

    private void createServer() {
        System.out.println(".....create new server............");
        System.out.print("Enter server name: ");
        String serverName = scanner.nextLine();
        data.put("method", "server");
        data.put("serverName", serverName);
        data.put("process", "newServer");
    }

    private void messagesDisplay() {
        JSONArray messages = data.getJSONArray("messages");
        if (messages.isEmpty()) {
            System.out.println("No messages to show.");
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            return;
        }

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

    private void Chatting(){

        System.err.println("type (exit) to leave chat.");
        System.out.println("Enter your message:  (press Enter to send message)");
        System.out.print("> ");
        String message = scanner.nextLine();

        if (!message.equals("exit")) {
            data.put("method", "chat");
            data.put("message", message);
            data.put("process", "chatting");
        } else {
            action();
        }
    }

    private void selectFriendToChat() {
        JSONArray friends = data.getJSONArray("friends");
        if (friends.isEmpty()) {
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            System.out.println("No friends to show.");
            return;
        }
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

    private void changePassword() {
        System.out.println("ChangePassword: .....................");
        System.out.print("Enter Old password: ");

        if (scanner.nextLine().equals(data.getString("password"))) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm new password: ");

            if (newPassword.equals(scanner.nextLine())) {
                data.put("newPassword", newPassword);
            } else {
                System.out.println("not match.");
                action();
            }
        } else {
            System.out.println("Invalid password.");
            action();
        }
    }

    private void blockUser() {
        JSONArray friendsList = data.getJSONArray("friendsList");

        if (friendsList.isEmpty()) {
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            System.out.println("No friends to show.");
            return;
        }

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
                    case 2 -> {
                    }
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
        if (friendsList.isEmpty()) {
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            System.out.println("No friends to show.");
            return;
        }
        System.out.println("----<My friends>-------------------------");
        int listOrder = 1;
        for (Object object : friendsList) {
            System.out.println(listOrder + ". " + object);
            listOrder++;
        }
        System.out.print("press any key to go main menu...");
        scanner.nextLine();
        data.remove("friendsList");
        data.put("method", "loggedIn");
        data.put("process", "applyBlocking");
    }

    public void friendRequests() {
        JSONArray friends = data.getJSONArray("friendRequests");
        if (friends.isEmpty()) {
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            System.out.println("You have no friend requests.");
            return;
        }
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
        if (users.isEmpty()) {
            data.put("method", "loggedIn");
            data.put("process", "loggedIn");
            System.out.println("No users to show.");
            return;
        }

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
