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
        System.out.println("Select Status:\n1.Online    2.Idle    3.Do not disturb   4.Invisible");
        System.out.print("> ");

        String status = scanner.nextLine();
        try {
            switch (status) {
                case "1" -> status = "online";
                case "2" -> status = "idle";
                case "3" -> status = "doNotDisturb";
                case "4" -> status = "invisible";
            }
        }catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        data.put("method", "signUp");
        data.put("userName", userName);
        data.put("password", password);
        data.put("email", email);
        data.put("phone", phone);
        data.put("status", status);
    }

}
