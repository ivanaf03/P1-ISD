package es.udc.ws.app.model.util.service;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.model.util.compra.SqlSaleDao;
import es.udc.ws.app.model.util.compra.SqlSaleDaoFactory;
import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.model.util.partido.SqlGameDao;
import es.udc.ws.app.model.util.partido.SqlGameDaoFactory;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import javax.sql.DataSource;

import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.udc.ws.app.model.util.utils.ModelConstants.*;

public class ServiceImplementation implements Service {
    private DataSource dataSource = null;
    private SqlGameDao gameDao = null;
    private SqlSaleDao saleDao = null;

    public ServiceImplementation() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        gameDao = SqlGameDaoFactory.getDao();
        saleDao = SqlSaleDaoFactory.getDao();
    }

    //comprueba que no se hayan cogido las entradas
    public static boolean validateBoolean(boolean bool, boolean expectedValue) {
        if (bool != expectedValue) {
            return false;
        } else {
            return true;
        }
    }

    //VALIDAR PARTIDO Y COMPRA

    private void validateGame(Game game) throws InputValidationException {
        PropertyValidator.validateDouble("price", game.getPrice(), MIN_PRICE, MAX_PRICE);
        PropertyValidator.validateMandatoryString("visit", game.getVisit());
        PropertyValidator.validateLong("soldTickets", game.getSoldTickets(), MIN_TICKETS, MAX_TICKETS);

    }

    private void validateSale(Sale sale) throws InputValidationException {
        PropertyValidator.validateNotNegativeLong("gameId", sale.getGameId());
        PropertyValidator.validateMandatoryString("userEmail", sale.getUserEmail());
        PropertyValidator.validateCreditCard(sale.getCardNum());
        PropertyValidator.validateLong("ticketNum", sale.getTicketNum(), MIN_TICKETS, MAX_TICKETS);
    }

    private static boolean isValidEmail(String email) {
        //El correo debe contener letras o digitos antes del @, debe contener un @, despues del @ deben haber dígitos o caracteres. Debe terminar con una letra o dígito
        String patron = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public Game addGame(Game game) throws InputValidationException {
        validateGame(game); //si no es válido ya sale
        if(game.getAvailableTickets()<=0){
            throw new InputValidationException("Invalid " + game.getAvailableTickets() +". Tickets of game can't be 0.");
        }
        if(game.getDateTimeCelebration().isBefore(LocalDateTime.now())){
            throw new InputValidationException("Invalid " + game.getDateTimeCelebration() +". You can't create a game in a past date.");
        }
        game.setDateTimeEntry(LocalDateTime.now());
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Game createdGame = gameDao.create(connection, game);
                connection.commit();
                return createdGame;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> findBetweenDates(LocalDateTime ini, LocalDateTime fin) throws InputValidationException {
        if (ini == null || fin == null) {
            throw new InputValidationException("Las fechas no son validas");
        }
        try (Connection connection = dataSource.getConnection()) {
            return gameDao.findBetweenDates(connection, ini, fin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game findGameById(Long gameId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return gameDao.findById(connection, gameId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sale buy(Long gameId, String userEmail, String cardNumber, int ticketsNum) throws InstanceNotFoundException, InputValidationException, NoTicketsException, GameCelebratedException {
        PropertyValidator.validateCreditCard(cardNumber);

        if (!isValidEmail(userEmail)) {
            throw new InputValidationException("User email is not valid.");
        }
        if (ticketsNum<=0) {
            throw new InputValidationException("You must buy more than 0 tickets");
        }

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Game game = gameDao.findById(connection, gameId);

                LocalDateTime expirationDate = game.getDateTimeCelebration();
                int tickets = game.getAvailableTickets() - ticketsNum;

                if (tickets < 0) {
                    throw new NoTicketsException(gameId);
                }

                if (LocalDateTime.now().isAfter(expirationDate)) {
                    throw new GameCelebratedException(gameId);
                }

                Sale sale = saleDao.create(connection, new Sale(gameId, userEmail, cardNumber, game.getPrice() * ticketsNum, false, LocalDateTime.now(), ticketsNum));

                game.setAvailableTickets(tickets);
                game.setSoldTickets(ticketsNum);

                Game g = gameDao.update(connection, game);
                connection.commit();

                return sale;
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Sale> findSale(String userEmail) throws InputValidationException {
        if (!isValidEmail(userEmail)) {
            throw new InputValidationException("El email no es valido");
        }
        try (Connection connection = dataSource.getConnection()) {
            return saleDao.findSale(connection, userEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sale pickUpTickets(Long saleId, String cardNumber) throws InstanceNotFoundException, InvalidCreditCardException, TicketsAlreadyDeliveredException, InputValidationException {
        PropertyValidator.validateCreditCard(cardNumber);
            try (Connection connection = dataSource.getConnection()) {
                try {
                    connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                    connection.setAutoCommit(false);
                    Sale sale=saleDao.findBySaleId(connection, saleId);
                    if (!validateBoolean(sale.isTakedTickets(), false)) {
                        throw new TicketsAlreadyDeliveredException();
                    } else if (!sale.getCardNum().equals(cardNumber)) {
                        throw new InvalidCreditCardException(cardNumber);
                    }
                    sale.setTakedTickets(true);
                    saleDao.update(connection, sale);
                    connection.commit();
                    return sale;
                } catch (InstanceNotFoundException e) {
                    connection.commit();
                    throw e;
                } catch (SQLException e) {
                    connection.rollback();
                    throw new RuntimeException(e);
                } catch (RuntimeException | Error e) {
                    connection.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}
