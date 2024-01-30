package es.udc.ws.app.restservice.dto;


import es.udc.ws.app.model.util.compra.Sale;

public class SaleToRestSaleDtoConversor {

    public static RestSaleDto toRestSaleDto(Sale sale) {
        return new RestSaleDto(sale.getSaleId(), sale.getGameId(), sale.getUserEmail(), sale.getCardNum(), sale.isTakedTickets(),sale.getDateTimeSale(), sale.getTicketNum(), sale.getPrice());
    }
    public static Sale toSale(RestSaleDto sale) {
        return new Sale(sale.getSaleId(), sale.getGameId(), sale.getUserEmail(), sale.getCardNum(), sale.getPrice(), sale.isTakedTickets(),
                sale.getDateTimeSale(), sale.getTicketNum());
    }
}