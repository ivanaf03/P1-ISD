package es.udc.ws.app.client.rest.json;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientSaleDtoConversor {
    public static ClientSaleDto toClientSaleDto(InputStream jsonSale) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode saleObject = (ObjectNode) rootNode;

                return parseSaleNode(saleObject);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientSaleDto> toClientSalesDto(InputStream jsonSale) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode salesArray = (ArrayNode) rootNode;
                List<ClientSaleDto> clientSalesList = new ArrayList<>();

                for (JsonNode saleNode : salesArray){
                    ClientSaleDto clientSaleDto = parseSaleNode(saleNode);
                    clientSalesList.add(clientSaleDto);
                }
                return clientSalesList;

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientSaleDto parseSaleNode(JsonNode saleNode) throws ParsingException {
        try {
            // Verificar si el nodo 'saleId' existe y no es nulo
            Long saleId = (saleNode.has("saleId") && !saleNode.get("saleId").isNull())
                    ? saleNode.get("saleId").longValue()
                    : null;

            // Verificar si el nodo 'gameId' existe y no es nulo
            Long gameId = (saleNode.has("gameId") && !saleNode.get("gameId").isNull())
                    ? saleNode.get("gameId").longValue()
                    : null;

            // Verificar si el nodo 'userEmail' existe y no es nulo
            String userEmail = (saleNode.has("userEmail") && !saleNode.get("userEmail").isNull())
                    ? saleNode.get("userEmail").textValue().trim()
                    : null;

            // Verificar si el nodo 'cardNum' existe y no es nulo
            String cardNum = (saleNode.has("cardNum") && !saleNode.get("cardNum").isNull())
                    ? saleNode.get("cardNum").textValue().trim()
                    : null;

            // Verificar si el nodo 'takedTickets' existe y no es nulo
            boolean takedTickets = saleNode.has("takedTickets") && saleNode.get("takedTickets").booleanValue();

            // Verificar si el nodo 'dateTimeSale' existe y no es nulo
            String dateTimeSale = (saleNode.has("dateTimeSale") && !saleNode.get("dateTimeSale").isNull())
                    ? saleNode.get("dateTimeSale").textValue().trim()
                    : null;

            // Verificar si el nodo 'ticketNum' existe y no es nulo
            int ticketNumber = (saleNode.has("ticketNum") && !saleNode.get("ticketNum").isNull())
                    ? saleNode.get("ticketNum").intValue()
                    : 0;

            // Verificar si el nodo 'price' existe y no es nulo
            double price = (saleNode.has("price") && !saleNode.get("price").isNull())
                    ? saleNode.get("price").doubleValue()
                    : 0.0;

            // Crear y devolver el objeto ClientSaleDto
            return new ClientSaleDto(saleId, gameId, userEmail, cardNum, takedTickets, dateTimeSale, ticketNumber, price);
        } catch (Exception e) {
            throw new ParsingException("Error parsing sale node", e);
        }
    }

}
