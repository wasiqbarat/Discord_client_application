package Windows;

import org.json.JSONObject;

public class SignUpWindow extends Window{

    @Override
    public JSONObject action() {
        System.out.println("-------signup---------");
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("creat new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your phone: ");
        String phone = scanner.nextLine();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", "signUp");
        jsonObject.put("userName", userName);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        jsonObject.put("phone", phone);

        return jsonObject;
    }

}
