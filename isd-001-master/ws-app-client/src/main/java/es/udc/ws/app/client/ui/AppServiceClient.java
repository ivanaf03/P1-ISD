package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.ClientGameService;
import es.udc.ws.app.client.ClientGameServiceFactory;
import es.udc.ws.app.client.dto.ClientGameDto;
import es.udc.ws.app.client.dto.ClientSaleDto;
import es.udc.ws.app.client.exceptions.ClientGameCelebratedException;
import es.udc.ws.app.client.exceptions.ClientInvalidCreditCardException;
import es.udc.ws.app.client.exceptions.ClientNoTicketsException;
import es.udc.ws.app.client.exceptions.ClientTicketsAlreadyDeliveredException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AppServiceClient {
    public static void main(String[] args) throws InputValidationException {
        if(args.length == 0){
            printUsage();
        }

        ClientGameService clientGameService = ClientGameServiceFactory.getService();

        if("-addMatch".equalsIgnoreCase(args[0])){
            //[addMatch]     ServiceClient -addMatch <visitor> <celebrationdate> <price> <maxTickets>
            validateArgs(args, 5, new int[] {3,4});

            try{
                Long gameId = clientGameService.addGame(new ClientGameDto(null, args[1], args[2],
                        Double.parseDouble(args[3]), Integer.parseInt(args[4])));
                System.out.println("Game " + gameId + " creado correctamente!\n");
            }catch (InputValidationException e) {
                e.printStackTrace(System.err);
            }

        } else if ("-findMatches".equalsIgnoreCase(args[0])){
            //[findMatches]  ServiceClient -findMatches <untilDay>
            validateArgs(args, 2, new int[] {});

            try{
                args[1]=args[1]+"T00:00";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
                LocalDateTime date = LocalDateTime.parse(args[1], formatter);


                List<ClientGameDto> games = clientGameService.findBetweenDates(date.toString());
                System.out.println("Found " + games.size() + "game(s) between now and '" + args[1]+"'");
                for (ClientGameDto gameDto : games) {
                    System.out.println("Id: " + gameDto.getGameId() +
                            ", Visit: " + gameDto.getVisit() +
                            ", Date Time Celebration: " + gameDto.getDateTimeCelebration() +
                            ", Price: " + gameDto.getPrice() +
                            ", Available Tickets: " + gameDto.getAvailableTickets());
                }

            } catch (InputValidationException e) {
                e.printStackTrace(System.err);
            }

        } else if ("-findMatch".equalsIgnoreCase(args[0])){
            //[findMatch]    ServiceClient -findMatch <matchId>
            validateArgs(args, 2, new int[] {1});

            try{
                ClientGameDto game = clientGameService.findGameById(Long.valueOf(args[1]));
                System.out.println("Id: " + game.getGameId() +
                        ", Visit: " + game.getVisit() +
                        ", Date Time Celebration: " + game.getDateTimeCelebration() +
                        ", Price: " + game.getPrice() +
                        ", Available Tickets: " + game.getAvailableTickets());

            } catch (InstanceNotFoundException e) {
                e.printStackTrace(System.err);
            }

        } else if ("-buy".equalsIgnoreCase(args[0])){
            validateArgs(args, 5, new int[] {1,3,4});
            //[buy]          ServiceClient -buy <gameId> <userEmail> <ticketNum> <CCNum>
            Long saleId;
            try{
                saleId = clientGameService.buy(Long.parseLong(args[1]), args[2], args[4], Integer.parseInt(args[3]));
                System.out.println("Game " + args[1] +
                        " purchased sucessfully " + args[3] + " tickets with sale number " +
                        saleId);

            } catch (ClientGameCelebratedException | InstanceNotFoundException | InputValidationException | ClientNoTicketsException e) {
                e.printStackTrace(System.err);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if ("-collect".equalsIgnoreCase(args[0])){
            validateArgs(args, 3, new int[] {1});
            //[collect]      ServiceClient -collect <purchaseId> <userEmail>
            try{
                clientGameService.pickUpTickets(args[1], args[2]);
                System.out.println("The tickets from the sale" + args[1] + " have been taken by" +
                        " the user with the card " + args[2]);

            } catch (ClientInvalidCreditCardException | ClientTicketsAlreadyDeliveredException | InstanceNotFoundException e) {
                e.printStackTrace(System.err);
            }


        } else if ("-findPurchases".equalsIgnoreCase(args[0])){
            try{
                validateArgs(args, 2, new int[] {});
                //[purchases]    ServiceClient -findPurchases <userEmail>
                List<ClientSaleDto> saleList = clientGameService.findSale(args[1]);

                System.out.println("The sales for the user " + args[1] + " are:");
                for (ClientSaleDto sale : saleList){
                    System.out.println("Sale id: " + sale.getSaleId() + " -> Game: " + sale.getGameId() + ", CC: " +
                            sale.getCardNum());
                }
            } catch (Exception e){
                e.printStackTrace(System.err);
            }
        }

    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsage();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsage();
            }
        }
    }

    public static void printUsage(){
        System.err.println("""
                Usage:
                     [addMatch]     ServiceClient -addMatch <visitor> <celebrationdate> <price> <maxTickets>
                     [findMatches]  ServiceClient -findMatches <untilDay>
                     [findMatch]    ServiceClient -findMatch <matchId>
                     [buy]          ServiceClient -buy <gameId> <userEmail> <ticketNum> <CCNum>
                     [collect]      ServiceClient -collect <purchaseId> <userEmail>
                     [purchases]    ServiceClient -findPurchases <userEmail>
                """);
        System.exit(-1);
    }
}