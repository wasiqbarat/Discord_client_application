package Exceptions;

public class EmailAndPhoneNumberException extends Exception{
    @Override
    public String getMessage() {
        return "Email or Phone Invalid.";
    }
}
