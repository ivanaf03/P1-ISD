package es.udc.ws.app.restservice.dto;
import es.udc.ws.app.model.util.partido.Game;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class GameToRestGameDtoConversor {
    public static List<RestGameDto> toRestGameDtos(List<Game> games) {
        List<RestGameDto> gameDtos = new ArrayList<>(games.size());
        for (Game game : games) {
            gameDtos.add(toRestGameDto(game));
        }
        return gameDtos;
    }

    public static RestGameDto toRestGameDto(Game game) {
        return new RestGameDto(game.getGameId(), game.getDateTimeCelebration().toString(), game.getVisit(), game.getPrice(),
                game.getAvailableTickets(), game.getSoldTickets());
    }

    public static Game toGame(RestGameDto game) {
        return new Game(game.getGameId(), game.getVisit(), game.getPrice(), game.getAvailableTickets(), LocalDateTime.parse(game.getDateTimeCelebration()));
    }

}
