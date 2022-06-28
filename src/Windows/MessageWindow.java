package Windows;

import org.json.JSONObject;

public class MessageWindow extends Window{
    public MessageWindow(JSONObject data) {
        super(data);
    }

    @Override
    public void action() {
        System.err.println(data);
    }

}
