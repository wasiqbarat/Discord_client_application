package Users;

/**
 * User class simulates a discord user name
 *
 * @author wasiq
 */
public class User {
    private final String userName;
    private final String password;
    private final String email;
    private final String phoneNumber;


    public User(String userName, String password, String email, String phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

