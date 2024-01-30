package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.util.compra.Sale;
import es.udc.ws.app.thrift.ThriftSaleDto;

import java.util.ArrayList;
import java.util.List;

public class SaleToThriftSaleDtoConversor {
    public static ThriftSaleDto toThriftSaleDto(Sale sale) {

        return new ThriftSaleDto(sale.getSaleId(), sale.getGameId(), sale.getUserEmail(), sale.getCardNum(),
                sale.isTakedTickets(), sale.getDateTimeSale().toString(), sale.getTicketNum(), sale.getPrice());

    }

    public static List<ThriftSaleDto> toThriftSaleDtos(List<Sale> sales) {

        List<ThriftSaleDto> dtos = new ArrayList<>(sales.size());

        for (Sale sale: sales){
            dtos.add(toThriftSaleDto(sale));
        }

        return dtos;

    }
}
