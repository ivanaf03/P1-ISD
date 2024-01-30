package es.udc.ws.app.model.util.compra;

import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlSale implements SqlSaleDao {
    protected AbstractSqlSale() {
    }

    public Sale update(Connection connection, Sale sale) throws InstanceNotFoundException {
        String queryString = "UPDATE salegame SET takedTickets = ? WHERE saleId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setBoolean(1, true); // Marcar las entradas como recogidas
            preparedStatement.setLong(2, sale.getSaleId());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new InstanceNotFoundException(sale, Sale.class.getName());
            }
            return findBySaleId(connection, sale.getSaleId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Sale findBySaleId(Connection connection, Long saleId) throws InstanceNotFoundException {
        String queryString = "SELECT * FROM salegame WHERE saleId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, saleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long gameId = resultSet.getLong("gameId");
                String userEmail = resultSet.getString("userEmail");
                String cardNum = resultSet.getString("cardNum");
                boolean takedTickets = resultSet.getBoolean("takedTickets");
                LocalDateTime dateTimeSale = resultSet.getTimestamp("dateTimeSale").toLocalDateTime();
                int ticketNum = resultSet.getInt("ticketNum");
                double price = resultSet.getDouble("price");
                return new Sale(saleId, gameId, userEmail, cardNum, price, takedTickets, dateTimeSale, ticketNum);
            } else {
                throw new InstanceNotFoundException(saleId, Sale.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Sale> findSale(Connection connection, String userEmail){
        String queryString = "SELECT * FROM salegame WHERE userEmail = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Sale> saleList = new ArrayList<>();
            while (resultSet.next()) {
                Long saleIdEntry = resultSet.getLong("saleId");
                Long gameIdEntry = resultSet.getLong("gameId");
                String userEmailEntry = resultSet.getString("userEmail");
                String cardNumEntry = resultSet.getString("cardNum");
                boolean takedTicketsEntry = resultSet.getBoolean("takedTickets");
                LocalDateTime dateTimeSaleEntry = resultSet.getTimestamp("dateTimeSale").toLocalDateTime();
                int ticketNumEntry = resultSet.getInt("ticketNum");
                Double price = resultSet.getDouble("price");
                Sale saleEntry = new Sale(saleIdEntry, gameIdEntry, userEmailEntry, cardNumEntry, price, takedTicketsEntry, dateTimeSaleEntry, ticketNumEntry);
                saleList.add(saleEntry);
            }
            return saleList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long saleId)
            throws InstanceNotFoundException {
        String queryString = "DELETE FROM salegame WHERE" + " saleId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setLong(i++, saleId);
            int removedRows = preparedStatement.executeUpdate();
            if (removedRows == 0) {
                throw new InstanceNotFoundException(saleId,
                        Sale.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

