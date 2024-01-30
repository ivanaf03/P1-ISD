package es.udc.ws.app.model.util.compra;

import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlSaleDao {
    public Sale update(Connection connection, Sale sale) throws InstanceNotFoundException;

    public Sale findBySaleId(Connection connection, Long saleId) throws InstanceNotFoundException;

    public List<Sale> findSale(Connection connection, String userEmail);

    public Sale create(Connection connection, Sale sale);

    public void remove(Connection connection, Long saleId) throws InstanceNotFoundException;
}
