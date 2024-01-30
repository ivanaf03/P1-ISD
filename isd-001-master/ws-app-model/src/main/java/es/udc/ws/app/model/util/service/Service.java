package es.udc.ws.app.model.util.service;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface Service {
    public Game addGame(Game game) throws InputValidationException;

    public List<Game> findBetweenDates(LocalDateTime ini, LocalDateTime fin) throws InputValidationException;

    public Game findGameById(Long gameId) throws InstanceNotFoundException;

    public Sale buy(Long gameId, String userEmail, String cardNumber, int ticketsNum) throws InstanceNotFoundException, InputValidationException, NoTicketsException, GameCelebratedException;

    public List<Sale> findSale(String userEmail) throws InputValidationException;

    public Sale pickUpTickets(Long saleId, String cardNumber) throws InstanceNotFoundException, InvalidCreditCardException, TicketsAlreadyDeliveredException, InputValidationException;
}
