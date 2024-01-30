package es.udc.ws.app.client.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.ClientGameService;
import es.udc.ws.app.client.dto.ClientGameDto;
import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.app.client.exceptions.ClientGameCelebratedException;
import es.udc.ws.app.client.exceptions.ClientInvalidCreditCardException;
import es.udc.ws.app.client.exceptions.ClientNoTicketsException;
import es.udc.ws.app.client.exceptions.ClientTicketsAlreadyDeliveredException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.client.rest.json.*;

public class RestClientGameService implements ClientGameService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientGameService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addGame(ClientGameDto game) throws InputValidationException {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "games").
                    bodyStream(toInputStream(game), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientGameDtoConversor.toClientGameDto(response.getEntity().getContent()).getGameId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientGameDto> findBetweenDates(String fin) throws InputValidationException{
        try{
            String fechaCodificada = URLEncoder.encode(fin, StandardCharsets.UTF_8);
            fechaCodificada = fechaCodificada.replace("%3A", ":");
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "games?date=" +
                    fechaCodificada).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientGameDtoConversor.toClientGameDtos(response.getEntity().getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientGameDto findGameById(Long gameId) throws InstanceNotFoundException{
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "games?gameId=" +
                    URLEncoder.encode(Long.toString(gameId), StandardCharsets.UTF_8)).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK,response);
            return JsonToClientGameDtoConversor.toClientGameDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long buy(Long gameId, String userEmail, String cardNumber, int ticketsNum)
            throws InstanceNotFoundException, InputValidationException, ClientNoTicketsException, ClientGameCelebratedException {
        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "sales").
                    bodyForm(
                            Form.form().
                                    add("gameId", Long.toString(gameId)).
                                    add("userEmail", userEmail).
                                    add("cardNum", cardNumber).
                                    add("ticketNum", Integer.toString(ticketsNum)).
                                    add("dateTimeSale", LocalDateTime.now().toString()).
                                    add("price", Double.toString(findGameById(gameId).getPrice())).
                                    build()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientSaleDtoConversor.toClientSaleDto(response.getEntity().getContent()).getSaleId();
        } catch (ClientGameCelebratedException | InstanceNotFoundException | InputValidationException | ClientNoTicketsException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientSaleDto> findSale(String userEmail) throws InstanceNotFoundException, InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "sales?userEmail=" +
                    URLEncoder.encode(userEmail, StandardCharsets.UTF_8)).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);
            
            return JsonToClientSaleDtoConversor.toClientSalesDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException | InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientSaleDto pickUpTickets(String saleId, String cardNumber) throws InstanceNotFoundException, ClientInvalidCreditCardException, ClientTicketsAlreadyDeliveredException, InputValidationException {

        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "sales/" +
                    URLEncoder.encode(saleId, StandardCharsets.UTF_8)).
                    bodyForm(
                            Form.form().
                                    add("cardNum", cardNumber).
                                    build()).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientSaleDtoConversor.toClientSaleDto(response.getEntity().getContent());

        } catch (InstanceNotFoundException | ClientInvalidCreditCardException | InputValidationException | ClientTicketsAlreadyDeliveredException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientGameDto game) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientGameDtoConversor.toObjectNode(game));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_CONFLICT -> throw JsonToClientExceptionConversor.fromConflictErrorCode(response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
