package es.udc.ws.app.model.util.partido;

import es.udc.ws.app.model.util.compra.Jdbc3CcSqlSaleDao;

import java.sql.*;
import java.time.LocalDateTime;

public class Jdbc3CcSqlGameDao extends AbstractSqlGame {
    @Override
    public Game create(Connection connection, Game game) {
        String queryString = "INSERT INTO Game" + " (visit, price, dateTimeEntry, dateTimeCelebration, availableTickets, soldTickets)" + " VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setString(i++, game.getVisit());
            preparedStatement.setDouble(i++, game.getPrice());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(game.getDateTimeEntry()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(game.getDateTimeCelebration()));
            preparedStatement.setInt(i++, game.getAvailableTickets());
            preparedStatement.setInt(i++, game.getSoldTickets());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new SQLException("JDBC driver did not return generated key.");
            }
            Long gameId = resultSet.getLong(1);
            return new Game(gameId, game.getDateTimeEntry(), game.getDateTimeCelebration(), game.getVisit(), game.getPrice(), game.getAvailableTickets(), game.getSoldTickets());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
