package es.udc.ws.app.client;

import es.udc.ws.app.client.dto.ClientGameDto;
import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.app.client.exceptions.ClientGameCelebratedException;
import es.udc.ws.app.client.exceptions.ClientInvalidCreditCardException;
import es.udc.ws.app.client.exceptions.ClientNoTicketsException;
import es.udc.ws.app.client.exceptions.ClientTicketsAlreadyDeliveredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientGameService {
    Long addGame(ClientGameDto game) throws InputValidationException;

    List<ClientGameDto> findBetweenDates(String fin) throws InputValidationException;

    ClientGameDto findGameById(Long gameId) throws InstanceNotFoundException;

    Long buy(Long gameId, String userEmail, String cardNumber, int ticketsNum) throws InstanceNotFoundException, InputValidationException, ClientNoTicketsException, ClientGameCelebratedException;

    List<ClientSaleDto> findSale(String userEmail) throws InstanceNotFoundException, InputValidationException;

    ClientSaleDto pickUpTickets(String saleId, String cardNumber) throws InstanceNotFoundException, ClientInvalidCreditCardException, ClientTicketsAlreadyDeliveredException, InputValidationException;
}
