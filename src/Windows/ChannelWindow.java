package Windows;

import Console.Console;
import Console.Responder;
import org.json.JSONObject;

public class ChannelWindow extends Window{
    public ChannelWindow(JSONObject data) {
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

        System.out.println("........." + data.getString("channelName") + " panel..........");
        System.out.println("""
                1. Chatting
                2. Show pinned message
                3. Show chat history
                4. Pin a message
                5. React to a message
                6. Back to server menu
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
                data.put("method", "server");
                data.put("process", "channelChat");
                chatting();
            }
            case 2 -> {
                data.put("method", "server");
                data.put("process", "showPinnedMessage");
            }
            case 3 -> {
                data.put("method", "server");
                data.put("process", "authentication");
                data.put("action", "chatHistoryAccess");
            }

            case 4 -> {
                data.put("method", "server");
                data.put("process", "authentication");
                data.put("action", "pinMessage");
            }

            case 5 -> {
                data.put("method", "server");
                data.put("process", "reactToMessage");
            }
            case 6 -> {
                data.put("method", "loggedIn");
                data.put("process", "serverPanel");
            }

        }
    }

    private void chatting() {
        System.out.print("Enter you message: ");
        String message = scanner.nextLine();

        data.put("message", message);
    }


}
