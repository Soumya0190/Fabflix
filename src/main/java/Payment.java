package main.java;

public class Payment
{
    String cardNumber;
    String customerFirstName;
    String customerLastName;
    String cardExpirationDate;
    String ccId;

    public Payment(String cardNumber, String customerFirstName, String customerLastName, String cardExpirationDate, String ccId)
    {
        this.cardNumber = cardNumber;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.cardExpirationDate = cardExpirationDate;
        this.ccId = ccId;
    }
}
