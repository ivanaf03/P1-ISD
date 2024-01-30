package es.udc.ws.app.model.util.service.exceptions;

public class NoTicketsException extends Throwable {
    private Long gameId;

    public NoTicketsException(Long gameId) {
        super("There are no more tickets available for the match" + gameId + ".");
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
