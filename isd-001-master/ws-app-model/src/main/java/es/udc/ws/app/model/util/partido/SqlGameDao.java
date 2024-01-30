package es.udc.ws.app.model.util.partido;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlGameDao {
    public Game update(Connection connection, Game game) throws InstanceNotFoundException;
    public Game findById(Connection connection, Long id) throws InstanceNotFoundException;

    public List<Game> findBetweenDates(Connection connection, LocalDateTime ini, LocalDateTime fin);

    public Game create(Connection connection, Game game);

    public void remove(Connection connection, Long gameId) throws InstanceNotFoundException;
}
