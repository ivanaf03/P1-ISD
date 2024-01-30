package es.udc.ws.app.client.exceptions;

public class ClientTicketsAlreadyDeliveredException extends Exception {
    public ClientTicketsAlreadyDeliveredException() {
        super("These tickets have already been delivered.");
    }
}
