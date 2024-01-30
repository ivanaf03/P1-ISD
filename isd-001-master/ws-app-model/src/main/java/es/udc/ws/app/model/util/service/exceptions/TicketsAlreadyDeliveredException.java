package es.udc.ws.app.model.util.service.exceptions;

public class TicketsAlreadyDeliveredException extends Throwable {
    public TicketsAlreadyDeliveredException() {
        super("These tickets have already been delivered.");
    }
}
