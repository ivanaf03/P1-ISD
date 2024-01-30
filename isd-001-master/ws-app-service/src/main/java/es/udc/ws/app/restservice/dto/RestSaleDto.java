package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
public class RestSaleDto {
    private Long saleId;
    private Long gameId;
    private String userEmail;
    private String cardNum;
    private boolean takedTickets;
    private LocalDateTime dateTimeSale;
    private int ticketNum;
    private double price;

    public RestSaleDto(Long saleId, Long gameId, String userEmail, String cardNum, boolean takedTickets, LocalDateTime dateTimeSale, int ticketNum, double price) {
        this.saleId = saleId;
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.cardNum = cardNum.substring(12);
        this.takedTickets = takedTickets;
        this.dateTimeSale = dateTimeSale;
        this.ticketNum = ticketNum;
        this.price = price;
    }

    public RestSaleDto() {
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

    public LocalDateTime getDateTimeSale() {
        return dateTimeSale;
    }

    public void setDateTimeSale(LocalDateTime dateTimeSale) {
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

    @Override
    public String toString() {
        return "RestSaleDto{" +
                "saleId=" + saleId +
                ", gameId=" + gameId +
                ", userEmail='" + userEmail + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", takedTickets=" + takedTickets +
                ", dateTimeSale=" + dateTimeSale +
                ", ticketNum=" + ticketNum +
                ", price=" + price +
                '}';
    }

}
