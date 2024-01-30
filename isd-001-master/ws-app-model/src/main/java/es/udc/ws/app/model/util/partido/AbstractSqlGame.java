package es.udc.ws.app.model.util.partido;

import java.sql.*;
import java.time.LocalDateTime; // Cambio en la importación
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlGame implements SqlGameDao {

    @Override
    public Game findById(Connection connection, Long gameId)
            throws InstanceNotFoundException {
        String queryString = "SELECT dateTimeEntry, dateTimeCelebration,visit, availableTickets, price, soldTickets FROM Game WHERE gameId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, gameId.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new InstanceNotFoundException(gameId, Game.class.getName());
            }
            LocalDateTime dateTimeEntry = resultSet.getTimestamp("dateTimeEntry").toLocalDateTime();
            LocalDateTime dateTimeCelebration = resultSet.getTimestamp("dateTimeCelebration").toLocalDateTime();
            String visit = resultSet.getString("visit");
            double price = resultSet.getDouble("price");
            int availableTickets = resultSet.getInt("availableTickets");
            int soldTickets = resultSet.getInt("soldTickets");
            return new Game(gameId, dateTimeEntry, dateTimeCelebration, visit, price, availableTickets, soldTickets);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Game> findBetweenDates(Connection connection, LocalDateTime ini, LocalDateTime fin) {
        String queryString = "SELECT * FROM Game WHERE dateTimeCelebration BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(ini));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(fin));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Game> gameList = new ArrayList<>();
            while (resultSet.next()) {
                LocalDateTime dateTimeCelebrationEntry = resultSet.getTimestamp("dateTimeCelebration").toLocalDateTime();
                LocalDateTime dateTimeEntryEntry = resultSet.getTimestamp("dateTimeEntry").toLocalDateTime();
                Long gameIdEntry = resultSet.getLong("gameId");
                String visitEntry = resultSet.getString("visit");
                double priceEntry = resultSet.getDouble("price");
                int availableTicketsEntry = resultSet.getInt("availableTickets");
                int soldTicketsEntry = resultSet.getInt("soldTickets");
                Game gameEntry = new Game(gameIdEntry, dateTimeEntryEntry, dateTimeCelebrationEntry, visitEntry, priceEntry, availableTicketsEntry, soldTicketsEntry);
                gameList.add(gameEntry);
            }
            return gameList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long gameId)
            throws InstanceNotFoundException {
        String queryString = "DELETE FROM game WHERE" + " gameId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, gameId);
            int removedRows = preparedStatement.executeUpdate();
            if (removedRows == 0) {
                throw new InstanceNotFoundException(gameId,
                        Game.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Game update(Connection connection, Game game) throws InstanceNotFoundException {
        String queryString = "UPDATE game" + " SET availableTickets = ?, soldTickets = ?" + " WHERE gameId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setInt(i++,game.getAvailableTickets());
            preparedStatement.setInt(i++, game.getSoldTickets());
            preparedStatement.setLong(i++, game.getGameId());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new InstanceNotFoundException(game, Game.class.getName());
            }
            return findById(connection, game.getGameId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}