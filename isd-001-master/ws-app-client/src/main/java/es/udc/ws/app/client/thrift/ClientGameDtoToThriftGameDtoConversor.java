package es.udc.ws.app.client.thrift;

import es.udc.ws.app.client.dto.ClientGameDto;
import es.udc.ws.app.thrift.ThriftGameDto;

import java.util.ArrayList;
import java.util.List;

public class ClientGameDtoToThriftGameDtoConversor {

    public static ThriftGameDto toThriftGameDto(
            ClientGameDto clientGameDto) {

        Long gameId = clientGameDto.getGameId();

        return new ThriftGameDto(
                gameId == null ? -1 : gameId.longValue(),
                clientGameDto.getDateTimeCelebration(), clientGameDto.getVisit(),
                clientGameDto.getPrice(), clientGameDto.getAvailableTickets(),0);

    }

    public static List<ClientGameDto> toClientGameDtos(List<ThriftGameDto> games) {

        List<ClientGameDto> clientGameDtos = new ArrayList<>(games.size());

        for (ThriftGameDto game : games) {
            clientGameDtos.add(toClientGameDto(game));
        }
        return clientGameDtos;

    }

    public static ClientGameDto toClientGameDto(ThriftGameDto game) {

        return new ClientGameDto(
                game.getGameId(), game.getDateTimeCelebration(), game.getVisit(),
                game.getPrice(), game.getAvailableTickets());

    }

}
