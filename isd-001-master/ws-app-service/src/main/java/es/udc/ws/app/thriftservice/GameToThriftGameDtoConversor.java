package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.thrift.ThriftGameDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameToThriftGameDtoConversor {
    public static Game toGame(ThriftGameDto game) {
        return new Game(game.getGameId(), game.getVisit(), game.getPrice(), game.getAvailableTickets(), LocalDateTime.parse(game.getDateTimeCelebration()));
    }

    public static List<ThriftGameDto> toThriftGameDtos(List<Game> games){

        List<ThriftGameDto> dtos = new ArrayList<>(games.size());

        for (Game game : games) {
            dtos.add(toThriftGameDto(game));
        }
        return dtos;

    }

    public static ThriftGameDto toThriftGameDto(Game game) {

        return new ThriftGameDto(game.getGameId(), game.getDateTimeCelebration().toString(),
                game.getVisit(), game.getPrice(),
                game.getAvailableTickets(), game.getSoldTickets());

    }
}


