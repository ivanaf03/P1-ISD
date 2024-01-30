package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.model.util.service.ServiceFactory;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.util.List;


public class ThriftGameServiceImplementation implements ThriftGameService.Iface {

    @Override
    public ThriftGameDto addGame(ThriftGameDto gameDto) throws TException {
        Game game=GameToThriftGameDtoConversor.toGame(gameDto);
        try {
            Game addedGame=ServiceFactory.getService().addGame(game);
            return GameToThriftGameDtoConversor.toThriftGameDto(addedGame);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public List<ThriftGameDto> findBetweenDates(String fin) throws ThriftInputValidationException, TException {
        try{
            List<Game> gameList = ServiceFactory.getService().findBetweenDates(LocalDateTime.now(),LocalDateTime.parse(fin));
            return GameToThriftGameDtoConversor.toThriftGameDtos(gameList);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }


    @Override
    public ThriftGameDto findById(long gameId) throws TException {
        try {
            Game game = ServiceFactory.getService().findGameById(gameId);
            return GameToThriftGameDtoConversor.toThriftGameDto(game);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public ThriftSaleDto buy(long gameId, String userEmail, String cardNumber, int ticketsNum) throws TException {
        try {
            Sale sale = ServiceFactory.getService().buy(gameId, userEmail, cardNumber, ticketsNum);
            return SaleToThriftSaleDtoConversor.toThriftSaleDto(sale);
        } catch (GameCelebratedException e) {
            throw new ThriftGameCelebratedException();
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException();
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException();
        } catch (NoTicketsException e) {
            throw new ThriftNoTicketsException();
        }
    }

    @Override
    public List<ThriftSaleDto> findSale(String userEmail) throws ThriftInstanceNotFoundException, ThriftInputValidationException, TException {
        try{
            List<Sale> saleList = ServiceFactory.getService().findSale(userEmail);
            return SaleToThriftSaleDtoConversor.toThriftSaleDtos(saleList);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ThriftSaleDto pickUpTickets(long saleId, String cardNum) throws TException {
        try {
            return SaleToThriftSaleDtoConversor.toThriftSaleDto(ServiceFactory.getService().pickUpTickets( saleId, cardNum));
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (TicketsAlreadyDeliveredException e) {
            throw new ThriftTicketsAlreadyDeliveredException(e.getMessage());
        } catch (InvalidCreditCardException e) {
            throw new ThriftInvalidCreditCardException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }
}
