package es.udc.ws.app.client.exceptions;

public class ClientInvalidCreditCardException extends Exception{

    public ClientInvalidCreditCardException() {
        super("This credit card is invalid.");
    }
}
