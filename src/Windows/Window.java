package Windows;

import org.json.JSONObject;
import java.util.Scanner;

public abstract class Window {
    protected static Scanner scanner;

    public Window() {
        scanner = new Scanner(System.in);
    }

    public abstract JSONObject action();
}
