package es.udc.ws.app.model.util.service.exceptions;

public class GameCelebratedException extends Throwable {
    private Long gameId;

    public GameCelebratedException(Long gameId) {
        super("The game " + gameId + " has been celebrated.");
        this.gameId = gameId;
    }

    public Long getSaleId() {
        return gameId;
    }

    public void setSaleId(Long gameId) {
        this.gameId = gameId;
    }
}
