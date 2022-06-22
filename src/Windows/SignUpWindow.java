package Windows;

import org.json.JSONObject;

public class SignUpWindow extends Window{


    public SignUpWindow(JSONObject data) {
        super(data);
    }

    @Override
    public void action() {
        System.out.println("-------signup---------");
        System.out.print("Enter username: ");
        String userName = scanner.nextLine();
        System.out.print("creat new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your phone: ");
        String phone = scanner.nextLine();

        data.put("method", "signUp");
        data.put("userName", userName);
        data.put("password", password);
        data.put("email", email);
        data.put("phone", phone);

        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        action();
    }
}
