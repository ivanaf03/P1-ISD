package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestSaleDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestSaleDto {
    public static ObjectNode toObjectNode(RestSaleDto sale){
        ObjectNode saleNode = JsonNodeFactory.instance.objectNode();

        saleNode.put("saleId", sale.getSaleId()).
                put("gameId", sale.getGameId()).
                put("userEmail", sale.getUserEmail()).
                put("cardNum", sale.getCardNum().substring(sale.getCardNum().length()-4)).
                put("isTakedTickets", sale.isTakedTickets()).
                put("dateTimeSale", sale.getDateTimeSale().toString()).
                put("ticketNum", sale.getTicketNum()).
                put("price", sale.getPrice());
        return saleNode;
    }

    public static RestSaleDto toRestSaleDto(InputStream jsonSale) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode saleObject = (ObjectNode) rootNode;

                JsonNode saleIdNode = saleObject.get("saleId");
                Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;

                Long gameId = saleObject.get("gameId").longValue();
                String userEmail = saleObject.get("userEmail").textValue().trim();
                String cardNum =  saleObject.get("cardNum").textValue().trim();
                boolean takedTickets = saleObject.get("takedTickets").booleanValue();
                LocalDateTime dateTimeSale = LocalDateTime.parse(saleObject.get("dateTimeSale").asText());
                int ticketNum = saleObject.get("ticketNum").intValue();
                double price =  saleObject.get("price").doubleValue();
                return new RestSaleDto(saleId,gameId, userEmail, cardNum, takedTickets, dateTimeSale, ticketNum, price);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ArrayNode toArrayNode(List<RestSaleDto> sales) {

        ArrayNode salesNode = JsonNodeFactory.instance.arrayNode();
        for (RestSaleDto saleDto : sales) {
            ObjectNode gameObject = toObjectNode(saleDto);
            salesNode.add(gameObject);
        }
        return salesNode;
    }
}
