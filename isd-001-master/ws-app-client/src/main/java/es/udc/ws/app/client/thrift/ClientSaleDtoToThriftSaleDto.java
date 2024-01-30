package es.udc.ws.app.client.thrift;


import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.app.thrift.ThriftSaleDto;

import java.util.ArrayList;
import java.util.List;

public class ClientSaleDtoToThriftSaleDto {
    public static ClientSaleDto toClientSaleDto(ThriftSaleDto sale) {
        return new ClientSaleDto(
                sale.getSaleId(),
                sale.getGameId(),
                sale.getUserEmail(),
                sale.getCardNum(),
                sale.isTakedTickets(),
                sale.getDateTimeSale(),
                sale.getTicketNum(),
                sale.getPrice()
        );
    }

    public static ThriftSaleDto toThriftSaleDto(ClientSaleDto clientSaleDto){
        return new ThriftSaleDto(
                clientSaleDto.getSaleId(),
                clientSaleDto.getGameId(),
                clientSaleDto.getUserEmail(),
                clientSaleDto.getCardNum(),
                clientSaleDto.isTakedTickets(),
                clientSaleDto.getDateTimeSale(),
                clientSaleDto.getTicketNum(),
                clientSaleDto.getPrice()
        );
    }

    public static List<ClientSaleDto> toClientSaleDtos(List<ThriftSaleDto> thriftList) {
        List<ClientSaleDto> saleList = new ArrayList<>();
        for (ThriftSaleDto sale : thriftList){
            saleList.add(toClientSaleDto(sale));
        }
        return saleList;
    }




}
