package Windows;

import org.json.JSONObject;
import java.util.Scanner;

public abstract class Window implements Runnable{
    protected static Scanner scanner;
    protected JSONObject data;

    public Window(JSONObject data) {
        scanner = new Scanner(System.in);
        this.data = data;
    }

    public abstract JSONObject action();
}
