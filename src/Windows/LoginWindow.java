package Windows;

import org.json.JSONObject;

public class LoginWindow extends Window{

    @Override
    public JSONObject action() {
        System.out.println("------login panel-------");
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        JSONObject identity = new JSONObject();
        identity.put("method", "logIn");
        identity.put("userName", userName);
        identity.put("password", password);
        return identity;
    }
}
