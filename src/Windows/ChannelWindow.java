package Windows;

import org.json.JSONObject;

public class ChannelWindow extends Window{
    public ChannelWindow(JSONObject data) {
        super(data);
    }

    @Override
    public void action() {
        System.out.println("........." + data.getString("channelName") + " panel..........");
        System.out.println("""
                1. Messaging
                2. Show pinned message
                3. Show chat history
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

            }

        }
    }


}
