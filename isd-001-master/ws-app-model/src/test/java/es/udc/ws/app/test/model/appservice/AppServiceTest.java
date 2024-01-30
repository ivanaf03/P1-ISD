package es.udc.ws.app.test.model.appservice;
import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.model.util.compra.SqlSaleDao;
import es.udc.ws.app.model.util.compra.SqlSaleDaoFactory;
import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.model.util.partido.SqlGameDao;
import es.udc.ws.app.model.util.partido.SqlGameDaoFactory;
import es.udc.ws.app.model.util.service.Service;
import es.udc.ws.app.model.util.service.ServiceFactory;
import es.udc.ws.app.model.util.service.exceptions.GameCelebratedException;
import es.udc.ws.app.model.util.service.exceptions.InvalidCreditCardException;
import es.udc.ws.app.model.util.service.exceptions.NoTicketsException;
import es.udc.ws.app.model.util.service.exceptions.TicketsAlreadyDeliveredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static es.udc.ws.app.model.util.utils.ModelConstants.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class AppServiceTest {
    private static DataSource dataSource;

    private static Service gameService = null;

    private static SqlGameDao gameDao = null;

    private static SqlSaleDao saleDao = null;
    private final long NON_EXISTENT_GAME_ID = -1;

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private final String VALID_EMAIL = "a@gmail.com";
    private final String INVALID_EMAIL = "";
    LocalDateTime dateTime = LocalDateTime.of(2030, Month.JANUARY, 5, 22, 0);
    LocalDateTime dateTime2 = LocalDateTime.of(2030, Month.MAY, 15, 20, 0);
    LocalDateTime dateTime3 = LocalDateTime.of(2030, Month.JULY, 1, 10, 0);
    LocalDateTime dateTime4 = LocalDateTime.of(2030, Month.DECEMBER, 31, 22, 0);
    LocalDateTime dateTime5 = LocalDateTime.of(2031, Month.JANUARY, 1, 22, 0);

    @BeforeAll
    public static void init() {
        dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        gameService = ServiceFactory.getService();
        saleDao = SqlSaleDaoFactory.getDao();
        gameDao = SqlGameDaoFactory.getDao();
    }

    private Game getValidGame() {
        return new Game("inef", 15., 500, dateTime);
    }

    private Game getValidGame2() {
        return new Game("ficgods", 15., 200, dateTime2);
    }

    private Game getValidGame3() {
        return new Game("javalearners", 15., 350, dateTime3);
    }


    private Sale findSaleById(long saleId) throws SQLException, InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return saleDao.findBySaleId(connection, saleId);
        }
    }


    private void removeSale(Long saleId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                saleDao.remove(connection, saleId);
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeGame(Long saleId) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                gameDao.remove(connection, saleId);
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void updateSale(Sale sale) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                saleDao.update(connection, sale);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException();
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testAddFindGame() throws InstanceNotFoundException, InputValidationException {
        Game game = getValidGame();
        Game addedGame = null;
        try {
            addedGame = gameService.addGame(game);
            assertEquals(addedGame.getAvailableTickets(), game.getAvailableTickets());
            assertEquals(addedGame.getPrice(), game.getPrice());
            assertEquals(addedGame.getVisit(), game.getVisit());
            assertEquals(addedGame.getSoldTickets(), game.getSoldTickets());
            assertEquals(addedGame.getDateTimeCelebration(), game.getDateTimeCelebration());
            assertEquals(addedGame.getDateTimeEntry(), game.getDateTimeEntry());
            Game foundGame = gameService.findGameById(addedGame.getGameId());
            assertEquals(addedGame, foundGame);
        } finally {
            if (addedGame != null) {
                removeGame(addedGame.getGameId());
            }
        }
    }

    @Test
    public void testAddFindInvalidGameEmail() throws InstanceNotFoundException, InputValidationException {
        Long gameId = 1L;
        int ticketsNum = 2;

        assertThrows(InputValidationException.class, () ->
            gameService.buy(gameId, INVALID_EMAIL, VALID_CREDIT_CARD_NUMBER, ticketsNum)
        );
    }

    @Test
    public void testAddFindInvalidGameCreditCard() throws InstanceNotFoundException, InputValidationException {
        Long gameId = 1L;
        int ticketsNum = 2;

        assertThrows(InputValidationException.class, () ->
                gameService.buy(gameId, VALID_EMAIL, INVALID_CREDIT_CARD_NUMBER, ticketsNum)
        );
    }


    @Test
    public void testPickUp()
            throws InputValidationException, InstanceNotFoundException, GameCelebratedException, NoTicketsException, InvalidCreditCardException, TicketsAlreadyDeliveredException {
        Game game = getValidGame();
        Game createdGame = gameService.addGame(game);
        Sale createdSale = gameService.buy(createdGame.getGameId(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 5);

        // Inicializa la conexión fuera del bloque try para evitar el warning
        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            // Realiza las operaciones necesarias utilizando la conexión
            gameService.pickUpTickets(createdSale.getSaleId(), VALID_CREDIT_CARD_NUMBER);

            // Verifica el estado de la venta después de recoger los boletos
            Sale s = findSaleById(createdSale.getSaleId());
            assertEquals(s.getGameId(), createdSale.getGameId());
            assertTrue(s.isTakedTickets());
        } catch (SQLException e) {
            throw new RuntimeException("Error al realizar operaciones con la base de datos", e);
        } finally {
            // Cierra la conexión en el bloque finally
            try {
                if (connection != null) {
                    connection.close();
                }
                // Elimina la venta y el juego, incluso si ocurre una excepción
                removeSale(createdSale.getSaleId());
                removeGame(createdGame.getGameId());
            } catch (Exception e) {
                // Maneja cualquier excepción que pueda ocurrir al cerrar la conexión o eliminar la venta o el juego
                e.printStackTrace(); // Puedes personalizar el manejo de la excepción según tus necesidades
            }
        }
    }

    @Test
    public void testPickUpInvCard()
            throws InputValidationException, InstanceNotFoundException, GameCelebratedException, NoTicketsException {
        Game game = getValidGame();
        Game createdGame = gameService.addGame(game);
        Sale createdSale = gameService.buy(createdGame.getGameId(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 5);

        Connection connection = null;

        try {
            connection = dataSource.getConnection();

            assertThrows(InputValidationException.class, () -> gameService.pickUpTickets(createdSale.getSaleId(), INVALID_CREDIT_CARD_NUMBER));


        } catch (SQLException e) {
            throw new RuntimeException("Error al realizar operaciones con la base de datos", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                removeSale(createdSale.getSaleId());
                removeGame(createdGame.getGameId());
            } catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al cerrar la conexión o eliminar la venta o el juego
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testPickUpInvEmail() throws InputValidationException {
        Game createdGame = null;
        try {
            Game game = getValidGame();
            createdGame = gameService.addGame(game);
            assertThrows(InputValidationException.class, () -> gameService.buy(game.getGameId(), INVALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 5));
        } finally {
            if (createdGame != null) {
                removeGame(createdGame.getGameId());
            }
        }
    }

    @Test
    public void testInvFindBtwnDates() throws InputValidationException {
        Game createdGame = null;
        try {
            Game game = getValidGame();
            createdGame = gameService.addGame(game);
            assertThrows(InputValidationException.class, () -> gameService.findBetweenDates(null, null));
        } finally {
            if (createdGame != null) {
                removeGame(createdGame.getGameId());
            }
        }
    }

    @Test
    public void testFindBtwnDates() throws InputValidationException, InstanceNotFoundException {
        Game game1 = getValidGame();
        Game game2 = getValidGame2();
        Game game3 = getValidGame3();

        Game createdGame1 = null;
        Game createdGame2 = null;
        Game createdGame3 = null;
        try {
            createdGame1 = gameService.addGame(game1);
            createdGame2 = gameService.addGame(game2);
            createdGame3 = gameService.addGame(game3);

            List<Game> foundGames1 = gameService.findBetweenDates(dateTime, dateTime2);
            List<Game> shouldFoundGames1 = new ArrayList<>();
            shouldFoundGames1.add(createdGame1);
            shouldFoundGames1.add(createdGame2);
            assertEquals(foundGames1, shouldFoundGames1);

            List<Game> foundGames2 = gameService.findBetweenDates(dateTime2, dateTime3);
            List<Game> shouldFoundGames2 = new ArrayList<>();
            shouldFoundGames2.add(createdGame2);
            shouldFoundGames2.add(createdGame3);
            assertEquals(foundGames2, shouldFoundGames2);

            List<Game> foundGames3 = gameService.findBetweenDates(dateTime, dateTime4);
            List<Game> shouldFoundGames3 = new ArrayList<>();
            shouldFoundGames3.add(createdGame1);
            shouldFoundGames3.add(createdGame2);
            shouldFoundGames3.add(createdGame3);
            assertEquals(foundGames3, shouldFoundGames3);

            List<Game> foundGames4 = gameService.findBetweenDates(dateTime4, dateTime5);
            List<Game> shouldFoundGames4 = new ArrayList<>();
            assertEquals(foundGames4, shouldFoundGames4);

        } finally {
            if (createdGame1 != null) {
                removeGame(createdGame1.getGameId());
            }
            if (createdGame2 != null) {
                removeGame(createdGame2.getGameId());
            }
            if (createdGame3 != null) {
                removeGame(createdGame3.getGameId());
            }
        }
    }

    @Test
    public void findInvEmailSale() throws InstanceNotFoundException, InputValidationException, GameCelebratedException, NoTicketsException {
        Game game = getValidGame();
        Game createdGame = gameService.addGame(game);
        try {
            assertThrows(InputValidationException.class, () -> gameService.buy(createdGame.getGameId(), INVALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 5));
        } finally {
            removeGame(createdGame.getGameId());
        }
    }

    @Test
    public void findSaleTest() throws InstanceNotFoundException, InputValidationException, GameCelebratedException, NoTicketsException {
        Game game1 = getValidGame();
        Game game2 = getValidGame2();
        Game game3 = getValidGame3();

        Game createdGame1 = gameService.addGame(game1);
        Game createdGame2 = gameService.addGame(game2);
        Game createdGame3 = gameService.addGame(game3);

        Sale createdSale1 = gameService.buy(createdGame1.getGameId(),
                VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
        Sale createdSale2 = gameService.buy(createdGame2.getGameId(),
                VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 3);
        Sale createdSale3 = gameService.buy(createdGame3.getGameId(),
                VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 10);

        Sale createdSale4 = gameService.buy(createdGame1.getGameId(),
                "b@gmail.com", VALID_CREDIT_CARD_NUMBER, 1);
        Sale createdSale5 = gameService.buy(createdGame3.getGameId(),
                "b@gmail.com", VALID_CREDIT_CARD_NUMBER, 1);

        try {
            List<Sale> userSales = gameService.findSale(VALID_EMAIL);
            List<Sale> userExpectedSales = new ArrayList<>();

            userExpectedSales.add(createdSale1);
            userExpectedSales.add(createdSale2);
            userExpectedSales.add(createdSale3);

            assertEquals(userSales, userExpectedSales);

            List<Sale> userSales2 = gameService.findSale("b@gmail.com");
            List<Sale> userExpectedSales2 = new ArrayList<>();

            userExpectedSales2.add(createdSale4);
            userExpectedSales2.add(createdSale5);

            assertEquals(userSales2, userExpectedSales2);

        } finally {
            if (createdSale1 != null) {
                removeSale(createdSale1.getSaleId());
            }
            if (createdSale2 != null) {
                removeSale(createdSale2.getSaleId());
            }
            if (createdSale3 != null) {
                removeSale(createdSale3.getSaleId());
            }
            if (createdSale4 != null) {
                removeSale(createdSale4.getSaleId());
            }
            if (createdSale5 != null) {
                removeSale(createdSale5.getSaleId());
            }
            removeGame(createdGame1.getGameId());
            removeGame(createdGame2.getGameId());
            removeGame(createdGame3.getGameId());
        }
    }

    @Test
    public void testInvalidIdSearch() throws InputValidationException {
        Long invalidId = -1L;
        Game game = getValidGame();
        Game addedGame = null;
        try {
            addedGame = gameService.addGame(game);
            assertThrows(InstanceNotFoundException.class, () -> gameService.findGameById(invalidId));
        }finally {
            if(addedGame != null){
                removeGame(addedGame.getGameId());
            }
        }
    }

    @Test
    public void testTicketAlreadyDelivered() throws InputValidationException, GameCelebratedException, InstanceNotFoundException, NoTicketsException {
        Game game = getValidGame();
        Game createdGame = gameService.addGame(game);
        Sale createdSale = gameService.buy(createdGame.getGameId(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
        Game g = gameService.findGameById(createdGame.getGameId());
        try {
            // Recoger los tickets por primera vez
            gameService.pickUpTickets(createdSale.getSaleId(), VALID_CREDIT_CARD_NUMBER);
            //Volvemos a intentar recogerlos
            assertThrows(TicketsAlreadyDeliveredException.class, () -> gameService.pickUpTickets(createdSale.getSaleId(), VALID_CREDIT_CARD_NUMBER));
        } catch (InvalidCreditCardException | TicketsAlreadyDeliveredException e) {
            throw new RuntimeException(e);
        } finally {
            if (createdSale != null) {
                removeSale(createdSale.getSaleId());
            }
            removeGame(createdGame.getGameId());
        }
    }

    @Test
    public void testCantBuyMoreTickets() throws InstanceNotFoundException, InputValidationException, NoTicketsException, GameCelebratedException {
        Game game = getValidGame();
        Game addedGame = null;
        try {
            addedGame = gameService.addGame(game);
            Game finalAddedGame = addedGame;
            assertThrows(NoTicketsException.class, () ->
                gameService.buy(finalAddedGame.getGameId(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 1000)
            );
        } finally {
            if (addedGame != null) {
                removeGame(addedGame.getGameId());
            }
        }
    }

    @Test
    public void testCantBuyTicketsForNotFoundGame() {
        // Definir un gameId que no existe
         Game game = new Game(NON_EXISTENT_GAME_ID, "Juego no existente", 10.0, 100, dateTime4);
        // Asegurarse de que arroje una InstanceNotFoundException
        assertThrows(InstanceNotFoundException.class, () -> {
            gameService.buy(game.getGameId(), VALID_EMAIL, VALID_CREDIT_CARD_NUMBER, 1);
        });
    }
}
