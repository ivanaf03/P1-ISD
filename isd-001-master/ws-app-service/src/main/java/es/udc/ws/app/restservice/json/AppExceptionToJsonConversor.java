package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;

public class AppExceptionToJsonConversor {
    public static ObjectNode toGameCelebratedException(GameCelebratedException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "GameCelebrated");
        exceptionObject.put("gameId", (ex.getSaleId()!=null) ? ex.getSaleId() : null);

        return exceptionObject;
    }

    public static ObjectNode toInvalidCreditCardException(InvalidCreditCardException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidCC");
        exceptionObject.put("creditCardNum", (ex.getCreditCardNum() != null) ? ex.getCreditCardNum() : null);

        return exceptionObject;
    }

    public static ObjectNode toNoTicketsException(NoTicketsException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType","NoTickets");
        exceptionObject.put("gameId",(ex.getGameId() != null) ? ex.getGameId() : null);

        return exceptionObject;
    }

    public static ObjectNode toTicketsAlreadyDeliveredException(TicketsAlreadyDeliveredException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "TicketsDelivered");

        return exceptionObject;
    }
}
