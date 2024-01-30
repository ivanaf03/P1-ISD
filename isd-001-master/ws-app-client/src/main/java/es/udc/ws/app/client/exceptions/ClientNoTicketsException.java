package es.udc.ws.app.client.exceptions;

public class ClientNoTicketsException extends Exception{

    public ClientNoTicketsException() {
        super("There are no more tickets available for the match.");
    }

}
