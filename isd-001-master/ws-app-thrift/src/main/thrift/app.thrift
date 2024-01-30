namespace java es.udc.ws.app.thrift

struct ThriftGameDto {
    1: i64 gameId
    2: string dateTimeCelebration
    3: string visit
    4: double price
    5: i32 availableTickets
    6: i32 soldTickets
}

struct ThriftSaleDto {
    1: i64 saleId
    2: i64 gameId
    3: string userEmail
    4: string cardNum
    5: bool takedTickets
    6: string dateTimeSale,
    7: i32 ticketNum
    8: double price
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftTicketsAlreadyDeliveredException {
    1: string message
}

exception ThriftNoTicketsException {
    1: string message
}

exception ThriftInvalidCreditCardException {
    1: string message
}

exception ThriftGameCelebratedException {
    1: string message
}


service ThriftGameService {

   ThriftGameDto addGame(1: ThriftGameDto gameDto) throws (ThriftInputValidationException e)

   list<ThriftGameDto> findBetweenDates (1: string fin) throws (ThriftInputValidationException e);

   ThriftGameDto findById(1: i64 gameId) throws (ThriftInstanceNotFoundException e)

   ThriftSaleDto buy(1: i64 gameId, 2: string userEmail, 3: string cardNumber, 4: i32 ticketsNum) throws (ThriftInstanceNotFoundException e, ThriftInputValidationException ee, ThriftNoTicketsException eee, ThriftGameCelebratedException eeee)

   list<ThriftSaleDto> findSale(1: string userEmail) throws (ThriftInstanceNotFoundException e, ThriftInputValidationException ee);

   ThriftSaleDto pickUpTickets(1: i64 saleId, 2: string cardNum) throws (ThriftInstanceNotFoundException e, ThriftInvalidCreditCardException ee, ThriftTicketsAlreadyDeliveredException eee, ThriftInputValidationException eeee)
}
