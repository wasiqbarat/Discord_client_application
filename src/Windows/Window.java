package Windows;

import org.json.JSONObject;
import java.util.Scanner;

/**
 * Window class provide user interface
 *
 * @author wasiq
 *
 * @see SignUpWindow
 * @see LoginWindow
 * @see LoggedInWindow
 */
public abstract class Window {
    protected static Scanner scanner;
    protected JSONObject data;

    public Window(JSONObject data) {
        scanner = new Scanner(System.in);
        this.data = data;
    }

    public abstract void action();
}
