package es.udc.ws.app.client.thrift;

import es.udc.ws.app.client.ClientGameService;
import es.udc.ws.app.client.dto.ClientGameDto;
import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.app.client.exceptions.ClientGameCelebratedException;
import es.udc.ws.app.client.exceptions.ClientInvalidCreditCardException;
import es.udc.ws.app.client.exceptions.ClientNoTicketsException;
import es.udc.ws.app.client.exceptions.ClientTicketsAlreadyDeliveredException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientGameService implements ClientGameService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientGameService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
    private ThriftGameService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftGameService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public Long addGame(ClientGameDto game) throws InputValidationException {

        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.addGame(ClientGameDtoToThriftGameDtoConversor.toThriftGameDto(game)).getGameId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientGameDto> findBetweenDates(String fin) throws InputValidationException{
        ThriftGameService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()){

            transport.open();

            return ClientGameDtoToThriftGameDtoConversor.toClientGameDtos(client.findBetweenDates(fin));

        } catch (ThriftInputValidationException e){
            throw new InputValidationException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public ClientGameDto findGameById(Long gameId) throws InstanceNotFoundException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            ThriftGameDto game = client.findById(gameId);
            return ClientGameDtoToThriftGameDtoConversor.toClientGameDto(game);
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Long buy(Long gameId, String userEmail, String cardNumber, int ticketsNum) throws InstanceNotFoundException, InputValidationException, ClientNoTicketsException, ClientGameCelebratedException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.buy(gameId, userEmail, cardNumber, ticketsNum).getSaleId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftNoTicketsException e) {
            throw new ClientNoTicketsException();
        } catch (ThriftGameCelebratedException e) {
            throw new ClientGameCelebratedException();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientSaleDto> findSale(String userEmail) throws InstanceNotFoundException, InputValidationException {
        ThriftGameService.Client client = getClient();

        try(TTransport transport = client.getInputProtocol().getTransport()){

            transport.open();

            return ClientSaleDtoToThriftSaleDto.toClientSaleDtos(client.findSale(userEmail));


        } catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftInputValidationException ee) {
            throw new InputValidationException(ee.getMessage());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientSaleDto pickUpTickets(String saleId, String cardNumber) throws InstanceNotFoundException, ClientInvalidCreditCardException, ClientTicketsAlreadyDeliveredException, InputValidationException {
        ThriftGameService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();
            return ClientSaleDtoToThriftSaleDto.toClientSaleDto(client.pickUpTickets(Long.parseLong(saleId), cardNumber));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch(ThriftInvalidCreditCardException e){
            throw new ClientInvalidCreditCardException();
        }catch(ThriftTicketsAlreadyDeliveredException e){
            throw new ClientTicketsAlreadyDeliveredException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


