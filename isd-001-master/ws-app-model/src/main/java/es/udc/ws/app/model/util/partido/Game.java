package es.udc.ws.app.model.util.partido;

import java.time.LocalDateTime;
import java.util.Objects;

public class Game {
    private Long gameId;
    private LocalDateTime dateTimeEntry;
    private LocalDateTime dateTimeCelebration;
    private String visit;
    private double price;
    private int availableTickets;
    private int soldTickets;

    public Game(String visit, double price, int availableTickets, LocalDateTime dateTimeCelebration) {
        this.visit = visit;
        this.price = price;
        this.availableTickets = availableTickets;
        this.soldTickets = 0;
        this.dateTimeCelebration = dateTimeCelebration;
    }

    public Game(Long gameId, String visit, double price, int availableTickets, LocalDateTime dateTimeCelebration) {
        this(visit, price, availableTickets, dateTimeCelebration);
        this.gameId = gameId;
    }

    public Game(Long gameId, LocalDateTime dateTimeEntry, LocalDateTime dateTimeCelebration, String visit, double price, int availableTickets, int soldTickets) {
        this(gameId, visit, price, availableTickets, dateTimeCelebration);
        this.soldTickets = soldTickets;
        this.dateTimeEntry = (dateTimeEntry != null) ? dateTimeEntry.withNano(0) : null;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getDateTimeEntry() {
        return dateTimeEntry;
    }

    public void setDateTimeEntry(LocalDateTime dateTimeEntry) {
        this.dateTimeEntry = (dateTimeEntry != null) ? dateTimeEntry.withNano(0) : null;
    }

    public LocalDateTime getDateTimeCelebration() {
        return dateTimeCelebration;
    }

    public void setDateTimeCelebration(LocalDateTime dateTimeCelebration) {
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

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets += soldTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Double.compare(price, game.price) == 0 && Objects.equals(gameId, game.gameId) && Objects.equals(dateTimeEntry, game.dateTimeEntry) && Objects.equals(dateTimeCelebration, game.dateTimeCelebration) && Objects.equals(visit, game.visit) && Objects.equals(availableTickets, game.availableTickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, dateTimeEntry, dateTimeCelebration, visit, price, availableTickets);
    }
}
