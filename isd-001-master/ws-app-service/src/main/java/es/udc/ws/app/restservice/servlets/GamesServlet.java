package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.util.partido.Game;
import es.udc.ws.app.model.util.service.ServiceFactory;
import es.udc.ws.app.restservice.dto.GameToRestGameDtoConversor;
import es.udc.ws.app.restservice.dto.RestGameDto;
import es.udc.ws.app.restservice.json.JsonToRestGameDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestGameDto gameDto = JsonToRestGameDto.toRestGameDto(req.getInputStream());
        Game game = GameToRestGameDtoConversor.toGame(gameDto);

        game = ServiceFactory.getService().addGame(game);

        gameDto = GameToRestGameDtoConversor.toRestGameDto(game);
        String gameURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + game.getGameId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", gameURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestGameDto.toObjectNode(gameDto),
                headers);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String gameIdParam = req.getParameter("gameId");
        String dateParam = req.getParameter("date");

        if (gameIdParam != null) {

            Long gameId = Long.parseLong(gameIdParam);
            Game game = ServiceFactory.getService().findGameById(gameId);

            if (game != null) {
                RestGameDto gameDto = GameToRestGameDtoConversor.toRestGameDto(game);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestGameDto.toObjectNode(gameDto), null);
            } else {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        null, null);
            }
        } else if (dateParam != null) {
            LocalDateTime dateKeyword = LocalDateTime.parse(dateParam, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            List<Game> games = ServiceFactory.getService().findBetweenDates(LocalDateTime.now(), dateKeyword);
            List<RestGameDto> gameDtos = GameToRestGameDtoConversor.toRestGameDtos(games);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestGameDto.toArrayNode(gameDtos),
                    null);
        } else {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    null, null);
        }
    }

}
