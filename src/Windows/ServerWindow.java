package Windows;

import Console.Console;
import Console.Responder;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerWindow extends Window {
    public ServerWindow(JSONObject data) {
        super(data);
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

        System.out.println("......................Server Panel......................");
        System.out.println("""
                  1. Add member
                  2. Remove member
                  3. Channels
                  4. Create new channel
                  5. Members active status
                  6. Create new role
                  7. Main menu
                -------------------------------< select by number >""");
        System.out.print("> ");
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            action();
        }

        switch (input) {
            case 1 -> {
                data.put("process", "addMember");
                data.put("method", "server");
            }
            case 2 -> {
                data.put("process", "authentication");
                data.put("action", "removeMember");
            }
            case 3 -> data.put("process", "serverChannels");
            case 4 -> {
                data.put("process", "authentication");
                data.put("action", "createChannel");
            }
            case 5 -> data.put("process", "membersActiveStatus");
            case 6 -> createRole();
            case 7 -> {
                data.put("method", "loggedIn");
                data.put("process", "action");
                data.remove("serverName");
                return;
            }
        }

        if (data.getString("method").equals("loggedIn")) {
            data.put("method", "server");
        }

    }

    private void createRole() {
        System.out.println(".......................Create new role......................");
        System.out.print("Enter role name: ");
        String roleName = scanner.nextLine();

        JSONArray permissions = new JSONArray();
        permissions.put("createChannel");
        permissions.put("removeChannel");
        permissions.put("userRemove");
        permissions.put("userLimit");
        permissions.put("banUser");
        permissions.put("renameServer");
        permissions.put("chatHistoryAccess");
        permissions.put("messagePin");
        System.out.println("  Choose permissions that you want your role should have: ");

        for (int i = 0; i < permissions.length(); i++) {
            switch (permissions.getString(i)) {
                case "createChannel" -> System.out.println("-Ability to create channel");
                case "removeChannel" -> System.out.println("-Ability to remove channel");
                case "userRemove" -> System.out.println("-Ability to remove user");
                case "userLimit" -> System.out.println("-Ability to limit a user for access to a channel");
                case "banUser" -> System.out.println("-Ability to ban user, and access to some channel");
                case "renameServer" -> System.out.println("-Ability to rename server");
                case "chatHistoryAccess" -> System.out.println("-Ability to access channel chat history");
                case "messagePin" -> System.out.println("-Ability to pin a message");
            }
            System.out.println("  1.Yes\n  2.No");
            System.out.print("> ");
            int input = Integer.parseInt(scanner.nextLine());

            if (input == 2) {
                permissions.remove(i);
            }

        }

        data.put("roleName", roleName);
        data.put("permissions", permissions);
        data.put("process", "createRole");
    }
}
