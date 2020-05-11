package main.java;
/**
 * This User class only has the username field in this example.
 * You can add more attributes such as the user's shopping cart items.
 */
public class User
{
    public final String username;
    public final String userId;
    public final String firstName;
    public final String lastName;
    public final String role;
    public Boolean isUserValid = false;

    public User(String username, String userId, String firstName, String lastName, Boolean isUserValid)
    {
        this.username = username;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isUserValid = isUserValid;
        this.role = "";

    }
    public User(String username, String userId, String firstName, String lastName, Boolean isUserValid, String role)
    {
        this.username = username;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isUserValid = isUserValid;
        this.role = role;
    }
}

