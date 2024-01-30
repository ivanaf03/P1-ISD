package es.udc.ws.app.restservice.dto;

public class RestGameDto {
        private Long gameId;
        private String dateTimeCelebration;
        private String visit;
        private double price;
        private int availableTickets;
        private int soldTickets;

    public RestGameDto(Long gameId, String dateTimeCelebration, String visit, double price, int availableTickets, int soldTickets) {
        this.gameId = gameId;
        this.dateTimeCelebration = dateTimeCelebration;
        this.visit = visit;
        this.price = price;
        this.availableTickets = availableTickets;
        this.soldTickets = soldTickets;
    }

    public RestGameDto() {
    }

    @Override
    public String toString() {
        return "RestGameDto{" +
                "gameId=" + gameId +
                ", dateTimeCelebration=" + dateTimeCelebration +
                ", visit='" + visit + '\'' +
                ", price=" + price +
                ", availableTickets=" + availableTickets +
                ", soldTickets=" + soldTickets +
                '}';
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setDateTimeCelebration(String dateTimeCelebration) {
        this.dateTimeCelebration = dateTimeCelebration;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets += soldTickets;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getDateTimeCelebration() {
        return dateTimeCelebration;
    }

    public String getVisit() {
        return visit;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }
}

