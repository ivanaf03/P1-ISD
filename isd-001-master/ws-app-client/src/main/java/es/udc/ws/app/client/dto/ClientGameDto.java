package es.udc.ws.app.client.dto;

public class ClientGameDto {
    private Long gameId;
    private String dateTimeCelebration;
    private String visit;
    private double price;
    private int availableTickets;

    public ClientGameDto(Long gameId, String visit,  String dateTimeCelebration,double price, int availableTickets) {
        this.gameId = gameId;
        this.dateTimeCelebration = dateTimeCelebration;
        this.visit = visit;
        this.price = price;
        this.availableTickets = availableTickets;
    }

    public ClientGameDto() {
    }

    @Override
    public String toString() {
        return "ClientGameDto{" +
                "gameId=" + gameId +
                ", dateTimeCelebration='" + dateTimeCelebration + '\'' +
                ", visit='" + visit + '\'' +
                ", price=" + price +
                ", availableTickets=" + availableTickets +
                '}';
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getDateTimeCelebration() {
        return dateTimeCelebration;
    }

    public void setDateTimeCelebration(String dateTimeCelebration) {
        this.dateTimeCelebration = dateTimeCelebration;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }
}
