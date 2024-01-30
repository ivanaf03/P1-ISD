package es.udc.ws.app.model.util.compra;

import es.udc.ws.app.model.util.partido.Game;

import java.sql.*;

public class Jdbc3CcSqlSaleDao extends AbstractSqlSale {

    @Override
    public Sale create(Connection connection, Sale sale) {
        String queryString = "INSERT INTO salegame"
                + " (gameId, userEmail, cardNum, price, takedTickets, dateTimeSale, ticketNum)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            preparedStatement.setLong(i++, sale.getGameId());
            preparedStatement.setString(i++, sale.getUserEmail());
            preparedStatement.setString(i++, sale.getCardNum());
            preparedStatement.setDouble(i++, sale.getPrice());
            preparedStatement.setBoolean(i++, sale.isTakedTickets());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(sale.getDateTimeSale()));
            preparedStatement.setInt(i++, sale.getTicketNum());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long saleId = resultSet.getLong(1);
            return new Sale(saleId, sale.getGameId(), sale.getUserEmail(), sale.getCardNum(), sale.getPrice(), sale.isTakedTickets(), sale.getDateTimeSale(), sale.getTicketNum());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
