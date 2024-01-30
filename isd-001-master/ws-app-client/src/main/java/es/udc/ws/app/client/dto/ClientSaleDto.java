package es.udc.ws.app.client.dto;


public class ClientSaleDto {
    private Long saleId;
    private Long gameId;
    private String userEmail;
    private String cardNum;
    private boolean takedTickets;
    private String dateTimeSale;
    private int ticketNum;
    private double price;

    public ClientSaleDto() {
    }

    public ClientSaleDto(Long saleId, Long gameId, String userEmail, String cardNum, boolean takedTickets, String dateTimeSale, int ticketNum, double price) {
        this.saleId = saleId;
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.cardNum = cardNum;
        this.takedTickets = takedTickets;
        this.dateTimeSale = dateTimeSale;
        this.ticketNum = ticketNum;
        this.price = price;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public boolean isTakedTickets() {
        return takedTickets;
    }

    public void setTakedTickets(boolean takedTickets) {
        this.takedTickets = takedTickets;
    }

    public String getDateTimeSale() {
        return dateTimeSale;
    }

    public void setDateTimeSale(String dateTimeSale) {
        this.dateTimeSale = dateTimeSale;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
