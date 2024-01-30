package es.udc.ws.app.model.util.compra;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sale {
    private Long saleId;
    private Long gameId;
    private String userEmail;
    private String cardNum;
    private boolean takedTickets; //Nos indica si se recogieron las entradas o no
    private LocalDateTime dateTimeSale; //fecha y hora de la compra, se debe validar que sea hasta la fecha de inicio del partido
    private int ticketNum;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Sale(Long gameId, String userEmail, String cardNum, double price, boolean takedTickets, LocalDateTime dateTimeSale, int ticketNum) {
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.cardNum = cardNum;
        this.takedTickets = takedTickets;
        this.ticketNum = ticketNum;
        this.price = price;
        this.dateTimeSale = (dateTimeSale != null) ? dateTimeSale.withNano(0) : null;
    }

    public Sale(Long saleId, Long gameId, String userEmail, String cardNum, double price, boolean takedTickets, LocalDateTime dateTimeSale, int ticketNum) {
        this(gameId, userEmail, cardNum, price, takedTickets, dateTimeSale, ticketNum);
        this.saleId = saleId;
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

    public void setUserEmail(Long idUsuario) {
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
        this.dateTimeSale = (dateTimeSale != null) ? dateTimeSale.withNano(0) : null;
    }

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return takedTickets == sale.takedTickets && Double.compare(price, sale.price) == 0 && Objects.equals(saleId, sale.saleId) && Objects.equals(gameId, sale.gameId) && Objects.equals(userEmail, sale.userEmail) && Objects.equals(cardNum, sale.cardNum) && Objects.equals(dateTimeSale, sale.dateTimeSale) && Objects.equals(ticketNum, sale.ticketNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, gameId, userEmail, cardNum, takedTickets, dateTimeSale, ticketNum, price);
    }
}
