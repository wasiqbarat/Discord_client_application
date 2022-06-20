package Windows;

import org.json.JSONObject;

public class LoginWindow extends Window{

    public LoginWindow(JSONObject data) {
        super(data);
    }

    @Override
    public JSONObject action() {
        System.out.println("------login panel-------");
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        data.put("method", "logIn");
        data.put("userName", userName);
        data.put("password", password);
        return data;
    }

    @Override
    public void run() {
        action();
    }

}
